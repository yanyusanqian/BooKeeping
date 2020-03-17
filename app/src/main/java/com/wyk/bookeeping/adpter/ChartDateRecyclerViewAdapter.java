package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;

import java.util.List;

public class ChartDateRecyclerViewAdapter extends RecyclerView.Adapter<ChartDateRecyclerViewAdapter.ViewHolder> {
    private List<String> list;
    private Context context;
    public ChartDateRecyclerViewAdapter(Context context,List<String> list){
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_date;
        ViewHolder(@NonNull View itemView) {

            super(itemView);
           item_date=itemView.findViewById(R.id.item_date);
        }
    }
    @NonNull
    @Override
    public ChartDateRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_date,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartDateRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.item_date.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
