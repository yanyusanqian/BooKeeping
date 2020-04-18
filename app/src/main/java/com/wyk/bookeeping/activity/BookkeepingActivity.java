package com.wyk.bookeeping.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.MyFragmentAdapter;
import com.wyk.bookeeping.fragment.ExpenditureFragment;
import com.wyk.bookeeping.fragment.IncomeFragment;
import com.wyk.bookeeping.utils.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class BookkeepingActivity extends AppCompatActivity {
    private View view_status;
    private Toolbar toolbar;
    private TabLayout tablayout_1;
    private ViewPager viewpager_content;
    private Button btn_cancle;
    private FloatingActionButton bk_speech_button;
    private RecognizerDialog mIatDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_bookkeeping);

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5e9a5589");

        view_status = (View) findViewById(R.id.view_status);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tablayout_1 = (TabLayout) findViewById(R.id.tablayout_1);
        viewpager_content = (ViewPager) findViewById(R.id.viewpager_content);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        bk_speech_button = (FloatingActionButton) findViewById(R.id.bk_speech_button);

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setViewPager();

        viewpager_content.setOffscreenPageLimit(1);
        tablayout_1.setupWithViewPager(viewpager_content);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // 设置语音听写
        bk_speech_button.setOnClickListener(v -> {
            // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
            // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
            mIatDialog = new RecognizerDialog(BookkeepingActivity.this, code -> {
                if (code != ErrorCode.SUCCESS)
                    Log.i("TAG Speech", "语音识别初始化失败！");
            });
            setParam();
            //开始识别并设置监听器
            mIatDialog.setListener(mRecognizerDialogListener);
            //显示听写对话框
            mIatDialog.show();
            Toast.makeText(BookkeepingActivity.this,"请开始说话",Toast.LENGTH_SHORT).show();
        });


    }

    private void setViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();
        fragmentList.add(new ExpenditureFragment());
        fragmentList.add(new IncomeFragment());
        fragmentTitles.add("支出");
        fragmentTitles.add("收入");
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList, fragmentTitles, this, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewpager_content.setAdapter(adapter);
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d("TAG Speech", "recognizer result：" + results.getResultString());

            String text = JsonParser.parseIatResult(results.getResultString());
            Log.d("TAG Speech", "recognizer result text："+text);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Log.i("TAG Speech err",error.getPlainDescription(true));

        }

    };

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIatDialog.setParameter(SpeechConstant.PARAMS, null);
        mIatDialog.setParameter(SpeechConstant.SUBJECT, null);
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //此处engineType为“local”
        mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        //设置语音输入语言，zh_cn为简体中文
        mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        // 取值范围{1000～10000}
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        // 自动停止录音，范围{0~10000}
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIatDialog.setParameter(SpeechConstant.ASR_PTT, "0");


    }

}
