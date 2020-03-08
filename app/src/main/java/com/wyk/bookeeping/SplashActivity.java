package com.wyk.bookeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.wyk.bookeeping.bean.Icons;
import com.wyk.bookeeping.global.AppConstants;
import com.wyk.bookeeping.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 启动页
 */
public class SplashActivity extends Activity {
    private int[] ex_icon = {
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1};
    private String[] ex_iconName = {
            "餐饮", "购物", "日用", "交通",
            "蔬菜", "水果", "零食", "运动",
            "娱乐", "通讯", "服饰", "美容",
            "住房", "居家", "孩子", "长辈",
            "社交", "旅行", "烟酒", "数码",
            "汽车", "医疗", "书籍", "学习",
            "宠物", "礼金", "礼物", "办公",
            "维修", "捐赠", "彩票", "亲友"};
    private int[] in_icon = {
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1};
    private String[] in_iconName = {
            "工资", "兼职", "理财", "礼金",
            "其它", "设置"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            putIconsList();

            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);
    }

    private void putIconsList() {
        List<Icons> ex_iconsList = new ArrayList<>();
        for (int i = 0; i < ex_icon.length; i++) {
            Icons icons = new Icons(ex_icon[i], ex_iconName[i]);
            ex_iconsList.add(icons);
        }
        Gson gson = new Gson();
        String json = gson.toJson(ex_iconsList);
        SpUtils.putString(this, "ExIconsList", json);

        List<Icons> in_iconsList = new ArrayList<>();
        for (int i = 0; i < in_icon.length; i++) {
            Icons icons = new Icons(in_icon[i], in_iconName[i]);
            in_iconsList.add(icons);
        }
        Gson in_gson = new Gson();
        String in_json = gson.toJson(in_iconsList);
        SpUtils.putString(this, "InIconsList", in_json);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
