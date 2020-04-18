package com.wyk.bookeeping.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.CenterRecyclerViewAdapter;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.CenterItem;
import com.wyk.bookeeping.bean.DataStatus;
import com.wyk.bookeeping.livedata.AccountViewModel;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CenterFragment extends Fragment {
    private RecyclerView recyclerView_center;
    private Toolbar toolbar;
    private List<String> list;
    private AppBarLayout appbar;
    private TextView textView,totaldays,total;

    private List<CenterItem> centerItemList;
    private CenterItem centerItem;
    private CenterRecyclerViewAdapter centerRecyclerViewAdapter;
    private AccountViewModel accountViewModel;
    private List<Account> accountList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_center, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initToolBar();
        initRecyclerViewItemData();
        initRecyclerView();
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        accountViewModel.getCurrentName().observe(getActivity(), new Observer<DataStatus>() {
            @Override
            public void onChanged(DataStatus dataStatus) {
                upDateTotal();
                centerRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

    }

    private void upDateTotal(){
        String num = DBHelper.getInstance(getActivity()).getAllAccountNum(getActivity());
        total.setText(num);
    }



    private void initRecyclerViewItemData() {
        centerItemList = new ArrayList<>();
        centerItem = new CenterItem(CenterRecyclerViewAdapter.TYPE_ICONS, 1);
        centerItemList.add(centerItem);

        centerItem = new CenterItem(CenterRecyclerViewAdapter.TYPE_BILL, 5);
        centerItem.setString("type2");
        centerItemList.add(centerItem);

        centerItem = new CenterItem(CenterRecyclerViewAdapter.TYPE_BUDGET, 5);
        centerItem.setString("type3");
        centerItemList.add(centerItem);

        centerItem = new CenterItem(CenterRecyclerViewAdapter.TYPE_COMMONFUNCTIONS, 5);
        centerItem.setString("type4");
        centerItemList.add(centerItem);

        centerItem = new CenterItem(CenterRecyclerViewAdapter.TYPE_SETTING, 5);
        centerItem.setString("type5");
        centerItemList.add(centerItem);
    }

    private void initRecyclerView(){
        centerRecyclerViewAdapter = new CenterRecyclerViewAdapter(getActivity(),centerItemList);
        recyclerView_center = (RecyclerView)getActivity().findViewById(R.id.recyclerView_center);
        recyclerView_center.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_center.setAdapter(centerRecyclerViewAdapter);
        recyclerView_center.addItemDecoration(new SpacesItemDecoration(20));
    }
    private void initView(){
        list  = new ArrayList<>();
        for(int x = 0; x < 20; x++){
            list.add("x:"+x);
        }

        toolbar = (Toolbar) getActivity().findViewById(R.id.center_toolbar);
        appbar = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        textView = (TextView) getActivity().findViewById(R.id.textview);
        totaldays = (TextView) getActivity().findViewById(R.id.totaldays);
        total = (TextView) getActivity().findViewById(R.id.total);


        final int alphaMaxOffset = dpToPx(130);
        toolbar.getBackground().mutate().setAlpha(0);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // 设置 toolbar 背景
                if (verticalOffset > -alphaMaxOffset) {
                    toolbar.getBackground().mutate().setAlpha(255 * -verticalOffset / alphaMaxOffset);
                    textView.setText("");
                } else {
                    toolbar.getBackground().mutate().setAlpha(255);
                    textView.setText("我的");

                }
            }
        });
    }

    private void initToolBar() {
        String FirstDate = SpUtils.getString(getActivity(),"FIRST_INSTALL_TIME");
        String nowDate = TimeUtil.getNowDate();
        if(!"".equals(FirstDate)){
            String days = TimeUtil.getBetweenDays(FirstDate,nowDate);
            if(!TextUtils.isEmpty(days)){
                totaldays.setText(days);
            }
        }

        try {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.center_toolbar);
            if (toolbar != null) {
                // 沉浸模式
                int statusBarHeight = getStatusBarHeight();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    openAndroidLStyle();
                    toolbar.setPadding(0, statusBarHeight, 0, 0);
                    toolbar.getLayoutParams().height = dpToPx(46) + statusBarHeight;
                }/*else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getActivity().getWindow().setStatusBarColor(activity.getResources().getColor(colorId));
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启沉浸式模式支持
     */
    private void openAndroidLStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * dp转换为px
     */
    private static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    /**
     * RecyclerView设置间距辅助类
     */
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = space;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        centerRecyclerViewAdapter.notifyDataSetChanged();
    }
}
