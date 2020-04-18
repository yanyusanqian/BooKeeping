package com.wyk.bookeeping.utils;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class okhttpUtils {

    private static okhttpUtils okHttp = new okhttpUtils();
    /*创建OkHttpClient对象*/
    private OkHttpClient client;

    public static okhttpUtils getInstance() {
        return okHttp;
    }

    private okhttpUtils() {
        client = new OkHttpClient.Builder()
                .build();
    }

    public String Connection(String url, HashMap<String, String> params) throws Exception {

        final HttpUrl.Builder urlBuilder;

        /*连接参数*/
        urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                urlBuilder.setQueryParameter(key, params.get(key));
            }
        }

        /*创建Request对象,get方式不需要封装表单数据,给url*/
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        //创建Call对象: 执行请求和获取相应
        Call call = client.newCall(request);
        Response response = call.execute();

        String result = "";
        if (response.isSuccessful()) {
            //响应结果: 字符串,字节,字符流,字节流
            result = response.body().string();
        } else {
            result = "Failed";
        }
        return result;
    }

}
