package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wyk.bookeeping.R;

import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private  List<Fragment> fragmentList;
    private  List<String> fragmentTitles;
    private Context mContext;
    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> fragmentTitles, Context context, int behavior){
        super(fm,behavior);
        this.fragmentList = fragmentList;
        this.fragmentTitles = fragmentTitles;
        this.mContext= context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(fragmentTitles != null){
            return fragmentTitles.get(position);
        }else{
            return "";
        }
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout, null);
        TextView textView=view.findViewById(R.id.tab_layout_text);
        textView.setText(fragmentTitles.get(position));
        return view;
    }
}
