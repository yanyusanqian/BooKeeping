package com.wyk.bookeeping.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenditureFragment extends Fragment {
    private RecyclerView gridview_expend;
    private IconsAdpter iconsAdpter;
    private List<Icons> data_list;
    private EditText editText;
    private TextView text_count;
    private int postion;
    private PopupLayout popupLayout;
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
        Log.i("DATE:", data_list + "");
        iconsAdpter = new IconsAdpter(getActivity(), data_list);
        iconsAdpter.setOnMyItemClickListener(new IconsAdpter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int pos) {
                postion = pos;
                Log.i("HERE", "11111111");
                final View view = View.inflate(getActivity(), R.layout.layout_keyboard, null);
                popupLayout = PopupLayout.init(getActivity(), view);
                popupLayout.setUseRadius(false);
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom - oldBottom < -1) {
                            Log.i("HERE", "2222222222222222222222");
                            //软键盘弹上去了,动态设置高度为0
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    0);
                            view.findViewById(R.id.tablelayout).setLayoutParams(params);
                        } else if (bottom - oldBottom > 1) {
                            Log.i("HERE", "33333333333333333333333");
                        }
                    }
                });
                editText = view.findViewById(R.id.edit_text);
                text_count = view.findViewById(R.id.text_count);
                view.findViewById(R.id.kb_tv_done).setOnClickListener(boradlistener);
                popupLayout.show(PopupLayout.POSITION_BOTTOM);
            }
        });

        gridview_expend.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        //配置适配器
        gridview_expend.setAdapter(iconsAdpter);

    }

    View.OnClickListener boradlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.kb_tv_done:
                    Log.i("点击了","111");
                    if(TextUtils.isEmpty(editText.getText().toString())){
                        Toast.makeText(getActivity(),"还没有输入备注哦",Toast.LENGTH_LONG).show();
                    }else{
                        DBHelper dbHelper = DBHelper.getInstance(getActivity());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("count", Float.valueOf(text_count.getText().toString()));
                        values.put("inexType",1);
                        values.put("detailType", data_list.get(postion).getTitle());
                        values.put("imgRes", data_list.get(postion).getDrawableid());
                        values.put("time", TimeUtil.date2String(new Date(),"yyyy-MM-dd"));
                        values.put("note", editText.getText().toString());

                        db.insert("account",null,values);

                        popupLayout.dismiss();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();

                    }
                    break;

            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getData() {
        String json = SpUtils.getString(getActivity(), "ExIconsList");
        if (!TextUtils.isEmpty(json)) {
            Type type = new TypeToken<List<Icons>>() {
            }.getType();
            data_list = new Gson().fromJson(json, type);
        } else {
            int[] ex_icon = {
                    R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
                    R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
                    R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
                    R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
                    R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
                    R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
                    R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
                    R.drawable.pre1, R.drawable.pre1, R.drawable.pre1, R.drawable.pre1};
            String[] ex_iconName = {
                    "餐饮", "购物", "日用", "交通",
                    "蔬菜", "水果", "零食", "运动",
                    "娱乐", "通讯", "服饰", "美容",
                    "住房", "居家", "孩子", "长辈",
                    "社交", "旅行", "烟酒", "数码",
                    "汽车", "医疗", "书籍", "学习",
                    "宠物", "礼金", "礼物", "办公",
                    "维修", "捐赠", "彩票", "亲友"};

            for (int i = 0; i < ex_icon.length; i++) {
                Icons icons = new Icons(ex_icon[i], ex_iconName[i]);
                data_list.add(icons);
            }
        }
    }
}
