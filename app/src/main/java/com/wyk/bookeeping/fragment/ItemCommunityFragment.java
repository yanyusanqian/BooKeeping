package com.wyk.bookeeping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;

public class ItemCommunityFragment extends Fragment {
    private int Type;
    private TextView test_text;
    private RecyclerView community_recycle_view;

    static ItemCommunityFragment newInstance(int Type) {
        ItemCommunityFragment fragment = new ItemCommunityFragment();
        Bundle args = new Bundle();
        args.putInt("Type", Type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_communitylist, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            Type = getArguments().getInt("Type");
        }

        test_text = getActivity().findViewById(R.id.test_text);
        community_recycle_view = getActivity().findViewById(R.id.community_recycle_view);

        initRecyclerView();
    }

    private void initRecyclerView() {
        switch (Type){
            case 1:
                test_text.setText(111111+"");
                break;
            case 2:
                test_text.setText(22222+"");
                break;
        }
    }
}
