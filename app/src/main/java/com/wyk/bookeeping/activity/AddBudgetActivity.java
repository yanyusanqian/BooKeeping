package com.wyk.bookeeping.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.InputDialog;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AddBudgetActivity extends AppCompatActivity {
    PieChart mPieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbudget);

        Intent intent = getIntent();
        int type = intent.getIntExtra("TYPE",0);

        TextView center_budget_month, center_surplus_budget, center_budget, center_budget_expenditure, center_budget_edit;

        center_budget = findViewById(R.id.center_budget);
        center_budget_month = findViewById(R.id.center_budget_month);
        center_surplus_budget = findViewById(R.id.center_surplus_budget);
        center_budget_expenditure = findViewById(R.id.center_budget_expenditure);
        center_budget_edit = findViewById(R.id.center_budget_edit);
        mPieChart = findViewById(R.id.center_budget_piechart);

        initChart();

        String month = TimeUtil.getNowDateMonth_String();
        center_budget_month.setText(month);
        if (type == 0){
            showDialog();
        }else{
            float ex = intent.getFloatExtra("EX",0);
            center_budget_expenditure.setText(String.valueOf(ex));
            float Surplus = intent.getFloatExtra("Surplus",0);
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            String p = decimalFormat.format(Surplus);
            center_surplus_budget.setText(p);
            float Budget = intent.getFloatExtra("Budget",0);
            center_budget.setText(String.valueOf(Budget));
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            if(Surplus<=0){
                entries.add(new PieEntry(100,"支出"));
                entries.add(new PieEntry(0,"余额"));
                mPieChart.setCenterText("已超支");//设置环中间文字
            }else{
                entries.add(new PieEntry((ex/Budget)*100, "支出"));
                entries.add(new PieEntry((Surplus/Budget)*100, "余额"));
                mPieChart.setCenterText(generateCenterSpannableText((int)((Surplus/Budget)*100)));//设置环中间文字
            }
            setChart(entries);
        }

        center_budget_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void initChart() {
        mPieChart.setUsePercentValues(true);//设置value是否用显示百分数,默认为false
        mPieChart.getDescription().setEnabled(false);//设置描述
        mPieChart.setDragDecelerationFrictionCoef(0.95f);//设置阻尼系数,范围在[0,1]之间,越小饼状图转动越困难
        mPieChart.setDrawHoleEnabled(true);//是否绘制饼状图中间的圆
        mPieChart.setDrawCenterText(true);//是否绘制中间的文字
        mPieChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        mPieChart.setTransparentCircleColor(R.color.gray_dan);//设置透明圈的颜色
        mPieChart.setTransparentCircleAlpha(0);//设置透明圈的透明度[0,255]
        mPieChart.setHoleRadius(75f);//饼状图中间的圆的半径大小
        mPieChart.setTransparentCircleRadius(100f);//设置圆环的半径值
        mPieChart.setRotationAngle(0);//设置饼状图旋转的角度
        mPieChart.setExtraOffsets(0, 0, 0, 0);
        mPieChart.setTouchEnabled(false);
        //设置每个tab的显示位置
        Legend l = mPieChart.getLegend();
        l.setEnabled(false);
        // 输入标签样式
        mPieChart.setDrawEntryLabels(false);//设置是否绘制Label
        mPieChart.setLogEnabled(false);
        mPieChart.setUsePercentValues(false);
        //变化监听
        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void setChart(ArrayList<PieEntry> entries) {

        //设置数据
        PieDataSet dataSet = new PieDataSet(entries, "总预算");
        dataSet.setSliceSpace(0);
        dataSet.setDrawValues(false);

        //数据和颜色
        final int[]  PIE_COLORS={
                Color.rgb(245, 245, 245),Color.rgb(138, 43, 226)
        };
        dataSet.setColors(PIE_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void showDialog() {
        DialogSettings.style = DialogSettings.STYLE_IOS;
        DialogSettings.use_blur = false;

        InputDialog.show(AddBudgetActivity.this, "每月总预算", "请输入金额", "确定", new InputDialogOkButtonClickListener() {
            @Override
            public void onClick(Dialog dialog, String inputText) {
                if (TextUtils.isEmpty(inputText)) {
                    Toast.makeText(AddBudgetActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    if(!isNumber(inputText)){
                        Toast.makeText(AddBudgetActivity.this, "请正确输入", Toast.LENGTH_SHORT).show();
                    }else{
                        SpUtils.putString(AddBudgetActivity.this,"BudgetMonth",TimeUtil.getNowDateMonth_String());
                        SpUtils.putString(AddBudgetActivity.this,"Budget",new DecimalFormat("0.00").format(Float.valueOf(inputText)));
                        finish();
                    }
                }
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText(int x) {
        SpannableString s = new SpannableString("剩余\n"+x+"%");
        s.setSpan(new RelativeSizeSpan(1f), 0, 2, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 2, s.length() - 3, 0);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 2, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.3f), 2, s.length() , 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 2, s.length() , 0);
        return s;
    }

    /**
     * 判断一个字符串是否是数字。
     *
     * @param string
     * @return
     */
    public static boolean isNumber(String string) {
        if (string == null)
            return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(string).matches();
    }
}
