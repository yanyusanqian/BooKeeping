package com.wyk.bookeeping.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.activity.AddArticleActivity;
import com.wyk.bookeeping.activity.LoginActivity;
import com.wyk.bookeeping.adpter.MyFragmentAdapter;
import com.wyk.bookeeping.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {
    private TabLayout community_tablayout;
    private RecyclerView community_recycle_view;
    private FloatingActionButton community_flaot_btn;
    private ViewPager community_viewpager;
    private String userPhone = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        community_tablayout = getActivity().findViewById(R.id.community_tablayout);
        community_flaot_btn = getActivity().findViewById(R.id.community_flaot_btn);
        community_viewpager = getActivity().findViewById(R.id.community_viewpager);


        community_flaot_btn.setOnClickListener(v -> {
            if ("".equals(userPhone))
                startActivity(new Intent(getActivity(), LoginActivity.class));
            else
                startActivity(new Intent(getActivity(), AddArticleActivity.class));
        });

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();
        fragmentList.add(ItemCommunityFragment.newInstance(1)); // 最热
        fragmentList.add(ItemCommunityFragment.newInstance(2)); // 最新
        fragmentTitles.add("最热");
        fragmentTitles.add("最新");
        MyFragmentAdapter adapter = new MyFragmentAdapter(getChildFragmentManager(), fragmentList, fragmentTitles, getActivity(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        community_viewpager.setAdapter(adapter);

        community_viewpager.setOffscreenPageLimit(1);
        community_tablayout.setupWithViewPager(community_viewpager);

        community_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView title = (TextView) (((LinearLayout) ((LinearLayout) community_tablayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
                title.setTextSize(18);
                title.setTextAppearance(getActivity(), Typeface.BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView title = (TextView) (((LinearLayout) ((LinearLayout) community_tablayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
                title.setTextSize(16);
                title.setTextAppearance(getActivity(), Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TextView title = (TextView) (((LinearLayout) ((LinearLayout) community_tablayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
                title.setTextSize(18);
                title.setTextAppearance(getActivity(), Typeface.BOLD);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        userPhone = SpUtils.getString(getActivity(),"USERPHONE","");
    }
}
