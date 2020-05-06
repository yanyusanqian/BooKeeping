package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Reply;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
    private List<Reply> replyList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView reply_headportrait;
        TextView reply_name,reply_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reply_name = (TextView) itemView.findViewById(R.id.reply_name);
            reply_content = (TextView) itemView.findViewById(R.id.reply_content);
            reply_headportrait = (ImageView) itemView.findViewById(R.id.reply_headportrait);
        }
    }

    public ReplyAdapter(Context context,List<Reply> replyList) {
        this.context = context;
        this.replyList = replyList;
    }

    @NonNull
    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_list, parent, false);
        return new ReplyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyAdapter.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.headportrait)
                .centerCrop();
        Log.i("TAG glide","http://"+context.getString(R.string.localhost)+"/pic_file/"+replyList.get(position).getUser_imgage());
        Glide.with(context).load("http://"+context.getString(R.string.localhost)+"/pic_file/"+replyList.get(position).getUser_imgage()).apply(requestOptions).into(holder.reply_headportrait);

        holder.reply_name.setText(replyList.get(position).getUser_nikename());
        holder.reply_content.setText(replyList.get(position).getReply_content());
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public void setTextList(List<Reply> replyList) {
        this.replyList = replyList;
    }
}
