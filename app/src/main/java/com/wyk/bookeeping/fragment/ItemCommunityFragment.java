package com.wyk.bookeeping.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.activity.ReplyActivity;
import com.wyk.bookeeping.adpter.PostListAdapter;
import com.wyk.bookeeping.bean.Article;
import com.wyk.bookeeping.utils.okhttpUtils;

import java.util.HashMap;
import java.util.List;

public class ItemCommunityFragment extends Fragment {
    private int Type;
    private TextView test_text;
    private RecyclerView community_recycle_view;
    private PostListAdapter postListAdapter;
    private String response;
    private RefreshLayout reLayout = null;


    static ItemCommunityFragment newInstance(int Type) {
        Log.i("TAG Type", Type + " :1");
        ItemCommunityFragment fragment = new ItemCommunityFragment();
        Bundle args = new Bundle();
        args.putInt("Type", Type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_communitylist, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        community_recycle_view = view.findViewById(R.id.community_recycle_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        community_recycle_view.setLayoutManager(mLayoutManager);
        community_recycle_view.addItemDecoration(new SpacesItemDecoration(20));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            Type = getArguments().getInt("Type");
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        switch (Type) {
            // 最热
            case 1:
                Refresh();
                getArticleByTime();
                break;
            // 最新
            case 2:
//                test_text.setText(22222+"");
                break;
        }
    }

    private void getArticleByTime(){
        new Thread() {
            @Override
            public void run() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Type", "1");
                try {
                    String url = "http://" + getString(R.string.localhost) + "/Bookeeping/getAllArticle";
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
                Toast.makeText(getActivity(), "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if ("0".equals(response)) {
                Toast.makeText(getActivity(), "网络错误，请稍后再试", Toast.LENGTH_LONG).show();
            } else {
                if(reLayout!=null){
                    reLayout.finishRefresh();
                }

                Gson gson = new Gson();
                if (!TextUtils.isEmpty(response)) {
                    java.lang.reflect.Type type = new TypeToken<List<Article>>() {
                    }.getType();
                    List<Article> list = gson.fromJson(response, type);
                    for (int i =0;i<list.size();i++){
                        Log.i("TAG item",list.get(i).getUser_image());
                    }

                    PostListAdapter adapter = new PostListAdapter(getActivity(),list);
                    adapter.setOnMyItemClickListener((v, pos) -> {
                        Intent intent = new Intent(getActivity(), ReplyActivity.class);
                        intent.putExtra("article_id", list.get(pos).getArticle_id());
                        intent.putExtra("user_nikename", list.get(pos).getUser_nikename());
                        intent.putExtra("user_headportrait", list.get(pos).getUser_image());
                        intent.putExtra("user_id", list.get(pos).getUser_id());
                        intent.putExtra("article_time", list.get(pos).getArticle_time());
                        intent.putExtra("article_content", list.get(pos).getArticle_content());
                        intent.putExtra("article_img", list.get(pos).getArticle_image());
                        getActivity().startActivity(intent);
                    });
                    /*adapter.setOnMyItemImageClickListener((v,pos)->{

                    });*/
                    community_recycle_view.setAdapter(adapter);

                }
            }
        }
    };


    private void Refresh(){
        RefreshLayout refreshLayout = (RefreshLayout)getActivity().findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout re) {
                reLayout = re;
                getArticleByTime();
            }
        });
    }

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
