package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Icons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DragIconsGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Icons> list;
    private List<Boolean> isClicks;
    private Context context;
    private LayoutInflater mInflater;
    private int ColumnNum;
    private OnItemImageClickListener onItemImageClickListener = null;

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView dragicons_image;
        LinearLayout background_check;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            dragicons_image = (ImageView) itemView.findViewById(R.id.dragicons_image);
            background_check = (LinearLayout) itemView.findViewById(R.id.background_check);
        }
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView dragicons_title;

        TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            dragicons_title = (TextView) itemView.findViewById(R.id.dragicons_title);
        }
    }

    public DragIconsGridAdapter(Context context, List<Icons> list, int ColumnNum) {
        this.context = context;
        this.list = list;
        this.ColumnNum = ColumnNum;
        isClicks = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 1)
                isClicks.add(true);
            else
                isClicks.add(false);
        }
    }

    public void OnItemImageClickListener(OnItemImageClickListener listener) {
        onItemImageClickListener = listener;
    }

    public interface OnItemImageClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getIstitle();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        mInflater = LayoutInflater.from(context);
        //判断viewtype类型返回不同Viewholder
        switch (viewType) {
            case 1:
                viewHolder = new TitleViewHolder(mInflater.inflate(R.layout.item_dragicons_title, parent, false));
                break;
            case 0:
                viewHolder = new ImageViewHolder(mInflater.inflate(R.layout.item_drggicons_add_grid, parent, false));
                break;
        }
        return viewHolder;
    }

    //判断是否是title，如果是，title占满一行的所有子项，则是ColumnNum个，如果是item，占满一个子项
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //如果是title就占据2个单元格(重点)
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (list.get(position).getIstitle() == 0) {
                    return 1;
                } else {
                    return ColumnNum;
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        int viewType = holder.getItemViewType();
        if (viewType == 1) {
            TitleViewHolder viewHolder = (TitleViewHolder) holder;
            viewHolder.dragicons_title.setText(list.get(position).getTitle());
        } else {
            ImageViewHolder viewHolder = (ImageViewHolder) holder;
            if (isClicks.get(position)) {
                viewHolder.background_check.setBackgroundResource(R.drawable.icons_selected);
            } else {
                viewHolder.background_check.setBackgroundResource(R.drawable.icons_billlist);
            }
            viewHolder.dragicons_image.setImageResource(list.get(position).getDrawableid());
            viewHolder.dragicons_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < isClicks.size(); i++) {
                        isClicks.set(i, false);
                    }
                    isClicks.set(position, true);
                    notifyDataSetChanged();

                    onItemImageClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
