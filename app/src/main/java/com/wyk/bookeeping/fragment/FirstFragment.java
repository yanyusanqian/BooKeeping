package com.wyk.bookeeping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wyk.bookeeping.R;

/**
 * Created by Administrator on 2018/6/2.
 */

public class FirstFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, null);

        /*Button bt01 = view.findViewById(R.id.bt01);
        bt01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).getNavigationBar().setMsgPointCount(2, 109);
                    ((MainActivity) getActivity()).getNavigationBar().setMsgPointCount(0, 5);
                    ((MainActivity) getActivity()).getNavigationBar().setHintPoint(3, true);
                }
            }
        });


        Button bt02 = view.findViewById(R.id.bt02);
        bt02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).getNavigationBar().clearAllHintPoint();
                    ((MainActivity) getActivity()).getNavigationBar().clearAllMsgPoint();
                }

            }
        });

        Button bt03 = view.findViewById(R.id.bt03);
        bt03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).getNavigationBar().clearMsgPoint(0);
                }
            }
        });


        Button bt04 = view.findViewById(R.id.bt04);
        bt04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof WeiboActivity) {
                    ((WeiboActivity) getActivity()).getNavigationBar().clearHintPoint(3);

                }
            }
        });

        Button bt05 = view.findViewById(R.id.bt05);
        bt05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).getNavigationBar().selectTab(1);
                    ((SecondFragment) (((MainActivity) getActivity()).getNavigationBar().getAdapter().getItem(1))).showToast("嘻嘻哈哈嗝");

                }
            }
        });*/

        return view;
    }


}
