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

public class RemindAdapter extends RecyclerView.Adapter<RemindAdapter.ViewHolder> {
    private List<String> textList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView remind_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            remind_time = (TextView) itemView.findViewById(R.id.remind_time);
        }
    }

    public RemindAdapter(List<String> textlist){
        textList = textlist;
    }
    @NonNull
    @Override
    public RemindAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_remind_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindAdapter.ViewHolder holder, int position) {
        holder.remind_time.setText(textList.get(position));
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }
}
