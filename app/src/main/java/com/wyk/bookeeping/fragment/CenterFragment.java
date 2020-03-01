package com.wyk.bookeeping.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.CenterRecyclerViewAdapter;
import com.wyk.bookeeping.adpter.TestAdapter;
import com.wyk.bookeeping.bean.centerItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CenterFragment extends Fragment {
    private RecyclerView recyclerView_center;
    private Toolbar toolbar;
    private List<String> list;
    private TestAdapter testAdapter;
    private AppBarLayout appbar;
    private TextView textView;

    private List<centerItem> centerItemList;
    private centerItem centerItem;
    private CenterRecyclerViewAdapter centerRecyclerViewAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_center, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolBar();
        initView();
        initRecyclerViewItemData();
        initRecyclerView();
    }

    private void initRecyclerViewItemData() {
        centerItemList = new ArrayList<>();

        /*String[] titleList = {"消息","定时提醒","类别设置","导出数据","设置"};
        for(int i = 0;i<5;i++){
            centerItem = new centerItem(centerRecyclerViewAdapter.TYPE_ICONS, 1,titleList[i]);
            if (i % 2 == 0) {
                centerItem.isShow = true;
                centerItem.count = 6;
            } else {
                centerItem.isShow = false;
                centerItem.count = 0;
                centerItemList.add(centerItem);
            }
        }*/
        centerItem = new centerItem(centerRecyclerViewAdapter.TYPE_ICONS, 1);
        centerItemList.add(centerItem);

        centerItem = new centerItem(centerRecyclerViewAdapter.TYPE_BILL, 5);
        centerItem.setString("type2");
        centerItemList.add(centerItem);

        centerItem = new centerItem(centerRecyclerViewAdapter.TYPE_BUDGET, 5);
        centerItem.setString("type3");
        centerItemList.add(centerItem);

        centerItem = new centerItem(centerRecyclerViewAdapter.TYPE_COMMONFUNCTIONS, 5);
        centerItem.setString("type4");
        centerItemList.add(centerItem);

        centerItem = new centerItem(centerRecyclerViewAdapter.TYPE_SETTING, 5);
        centerItem.setString("type5");
        centerItemList.add(centerItem);
    }

    private void initRecyclerView(){
        centerRecyclerViewAdapter = new CenterRecyclerViewAdapter(getActivity(),centerItemList);
        recyclerView_center = (RecyclerView)getActivity().findViewById(R.id.recyclerView_center);
        recyclerView_center.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_center.setAdapter(centerRecyclerViewAdapter);
    }
    private void initView(){
        list  = new ArrayList<>();
        for(int x = 0; x < 20; x++){
            list.add("x:"+x);
        }

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        appbar = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        textView = (TextView) getActivity().findViewById(R.id.textview);

        final int alphaMaxOffset = dpToPx(130);
        toolbar.getBackground().mutate().setAlpha(0);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // 设置 toolbar 背景
                if (verticalOffset > -alphaMaxOffset) {
                    toolbar.getBackground().mutate().setAlpha(255 * -verticalOffset / alphaMaxOffset);
                    textView.setText("");
                } else {
                    toolbar.getBackground().mutate().setAlpha(255);
                    textView.setText("我的");

                }
            }
        });
    }

    private void initToolBar() {
        try {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            if (toolbar != null) {
                // 沉浸模式
                int statusBarHeight = getStatusBarHeight();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    openAndroidLStyle();

                    toolbar.setPadding(0, statusBarHeight, 0, 0);
                    toolbar.getLayoutParams().height = dpToPx(46) + statusBarHeight;
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    /*window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(activity.getResources().getColor(colorId));*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启沉浸式模式支持
     */
    private void openAndroidLStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * dp转换为px
     */
    private static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }
}
