package com.wyk.bookeeping.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wyk.bookeeping.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenditureFragment extends Fragment {
    private GridView gridview_expend;
    private SimpleAdapter sim_adapter;
    private List<Map<String, Object>> data_list;
    private int[] icon = { R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1, R.drawable.pre1, R.drawable.pre1,
            R.drawable.pre1 };
    private String[] iconName = { "test", "test", "test", "test", "test", "test", "test",
            "test", "test", "test", "test", "test" };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenditure, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridview_expend = (GridView) getActivity().findViewById(R.id.gridview_expend);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        Log.i("DATE:",data_list+"");
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this.getActivity(), data_list, R.layout.layout_iconsitem, from, to);
        Log.i("ADAPTER:",sim_adapter+"");
        Log.i("CONTEXT",getActivity()+"");
        //配置适配器
        gridview_expend.setAdapter(sim_adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }
}
