package com.wyk.bookeeping.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.DragIconsGridAdapter;
import com.wyk.bookeeping.bean.Icons;
import com.wyk.bookeeping.utils.SpUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddIconsActivity extends AppCompatActivity {
    private FrameLayout title_return;
    private TextView addicons_done, rg_type;
    private ImageView addicons_image;
    private EditText addicons_edit;
    private RecyclerView dragicons_add_grid;
    private List<Icons> list;
    private int RADIOBUTTONID;
    private List<Icons> data_list, data_list_not;
    private int useDrawableid;
    private String json;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.colorLavender));
        }
        setContentView(R.layout.activity_addicons);

        Intent intent = getIntent();
        RADIOBUTTONID = intent.getIntExtra("TYPE", 1);

        initView();
        setData();

        useDrawableid = list.get(1).getDrawableid();

        if (RADIOBUTTONID == 0) {
            rg_type.setText("添加收入新类别");
        }

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 5, LinearLayoutManager.VERTICAL, false);
        dragicons_add_grid.setLayoutManager(linearLayoutManager);
        DragIconsGridAdapter adapter = new DragIconsGridAdapter(this, list, 5);
        adapter.OnItemImageClickListener(new DragIconsGridAdapter.OnItemImageClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                useDrawableid = list.get(position).getDrawableid();
                addicons_image.setImageResource(list.get(position).getDrawableid());
            }
        });
        dragicons_add_grid.setAdapter(adapter);

        addicons_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(addicons_edit.getText().toString())) {
                    if (RADIOBUTTONID == 1)
                        getData(1);
                    else
                        getData(0);
                    if (!json.contains(addicons_edit.getText().toString())) {
                        data_list.add(new Icons(useDrawableid, addicons_edit.getText().toString()));
                        data_list_not.add(new Icons(useDrawableid, addicons_edit.getText().toString()));
                        saveData();
                        finish();
                    } else {
                        Toast.makeText(AddIconsActivity.this, "已有该标签", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AddIconsActivity.this, "请输入标签", Toast.LENGTH_LONG).show();
                }
            }
        });

        title_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        title_return = findViewById(R.id.title_return);
        rg_type = findViewById(R.id.rg_type);
        addicons_done = findViewById(R.id.addicons_done);
        addicons_image = findViewById(R.id.addicons_image);
        addicons_edit = findViewById(R.id.addicons_edit);
        dragicons_add_grid = findViewById(R.id.dragicons_add_grid);
    }

    private void setData() {
        list = new ArrayList<>();
        list.add(new Icons("娱乐", 1));
        list.add(new Icons(R.drawable.lanqiu, 0));
        list.add(new Icons(R.drawable.diannao, 0));
        list.add(new Icons(R.drawable.dianying, 0));
        list.add(new Icons(R.drawable.maikefeng, 0));
        list.add(new Icons(R.drawable.xiaqi, 0));
        list.add(new Icons(R.drawable.youxi, 0));
        list.add(new Icons(R.drawable.yule, 0));
        list.add(new Icons("饮食", 1));
        list.add(new Icons(R.drawable.noodle, 0));
        list.add(new Icons(R.drawable.pijiu, 0));
        list.add(new Icons(R.drawable.noodle1, 0));
        list.add(new Icons(R.drawable.frenchfries, 0));
        list.add(new Icons(R.drawable.hamburger, 0));
        list.add(new Icons(R.drawable.pizza, 0));
        list.add(new Icons(R.drawable.coffee1, 0));
        list.add(new Icons(R.drawable.popsicle, 0));
        list.add(new Icons(R.drawable.shrimp, 0));
        list.add(new Icons(R.drawable.coffee, 0));
        list.add(new Icons(R.drawable.drumsticks, 0));
        list.add(new Icons(R.drawable.fish, 0));
        list.add(new Icons(R.drawable.crab, 0));
        list.add(new Icons("生活", 1));
        list.add(new Icons(R.drawable.weixiu, 0));
        list.add(new Icons(R.drawable.dujia, 0));
        list.add(new Icons(R.drawable.shouyinji, 0));
        list.add(new Icons(R.drawable.diannao, 0));
        list.add(new Icons(R.drawable.meishu, 0));
        list.add(new Icons(R.drawable.yinle, 0));
        list.add(new Icons(R.drawable.zihangche, 0));
        list.add(new Icons(R.drawable.yundong, 0));
        list.add(new Icons(R.drawable.chaoshi, 0));
        list.add(new Icons(R.drawable.jiazhengfuwu, 0));
        list.add(new Icons("交通", 1));
        list.add(new Icons(R.drawable.chuanbo, 0));
        list.add(new Icons(R.drawable.deshi, 0));
        list.add(new Icons(R.drawable.ditie, 0));
        list.add(new Icons(R.drawable.xiaoche, 0));
        list.add(new Icons(R.drawable.zhishengji, 0));
        list.add(new Icons(R.drawable.zihangche, 0));
        list.add(new Icons("医疗", 1));
        list.add(new Icons(R.drawable.patch, 0));
        list.add(new Icons(R.drawable.pillsdrugs, 0));
        list.add(new Icons(R.drawable.heartpulse, 0));
        list.add(new Icons(R.drawable.dnahelix, 0));
        list.add(new Icons("校园", 1));
        list.add(new Icons(R.drawable.biaochi, 0));
        list.add(new Icons(R.drawable.biye, 0));
        list.add(new Icons(R.drawable.shouyinji, 0));
        list.add(new Icons(R.drawable.diannao, 0));
        list.add(new Icons(R.drawable.diqiuyi, 0));
        list.add(new Icons(R.drawable.heiban, 0));
        list.add(new Icons(R.drawable.huaban, 0));
        list.add(new Icons(R.drawable.huaxue, 0));
        list.add(new Icons(R.drawable.jiaoyu, 0));
        list.add(new Icons(R.drawable.shu, 0));
        list.add(new Icons(R.drawable.shubao, 0));
        list.add(new Icons(R.drawable.shuji, 0));
        list.add(new Icons(R.drawable.wenjianbao, 0));
        list.add(new Icons(R.drawable.xianweijing, 0));
        list.add(new Icons(R.drawable.xuexiao, 0));
    }

    private void getData(int t) {
        json = "";
        String json_s = "";
        if (t == 1) {
            json_s = SpUtils.getString(this, "ExSIconsList");
            json = SpUtils.getString(this, "ExIconsList");
        } else {
            json_s = SpUtils.getString(this, "InSIconsList");
            json = SpUtils.getString(this, "InIconsList");
        }
        if (!TextUtils.isEmpty(json)) {
            Type type = new TypeToken<List<Icons>>() {
            }.getType();
            data_list = new Gson().fromJson(json, type);
            data_list_not = new Gson().fromJson(json_s, type);
        } else {
            Toast.makeText(this, "错误:存储数据丢失!", Toast.LENGTH_LONG).show();
        }
    }

    private void saveData() {
        Gson gson = new Gson();
        String json_s = gson.toJson(data_list);
        String json = gson.toJson(data_list_not);
        if (RADIOBUTTONID == 1) {
            SpUtils.putString(this, "ExSIconsList", json);
            SpUtils.putString(this, "ExIconsList", json_s);

        } else {
            SpUtils.putString(this, "InSIconsList", json);
            SpUtils.putString(this, "InIconsList", json_s);
        }
    }
}
