package com.wyk.bookeeping.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.codingending.popuplayout.PopupLayout;
import com.google.android.material.tabs.TabLayout;
import com.wyk.bookeeping.adpter.MyFragmentAdapter;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.DataStatus;
import com.wyk.bookeeping.livedata.AccountViewModel;
import com.wyk.bookeeping.utils.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChartFragment extends Fragment {
    private TabLayout chart_tablayout;
    private ViewPager chart_viewpager;
    private TextView chart_toolbar_textview;
    private LinearLayout chart_choose;
    private MyFragmentAdapter myFragmentAdapter;
    private AccountViewModel accountViewModel;
    private List<Account> accountList;
    private SQLiteDatabase db;
    private PopupLayout popupLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        db = DBHelper.getInstance(getActivity()).getWritableDatabase();
        if ("支出".equals(chart_toolbar_textview.getText().toString())) {
            getMaxMinDate(1);
            accountViewModel.getInExType().setValue(1);
        } else {
            getMaxMinDate(0);
            accountViewModel.getInExType().setValue(0);
        }


        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        accountViewModel.getCurrentName().observe(getActivity(), new Observer<DataStatus>() {
            @Override
            public void onChanged(DataStatus dataStatus) {
                accountList = dataStatus.getAccountList();
                if (accountList != null)
                    if ("支出".equals(chart_toolbar_textview.getText().toString()))
                        getMaxMinDate(1);
                    else
                        getMaxMinDate(0);
            }
        });

    }

    private void getMaxMinDate(int type) {
        String sql = "SELECT Max(time),Min(time) FROM account WHERE inexType = " + type + "";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        Log.i("TAG44", cursor.getString(0) + "");
        if (cursor.getString(0) != null && cursor.getString(1) != null) {
            Map<String, String> map = new HashMap<>();
            map.put("MAX", cursor.getString(0));
            map.put("MIN", cursor.getString(1));
            accountViewModel.getDate().setValue(map);
        } else {
            Map<String, String> map = new HashMap<>();
            accountViewModel.getDate().setValue(map);
        }
    }

    private void initView() {
        chart_tablayout = (TabLayout) getActivity().findViewById(R.id.chart_tablayout);
        chart_viewpager = (ViewPager) getActivity().findViewById(R.id.chart_viewpager);
        chart_toolbar_textview = (TextView) getActivity().findViewById(R.id.chart_toolbar_textview);
        chart_choose = (LinearLayout) getActivity().findViewById(R.id.chart_choose);


        setViewPager();
        setCustomTablayout();
        setPopWindow();
    }

    private void setPopWindow() {
        chart_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(getActivity(), R.layout.layout_popspinner, null);
                popupLayout = PopupLayout.init(getActivity(), view);
                popupLayout.setUseRadius(false);
                view.findViewById(R.id.popspinner_expenditure).setOnClickListener(myListener);
                view.findViewById(R.id.popspinner_income).setOnClickListener(myListener);
                popupLayout.show(PopupLayout.POSITION_TOP);
            }
        });

    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.popspinner_expenditure:
                    if (!"支出".equals(chart_toolbar_textview.getText().toString())) {
                        chart_toolbar_textview.setText("支出");
                        accountViewModel.getInExType().setValue(1);
                        getMaxMinDate(1);

                        popupLayout.dismiss();
                    } else {
                        popupLayout.dismiss();
                    }
                    break;
                case R.id.popspinner_income:
                    if (!"收入".equals(chart_toolbar_textview.getText().toString())) {
                        chart_toolbar_textview.setText("收入");
                        accountViewModel.getInExType().setValue(0);
                        getMaxMinDate(0);

                        popupLayout.dismiss();
                    } else {
                        popupLayout.dismiss();
                    }
                    break;
            }
        }
    };

    private void setViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();
        fragmentList.add(new WeekFragment());
        fragmentList.add(new MonthFragment());
        fragmentList.add(new YearFargment());
        fragmentTitles.add("周");
        fragmentTitles.add("月");
        fragmentTitles.add("年");

        myFragmentAdapter = new MyFragmentAdapter(
                getChildFragmentManager(), fragmentList, fragmentTitles, getActivity(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        chart_viewpager.setAdapter(myFragmentAdapter);
        chart_viewpager.setOffscreenPageLimit(2);
        chart_viewpager.setCurrentItem(0);
    }

    private void setCustomTablayout() {
        chart_tablayout.setupWithViewPager(chart_viewpager);

        //设置自定义tab
        for (int i = 0; i < chart_tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = chart_tablayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(myFragmentAdapter.getTabView(i));
            }
        }

        chart_tablayout.setTabMode(TabLayout.MODE_FIXED);
        chart_tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        View view = chart_tablayout.getTabAt(0).getCustomView();
        TextView textView = view.findViewById(R.id.tab_layout_text);
        textView.setTextColor(getActivity().getResources().getColor(R.color.colorLavender));
        textView.setBackgroundResource(R.drawable.tablayout_item_check);
        chart_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView textView = view.findViewById(R.id.tab_layout_text);
                textView.setTextColor(getActivity().getResources().getColor(R.color.colorLavender));
                textView.setBackgroundResource(R.drawable.tablayout_item_check);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView textView = view.findViewById(R.id.tab_layout_text);
                textView.setTextColor(getActivity().getResources().getColor(R.color.white));
                textView.setBackgroundResource(R.drawable.tablayout_item_uncheck);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
