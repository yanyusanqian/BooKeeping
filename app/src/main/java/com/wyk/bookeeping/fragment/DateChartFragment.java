package com.wyk.bookeeping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.wyk.bookeeping.R;

public class DateChartFragment extends Fragment {
    private static final String TIME_TYPE = "TIME_TYPE";
    private static final String END_TIME = "END_TIME";
    private static final String MAX_VALUE = "MAX_VALUE";

    private RecyclerView chart_date_recycler;
    private LineChart chart;

    public static DateChartFragment newInstance(int timeType, String Time) {
        DateChartFragment fragment = new DateChartFragment();
        Bundle args = new Bundle();
        args.putInt(TIME_TYPE, timeType);
        args.putString(END_TIME, Time);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart_date,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void initView(){
        chart = getActivity().findViewById(R.id.chart);
        chart_date_recycler = (RecyclerView) getActivity().findViewById(R.id.chart_date_recycler);
    }
}
