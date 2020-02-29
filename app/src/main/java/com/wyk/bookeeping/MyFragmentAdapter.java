package com.wyk.bookeeping;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private  List<Fragment> fragmentList = new ArrayList<>();
    private  List<String> fragmentTitles = new ArrayList<>();
    public MyFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList, int behavior) {
        super(fm, behavior);
        this.fragmentList = fragmentList;
    }
    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> fragmentTitles, int behavior){
        super(fm,behavior);
        this.fragmentList = fragmentList;
        this.fragmentTitles = fragmentTitles;
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
}
