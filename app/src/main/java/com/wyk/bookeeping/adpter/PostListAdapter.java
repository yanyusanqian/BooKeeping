package com.wyk.bookeeping.adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.activity.AddArticleActivity;
import com.wyk.bookeeping.activity.ImageLookActivity;
import com.wyk.bookeeping.bean.Article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {
    private Context context;
    private List<Article> list;

    public PostListAdapter(Context context, List<Article> list) {
        this.context = context;
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView post_username, post_time, post_content;
        RecyclerView post_images_recycler;
        ImageView post_headportrait;
        LinearLayout item_post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_headportrait = itemView.findViewById(R.id.post_headportrait);
            post_username = itemView.findViewById(R.id.post_username);
            post_time = itemView.findViewById(R.id.post_time);
            post_content = itemView.findViewById(R.id.post_content);
            post_images_recycler = itemView.findViewById(R.id.post_images_recycler);
            item_post = itemView.findViewById(R.id.item_post);
        }
    }

    @NonNull
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postlayout, parent, false);
        return new PostListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.ViewHolder holder, int position) {
        holder.item_post.setOnClickListener(v->{
            listener.myItemClick(v,position);
        });
        holder.post_username.setText(list.get(position).getUser_nikename());
        if (list.get(position).getArticle_time() != null) {
            holder.post_time.setText(list.get(position).getArticle_time());
        }
        holder.post_content.setText(list.get(position).getArticle_content());
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.headportrait)
                .centerCrop();
        Log.i("TAG","http://"+context.getString(R.string.localhost)+"/pic_file/"+list.get(position).getUser_image());
        Glide.with(context)
                .load("http://"+context.getString(R.string.localhost)+"/pic_file/"+list.get(position).getUser_image())
                .apply(requestOptions).into(holder.post_headportrait);

        String imgs = list.get(position).getArticle_image();
        List<String> list = new ArrayList();

        if(imgs!=null){
            if(!TextUtils.isEmpty(imgs)){
                String[] a = imgs.split("##");
                for(int i=0;i<a.length;i++){
                    list.add("http://"+context.getString(R.string.localhost)+"/pic_file/"+a[i]);
                    Log.i("TAG image:","http://"+context.getString(R.string.localhost)+"/pic_file/"+a[i]);
                }
            }
        }
        PostImageGridAdapter adapter = new PostImageGridAdapter(context,list);
        adapter.setOnItemClickListener((position1, v) -> {
            Intent intent = new Intent(context,ImageLookActivity.class);
            intent.putExtra("IMAGE",list.get(position1));
            context.startActivity(intent);

           /* //组织数据
            ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>(); // 这个最好定义成成员变量
            ThumbViewInfo item;
            mThumbViewInfoList.clear();
            for (int i = 0;i < list.size(); i++) {
                Rect bounds = new Rect();
                //new ThumbViewInfo(图片地址);
                item=new ThumbViewInfo(list.get(i));
                item.setBounds(bounds);
                mThumbViewInfoList.add(item);
            }

            //打开预览界面
            GPreviewBuilder.from((Activity) context)
                    //是否使用自定义预览界面，当然8.0之后因为配置问题，必须要使用
                    .to(ImageLookActivity.class)
                    .setData(list.get(position1))
                    .setCurrentIndex(position)
                    .setSingleFling(true)
                    .setType(GPreviewBuilder.IndicatorType.Number)
                    // 小圆点
//  .setType(GPreviewBuilder.IndicatorType.Dot)
                    .start();//启动*/
        });

        holder.post_images_recycler.setLayoutManager(new GridLayoutManager(context, 3));
        //配置适配器
        holder.post_images_recycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private OnMyItemClickListener listener;
    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;
    }
    public interface OnMyItemClickListener{
        void myItemClick(View v,int pos);
    }

   /* private OnMyItemImageClickListener ImageListener;
    public void setOnMyItemImageClickListener(OnMyItemImageClickListener ImageListener){
        this.ImageListener = ImageListener;
    }
    public interface OnMyItemImageClickListener{
        void myItemClick(View v,int pos);
    }*/
}
