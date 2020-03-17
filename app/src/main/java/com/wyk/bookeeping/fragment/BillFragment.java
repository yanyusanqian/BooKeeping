package com.wyk.bookeeping.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.wyk.bookeeping.R;
import com.wyk.bookeeping.adpter.BillListAdapter;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.DataStatus;
import com.wyk.bookeeping.livedate.AccountViewModel;
import com.wyk.bookeeping.livedate.DataStatusLiveData;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.RcyclerUtils;
import com.wyk.bookeeping.utils.TimeUtil;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillFragment extends Fragment {
    private SQLiteDatabase db;
    private List<Account> accountList, accountList_recycler;
    private LinearLayout bill_mouth_chose;
    private TextView bill_income, bill_expenditure, bill_month, bill_year;
    private BillListAdapter billListAdapter;
    private SwipeRecyclerView bill_recyclerview;
    private String now_month,now_year;
    private AccountViewModel accountViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bill_notempty, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        initView();
        db = DBHelper.getInstance(getActivity()).getWritableDatabase();
        now_month = TimeUtil.getNowDateMonth_String();
        now_year = TimeUtil.getNowDateYear()+"";
        getFirstAccountList(now_year,now_month);

        accountList_recycler = RcyclerUtils.listAddType(accountList);
        Log.i("TAG3", accountList_recycler.toString());
        DataStatus dataStatus = new DataStatus();
        dataStatus.setAccountList(accountList);
        dataStatus.setAccountList_recycler(accountList_recycler);
        accountViewModel.getCurrentName().setValue(dataStatus);

        initRecyclerView();
        initToolBarDate(TimeUtil.getNowDateYear() + "年");
        initChangeDate();


        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        accountViewModel.getCurrentName().observe(getActivity(), new Observer<DataStatus>() {
            @Override
            public void onChanged(DataStatus dataStatus) {
                accountList = dataStatus.getAccountList();
                Log.i("TAG1", accountList.toString());
                accountList_recycler = dataStatus.getAccountList_recycler();
                Log.i("TAG2", accountList_recycler.toString());
                if (accountList_recycler != null)
                    billListAdapter.notifyDataSetChanged(accountList_recycler);
            }
        });
    }

    private void initToolBarDate(String year) {
        float mouth_expenditure = 0;
        float mouth_income = 0;
        for (int i = 0; i < accountList.size(); i++) {
            if (accountList.get(i).getView_type() != 1) {
                if (accountList.get(i).getInexType() == 1) {
                    mouth_expenditure += accountList.get(i).getCount();
                } else {
                    mouth_income += accountList.get(i).getCount();
                }
            }
        }

            bill_month.setText(now_month);
            bill_month.setText(now_month);
        bill_year.setText(year);

        bill_income.setText(mouth_income + "");
        bill_expenditure.setText(mouth_expenditure + "");
    }

    private void initChangeDate() {
        bill_mouth_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("HERE click", "22222222");
                TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Log.i("HERE click", "1111111111");
                        Toast.makeText(getActivity(), TimeUtil.date2String(date, "YYYY年MM月"), Toast.LENGTH_SHORT).show();
                        now_month = TimeUtil.date2String(date, "MM");
                        getFirstAccountList(TimeUtil.date2String(date,"YYYY"),TimeUtil.date2String(date, "MM"));
                        accountList_recycler = RcyclerUtils.listAddType(accountList);
                        DataStatus dataStatus = new DataStatus();
                        dataStatus.setAccountList(accountList);
                        dataStatus.setAccountList_recycler(accountList_recycler);
                        accountViewModel.getCurrentName().setValue(dataStatus);
                        initToolBarDate(TimeUtil.date2String(date, "YYYY年"));
                    }
                })
                        .setType(new boolean[]{true, true, false, false, false, false})
                        .setCancelText("取消")
                        .setSubmitText("确认")
                        .setTitleText("选择月份")
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)
                        .setLabel("年", "月", "", "", "", "")
                        .build();
                pvTime.show();
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        bill_recyclerview.setLayoutManager(mLayoutManager);
        bill_recyclerview.setOnItemMenuClickListener(mMenuItemClickListener);

        bill_recyclerview.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                int width = getResources().getDimensionPixelSize(R.dimen.dp_60);
                int height = ViewGroup.LayoutParams.MATCH_PARENT;

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity()).setBackground(R.drawable.selector_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                rightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

            }
        });

        billListAdapter = new BillListAdapter(getActivity());
        bill_recyclerview.setAdapter(billListAdapter);
        Log.i("TAG4", accountList_recycler.toString());
//        billListAdapter.notifyDataSetChanged(accountList_recycler);
    }

    private void initView() {
        bill_mouth_chose = (LinearLayout) getActivity().findViewById(R.id.bill_mouth_chose);
        bill_month = (TextView) getActivity().findViewById(R.id.bill_month);
        bill_year = (TextView) getActivity().findViewById(R.id.bill_year);
        bill_income = (TextView) getActivity().findViewById(R.id.bill_income);
        bill_expenditure = (TextView) getActivity().findViewById(R.id.bill_expenditure);
        bill_expenditure = (TextView) getActivity().findViewById(R.id.bill_expenditure);
        bill_recyclerview = getActivity().findViewById(R.id.bill_recyclerview);
    }


    /**
     * 分页查询
     */
    private void getFirstAccountList(String year,String month) {
        accountList = new ArrayList<>();
        Log.i("HERE mouth", "" + month);
        String sql = "select * from account where strftime('%Y-%m',time)='" + year+"-"+month + "' order by time desc";
//        String sql = "SELECT * FROM account WHERE strftime('%m', time) = '0" + mouth + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getLong(0), cursor.getFloat(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getInt(5),
                        TimeUtil.string2Date(cursor.getString(6), "yyyy-MM-dd")
                        , cursor.getString(7));
                accountList.add(account);
            }
        }
        Log.i("TAG5", accountList.toString());
        /*DataStatus dataStatus = new DataStatus();
        dataStatus.setAccountList(accountList);
        accountViewModel.getCurrentName().setValue(dataStatus);*/
    }


    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                /*Toast.makeText(getActivity(), "list第" + position + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();*/

                if (accountList_recycler.get(position).getView_type() == 1) {
                    Toast.makeText(getActivity(), "暂不支持按日删除账单，敬请期待", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Long id = accountList_recycler.get(position).getId();
                    int mouth = TimeUtil.getDateMonth(accountList_recycler.get(position).getTime()) + 1;
                    int year = TimeUtil.getYear(accountList_recycler.get(position).getTime());
                    db.delete("account", "_id = ?", new String[]{String.valueOf(id)});
                    if (mouth < 10) {
                        getFirstAccountList(year+"","0" + mouth);
                    } else {
                        getFirstAccountList(year+"","" + mouth);
                    }
                    Log.i("TAG6", accountList.toString());
                    Log.i("LIST Fragment1", accountList_recycler.toString());
                    accountList_recycler = RcyclerUtils.listAddType(accountList);
                    Log.i("LIST Fragment2", accountList_recycler.toString());
                    Log.i("TAG7", accountList_recycler.toString());
//                    billListAdapter.notifyDataSetChanged(accountList_recycler);
                    DataStatus dataStatus = new DataStatus();
                    dataStatus.setAccountList(accountList);
                    dataStatus.setAccountList_recycler(accountList_recycler);
                    accountViewModel.getCurrentName().setValue(dataStatus);
                    initToolBarDate(year+"");
                }
            }
        }
    };

}
