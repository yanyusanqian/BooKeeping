package com.wyk.bookeeping.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.SelectDialog;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.MyFragmentAdapter;
import com.wyk.bookeeping.bean.Icons;
import com.wyk.bookeeping.fragment.ExpenditureFragment;
import com.wyk.bookeeping.fragment.IncomeFragment;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.JsonParser;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookkeepingActivity extends AppCompatActivity {
    private View view_status;
    private Toolbar toolbar;
    private TabLayout tablayout_1;
    private ViewPager viewpager_content;
    private Button btn_cancle;
    private FloatingActionButton bk_speech_button;
    private RecognizerDialog mIatDialog;
    private String type, detils, count;
    private List<Icons> data_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_bookkeeping);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + "你自己的id");

        String json = SpUtils.getString(this, "ExIconsList");
        if (!TextUtils.isEmpty(json)) {
            Type type = new TypeToken<List<Icons>>() {
            }.getType();
            data_list = new Gson().fromJson(json, type);
        } else {
            Toast.makeText(BookkeepingActivity.this,"存储错误",Toast.LENGTH_SHORT).show();
        }

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
            mIatDialog.setParameter("nunum", "1");
            //开始识别并设置监听器
            mIatDialog.setListener(mRecognizerDialogListener);
            //显示听写对话框
            mIatDialog.show();
            Toast.makeText(BookkeepingActivity.this, "请开始说话", Toast.LENGTH_SHORT).show();
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

            DialogSettings.style = DialogSettings.STYLE_IOS;
            DialogSettings.use_blur = false;

            String text = JsonParser.parseIatResult(results.getResultString());
            Log.d("TAG Speech", "recognizer result text：" + text);

            if (!"".equals(text)) {
                Pattern p = Pattern.compile("^(收入|支出)((\\s|\\S)*[u4E00-u9FA5]*)(([1-9]\\d*)|0)(\\.\\d{0,2}){0,1}元$");
                Matcher m = p.matcher(text);
                boolean isMatch = m.matches();

                type = "支出";
                String text2 = "";
                if (isMatch) {
                    if (text.contains("收入")) {
                        type = "收入";
                        text2 = text.replaceAll("(收入)", "");
                    }
                    if (text.contains("支出")) {
                        text2 = text.replaceAll("支出", "");
                    }
                    if (text.contains("元")) {
                        text2 = text2.replaceAll("元", "");
                    }

                    count = text2.replaceAll("[\u4e00-\u9fa5]*", "");
                    detils = text2.replaceAll(count, "");
                    if (detils.equals("")) {
                        detils = "其他";
                    }

                    SelectDialog.show(BookkeepingActivity.this, "正确 符合规范",
                            "识别结果为:" + text + "\n记账类型:" + type + "\n记账类别:"
                                    + detils + "\n记账金额:" + count, (dialog, which) -> {
                                // 获取单例DBHelper
                                DBHelper dbHelper = DBHelper.getInstance(BookkeepingActivity.this);
                                // 创建一个新的SqliteDatabase对象
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                // 创建新的ContentValues对象，将数据存入其中
                                ContentValues values = new ContentValues();
                                Float c = Float.valueOf(count);
                                if(c%1==0){
                                    values.put("count", Math.abs(Double.valueOf(count)));
                                }else {
                                    DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                                    Log.i("TAG count",count);
                                    String p1 = decimalFormat.format(c);
                                    values.put("count", Float.valueOf(p1));
                                }
                                if("支出".equals(type)){
                                    values.put("inexType", 1);
                                }else{
                                    values.put("inexType", 0);
                                }
                                int flag = 0;
                                values.put("detailType", detils);
                                for(int i =0;i<data_list.size();i++){
                                    if(detils.equals(data_list.get(i).getTitle())){
                                        flag = 1;
                                        values.put("imgRes", data_list.get(i).getDrawableid());
                                    }
                                }
                                if(flag == 0){
                                    values.put("imgRes", R.drawable.n_bonus);
                                }
                                values.put("time", TimeUtil.getNowDateTime());
                                values.put("note", detils);
                                // 插入数据
                                Log.i("TAG",values.toString());
                                db.insert("account", null, values);
                                startActivity(new Intent(BookkeepingActivity.this, MainActivity.class));
                                finish();
                            });
                } else {
                    MessageDialog.show(BookkeepingActivity.this, "错误 不符合规范", "识别结果为:" + text);
                }
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Log.i("TAG Speech err", error.getPlainDescription(true));
        }

    };

    /**
     * 讯飞-参数设置
     *
     * @return
     */
    public void setParam() {
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(this, code -> {
            if (code != ErrorCode.SUCCESS)
                Log.i("TAG Speech", "语音识别初始化失败！");
        });
        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.SUBJECT, null);
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String mEngineType = SpeechConstant.TYPE_CLOUD;
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);

        /* 离线识别功能过少，已更换为在线识别//此处engineType为“local”
        String mEngineType= SpeechConstant.TYPE_LOCAL;
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        mIat.setParameter(SpeechConstant.ENGINE_MODE,SpeechConstant.MODE_AUTO);
        if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
            // 设置本地识别资源
            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        }*/
        //设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        // 取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        // 自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        mIat.setParameter("nunum", "1");
    }

    /**
     * 讯飞-设置本地识别资源
     *
     * @return
     */
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }

}
