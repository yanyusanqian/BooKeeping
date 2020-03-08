package com.wyk.bookeeping;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wyk.bookeeping.fragment.ExpenditureFragment;
import com.wyk.bookeeping.fragment.IncomeFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomAddActivity extends AppCompatActivity {
    private View view_status;
    private Toolbar toolbar;
    private TabLayout tablayout_1;
    private ViewPager viewpager_content;
    private Button btn_cancle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.layout_custom_add_view);
        view_status = (View) findViewById(R.id.view_status);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tablayout_1 = (TabLayout)findViewById(R.id.tablayout_1);
        viewpager_content = (ViewPager)findViewById(R.id.viewpager_content);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
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
    }
    private void setViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();
        fragmentList.add(new ExpenditureFragment());
        fragmentList.add(new IncomeFragment());
        fragmentTitles.add("支出");
        fragmentTitles.add("收入");
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),fragmentList,fragmentTitles, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewpager_content.setAdapter(adapter);
    }

}
