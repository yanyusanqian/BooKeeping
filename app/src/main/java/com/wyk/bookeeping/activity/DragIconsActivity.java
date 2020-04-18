package com.wyk.bookeeping.activity;

import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.DragAddIconsAdapter;
import com.wyk.bookeeping.adpter.DragIconsAdapter;
import com.wyk.bookeeping.bean.Icons;
import com.wyk.bookeeping.utils.SpUtils;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.touch.OnItemStateChangedListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragIconsActivity extends AppCompatActivity {
    private LinearLayout dragicons_addlayout;
    private RadioGroup rg_type;
    private List<Icons> data_list, data_list_not, data_delete;
    private SwipeRecyclerView dragicons_recycler;
    private RecyclerView dragicons_recycler_add;
    private DragAddIconsAdapter addIconsAdapter = null;
    private DragIconsAdapter dragIconsAdapter = null;
    private int RADIOBUTTONID = 1;

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
        setContentView(R.layout.activity_dragicons);
        initView();
        setSwipeRecyclerView();
        setAddRecyclerView();
        // 设置单选按钮监听
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_expend:
                        saveIconList();
                        RADIOBUTTONID = 1;
                        getData(1);
                        dragIconsAdapter.notifyDataSetChanged_icons(data_list);
                        break;
                    case R.id.rb_income:
                        saveIconList();
                        RADIOBUTTONID = 0;
                        getData(0);
                        dragIconsAdapter.notifyDataSetChanged_icons(data_list);
                        break;
                }
            }
        });
    }

    private void initView() {
        FrameLayout title_return = (FrameLayout) findViewById(R.id.title_return);
        LinearLayout dragicons_add = (LinearLayout) findViewById(R.id.dragicons_add);
        dragicons_addlayout = (LinearLayout) findViewById(R.id.dragicons_addlayout);
        rg_type = (RadioGroup) findViewById(R.id.rg_type);
        dragicons_recycler = (SwipeRecyclerView) findViewById(R.id.dragicons_recycler);
        dragicons_recycler_add = (RecyclerView) findViewById(R.id.dragicons_recycler_add);
        title_return.setOnClickListener(listener);
        dragicons_add.setOnClickListener(listener);
    }

    private void getData(int t) {
        String json = "";
        String json_s = "";
        String json_d = "";
        if (t == 1) {
            json_s = SpUtils.getString(this, "ExSIconsList");
            json = SpUtils.getString(this, "ExIconsList");
            json_d = SpUtils.getString(this, "ExDIconsList");
        } else {
            json_s = SpUtils.getString(this, "InSIconsList");
            json = SpUtils.getString(this, "InIconsList");
            json_d = SpUtils.getString(this, "InDIconsList");
        }
        if (!TextUtils.isEmpty(json)) {
            Type type = new TypeToken<List<Icons>>() {
            }.getType();
            data_list = new Gson().fromJson(json, type);
            data_list_not = new Gson().fromJson(json_s, type);
            if (!TextUtils.isEmpty(json_d))
                data_delete = new Gson().fromJson(json_d, type);
            else
                data_delete = new ArrayList<>();
        } else {
            Toast.makeText(this, "错误:存储数据丢失!", Toast.LENGTH_LONG).show();
        }
    }

    private void setSwipeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dragicons_recycler.setLayoutManager(linearLayoutManager);
        dragicons_recycler.setOnItemMenuClickListener(mMenuItemClickListener);
        dragicons_recycler.setLongPressDragEnabled(true); // 拖拽排序。
        dragicons_recycler.setOnItemMoveListener(mItemMoveListener);// 拖拽排序监听
        dragicons_recycler.setOnItemStateChangedListener(mOnItemStateChangedListener); // 监听Item的手指状态，拖拽、侧滑、松开。

        dragicons_recycler.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                int width = getResources().getDimensionPixelSize(R.dimen.dp_50);
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem deleteItem = new SwipeMenuItem(DragIconsActivity.this).setBackground(R.drawable.selector_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                rightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            }
        });
        dragIconsAdapter = new DragIconsAdapter(this, dragicons_recycler);
        dragIconsAdapter.OnDragDeleteClickListener(new DragIconsAdapter.OnDragDeleteClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dragicons_recycler.smoothOpenRightMenu(position);
            }
        });
        dragIconsAdapter.notifyDataSetChanged_icons(data_list);
        dragicons_recycler.setAdapter(dragIconsAdapter);
    }

    private void setAddRecyclerView() {
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dragicons_recycler_add.setLayoutManager(linearLayoutManager2);
        addIconsAdapter = new DragAddIconsAdapter(this, data_delete);
        addIconsAdapter.OnDragAddClickListener(new DragAddIconsAdapter.OnDragAddClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                data_list.add(data_delete.get(position));
                data_list_not.add(data_delete.get(position));
                data_delete.remove(position);
                dragIconsAdapter.notifyDataSetChanged_icons(data_list);
                if (data_delete.size() == 0) {
                    dragicons_addlayout.setVisibility(View.INVISIBLE);
                }
                addIconsAdapter.setList(data_delete);
                addIconsAdapter.notifyDataSetChanged();
            }
        });
        dragicons_recycler_add.setAdapter(addIconsAdapter);
    }

    /**
     * Item的拖拽/侧滑删除时，手指状态发生变化监听。
     */
    private OnItemStateChangedListener mOnItemStateChangedListener = new OnItemStateChangedListener() {
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState == OnItemStateChangedListener.ACTION_STATE_DRAG) {
                // 拖拽时背景
                viewHolder.itemView.setElevation(30);
                viewHolder.itemView.setBackgroundColor(
                        ContextCompat.getColor(DragIconsActivity.this, R.color.gray_cc));
            } else if (actionState == OnItemStateChangedListener.ACTION_STATE_IDLE) {
                // 在手松开的时候还原背景
                viewHolder.itemView.setElevation(0);
                ViewCompat.setBackground(viewHolder.itemView,
                        ContextCompat.getDrawable(DragIconsActivity.this, R.drawable.drag_background));
            }
        }
    };

    /**
     * 设置监听，待拖拽发生后，数据位置交换(侧滑无用)
     */
    OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
            // 此方法在Item拖拽交换位置时被调用。
            // 第一个参数是要交换为之的Item，第二个是目标位置的Item。
            // 交换数据，并更新adapter。
            int fromPosition = srcHolder.getAdapterPosition();
            int toPosition = targetHolder.getAdapterPosition();
            Collections.swap(data_list, fromPosition, toPosition);
            Collections.swap(data_list_not, fromPosition, toPosition);
            dragIconsAdapter.notifyItemMoved(fromPosition, toPosition);
            // 返回true，表示数据交换成功，ItemView可以交换位置。
            return true;
        }

        @Override
        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
            // 此方法在Item在侧滑删除时被调用。

            // 从数据源移除该Item对应的数据，并刷新Adapter。
            int position = srcHolder.getAdapterPosition();
            data_list.remove(position);
            data_list_not.remove(position);
            dragIconsAdapter.notifyItemRemoved(position);
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                data_delete.add(data_list.get(position));
                data_list.remove(position);
                data_list_not.remove(position);
                dragIconsAdapter.notifyItemRemoved(position);
                dragicons_addlayout.setVisibility(View.VISIBLE);
                addIconsAdapter.setList(data_delete);
                addIconsAdapter.notifyDataSetChanged();
            }
        }
    };

    private void saveIconList() {
        Gson gson = new Gson();
        String json_s = gson.toJson(data_list);
        String json = gson.toJson(data_list_not);
        String json_d = gson.toJson(data_delete);
        if (RADIOBUTTONID == 1) {
            SpUtils.putString(this, "ExSIconsList", json);
            SpUtils.putString(this, "ExIconsList", json_s);
            if (data_delete.size() != 0)
                SpUtils.putString(this, "ExDIconsList", json_d);
        } else {
            SpUtils.putString(this, "InSIconsList", json);
            SpUtils.putString(this, "InIconsList", json_s);
            if (data_delete.size() != 0)
                SpUtils.putString(this, "InDIconsList", json_d);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_return:
                    saveIconList();
                    DragIconsActivity.this.finish();
                    break;
                case R.id.dragicons_add:
                    saveIconList();
                    Intent intent = new Intent(DragIconsActivity.this, AddIconsActivity.class);
                    intent.putExtra("TYPE", RADIOBUTTONID);
                    startActivity(intent);
                    break;
            }
        }
    };

    /**
     * 复写系统返回事件
     */
    @Override
    public void onBackPressed() {
        saveIconList();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(RADIOBUTTONID);
        if(dragIconsAdapter!=null){
            dragIconsAdapter.notifyDataSetChanged_icons(data_list);
        }
        if(addIconsAdapter!=null){
            addIconsAdapter.setList(data_delete);
            addIconsAdapter.notifyDataSetChanged();
        }
    }
}
