package com.wyk.bookeeping.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.wyk.bookeeping.R;

public class LoginActivity extends AppCompatActivity {
    private ImageView im_return, im_change;
    private EditText ed_phonenum;
    private EditText ed_password;
    private Button login, login_findpassword, login_register;
    private TextView phonenum_err, password_err;

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
            String phonenum = ed_phonenum.getText().toString();
            String password = ed_password.getText().toString();
            if (!TextUtils.isEmpty(phonenum)&&!TextUtils.isEmpty(password)) {

            }else{
                Toast.makeText(LoginActivity.this,"请正确输入",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loseFocusListener() {
        //失去焦点验证
        phonenum_err.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // 此处为失去焦点时的处理内容
                String username = phonenum_err.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    phonenum_err.setText("请输入手机号");
                }
            }
        });

        phonenum_err.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // 此处为失去焦点时的处理内容
                String password = phonenum_err.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    phonenum_err.setText("请输入密码");
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
                ed_phonenum.setText(bundle.getString("phonenum"));
                ed_password.setText(bundle.getString("password"));
            }
            Toast.makeText(this, "已注册", Toast.LENGTH_SHORT).show();
        }
    }
}
