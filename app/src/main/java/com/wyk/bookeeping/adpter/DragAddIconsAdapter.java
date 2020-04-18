package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Icons;

import java.util.List;

public class DragAddIconsAdapter extends RecyclerView.Adapter<DragAddIconsAdapter.ViewHolder> {
    private List<Icons> iconsList;
    private Context context;
    private OnDragAddClickListener onDragAddClickListener = null;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView dragicons_add,dragicons_imageview;
        TextView dragicons_text;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            dragicons_text = (TextView) itemView.findViewById(R.id.dragicons_text);
            dragicons_add = (ImageView) itemView.findViewById(R.id.dragicons_add);
            dragicons_imageview = (ImageView) itemView.findViewById(R.id.dragicons_imageview);
        }
    }

    public DragAddIconsAdapter(Context context,List<Icons> iconsList){
        this.context = context;
        this.iconsList = iconsList;
    }
    @NonNull
    @Override
    public DragAddIconsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dragicons_add,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DragAddIconsAdapter.ViewHolder holder, int position) {
        holder.dragicons_text.setText(iconsList.get(position).getTitle());
        holder.dragicons_imageview.setImageResource(iconsList.get(position).getDrawableid());
        holder.dragicons_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDragAddClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconsList.size();
    }

    public void OnDragAddClickListener(OnDragAddClickListener listener){
        onDragAddClickListener= listener;
    }
    public interface OnDragAddClickListener{
        void onItemClick(View view,int position);
    }

    public void setList(List<Icons> list){
        this.iconsList = list;
    }
}
