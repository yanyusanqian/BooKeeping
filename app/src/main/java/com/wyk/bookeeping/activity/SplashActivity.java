package com.wyk.bookeeping.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.Icons;
import com.wyk.bookeeping.global.AppConstants;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.NetUtil;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.okhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {
    private String response;
    private List<Account> accountList;
    private int[] ex_icon = {
            R.drawable.n_food, R.drawable.n_shopping, R.drawable.n_dailyneces, R.drawable.n_traffic,
            R.drawable.n_vegetables, R.drawable.n_fruit, R.drawable.n_snacks, R.drawable.n_sport,
            R.drawable.n_entertainment, R.drawable.n_communication, R.drawable.n_clothes, R.drawable.n_cosmetology,
            R.drawable.n_house, R.drawable.n_home, R.drawable.n_child, R.drawable.n_oldman,
            R.drawable.n_socialcontact, R.drawable.n_travel, R.drawable.n_tobacco, R.drawable.n_digital,
            R.drawable.n_car, R.drawable.n_medicalcare, R.drawable.n_book, R.drawable.n_education,
            R.drawable.n_pets, R.drawable.n_cashgift, R.drawable.n_gift, R.drawable.n_work,
            R.drawable.n_carrepair, R.drawable.n_donation, R.drawable.n_lottery, R.drawable.n_friends,
            R.drawable.n_express};
    private String[] ex_iconName = {
            "餐饮", "购物", "日用", "交通",
            "蔬菜", "水果", "零食", "运动",
            "娱乐", "通讯", "服饰", "美容",
            "住房", "居家", "孩子", "长辈",
            "社交", "旅行", "烟酒", "数码",
            "汽车", "医疗", "书籍", "学习",
            "宠物", "礼金", "礼物", "办公",
            "维修", "捐赠", "彩票", "亲友",
            "快递"};
    private int[] ex_s_icon = {
            R.drawable.s_food, R.drawable.s_shopping, R.drawable.s_dailyneces, R.drawable.s_traffic,
            R.drawable.s_vegetables, R.drawable.s_fruit, R.drawable.s_snacks, R.drawable.s_sport,
            R.drawable.s_entertainment, R.drawable.s_communication, R.drawable.s_clothes, R.drawable.s_cosmetology,
            R.drawable.s_house, R.drawable.s_home, R.drawable.s_child, R.drawable.s_oldman,
            R.drawable.s_socialcontact, R.drawable.s_travel, R.drawable.s_tobacco, R.drawable.s_digital,
            R.drawable.s_car, R.drawable.s_medicalcare, R.drawable.s_book, R.drawable.s_education,
            R.drawable.s_pets, R.drawable.s_cashgift, R.drawable.s_gift, R.drawable.s_work,
            R.drawable.s_carrepair, R.drawable.s_donation, R.drawable.s_lottery, R.drawable.s_friends,
            R.drawable.s_express
    };
    private int[] in_icon = {
            R.drawable.n_wages, R.drawable.n_parttime, R.drawable.n_manage, R.drawable.n_cashgift,
            R.drawable.n_bonus};
    private String[] in_iconName = {
            "工资", "兼职", "理财", "礼金",
            "其它"};
    private int[] in_s_icon = {
            R.drawable.s_wages, R.drawable.s_parttime, R.drawable.s_manage, R.drawable.s_cashgift,
            R.drawable.s_bonus};

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

        if (!NetUtil.iConnected(this)) {
            SpUtils.putString(this, "USERPHONE", "");
            SpUtils.putString(this, "USERSTATUS", "");
        }

        String userPhone = SpUtils.getString(this, "USERPHONE", "");
        String userStatus = SpUtils.getString(this, "USERSTATUS", "");

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);
        TextView textview = (TextView) findViewById(R.id.font_textview);
        textview.setTypeface(Typeface.createFromAsset(getAssets(), "font/dataloo.ttf"));

        if (!userPhone.equals("") && !userStatus.equals("")) {
            DialogSettings.style = DialogSettings.STYLE_IOS;
            DialogSettings.use_blur = false;
            WaitDialog.show(SplashActivity.this, "载入中...");

            accountList = DBHelper.getInstance(SplashActivity.this).getAllAccountList(SplashActivity.this);
            if(accountList.size()==0){
                Log.i("TAG","本地数据库中无数据");
                new Thread() {
                    @Override
                    public void run() {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("user_phone", userPhone);
                        try {
                            String url = "http://" + getString(R.string.localhost) + "/Bookeeping/getAccount";
                            response = okhttpUtils.getInstance().Connection(url, params);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = mHandler.obtainMessage();
                            msg.obj = "Failed";
                            mHandler.sendMessage(msg);
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.obj = response;
                        mHandler.sendMessage(msg);
                    }
                }.start();
            }else{
                Log.i("TAG","本地数据库中有数据");
                Gson gson = new Gson();
                String json = gson.toJson(accountList);
                Log.i("TAG json",json);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_phone", userPhone);
                    jsonObject.put("account",json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = "http://" + getString(R.string.localhost) + "/Bookeeping/syncAccount";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //申明给服务端传递一个json串
                        //创建一个OkHttpClient对象
                        OkHttpClient okHttpClient = new OkHttpClient();
                        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                        RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));
                        //创建一个请求对象
                        Request request = new Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                        //发送请求获取响应

                        Response responsereslut= null;
                        try {
                            responsereslut = okHttpClient.newCall(request).execute();
                            //判断请求是否成功
                            if(responsereslut.isSuccessful()){
                                //打印服务端返回结果
                                response = responsereslut.body().string();
                                Log.i("TAG 1111",response);
                            }else{
                                response = "Failed";
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Message msg = mHandler.obtainMessage();
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHomeActivity();
                }
            }, 2000);
        }
    }


    /**
     * 异步通讯回传数据处理
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if ("0".equals(response)) {
                Toast.makeText(SplashActivity.this,"远程无数据",Toast.LENGTH_LONG).show();
                TipDialog.show(SplashActivity.this, "完成", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                enterHomeActivity();
            }else if("1".equals(response)){
                Toast.makeText(SplashActivity.this,"移动端无数据，出现了不应该出现的操作！",Toast.LENGTH_LONG).show();
                TipDialog.show(SplashActivity.this, "错误", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_ERROR);
            }else if ("Failed".equals(response)) {
                Toast.makeText(SplashActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                TipDialog.show(SplashActivity.this, "网络错误", TipDialog.SHOW_TIME_LONG, TipDialog.TYPE_ERROR);
            } else{
                Log.i("HERE!","HERE");
                DBHelper.getInstance(SplashActivity.this).deleteAccount(SplashActivity.this);
                Gson gson = new Gson();
                Log.i("TAG response sysn",response);
                if (!TextUtils.isEmpty(response)) {
                    Log.i("TAG account",response);
                    java.lang.reflect.Type type = new TypeToken<List<Account>>() {
                    }.getType();
                    List<Account> list = gson.fromJson(response, type);


                    DBHelper.getInstance(SplashActivity.this).InsertAllAccount(SplashActivity.this, list);
                }
                SpUtils.putString(SplashActivity.this, "USERSTATUS", "");
                TipDialog.show(SplashActivity.this, "完成", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                enterHomeActivity();
            }

        }
    };

    private void putIconsList() {
        List<Icons> ex_iconsList = new ArrayList<>();
        List<Icons> ex_s_iconsList = new ArrayList<>();
        for (int i = 0; i < ex_icon.length; i++) {
            Icons icons = new Icons(ex_icon[i], ex_iconName[i]);
            Icons icons_s = new Icons(ex_s_icon[i], ex_iconName[i]);
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
