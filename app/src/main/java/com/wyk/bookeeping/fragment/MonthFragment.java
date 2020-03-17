package com.wyk.bookeeping.fragment;

import android.os.Bundle;
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
import com.wyk.bookeeping.livedate.AccountViewModel;
import com.wyk.bookeeping.utils.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthFragment extends Fragment {
    private TabLayout chart_date_tablayout;
    private AccountViewModel accountViewModel;
    private Map<String, String> map;
    private FrameLayout emptyview;
    private LinearLayout not_emptyview;
    private List<Fragment> fragmentList;
    private List<String> datelist;
    private ViewPager month_viewpager;
    public static final int TYPE_MONTH = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart_month, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        accountViewModel.getDate().observe(getActivity(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                map = stringStringMap;
            }
        });
        initView();
        initChartData();
    }

    private void initView() {
        chart_date_tablayout = (TabLayout) getActivity().findViewById(R.id.chart_date_tablayout_month);
        not_emptyview = (LinearLayout) getActivity().findViewById(R.id.month_not_emptyview);
        emptyview = (FrameLayout) getActivity().findViewById(R.id.month_emptyview);
        month_viewpager = (ViewPager) getActivity().findViewById(R.id.month_viewpager);
    }

    private void initChartData() {
        if(map.isEmpty()){
            not_emptyview.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        }else{
            datelist = TimeUtil.getBetweenDateList_month(map);
            fragmentList = new ArrayList<>();
            for(int i =0;i<datelist.size();i++){
                //i 为对应time列表第几个
                fragmentList.add(DateChartFragment.newInstance(TYPE_MONTH,datelist.get(i)));
            }
            MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(
                    getChildFragmentManager(), fragmentList, datelist, getActivity(),
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            month_viewpager.setAdapter(myFragmentAdapter);
            month_viewpager.setOffscreenPageLimit(1);
            month_viewpager.setCurrentItem(0);
            if (datelist.size() < 6)
                chart_date_tablayout.setTabMode(TabLayout.MODE_FIXED);
            else
                chart_date_tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            chart_date_tablayout.setupWithViewPager(month_viewpager);
        }
    }
}
