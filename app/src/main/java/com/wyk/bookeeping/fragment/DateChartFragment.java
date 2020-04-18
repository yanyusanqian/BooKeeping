package com.wyk.bookeeping.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.ChartRecyclerViewAdapter;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.MyChartData;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.TimeUtil;
import com.wyk.bookeeping.view.ChartMarkerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DateChartFragment extends Fragment {
    private static final String TIME_TYPE = "TIME_TYPE";
    private static final String TIME = "TIME";
    private static final String INEX_TYPE = "INEX_TYPE";

    // 传递过来的时间类型，参数为年 = 3\月 = 2\周 = 1
    private int timetype;
    // 传递过来的对应时间，如果是周则为当年第x周
    private String time;
    private int InExtype = 0;
    private RecyclerView chart_date_recycler;
    private LineChart chart;
    private TextView linechart_text, linechart_count, lincahrt_num, linechat_max_count;
    private LinearLayout chartdate_empty;

    static DateChartFragment newInstance(int timeType, String Time, int inExtype) {
        DateChartFragment fragment = new DateChartFragment();
        Bundle args = new Bundle();
        args.putInt(TIME_TYPE, timeType);
        args.putString(TIME, Time);
        args.putInt(INEX_TYPE, inExtype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_date, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            timetype = getArguments().getInt(TIME_TYPE);
            time = getArguments().getString(TIME);
            InExtype = getArguments().getInt(INEX_TYPE);
        }
        initLineChart();
    }

    private void initView(View view) {
        chart = view.findViewById(R.id.chart);
        chart_date_recycler = view.findViewById(R.id.chart_date_recycler);
        chartdate_empty = view.findViewById(R.id.chartdate_empty);
        linechart_text = (TextView) view.findViewById(R.id.linechart_text);
        linechart_count = (TextView) view.findViewById(R.id.linechart_count);
        lincahrt_num = (TextView) view.findViewById(R.id.lincahrt_num);
        linechat_max_count = (TextView) view.findViewById(R.id.linechat_max_count);
    }

    private void initLineChart() {
        switch (timetype) { // 根据不同类型选择不同处理逻辑
            case WeekFragment.TYPE_WEEK:
                List<Entry> entryList = new ArrayList<>();
                List<String> list = TimeUtil.getDateListByWeek(time);
                time = time.replaceAll("周", "");
                // 从数据库中取出数据，存放进MyChartData数据实体类中
                MyChartData myChartData = DBHelper.getInstance(getContext()).getWeekData(getContext(), TimeUtil.getNowDateYear() + "", time, InExtype);
                if (InExtype == 1) {
                    linechart_text.setText("总支出");
                } else {
                    linechart_text.setText("总收入");
                }

                linechart_count.setText(myChartData.getAllcount() + "");
                lincahrt_num.setText(myChartData.getNum() + "笔");
                linechat_max_count.setText(myChartData.getMaxcount() + "");
                // 生成图表数据
                Map<String, Float> map = myChartData.getCount();
                if (map != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (!map.containsKey(list.get(i))) {
                            entryList.add(new Entry(i, 0));
                        } else {
                            entryList.add(new Entry(i, map.get(list.get(i))));
                        }
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        entryList.add(new Entry(i, 0));
                    }
                }
                ChartStyleSet(entryList, list);

                if (myChartData.getList() != null) {
                    chartdate_empty.setVisibility(View.INVISIBLE);
                    setRecyclerView(myChartData.getList(), myChartData.getAllcount());
                } else
                    chartdate_empty.setVisibility(View.VISIBLE);
                break;
            case MonthFragment.TYPE_MONTH:
                List<Entry> entryList2 = new ArrayList<>();
                List<String> list2 = TimeUtil.getDateListByMonth(time);
                time = time.replaceAll("月", "");
                MyChartData myChartData2 = DBHelper.getInstance(getContext()).getMonthData(getContext(), TimeUtil.getNowDateYear() + "", time, InExtype);
                if (InExtype == 1) {
                    linechart_text.setText("总支出");
                } else {
                    linechart_text.setText("总收入");
                }
                linechart_count.setText(myChartData2.getAllcount() + "");
                lincahrt_num.setText(myChartData2.getNum() + "笔");
                linechat_max_count.setText(myChartData2.getMaxcount() + "");

                Map<String, Float> map2 = myChartData2.getCount();
                if (map2 != null) {
                    for (int i = 0; i < list2.size(); i++) {
                        if (!map2.containsKey(list2.get(i))) {
                            entryList2.add(new Entry(i, 0));
                        } else {
                            entryList2.add(new Entry(i, map2.get(list2.get(i))));
                        }
                    }
                } else {
                    for (int i = 0; i < list2.size(); i++) {
                        entryList2.add(new Entry(i, 0));
                    }
                }
                ChartStyleSet(entryList2, list2);
                if (myChartData2.getList() != null) {
                    chartdate_empty.setVisibility(View.INVISIBLE);
                    setRecyclerView(myChartData2.getList(), myChartData2.getAllcount());
                } else
                    chartdate_empty.setVisibility(View.VISIBLE);
                break;
            case YearFargment.TYPE_YEAR:
                List<Entry> entryList3 = new ArrayList<>();
                List<String> list3 = TimeUtil.getDateListByYear();
                time = time.replaceAll("年", "");
                MyChartData myChartData3 = DBHelper.getInstance(getContext()).getYearData(getContext(), time, InExtype);
                if (InExtype == 1) {
                    linechart_text.setText("总支出");
                } else {
                    linechart_text.setText("总收入");
                }

                linechart_count.setText(myChartData3.getAllcount() + "");
                lincahrt_num.setText(myChartData3.getNum() + "笔");
                linechat_max_count.setText(myChartData3.getMaxcount() + "");

                Map<String, Float> map3 = myChartData3.getCount();
                if (map3 != null) {
                    for (int i = 0; i < list3.size(); i++) {
                        if (!map3.containsKey(list3.get(i))) {
                            entryList3.add(new Entry(i, 0));
                        } else {
                            entryList3.add(new Entry(i, map3.get(list3.get(i))));
                        }
                    }
                } else {
                    for (int i = 0; i < list3.size(); i++) {
                        entryList3.add(new Entry(i, 0));
                    }
                }
                ChartStyleSet(entryList3, list3);
                if (myChartData3.getList() != null) {
                    chartdate_empty.setVisibility(View.INVISIBLE);
                    setRecyclerView(myChartData3.getList(), myChartData3.getAllcount());
                } else
                    chartdate_empty.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void setRecyclerView(List<Account> list, Float allCount) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        chart_date_recycler.setLayoutManager(mLayoutManager);
        ChartRecyclerViewAdapter chartRecyclerViewAdapter = new ChartRecyclerViewAdapter(getActivity(), list, allCount);
        chart_date_recycler.setAdapter(chartRecyclerViewAdapter);
    }


    private void ChartStyleSet(List<Entry> entryList, List<String> xList) {
        // 初始化折线图数据
        LineDataSet dataSet = new LineDataSet(entryList, "Label");
        dataSet.setColor(Color.parseColor("#8A2BE2"));//线条颜色
        dataSet.setCircleColor(Color.parseColor("#8A2BE2"));//圆点颜色
        dataSet.setLineWidth(1f);//线条宽度
        dataSet.setValueTextSize(10f);
        // 设置Y轴样式
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);
        // 设置X轴样式
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#333333"));
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);//禁止放大后x轴标签重绘
        // 设置X轴数值
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xList));
        //透明化图例
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);
        //隐藏x轴描述
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);
        // 设置折线图属性
        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(false);
        chart.setData(lineData);
        chart.invalidate(); // refresh
        //设置折线图填充
        Drawable drawable = getActivity().getResources().getDrawable(R.drawable.linechart_back, null);
        setChartFillDrawable(drawable);
        //显示默认markerview
        ChartMarkerView myMarkerView = new ChartMarkerView(getActivity());
        myMarkerView.setChartView(chart);
        chart.setMarker(myMarkerView);
    }


    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            chart.invalidate();
        }
    }
}
