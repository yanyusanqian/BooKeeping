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

import java.util.ArrayList;
import java.util.List;

public class IconsAdpter extends RecyclerView.Adapter<IconsAdpter.ViewHolder> {
    private List<Icons> iconsList,iconsList_s;
    private Context context;
    private List<Boolean> isClicks;

    private OnMyItemClickListener listener;
    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;
    }

    public interface OnMyItemClickListener{
        void myClick(View v,int pos);
    }

    public IconsAdpter(@NonNull Context context,List<Icons> iconsList,List<Icons> iconsList_s) {
        this.context = context;
        this.iconsList = iconsList;
        this.iconsList_s = iconsList_s;
        isClicks = new ArrayList<>();
        for(int i = 0;i<iconsList.size();i++){
            isClicks.add(false);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearlayout,background_check;
        ImageView imageView;
        TextView textView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearlayout = (LinearLayout)itemView.findViewById(R.id.linearlayout);
            background_check = (LinearLayout)itemView.findViewById(R.id.background_check);
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
    public void onBindViewHolder(@NonNull final IconsAdpter.ViewHolder holder, final int position) {
        holder.textView.setText(iconsList.get(position).getTitle());
        holder.imageView.setImageResource(iconsList.get(position).getDrawableid());
        if(isClicks.get(position)){
            holder.background_check.setBackgroundResource(R.drawable.icons_selected);
            holder.imageView.setImageResource(iconsList_s.get(position).getDrawableid());
        }else{
            holder.background_check.setBackgroundResource(R.drawable.icons_not_selected);
            holder.imageView.setImageResource(iconsList.get(position).getDrawableid());
        }

        if (listener!=null){
            holder.linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0; i <isClicks.size();i++){
                        isClicks.set(i,false);
                    }
                    isClicks.set(position,true);
                    notifyDataSetChanged();
//                    holder.background_check.setBackgroundResource(R.drawable.icons_selected);
//                    holder.imageView.setImageResource(iconsList_s.get(position).getDrawableid());
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
