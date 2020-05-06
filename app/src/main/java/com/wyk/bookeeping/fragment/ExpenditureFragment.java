package com.wyk.bookeeping.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.codingending.popuplayout.PopupLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyk.bookeeping.activity.LoginActivity;
import com.wyk.bookeeping.activity.MainActivity;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.IconsAdpter;
import com.wyk.bookeeping.bean.Icons;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;
import com.wyk.bookeeping.utils.okhttpUtils;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExpenditureFragment extends Fragment {
    private List<Icons> data_list, data_list_s;
    private EditText editText;
    private TextView text_count;
    private TextView kb_tv_done;
    private TextView date_text;
    private ImageView date_image;
    private int postion;
    private PopupLayout popupLayout;
    private boolean flag = false;// 符号标识
    private String str = "", response, note, time;
    View view;
    private Float count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenditure, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView gridview_expend = (RecyclerView) getActivity().findViewById(R.id.gridview_expend);
        //新建List
        data_list = new ArrayList<>();
        //获取数据
        getData();

        IconsAdpter iconsAdpter = new IconsAdpter(getActivity(), data_list, data_list_s);
        iconsAdpter.setOnMyItemClickListener(new IconsAdpter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int pos) {
                postion = pos;
                view = View.inflate(getActivity(), R.layout.layout_keyboard, null);

                popupLayout = PopupLayout.init(getActivity(), view);
                popupLayout.setUseRadius(false);
                initView(view);
                popupLayout.show(PopupLayout.POSITION_BOTTOM);
            }
        });

        gridview_expend.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        //配置适配器
        gridview_expend.setAdapter(iconsAdpter);

    }

    private void initView(View view) {
        editText = view.findViewById(R.id.edit_text);
        date_text = view.findViewById(R.id.date_text);
        date_image = view.findViewById(R.id.date_image);
        text_count = view.findViewById(R.id.text_count);
        kb_tv_done = view.findViewById(R.id.kb_tv_done);
        TextView kb_tv_add = view.findViewById(R.id.kb_tv_add);
        TextView kb_tv_sub = view.findViewById(R.id.kb_tv_sub);
        TextView kb_tv_1 = view.findViewById(R.id.kb_tv_1);
        TextView kb_tv_2 = view.findViewById(R.id.kb_tv_2);
        TextView kb_tv_3 = view.findViewById(R.id.kb_tv_3);
        TextView kb_tv_4 = view.findViewById(R.id.kb_tv_4);
        TextView kb_tv_5 = view.findViewById(R.id.kb_tv_5);
        TextView kb_tv_6 = view.findViewById(R.id.kb_tv_6);
        TextView kb_tv_7 = view.findViewById(R.id.kb_tv_7);
        TextView kb_tv_8 = view.findViewById(R.id.kb_tv_8);
        TextView kb_tv_9 = view.findViewById(R.id.kb_tv_9);
        TextView kb_tv_0 = view.findViewById(R.id.kb_tv_0);
        TextView kb_tv_point = view.findViewById(R.id.kb_tv_point);
        LinearLayout text_delete = view.findViewById(R.id.text_delete);
        LinearLayout date_change = view.findViewById(R.id.date_change);

        kb_tv_done.setOnClickListener(boradlistener);
        kb_tv_add.setOnClickListener(boradlistener);
        kb_tv_sub.setOnClickListener(boradlistener);
        kb_tv_1.setOnClickListener(boradlistener);
        kb_tv_2.setOnClickListener(boradlistener);
        kb_tv_3.setOnClickListener(boradlistener);
        kb_tv_4.setOnClickListener(boradlistener);
        kb_tv_5.setOnClickListener(boradlistener);
        kb_tv_6.setOnClickListener(boradlistener);
        kb_tv_7.setOnClickListener(boradlistener);
        kb_tv_8.setOnClickListener(boradlistener);
        kb_tv_9.setOnClickListener(boradlistener);
        kb_tv_0.setOnClickListener(boradlistener);
        kb_tv_point.setOnClickListener(boradlistener);
        text_delete.setOnClickListener(boradlistener);
        date_change.setOnClickListener(boradlistener);
    }

    View.OnClickListener boradlistener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.kb_tv_0:
                case R.id.kb_tv_1:
                case R.id.kb_tv_2:
                case R.id.kb_tv_3:
                case R.id.kb_tv_4:
                case R.id.kb_tv_5:
                case R.id.kb_tv_6:
                case R.id.kb_tv_7:
                case R.id.kb_tv_8:
                case R.id.kb_tv_9:
                case R.id.kb_tv_point:
                    str += ((TextView) v).getText().toString();
                    text_count.setText(str);
                    break;
                case R.id.kb_tv_add:
                    str = getResult() + "+";
                    text_count.setText(str);
                    flag = true;
                    kb_tv_done.setText("=");
                    kb_tv_done.setGravity(Gravity.CENTER);
                    kb_tv_done.setTextSize(24);
                    break;
                case R.id.kb_tv_sub:
                    str = getResult() + "-";
                    text_count.setText(str);
                    flag = true;
                    kb_tv_done.setText("=");
                    kb_tv_done.setGravity(Gravity.CENTER);
                    kb_tv_done.setTextSize(24);
                    break;
                case R.id.text_delete:
                    if (str != null && !str.equals("")) {
                        str = str.substring(0, str.length() - 1);
                        text_count.setText(str);    //删除一个字符
                        if ("".equals(str)) {
                            text_count.setText("0");
                        }
                    }
                    break;
                case R.id.kb_tv_done:
                    if (flag) {
                        str = getResult();
                        text_count.setText(str);
                        flag = false;
                        kb_tv_done.setText("完成");
                        kb_tv_done.setGravity(Gravity.CENTER);
                        kb_tv_done.setTextSize(20);
                    } else {
                        note = editText.getText().toString();
                        if (TextUtils.isEmpty(note)) {
                            note = data_list.get(postion).getTitle();
                        }
                        if (Float.valueOf(text_count.getText().toString()) == 0) {
                            Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                        } else {
                            String userPhone = SpUtils.getString(getActivity(), "USERPHONE", "");
                            if (Float.valueOf(text_count.getText().toString()) % 1 == 0) {
                                count = Math.abs(Float.valueOf(text_count.getText().toString()));
                            } else {
                                DecimalFormat decimalFormat = new DecimalFormat(".00");
                                String p = decimalFormat.format(Float.valueOf(text_count.getText().toString()));
                                count = Float.valueOf(p);
                            }
                            if ("今天".equals(date_text.getText().toString())) {
                                time = TimeUtil.getNowDate();
                            } else {
                                time = date_text.getText().toString();
                            }
                            // 如果用户登陆
                            if (!"".equals(userPhone)) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("user_phone", userPhone);
                                        params.put("bill_count", count + "");
                                        params.put("bill_inexType", "1");
                                        params.put("bill_detailType", data_list.get(postion).getTitle());
                                        params.put("bill_imgRes", data_list_s.get(postion).getDrawableid() + "");
                                        params.put("bill_time", time);
                                        params.put("bill_note", note);
                                        try {
                                            String url = "http://" + getString(R.string.localhost) + "/Bookeeping/AddAccount";
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
                                InsertAccountInLocalSql(0);
                                popupLayout.dismiss();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        }
                    }
                    break;
                case R.id.date_change:
                    popupLayout.hide();
                    TimePickerView pvTime = new TimePickerBuilder(view.getContext(), new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            Toast.makeText(getActivity(), TimeUtil.date2String(date, "yyyy-MM-dd"), Toast.LENGTH_SHORT).show();
                            String textDate = TimeUtil.date2String(date, "yyyy-MM-dd");
                            String nowDate = TimeUtil.getNowDate();
                            if (textDate.equals(nowDate)) {
                                date_image.setVisibility(View.VISIBLE);
                                date_text.setText("今天");
                                popupLayout.show();
                            } else {
                                date_image.setVisibility(View.GONE);
                                date_text.setText(textDate);
                                popupLayout.show();
                            }
                        }
                    })
                            .setType(new boolean[]{true, true, true, false, false, false})
                            .setCancelText("取消")
                            .setSubmitText("确认")
                            .setTitleText("选择日期")
                            .setTitleColor(Color.BLACK)//标题文字颜色
                            .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                            .setCancelColor(Color.BLACK)
                            .setLabel("年", "月", "日", "", "", "")
                            .build();
                    pvTime.show();
                    break;
            }
        }
    };

    /**
     * 向本地数据库插入账单数据
     */
    private void InsertAccountInLocalSql(int id) {
        // 获取单例DBHelper
        DBHelper dbHelper = DBHelper.getInstance(getActivity());
        // 创建一个新的SqliteDatabase对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 创建新的ContentValues对象，将数据存入其中
        ContentValues values = new ContentValues();
        if(id!=0)
            values.put("_id",id);
        values.put("count", count);
        values.put("inexType", 1);
        values.put("detailType", data_list.get(postion).getTitle());
        values.put("imgRes", data_list_s.get(postion).getDrawableid());
        values.put("time", time);
        values.put("note", note);
        // 插入数据
        Log.i("TAG 11:", values.toString());
        db.insert("account", null, values);
    }

    /**
     * 异步通讯回传数据处理
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if ("Failed".equals(response)) {
                Toast.makeText(getActivity(), "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if ("wrong".equals(msg.obj + "")) {
                Toast.makeText(getActivity(), "网络连接失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if ("0".equals(response)) {
                Toast.makeText(getActivity(), "插入失败", Toast.LENGTH_SHORT).show();
            } else {
                int id = Integer.parseInt(response);
                InsertAccountInLocalSql(id);
                popupLayout.dismiss();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }
    };


    /**
     * 简易计算器算法
     *
     * @return
     */
    private String getResult() {
        String s = text_count.getText().toString();
        float result = 0;
        boolean point = s.contains(".");
        if (!s.contains("+") && !s.contains("-")) {
            return s;
        } else if (s.contains("+")) {
            // 加法运算
            String[] a = s.split("\\+");
            for (String value : a) {
                if (!"".equals(value)) {
                    result += Float.parseFloat(value);
                }
            }
            if (point)
                return result + "";
            else
                return (int) result + "";
        } else if (s.contains("-")) {
            // 减法运算
            String[] a = s.split("-");

            for (int i = 0; i < a.length; i++) {
                if (!"".equals(a[i])) {
                    if (i == 0) {
                        result = Float.parseFloat(a[0]);
                        continue;
                    }
                    result -= Float.parseFloat(a[i]);
                }
            }
            if (point)
                return result + "";
            else
                return (int) result + "";
        }
        return s;
    }


    /**
     * 获取列表数据
     */
    private void getData() {
        String json = SpUtils.getString(getActivity(), "ExIconsList");
        String json_s = SpUtils.getString(getActivity(), "ExSIconsList");
        if (!TextUtils.isEmpty(json)) {
            Type type = new TypeToken<List<Icons>>() {
            }.getType();
            data_list = new Gson().fromJson(json, type);
            data_list_s = new Gson().fromJson(json_s, type);
        } else {
            Toast.makeText(getActivity(), "记账类型数据丢失,请重装软件", Toast.LENGTH_LONG).show();
        }
    }
}
