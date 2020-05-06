package com.wyk.bookeeping.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.Article;
import com.wyk.bookeeping.bean.User;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.okhttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private ImageView im_return, im_change;
    private EditText ed_phonenum;
    private EditText ed_password;
    private Button login, login_findpassword, login_register;
    private TextView phonenum_err, password_err;
    private String response;
    private String phonenum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        loseFocusListener();
    }

    private void initView() {
        im_return = findViewById(R.id.login_return);
        im_change = findViewById(R.id.login_change);
        ed_phonenum = findViewById(R.id.login_phonenum);
        ed_password = findViewById(R.id.login_password);
        login = findViewById(R.id.login);
        login_findpassword = findViewById(R.id.login_findpassword);
        login_register = findViewById(R.id.login_register);
        phonenum_err = findViewById(R.id.login_phonenum_err);
        password_err = findViewById(R.id.login_password_err);

        im_return.setOnClickListener(v -> {
            finish();
        });

        im_change.setOnClickListener(v -> {
            TransformationMethod method = ed_password.getTransformationMethod();
            if (method == HideReturnsTransformationMethod.getInstance()) {
                ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                im_change.setBackgroundResource(R.drawable.eyes_open);
            } else {
                ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                im_change.setBackgroundResource(R.drawable.eyes_close);
            }
            // 保证切换后光标位于文本末尾
            Spannable spanText = ed_password.getText();
            if (spanText != null) {
                Selection.setSelection(spanText, spanText.length());
            }
        });

        login_findpassword.setOnClickListener(v -> {
            //跳转到找回密码页面
            startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
        });

        login_register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        });

        login.setOnClickListener(v -> {
            phonenum = ed_phonenum.getText().toString();
            String password = ed_password.getText().toString();
            if (!TextUtils.isEmpty(phonenum) && !TextUtils.isEmpty(password)) {
                if (phonenum.length() != 11) {
                    phonenum_err.setText("长度不太对哦~请输入正确的手机号");
                    return;
                }
                Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{4,8}$");
                Matcher m = p.matcher(phonenum);
                boolean isMatch = m.matches();
                if (!isMatch) {
                    phonenum_err.setText("请输入正确的手机号");
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("user_phone", phonenum);
                        params.put("user_password", password);
                        try {
                            Log.i("HERE", "33333");
                            String url = "http://" + getString(R.string.localhost) + "/Bookeeping/LoginUser";
                            Log.i("HERE url", url);
                            response = okhttpUtils.getInstance().Connection(url, params);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = mHandler.obtainMessage();
                            msg.obj = "wrong";
                            mHandler.sendMessage(msg);
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.obj = response;
                        mHandler.sendMessage(msg);
                    }
                }.start();

            } else {
                Toast.makeText(LoginActivity.this, "请正确输入", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("Login Response", response);
            if ("Failed".equals(response)) {
                Toast.makeText(LoginActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if ("0".equals(response)) {
                phonenum_err.setText("手机号不存在");
            } else if ("1".equals(response)) {
                phonenum_err.setText("手机号不存在或密码错误");
            } else if ("wrong".equals(msg.obj + "")) {
                Toast.makeText(LoginActivity.this, "网络连接失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                if (!TextUtils.isEmpty(response)) {
                    java.lang.reflect.Type type = new TypeToken<List<User>>() {
                    }.getType();
                    List<User> list = gson.fromJson(response, type);
                    SpUtils.putString(LoginActivity.this, "USERPHONE", phonenum);
                    SpUtils.putString(LoginActivity.this, "USERSTATUS", "1");
                    if (list.get(0).getUser_nikename().equals("未命名"))
                        SpUtils.putString(LoginActivity.this, "USERNAME", "未命名");
                    else
                        SpUtils.putString(LoginActivity.this, "USERNAME", list.get(0).getUser_nikename());
                    if (list.get(0).getUser_image() != null)
                        SpUtils.putString(LoginActivity.this, "USERIMAGE", list.get(0).getUser_image());

                    // 要判断两者数据库是否相同，多的加上去。
                    // 这样，假设本地没数据，就直接跳转到启动页，拉取远程数据。
                    // 如果本地有数据，就把本地数据传上去，按userphone插入到远程数据库中，再拉取远程数据库的内容到本地数据库来。

                    startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                    finish();

                }
            }
        }
    };


    private void loseFocusListener() {
        //失去焦点验证
        ed_phonenum.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // 此处为失去焦点时的处理内容
                String username = ed_phonenum.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    phonenum_err.setText("请输入手机号");
                }
            }
        });

        ed_password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // 此处为失去焦点时的处理内容
                String password = ed_password.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    password_err.setText("请输入密码");
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RegisterActivity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                ed_phonenum.setText(bundle.getString("userphone"));
                ed_password.setText(bundle.getString("password"));
            }
            Toast.makeText(this, "已注册", Toast.LENGTH_SHORT).show();
        }
    }
}

