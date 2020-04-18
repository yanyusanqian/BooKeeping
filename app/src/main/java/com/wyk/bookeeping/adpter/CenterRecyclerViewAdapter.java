package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.activity.AddBudgetActivity;
import com.wyk.bookeeping.activity.AddRemindActivity;
import com.wyk.bookeeping.activity.DragIconsActivity;
import com.wyk.bookeeping.bean.CenterItem;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CenterRecyclerViewAdapter extends RecyclerView.Adapter<CenterRecyclerViewAdapter.ViewHolder> {
    public static final int TYPE_ICONS = 1;
    public static final int TYPE_BILL = 2;
    public static final int TYPE_BUDGET = 3;
    public static final int TYPE_COMMONFUNCTIONS = 4;
    public static final int TYPE_SETTING = 5;

    private Context context;
    private List<CenterItem> centerItemList;
    private Map<String, Float> map;
    private float expenditure = 0,surplus = 0,budget =  0;

    public CenterRecyclerViewAdapter(Context context, List<CenterItem> centerItemList) {
        this.context = context;
        this.centerItemList = centerItemList;
    }

    public void setMap(Map<String, Float> map) {
        this.map = map;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public CenterRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ICONS:
                return new IconsHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_icons, parent, false));
            case TYPE_BILL:
                return new BillHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_bill, parent, false));
            case TYPE_BUDGET:
                return new BudgetHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_budget, parent, false));
            case TYPE_COMMONFUNCTIONS:
                return new CommonFunctionsHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_commonfunctions, parent, false));
            case TYPE_SETTING:
                return new SettingHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_setting, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return centerItemList.get(position).getItemType();
    }

    /**
     * 绑定控件，可以写事件和方法
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CenterRecyclerViewAdapter.ViewHolder holder, int position) {
        CenterItem centerItem = centerItemList.get(position);
        switch (centerItem.getItemType()) {
            case TYPE_ICONS:
                IconsHolder iconsHolder = (IconsHolder) holder;
                iconsHolder.center_massage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                iconsHolder.center_clock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, AddRemindActivity.class));
                    }
                });
                iconsHolder.center_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DragIconsActivity.class);
                        context.startActivity(intent);
                    }
                });
                iconsHolder.center_export.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                iconsHolder.center_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case TYPE_BILL:
                BillHolder billHolder = (BillHolder) holder;
                billHolder.center_month.setText(TimeUtil.getNowDateMonth_String());
                Map<String, Float> map = DBHelper.getInstance(context).getInExCount(context);
                float in = 0;
                float ex = 0;
                if (!map.isEmpty()) {
                    if (map.get("in") != null) {
                        billHolder.center_income.setText(map.get("in") + "");
                        in = map.get("in");
                    }
                    if (map.get("ex") != null) {
                        billHolder.center_expenditure.setText(map.get("ex") + "");
                        ex = map.get("ex");
                        expenditure = ex;
                    }
                }
                billHolder.center_balance.setText(String.valueOf((in - ex)));
                break;
            case TYPE_BUDGET:
                BudgetHolder budgetHolder = (BudgetHolder) holder;
                budgetHolder.center_budget_expenditure.setText(new DecimalFormat("0.00").format(expenditure));

                String spmonth = SpUtils.getString(context, "BudgetMonth", "");
                String spbudget = SpUtils.getString(context, "Budget", "");
                String month = TimeUtil.getNowDateMonth_String();
                budgetHolder.center_budget_month.setText(month);

                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                if (month.equals(spmonth)) {
                    budgetHolder.center_budget.setText(spbudget);
                    budget = Float.parseFloat(spbudget);
                    surplus = budget - expenditure;
                    budgetHolder.center_surplus_budget.setText(new DecimalFormat("0.00").format(surplus));
                    if(surplus<=0){
                        entries.add(new PieEntry(100,"支出"));
                        budgetHolder.mPieChart.setCenterText("已超支");//设置环中间文字
                        budgetHolder.mPieChart.setCenterTextColor(Color.parseColor("#D81B60"));
                    }else{
                        entries.add(new PieEntry((expenditure/budget)*100, "支出"));
                        entries.add(new PieEntry((surplus/budget)*100, "余额"));
                        Log.i("TAG xx",(int)((surplus/budget)*100)+"");
                        budgetHolder.mPieChart.setCenterText(generateCenterSpannableText((int)((surplus/budget)*100)));//设置环中间文字
                    }
                    budgetHolder.center_budget_show.setVisibility(View.VISIBLE);
                    budgetHolder.center_budget_add.setVisibility(View.GONE);
                    budgetHolder.center_budget_show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AddBudgetActivity.class);
                            intent.putExtra("TYPE",1);
                            intent.putExtra("EX",expenditure);
                            intent.putExtra("Surplus",surplus);
                            intent.putExtra("Budget",budget);
                            context.startActivity(intent);
                        }
                    });
                }else{
                    entries.add(new PieEntry(100, "余额"));
                    budgetHolder.center_budget_show.setVisibility(View.GONE);
                    budgetHolder.center_budget_add.setVisibility(View.VISIBLE);
                    budgetHolder.mPieChart.setCenterText(generateCenterSpannableText(0));//设置环中间文字
                    budgetHolder.center_budget_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AddBudgetActivity.class);
                            intent.putExtra("TYPE",0);
                            context.startActivity(intent);
                        }
                    });
                }

                budgetHolder.mPieChart.setUsePercentValues(true);//设置value是否用显示百分数,默认为false
                budgetHolder.mPieChart.getDescription().setEnabled(false);//设置描述
                budgetHolder.mPieChart.setDragDecelerationFrictionCoef(0.95f);//设置阻尼系数,范围在[0,1]之间,越小饼状图转动越困难
                budgetHolder.mPieChart.setDrawHoleEnabled(true);//是否绘制饼状图中间的圆
                budgetHolder.mPieChart.setDrawCenterText(true);//是否绘制中间的文字

                budgetHolder.mPieChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
                budgetHolder.mPieChart.setTransparentCircleColor(R.color.gray_dan);//设置透明圈的颜色
                budgetHolder.mPieChart.setTransparentCircleAlpha(0);//设置透明圈的透明度[0,255]
                budgetHolder.mPieChart.setHoleRadius(75f);//饼状图中间的圆的半径大小
                budgetHolder.mPieChart.setTransparentCircleRadius(100f);//设置圆环的半径值
                budgetHolder.mPieChart.setRotationAngle(0);//设置饼状图旋转的角度
                budgetHolder.mPieChart.setExtraOffsets(0, 0, 0, 0);
                budgetHolder.mPieChart.setTouchEnabled(false);
                //变化监听
                budgetHolder.mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {

                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });
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

                budgetHolder.mPieChart.setData(data);
                budgetHolder.mPieChart.highlightValues(null);
                //刷新
                budgetHolder.mPieChart.invalidate();
                budgetHolder.mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                //设置每个tab的显示位置
                Legend l = budgetHolder.mPieChart.getLegend();
                l.setEnabled(false);
                // 输入标签样式
                budgetHolder.mPieChart.setDrawEntryLabels(false);//设置是否绘制Label
                budgetHolder.mPieChart.setLogEnabled(false);
                budgetHolder.mPieChart.setUsePercentValues(false);





                break;
            case TYPE_COMMONFUNCTIONS:
                CommonFunctionsHolder commonFunctionsHolder = (CommonFunctionsHolder) holder;
                break;
            case TYPE_SETTING:
                SettingHolder settingHolder = (SettingHolder) holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return centerItemList.size();
    }


    class IconsHolder extends CenterRecyclerViewAdapter.ViewHolder {
        LinearLayout center_massage, center_clock, center_category, center_export, center_setting;

        IconsHolder(@NonNull View itemView) {
            super(itemView);
            center_massage = itemView.findViewById(R.id.center_massage);
            center_clock = itemView.findViewById(R.id.center_clock);
            center_category = itemView.findViewById(R.id.center_category);
            center_export = itemView.findViewById(R.id.center_export);
            center_setting = itemView.findViewById(R.id.center_setting);
        }
    }

    class BillHolder extends CenterRecyclerViewAdapter.ViewHolder {
        private TextView center_month, center_income, center_expenditure, center_balance;

        BillHolder(@NonNull View itemView) {
            super(itemView);
            center_month = itemView.findViewById(R.id.center_month);
            center_income = itemView.findViewById(R.id.center_income);
            center_expenditure = itemView.findViewById(R.id.center_expenditure);
            center_balance = itemView.findViewById(R.id.center_balance);
        }
    }

    class BudgetHolder extends CenterRecyclerViewAdapter.ViewHolder {
        TextView center_budget_month, center_surplus_budget, center_budget, center_budget_expenditure;
        LinearLayout center_budget_add,center_budget_show;
        PieChart mPieChart;

        BudgetHolder(@NonNull View itemView) {
            super(itemView);
            center_budget = itemView.findViewById(R.id.center_budget);
            center_budget_month = itemView.findViewById(R.id.center_budget_month);
            center_surplus_budget = itemView.findViewById(R.id.center_surplus_budget);
            center_budget_expenditure = itemView.findViewById(R.id.center_budget_expenditure);
            center_budget_add = itemView.findViewById(R.id.center_budget_add);
            center_budget_show = itemView.findViewById(R.id.center_budget_show);
            mPieChart = itemView.findViewById(R.id.center_budget_piechart);
        }
    }

    class CommonFunctionsHolder extends CenterRecyclerViewAdapter.ViewHolder {
        CommonFunctionsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class SettingHolder extends CenterRecyclerViewAdapter.ViewHolder {
        SettingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    //设置中间文字 "剩余\n0%"

    private SpannableString generateCenterSpannableText(int x) {
        SpannableString s = new SpannableString("剩余\n"+x+"%");
        s.setSpan(new RelativeSizeSpan(1f), 0, 2, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 2, s.length() - 3, 0);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 2, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.3f), 2, s.length() , 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 2, s.length() , 0);
        return s;
    }
}
