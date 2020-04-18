package com.wyk.bookeeping.activity;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.BaseAdapter;
import com.wyk.bookeeping.adpter.TextAdapter;
import com.wyk.bookeeping.bean.Icons;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

public class AddRemindActivity extends AppCompatActivity {
    private SwipeRecyclerView remind_recycler;
    private LinearLayout remind_add;
    private List<String> list;
    private TextAdapter textAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorLavender));
        }

        setContentView(R.layout.activity_remind);
        initView();
        initRecyclerView();

        remind_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePickerView pvTime = new TimePickerBuilder(AddRemindActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        list.add(TimeUtil.date2String(date, "HH:mm"));
                        saveData();
                        textAdapter.setTextList(list);
                        textAdapter.notifyDataSetChanged();
                    }
                }).setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setTitleText("每天")//标题文字
                        .isCyclic(false)//是否循环滚动
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .isCenterLabel(true)
                        .setLabel("", "", "", "", "   ", "")
                        .build();
                pvTime.show();
            }
        });
    }

    private void initRecyclerView() {
        list = new ArrayList<>();
        String json = "";
        json = SpUtils.getString(this, "remindList");
        if (!TextUtils.isEmpty(json)) {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            list = new Gson().fromJson(json, type);
        }
        remind_recycler.setLayoutManager(new LinearLayoutManager(this));
        remind_recycler.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                int width = getResources().getDimensionPixelSize(R.dimen.dp_50);
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem deleteItem = new SwipeMenuItem(AddRemindActivity.this).setBackground(R.drawable.selector_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                rightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            }
        });
        remind_recycler.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
                menuBridge.closeMenu();

                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                    list.remove(adapterPosition);
                    textAdapter.setTextList(list);
                    textAdapter.notifyDataSetChanged();
                    saveData();
                }
            }
        });
        textAdapter = new TextAdapter(list);
        remind_recycler.setAdapter(textAdapter);
    }

    private void saveData() {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        SpUtils.putString(this, "remindList", json);
    }

    private void initView() {
        remind_recycler = findViewById(R.id.remind_recycler);
        remind_add = findViewById(R.id.remind_add);
        FrameLayout title_return = findViewById(R.id.title_return);
        title_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 极光推送本地API，暂放，没搞懂啥意思
     */
    private void addRemind() {
        // Android 8 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup("MyGroupId", "自定义通知组");
                nm.createNotificationChannelGroup(notificationChannelGroup);

                NotificationChannel notificationChannel = new NotificationChannel("MyChannelId", "自定义通知", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setGroup("MyGroupId");
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
//                notificationChannel.setSound("android.resource://包名/raw/铃声文件", null);    //设置自定义铃声

                nm.createNotificationChannel(notificationChannel);
            }
        } else {
            JPushLocalNotification ln = new JPushLocalNotification();
            ln.setBuilderId(0);
            ln.setContent("记账提醒");
            ln.setTitle("BooKeeping");
            ln.setNotificationId(11111111);
            ln.setBroadcastTime(System.currentTimeMillis() + 1000 * 60 * 10);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", "jpush");
            map.put("test", "111");
            JSONObject json = new JSONObject(map);
            ln.setExtras(json.toString());
            JPushInterface.addLocalNotification(getApplicationContext(), ln);
        }

    }

}
