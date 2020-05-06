package com.wyk.bookeeping.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.PostImageGridAdapter;
import com.wyk.bookeeping.adpter.ReplyAdapter;
import com.wyk.bookeeping.bean.Reply;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.okhttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReplyActivity extends AppCompatActivity {
    private int article_id,reply_to_user_id;
    private String user_nikename,user_headportrait,article_time,article_content,article_img,response;
    private RecyclerView post_images_recycler, reply_recyclerview;
    private FrameLayout article_return;
    TextView post_username, post_time, post_content,reply;
    ImageView post_headportrait;
    private EditText reply_edit;
    private ReplyAdapter replyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Intent intent = getIntent();
        article_id = intent.getIntExtra("article_id",0);
        Log.i("TAG article_id",article_id+"");
        user_nikename = intent.getStringExtra("user_nikename");
        user_headportrait = intent.getStringExtra("user_headportrait");
        reply_to_user_id = intent.getIntExtra("user_id",0);
        article_time = intent.getStringExtra("article_time");
        article_content = intent.getStringExtra("article_content");
        article_img = intent.getStringExtra("article_img");

        post_headportrait = this.findViewById(R.id.post_headportrait);
        post_username = this.findViewById(R.id.post_username);
        post_time = this.findViewById(R.id.post_time);
        post_content = this.findViewById(R.id.post_content);
        post_images_recycler = this.findViewById(R.id.post_images_recycler);
        reply_recyclerview = this.findViewById(R.id.reply_recyclerview);
        article_return = this.findViewById(R.id.article_return);
        reply_edit = this.findViewById(R.id.reply_edit);
        reply = this.findViewById(R.id.reply);

        article_return.setOnClickListener(v->{
            finish();
        });

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.headportrait)
                .centerCrop();
        Glide.with(this).load("http://"+getString(R.string.localhost)+"/pic_file/"+user_headportrait)
                .apply(requestOptions).into(post_headportrait);

        post_username.setText(user_nikename);
        post_time.setText(article_time);
        post_content.setText(article_content);

        List list = new ArrayList();
        if(article_img!=null){
            if(!TextUtils.isEmpty(article_img)){
                String[] a = article_img.split("##");
                for(int i=0;i<a.length;i++){
                    list.add("http://"+getString(R.string.localhost)+"/pic_file/"+a[i]);
                    Log.i("TAG image:","http://"+getString(R.string.localhost)+"/pic_file/"+a[i]);
                }
            }
        }
        PostImageGridAdapter adapter = new PostImageGridAdapter(this,list);
        post_images_recycler.setLayoutManager(new GridLayoutManager(this, 3));
        //配置适配器
        post_images_recycler.setAdapter(adapter);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        reply_recyclerview.setLayoutManager(mLayoutManager);
        reply_recyclerview.addItemDecoration(new SpacesItemDecoration(1));
        List<Reply> replies = new ArrayList<>();
        replyAdapter = new ReplyAdapter(ReplyActivity.this,replies);
        reply_recyclerview.setAdapter(replyAdapter);
        reply_recyclerview.setNestedScrollingEnabled(false);
        setReplyData();
        Reply();
    }

    private void Reply() {
        reply.setOnClickListener(v->{
            String content = reply_edit.getText().toString();
            String user_phone = SpUtils.getString(ReplyActivity.this,"USERPHONE","");
            if(!TextUtils.isEmpty(content)){
                new Thread() {
                    @Override
                    public void run() {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("article_id", article_id+"");
                        params.put("reply_to_user_id", reply_to_user_id+"");
                        params.put("user_phone", user_phone);
                        params.put("reply_content", content);
                        try {
                            String url = "http://"+getString(R.string.localhost)+"/Bookeeping/AddReply";
                            response = okhttpUtils.getInstance().Connection(url, params);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.obj = response;
                        mHandler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }

    private void setReplyData() {
        new Thread() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put("article_id", article_id+"");
                Log.i("TAG article_id 2",article_id+"");
                try {
                    String url = "http://" + getString(R.string.localhost) + "/Bookeeping/getReply";
                    response = okhttpUtils.getInstance().Connection(url, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = mHandler.obtainMessage();
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if ("Failed".equals(response)) {
                Toast.makeText(ReplyActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if ("0".equals(response)) {
                Toast.makeText(ReplyActivity.this, "暂无评论", Toast.LENGTH_LONG).show();
            } else {
                Gson gson = new Gson();
                if (!TextUtils.isEmpty(response)) {
                    java.lang.reflect.Type type = new TypeToken<List<Reply>>() {
                    }.getType();
                    List<Reply> list = gson.fromJson(response, type);
                    for(int i =0;i<list.size();i++){
                        Log.i("TAG Reply item",list.get(i).getReply_content());
                    }
                    Log.i("TAG Reply",list.toString());

                    reply_edit.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    View v = getWindow().peekDecorView();
                    if (null != v) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    replyAdapter.setTextList(list);
                    replyAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    /**
     * RecyclerView设置间距辅助类
     */
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = space;
        }
    }
}
