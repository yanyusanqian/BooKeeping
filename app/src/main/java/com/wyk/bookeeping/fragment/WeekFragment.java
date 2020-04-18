package com.wyk.bookeeping.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.MyFragmentAdapter;
import com.wyk.bookeeping.livedata.AccountViewModel;
import com.wyk.bookeeping.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeekFragment extends Fragment {
    private TabLayout chart_date_tablayout;

    private AccountViewModel accountViewModel;
    private Map<String, String> map;
    private LinearLayout not_emptyview;
    private FrameLayout emptyview;
    private List<Fragment> fragmentList;
    private List<String> datelist;
    private ViewPager week_viewpager;

    public static final int TYPE_WEEK = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart_week, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        accountViewModel.getDate().observe(getActivity(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                    Log.i("TAG start","11");
                    map = stringStringMap;
                    int InExType = accountViewModel.getInExType().getValue();
                    Log.i("TAG inextype",InExType+"");
                    initChartData(InExType);
            }
        });
    }

    private void initView() {
        chart_date_tablayout = (TabLayout) getActivity().findViewById(R.id.chart_date_tablayout_week);
        not_emptyview = (LinearLayout) getActivity().findViewById(R.id.week_not_emptyview);
        emptyview = (FrameLayout) getActivity().findViewById(R.id.week_emptyview);
        week_viewpager = (ViewPager) getActivity().findViewById(R.id.week_viewpager);
    }

    private void initChartData(int InExType) {
        if (map.isEmpty()) {
            not_emptyview.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        } else {
            not_emptyview.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
            // 获取周期列表
            datelist = TimeUtil.getBetweenDateList_week(map);
            fragmentList = new ArrayList<>();
            for(int i =0;i<datelist.size();i++){
                // 生成每个周期对应Fragment
                fragmentList.add(DateChartFragment.newInstance(TYPE_WEEK,datelist.get(i),InExType));
            }
            // 设置Fragment适配器和布局管理器
            MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(
                    getChildFragmentManager(), fragmentList, datelist, getActivity(),
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            Log.i("TAG","HERE?");
            // 向ViewPager中设置Fragment适配器
            week_viewpager.setAdapter(myFragmentAdapter);
            Log.i("TAG","HERE2");
//            myFragmentAdapter.notifyDataSetChanged();
            week_viewpager.setOffscreenPageLimit(1);
            week_viewpager.setCurrentItem(0);
            if (datelist.size() < 6)
                chart_date_tablayout.setTabMode(TabLayout.MODE_FIXED);
            else
                chart_date_tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            chart_date_tablayout.post(new Runnable() {
                @Override
                public void run() {
                    chart_date_tablayout.setupWithViewPager(week_viewpager);
                }
            });
        }
    }
}