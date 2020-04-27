package com.wyk.bookeeping.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.wyk.bookeeping.R;
import com.wyk.bookeeping.utils.CountDownTimerUtils;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.okhttpUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText ed_phonenum, ed_password, register_code;
    private Button register_code_button, register;
    private TextView code_err, phonenum_err, password_err;
    private ImageView im_return, im_change;
    String vcode = "";
    String response, phonenum, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        ed_phonenum = findViewById(R.id.register_phonenum);
        ed_password = findViewById(R.id.register_password);
        register_code = findViewById(R.id.register_code);
        register_code_button = findViewById(R.id.register_code_button);
        register = findViewById(R.id.register);
        code_err = findViewById(R.id.register_code_err);
        phonenum_err = findViewById(R.id.register_phonenum_err);
        password_err = findViewById(R.id.register_password_err);
        im_return = findViewById(R.id.register_return);
        im_change = findViewById(R.id.register_im_change);

        // 失焦检测
        loseFocusListener();
        // 密码显隐
        passwordChange();
        im_return.setOnClickListener(v -> {
            finish();
        });
        // 生成验证码
        sendVerficationCode();

        register.setOnClickListener(v -> {
            Log.i("HERE", "11111111");
            phonenum = ed_phonenum.getText().toString();
            password = ed_password.getText().toString();
            String code = register_code.getText().toString();
            if (!TextUtils.isEmpty(phonenum) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(code)) {
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
                if (!code.equals(vcode)) {
                    code_err.setText("验证码错误");
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("user_phone", phonenum);
                        params.put("user_password", password);
                        params.put("user_nikename", "未命名");
                        try {
                            Log.i("HERE", "33333");
                            String url = "http://" + getString(R.string.localhost) + "/Bookeeping/RegisterUser";
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
                Toast.makeText(RegisterActivity.this, "请正确输入", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if ("1".equals(response)) {
                Intent intent = RegisterActivity.this.getIntent();
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    bundle.putString("userphone", phonenum);
                    bundle.putString("password", password);
                    intent.putExtras(bundle);
                }
                setResult(LoginActivity.RESULT_OK, intent);//返回登录页
                finish();
            } else if ("0".equals(response)) {
                phonenum_err.setText("手机号已注册");
            }
            Log.i("msg:",msg.obj+"");
            if("wrong".equals(msg.obj+"")){
                Toast.makeText(RegisterActivity.this,"网络不太好，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        }

    };


    /**
     * 用notification模拟短信，发送验证码(随机生成的6位数字)
     */
    private void sendVerficationCode() {
        register_code_button.setOnClickListener(v -> {
/*
            String CHANNEL_ID = "my_channel_01";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "my_channel_name";
                String description = "my_channel_description";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                Notification notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                        .setContentTitle("This is content title")
                        .setContentText("This is content text")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.appicon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.appicon))
                        .build();
                notificationManager.notify(1,notification);
            }

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(RegisterActivity.this,CHANNEL_ID).build();*/

            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(register_code_button, 60000, 1000); //倒计时1分钟
            mCountDownTimerUtils.start();

            vcode = (int) ((Math.random() * 9 + 1) * 100000) + "";
            // 获取系统 通知管理 服务
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 构建 Notification
            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentTitle("Bookeeping 验证码")
                    .setSmallIcon(R.mipmap.appicon)
                    .setContentText(vcode);
            // 兼容  API 26，Android 8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
                NotificationChannel notificationChannel = new NotificationChannel("VerificationCodeNotificationId", "VerificationCodeNotificationName", NotificationManager.IMPORTANCE_DEFAULT);
                // 注册通道，注册后除非卸载再安装否则不改变
                notificationManager.createNotificationChannel(notificationChannel);
                builder.setChannelId("VerificationCodeNotificationId");
            }
            // 发出通知
            notificationManager.notify(1, builder.build());
        });
    }


    /**
     * 点击图片，切换密码显示or隐藏
     */
    private void passwordChange() {
        im_change.setOnClickListener(v -> {
            TransformationMethod method = ed_password.getTransformationMethod();
            if (method == HideReturnsTransformationMethod.getInstance()) {
                ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                im_change.setBackgroundResource(R.drawable.eyes_close);
            } else {
                ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                im_change.setBackgroundResource(R.drawable.eyes_open);
            }
            // 保证切换后光标位于文本末尾
            Spannable spanText = ed_password.getText();
            if (spanText != null) {
                Selection.setSelection(spanText, spanText.length());
            }
        });
    }


    /**
     * 输入框失去焦点时检测
     */
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

        register_code.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // 此处为失去焦点时的处理内容
                String password = register_code.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    code_err.setText("请输入验证码");
                }
            }
        });
    }
}
