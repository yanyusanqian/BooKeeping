package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wyk.bookeeping.R;

import java.util.List;

public class ChartTablayoutAdapter extends FragmentPagerAdapter {
    private List<String> mTitles;
    private Context mContext;
    private List<Fragment> mFragments;
    public ChartTablayoutAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, List<String> titles, Context context,int behavior) {
        super(fm, behavior);
        this.mContext = context;
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout, null);
        TextView textView=view.findViewById(R.id.tab_layout_text);
        textView.setText(mTitles.get(position));
        return view;
    }

    public View getTabView_circular(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout_circular, null);
        TextView textView=view.findViewById(R.id.tab_layout_text);
        textView.setText(mTitles.get(position));
        return view;
    }
}
