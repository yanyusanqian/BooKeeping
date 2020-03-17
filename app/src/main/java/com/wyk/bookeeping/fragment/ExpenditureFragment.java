package com.wyk.bookeeping.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
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
import com.wyk.bookeeping.MainActivity;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.IconsAdpter;
import com.wyk.bookeeping.bean.Icons;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenditureFragment extends Fragment {
    private RecyclerView gridview_expend;
    private IconsAdpter iconsAdpter;
    private List<Icons> data_list, data_list_s;
    private EditText editText;
    private TextView text_count, kb_tv_done, kb_tv_add, kb_tv_sub,date_text;
    private TextView kb_tv_1, kb_tv_2, kb_tv_3, kb_tv_4, kb_tv_5, kb_tv_6, kb_tv_7, kb_tv_8, kb_tv_9, kb_tv_0, kb_tv_point;
    private LinearLayout date_change, text_delete;
    private ImageView date_image;
    private int postion;
    private PopupLayout popupLayout;
    boolean flag = false;// 符号标识
    private String str = "";
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenditure, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridview_expend = (RecyclerView) getActivity().findViewById(R.id.gridview_expend);
        //新建List
        data_list = new ArrayList<>();
        //获取数据
        getData();

        iconsAdpter = new IconsAdpter(getActivity(), data_list, data_list_s);
        iconsAdpter.setOnMyItemClickListener(new IconsAdpter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int pos) {
                postion = pos;
                view = View.inflate(getActivity(), R.layout.layout_keyboard, null);

                popupLayout = PopupLayout.init(getActivity(), view);
                popupLayout.setUseRadius(false);
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom - oldBottom < -1) {
                            //软键盘弹上去了,动态设置高度为0
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    0);
                            v.findViewById(R.id.tablelayout).setLayoutParams(params);
                        } else if (bottom - oldBottom > 1) {
                        }
                    }
                });
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
        kb_tv_add = view.findViewById(R.id.kb_tv_add);
        kb_tv_sub = view.findViewById(R.id.kb_tv_sub);
        kb_tv_1 = view.findViewById(R.id.kb_tv_1);
        kb_tv_2 = view.findViewById(R.id.kb_tv_2);
        kb_tv_3 = view.findViewById(R.id.kb_tv_3);
        kb_tv_4 = view.findViewById(R.id.kb_tv_4);
        kb_tv_5 = view.findViewById(R.id.kb_tv_5);
        kb_tv_6 = view.findViewById(R.id.kb_tv_6);
        kb_tv_7 = view.findViewById(R.id.kb_tv_7);
        kb_tv_8 = view.findViewById(R.id.kb_tv_8);
        kb_tv_9 = view.findViewById(R.id.kb_tv_9);
        kb_tv_0 = view.findViewById(R.id.kb_tv_0);
        kb_tv_point = view.findViewById(R.id.kb_tv_point);
        text_delete = view.findViewById(R.id.text_delete);
        date_change = view.findViewById(R.id.date_change);

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
                        if("".equals(str)){
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
                        String note = editText.getText().toString();
                        if (TextUtils.isEmpty(note)) {
                            note = data_list.get(postion).getTitle();
                        }
                        DBHelper dbHelper = DBHelper.getInstance(getActivity());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("count", Math.abs(Float.valueOf(text_count.getText().toString())));
                        values.put("inexType", 1);
                        values.put("detailType", data_list.get(postion).getTitle());
                        values.put("imgRes", data_list_s.get(postion).getDrawableid());
                        if("今天".equals(date_text.getText().toString())){
                            values.put("time", TimeUtil.getNowDate());
                        }else{
                            values.put("time", date_text.getText().toString());
                        }

                        values.put("note", note);

                        db.insert("account", null, values);

                        popupLayout.dismiss();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                    break;
                case R.id.date_change:
                    popupLayout.hide();
                    TimePickerView pvTime = new TimePickerBuilder(view.getContext(), new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            Log.i("HERE click", "1111111111");
                            Toast.makeText(getActivity(), TimeUtil.date2String(date, "yyyy-MM-dd"), Toast.LENGTH_SHORT).show();
                            String textDate = TimeUtil.date2String(date, "yyyy-MM-dd");
                            String nowDate = TimeUtil.getNowDate();
                            if(textDate.equals(nowDate)){
                                date_image.setVisibility(View.VISIBLE);
                                date_text.setText("今天");
                                popupLayout.show();
                            }else{
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

    private String getResult() {                            //算法
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getData() {
        String json = SpUtils.getString(getActivity(), "ExIconsList");
        String json_s = SpUtils.getString(getActivity(), "ExSIconsList");
        if (!TextUtils.isEmpty(json)) {
            Type type = new TypeToken<List<Icons>>() {
            }.getType();
            data_list = new Gson().fromJson(json, type);
            data_list_s = new Gson().fromJson(json_s, type);
        } else {
            int[] ex_icon = {
                    R.drawable.n_food, R.drawable.n_shopping, R.drawable.n_dailyneces, R.drawable.n_traffic,
                    R.drawable.n_vegetables, R.drawable.n_fruit, R.drawable.n_snacks, R.drawable.n_sport,
                    R.drawable.n_entertainment, R.drawable.n_communication, R.drawable.n_clothes, R.drawable.n_cosmetology,
                    R.drawable.n_house, R.drawable.n_home, R.drawable.n_child, R.drawable.n_oldman,
                    R.drawable.n_socialcontact, R.drawable.n_travel, R.drawable.n_tobacco, R.drawable.n_digital,
                    R.drawable.n_car, R.drawable.n_medicalcare, R.drawable.n_book, R.drawable.n_education,
                    R.drawable.n_pets, R.drawable.n_cashgift, R.drawable.n_gift, R.drawable.n_work,
                    R.drawable.n_carrepair, R.drawable.n_donation, R.drawable.n_lottery, R.drawable.n_friends,
                    R.drawable.n_express, R.drawable.n_setting};
            String[] ex_iconName = {
                    "餐饮", "购物", "日用", "交通",
                    "蔬菜", "水果", "零食", "运动",
                    "娱乐", "通讯", "服饰", "美容",
                    "住房", "居家", "孩子", "长辈",
                    "社交", "旅行", "烟酒", "数码",
                    "汽车", "医疗", "书籍", "学习",
                    "宠物", "礼金", "礼物", "办公",
                    "维修", "捐赠", "彩票", "亲友",
                    "快递", "设置"};

            for (int i = 0; i < ex_icon.length; i++) {
                Icons icons = new Icons(ex_icon[i], ex_iconName[i]);
                data_list.add(icons);
            }
        }
    }
}
