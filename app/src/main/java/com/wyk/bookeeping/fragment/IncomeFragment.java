package com.wyk.bookeeping.fragment;

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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomeFragment extends Fragment {
    private RecyclerView gridview_Income;
    private IconsAdpter iconsAdpter;
    private List<Icons> data_list,data_list_s;
    private int postion;
    private PopupLayout popupLayout;
    private EditText editText;
    private TextView text_count;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridview_Income = (RecyclerView) getActivity().findViewById(R.id.gridview_income);
        //新建List
        data_list = new ArrayList<>();
        //获取数据
        getData();
        iconsAdpter = new IconsAdpter(getActivity(),data_list,data_list_s);
        iconsAdpter.setOnMyItemClickListener(new IconsAdpter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int pos) {
                postion = pos;
                final View view = View.inflate(getActivity(), R.layout.layout_keyboard, null);
                popupLayout = PopupLayout.init(getActivity(), view);
                popupLayout.setUseRadius(false);
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom - oldBottom < -1) {
                            //软键盘弹上去了,动态设置高度为0
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    0);
                            view.findViewById(R.id.tablelayout).setLayoutParams(params);
                        } else if (bottom - oldBottom > 1) {

                        }
                    }
                });
                editText = view.findViewById(R.id.edit_text);
                text_count = view.findViewById(R.id.text_count);
                view.findViewById(R.id.kb_tv_done).setOnClickListener(boradlistener);
                popupLayout.show(PopupLayout.POSITION_BOTTOM);
            }
        });
        gridview_Income.setLayoutManager(new GridLayoutManager(getActivity(),4));
        //配置适配器
        gridview_Income.setAdapter(iconsAdpter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getData(){
        String json = SpUtils.getString(getActivity(),"InIconsList");
        String json_s = SpUtils.getString(getActivity(),"InSIconsList");
        if(!TextUtils.isEmpty(json)){
            Type type = new TypeToken<List<Icons>>(){}.getType();
            data_list= new Gson().fromJson(json, type);
            data_list_s= new Gson().fromJson(json_s, type);
        }else{
             int[] in_icon = {
                     R.drawable.n_wages, R.drawable.n_parttime, R.drawable.n_manage, R.drawable.n_cashgift,
                     R.drawable.n_bonus, R.drawable.n_setting};
             String[] in_iconName = {
                     "工资", "兼职", "理财", "礼金",
                     "其它", "设置"};

            for (int i = 0; i < in_icon.length; i++) {
                Icons icons = new Icons(in_icon[i], in_iconName[i]);
                data_list.add(icons);
            }
        }
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
                        values.put("inexType",0);
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
}
