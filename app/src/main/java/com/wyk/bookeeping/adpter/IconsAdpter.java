package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Icons;

import java.util.List;

public class IconsAdpter extends RecyclerView.Adapter<IconsAdpter.ViewHolder> {
    private List<Icons> iconsList;
    private Context context;

    private OnMyItemClickListener listener;
    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;
    }

    public interface OnMyItemClickListener{
        void myClick(View v,int pos);
    }

    public IconsAdpter(@NonNull Context context,List<Icons> iconsList) {
        this.context = context;
        this.iconsList = iconsList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearlayout;
        ImageView imageView;
        TextView textView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearlayout = (LinearLayout)itemView.findViewById(R.id.linearlayout);
            imageView = (ImageView)itemView.findViewById(R.id.imageview);
            textView = (TextView)itemView.findViewById(R.id.textview);
        }
    }

    @NonNull
    @Override
    public IconsAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_iconsitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconsAdpter.ViewHolder holder, final int position) {
        holder.textView.setText(iconsList.get(position).getTitle());
        holder.imageView.setImageResource(iconsList.get(position).getDrawableid());

        if (listener!=null){
            holder.linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.myClick(v,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return iconsList.size();
    }
}
