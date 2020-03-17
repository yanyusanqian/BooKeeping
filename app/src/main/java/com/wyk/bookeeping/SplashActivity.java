package com.wyk.bookeeping;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

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
            R.drawable.n_food, R.drawable.n_shopping, R.drawable.n_dailyneces, R.drawable.n_traffic,
            R.drawable.n_vegetables, R.drawable.n_fruit, R.drawable.n_snacks, R.drawable.n_sport,
            R.drawable.n_entertainment, R.drawable.n_communication, R.drawable.n_clothes, R.drawable.n_cosmetology,
            R.drawable.n_house, R.drawable.n_home, R.drawable.n_child, R.drawable.n_oldman,
            R.drawable.n_socialcontact, R.drawable.n_travel, R.drawable.n_tobacco, R.drawable.n_digital,
            R.drawable.n_car, R.drawable.n_medicalcare, R.drawable.n_book, R.drawable.n_education,
            R.drawable.n_pets, R.drawable.n_cashgift, R.drawable.n_gift, R.drawable.n_work,
            R.drawable.n_carrepair, R.drawable.n_donation, R.drawable.n_lottery, R.drawable.n_friends,
            R.drawable.n_express, R.drawable.n_setting};
    private String[] ex_iconName = {
            "餐饮", "购物", "日用", "交通",
            "蔬菜", "水果", "零食", "运动",
            "娱乐", "通讯", "服饰", "美容",
            "住房", "居家", "孩子", "长辈",
            "社交", "旅行", "烟酒", "数码",
            "汽车", "医疗", "书籍", "学习",
            "宠物", "礼金", "礼物", "办公",
            "维修", "捐赠", "彩票", "亲友",
            "快递", "设置"};
    private int[] ex_s_icon={
            R.drawable.s_food, R.drawable.s_shopping, R.drawable.s_dailyneces, R.drawable.s_traffic,
            R.drawable.s_vegetables, R.drawable.s_fruit, R.drawable.s_snacks, R.drawable.s_sport,
            R.drawable.s_entertainment, R.drawable.s_communication, R.drawable.s_clothes, R.drawable.s_cosmetology,
            R.drawable.s_house, R.drawable.s_home, R.drawable.s_child, R.drawable.s_oldman,
            R.drawable.s_socialcontact, R.drawable.s_travel, R.drawable.s_tobacco, R.drawable.s_digital,
            R.drawable.s_car, R.drawable.s_medicalcare, R.drawable.s_book, R.drawable.s_education,
            R.drawable.s_pets, R.drawable.s_cashgift, R.drawable.s_gift, R.drawable.s_work,
            R.drawable.s_carrepair, R.drawable.s_donation, R.drawable.s_lottery, R.drawable.s_friends,
            R.drawable.s_express, R.drawable.s_setting
    };
    private int[] in_icon = {
            R.drawable.n_wages, R.drawable.n_parttime, R.drawable.n_manage, R.drawable.n_cashgift,
            R.drawable.n_bonus, R.drawable.n_setting};
    private String[] in_iconName = {
            "工资", "兼职", "理财", "礼金",
            "其它", "设置"};
    private int[] in_s_icon = {
            R.drawable.s_wages, R.drawable.s_parttime, R.drawable.s_manage, R.drawable.s_cashgift,
            R.drawable.s_bonus, R.drawable.s_setting};

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
        TextView textview = (TextView) findViewById(R.id.font_textview);
        textview.setTypeface(Typeface.createFromAsset(getAssets(),"font/dataloo.ttf"));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);
    }

    private void putIconsList() {
        List<Icons> ex_iconsList = new ArrayList<>();
        List<Icons> ex_s_iconsList = new ArrayList<>();
        for (int i = 0; i < ex_icon.length; i++) {
            Icons icons = new Icons(ex_icon[i], ex_iconName[i]);
            Icons icons_s = new Icons(ex_s_icon[i],ex_iconName[i]);
            ex_iconsList.add(icons);
            ex_s_iconsList.add(icons_s);
        }
        Gson gson = new Gson();
        String json = gson.toJson(ex_iconsList);
        String json_s = gson.toJson(ex_s_iconsList);
        SpUtils.putString(this, "ExIconsList", json);
        SpUtils.putString(this, "ExSIconsList", json_s);

        List<Icons> in_iconsList = new ArrayList<>();
        List<Icons> in_s_iconsList = new ArrayList<>();
        for (int i = 0; i < in_icon.length; i++) {
            Icons icons = new Icons(in_icon[i], in_iconName[i]);
            Icons icons_s = new Icons(in_s_icon[i], in_iconName[i]);
            in_iconsList.add(icons);
            in_s_iconsList.add(icons_s);
        }
        Gson in_gson = new Gson();
        String in_json = in_gson.toJson(in_iconsList);
        String in_json_s = in_gson.toJson(in_s_iconsList);
        SpUtils.putString(this, "InIconsList", in_json);
        SpUtils.putString(this, "InSIconsList", in_json_s);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
