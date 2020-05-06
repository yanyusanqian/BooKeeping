package com.wyk.bookeeping.activity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.okhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class testActivity extends AppCompatActivity {
    private String response;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(v -> {
            List<Account> accountList = DBHelper.getInstance(this).getAllAccountList(this);
            if(accountList.size()==0){

            }else{
                Gson gson = new Gson();
                String json = gson.toJson(accountList);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_phone", SpUtils.getString(testActivity.this,"USERPHONE",""));
                    jsonObject.put("account",json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("TAG","HEER");
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
                                response = responsereslut.body().toString();
                                Log.i("TAG 1111",response);
                            }else{
                                response = "Failed";
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
               /* Message msg = mHandler.obtainMessage();
                msg.obj = response;
                mHandler.sendMessage(msg);*/
            }
        });
    }
}
