package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wyk.bookeeping.R;

import java.util.List;

public class PostImageGridAdapter extends RecyclerView.Adapter<PostImageGridAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List list;
    private Context context;

    public PostImageGridAdapter(Context context, List list) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView nine_image;
        ViewHolder(View view) {
            super(view);
            nine_image = view.findViewById(R.id.nine_image);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public PostImageGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postimage1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostImageGridAdapter.ViewHolder viewHolder, final int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.headportrait)
                .centerCrop();
        Glide.with(context).load(list.get(position)).apply(requestOptions).into(viewHolder.nine_image);

        viewHolder.nine_image.setOnClickListener(v->{
            mItemClickListener.onItemClick(position,v);
        });
    }

    private PostImageGridAdapter.OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(PostImageGridAdapter.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
