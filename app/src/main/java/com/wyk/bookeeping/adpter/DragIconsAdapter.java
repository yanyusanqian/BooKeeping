package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.Icons;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.List;

public class DragIconsAdapter extends BaseAdapter<DragIconsAdapter.ViewHolder> {
    private SwipeRecyclerView mMenuRecyclerView;
    private List<Icons> mDataList;
    private OnDragDeleteClickListener onDragDeleteClickListener = null;

    public DragIconsAdapter(Context context, SwipeRecyclerView menuRecyclerView) {
        super(context);
        this.mMenuRecyclerView = menuRecyclerView;
    }

    @Override
    public int getItemCount() {
        Log.i("TAG","date_list size"+(mDataList == null ? 0 : mDataList.size()));
        return mDataList == null ? 0 : mDataList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(getInflater().inflate(R.layout.item_dragicons, parent, false));
        viewHolder.mMenuRecyclerView = mMenuRecyclerView;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.dragicons_imageview.setImageResource(mDataList.get(position).getDrawableid());
        holder.dragicons_text.setText(mDataList.get(position).getTitle());
        holder.dragicons_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDragDeleteClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
            }
        });
    }

    @Override
    public void notifyDataSetChanged(List<Account> dataList) {

    }

    public void OnDragDeleteClickListener(OnDragDeleteClickListener listener){
        onDragDeleteClickListener= listener;
    }
    public interface OnDragDeleteClickListener{
        void onItemClick(View view,int position);
    }


    @Override
    public void notifyDataSetChanged_icons(List<Icons> dataList) {
        this.mDataList = dataList;
        super.notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        ImageView dragicons_imageview, dragicons_delete;
        TextView dragicons_text;
        SwipeRecyclerView mMenuRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            dragicons_text = itemView.findViewById(R.id.dragicons_text);
            dragicons_imageview = itemView.findViewById(R.id.dragicons_imageview);
            dragicons_delete = itemView.findViewById(R.id.dragicons_delete);
            itemView.findViewById(R.id.chartdetails_touch).setOnTouchListener(this);
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mMenuRecyclerView.startDrag(this);
                    break;
                }
            }
            return false;
        }
    }
}