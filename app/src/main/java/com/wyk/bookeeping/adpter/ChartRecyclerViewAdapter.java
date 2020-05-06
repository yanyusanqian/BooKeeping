package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Account;

import java.text.NumberFormat;
import java.util.List;

public class ChartRecyclerViewAdapter extends RecyclerView.Adapter<ChartRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Account> list;
    private Float allCount;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView chartdetails_text,chartdetails_percent,chartdetails_num;
        ImageView chartdetails_imageview;
        ProgressBar chartdetails_progress;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            chartdetails_text = (TextView) itemView.findViewById(R.id.chartdetails_text);
            chartdetails_percent = (TextView) itemView.findViewById(R.id.chartdetails_percent);
            chartdetails_num = (TextView) itemView.findViewById(R.id.chartdetails_num);
            chartdetails_imageview = itemView.findViewById(R.id.chartdetails_imageview);
            chartdetails_progress = itemView.findViewById(R.id.chartdetails_progress);
        }
    }
   public ChartRecyclerViewAdapter(Context context,List<Account> list,Float allCount){
        this.context = context;
        this.list = list;
        this.allCount = allCount;
    }

    @NonNull
    @Override
    public ChartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChartRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.chartdetails_text.setText(list.get(position).getBill_detailType());
        holder.chartdetails_num.setText(String.valueOf(list.get(position).getNum()));
        holder.chartdetails_imageview.setImageResource(list.get(position).getBill_imgRes());
        if (allCount!=null){
            float precent = list.get(position).getBill_count()/allCount;
            NumberFormat nt = NumberFormat.getPercentInstance();//获取格式化对象
            nt.setMinimumFractionDigits(1);
            holder.chartdetails_percent.setText(nt.format(precent));
            holder.chartdetails_progress.setProgress((int)(precent*100));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
