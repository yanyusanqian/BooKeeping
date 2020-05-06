package com.wyk.bookeeping.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.wyk.bookeeping.activity.BookkeepingActivity;
import com.wyk.bookeeping.activity.MainActivity;
import com.wyk.bookeeping.adpter.BillListAdapter;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.DataStatus;
import com.wyk.bookeeping.livedata.AccountViewModel;
import com.wyk.bookeeping.utils.DBHelper;
import com.wyk.bookeeping.utils.RcyclerUtils;
import com.wyk.bookeeping.utils.SpUtils;
import com.wyk.bookeeping.utils.TimeUtil;
import com.wyk.bookeeping.utils.okhttpUtils;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillFragment extends Fragment {
    private SQLiteDatabase db;
    private List<Account> accountList, accountList_recycler;
    private LinearLayout bill_mouth_chose;
    private TextView bill_income, bill_expenditure, bill_month, bill_year;
    private BillListAdapter billListAdapter;
    private SwipeRecyclerView bill_recyclerview;
    private String now_month,now_year,response;
    private AccountViewModel accountViewModel;
    private Long bill_id;
    private int year,mouth;

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
        accountList = DBHelper.getInstance(getContext()).getFirstAccountList(getActivity(),now_year,now_month);
        List<Account> list = new ArrayList<>();
        list.addAll(accountList);
        accountList_recycler = RcyclerUtils.listAddType(list);

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
                if (accountList.get(i).getBill_inexType() == 1) {
                    mouth_expenditure += accountList.get(i).getBill_count();
                } else {
                    mouth_income += accountList.get(i).getBill_count();
                }
            }
        }

            bill_month.setText(now_month);
            bill_month.setText(now_month);
        bill_year.setText(year);

        bill_income.setText(mouth_income + "");
        bill_expenditure.setText(mouth_expenditure + "");
    }

    /**
     * 初始化日期选择弹出框
     */
    private void initChangeDate() {
        bill_mouth_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Toast.makeText(getActivity(), TimeUtil.date2String(date, "YYYY年MM月"), Toast.LENGTH_SHORT).show();
                        now_month = TimeUtil.date2String(date, "MM");
                        accountList = DBHelper.getInstance(getContext()).getFirstAccountList(getActivity(),TimeUtil.date2String(date,"YYYY"),TimeUtil.date2String(date, "MM"));
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

    /**
     * 初始化SwipeRecyclerView(滑动删除)
     */
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
        String sql = "select * from account where strftime('%Y-%m',time)='" + year+"-"+month + "' order by time desc";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getLong(0), cursor.getFloat(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getInt(5),
                        cursor.getString(6)
                        , cursor.getString(7));
                accountList.add(account);
            }
        }
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
                if (accountList_recycler.get(position).getView_type() == 1) {
                    Toast.makeText(getActivity(), "暂不支持按日删除账单，敬请期待", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    bill_id = accountList_recycler.get(position).getBill_id();
                    mouth = TimeUtil.getDateMonth(TimeUtil.string2Date(accountList_recycler.get(position).getBill_time(),"yyyy-MM-dd")) + 1;
                    year = TimeUtil.getYear(TimeUtil.string2Date(accountList_recycler.get(position).getBill_time(),"yyyy-MM-dd"));
                    String userPhone = SpUtils.getString(getActivity(),"USERPHONE","");
                    if(!"".equals(userPhone)){
                        new Thread() {
                            @Override
                            public void run() {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("user_phone", userPhone);
                                params.put("bill_id", String.valueOf(bill_id));
                                try {
                                    String url = "http://" + getString(R.string.localhost) + "/Bookeeping/deleteAccount";
                                    response = okhttpUtils.getInstance().Connection(url, params);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Message msg = mHandler.obtainMessage();
                                    msg.obj = "wrong";
                                    mHandler.sendMessage(msg);
                                }
                                Message msg = mHandler.obtainMessage();
                                msg.obj = response;
                                mHandler.sendMessage(msg);
                            }
                        }.start();
                    }else{
                        db.delete("account", "_id = ?", new String[]{String.valueOf(bill_id)});
                        if (mouth < 10) {
                            accountList = DBHelper.getInstance(getContext()).getFirstAccountList(getActivity(),year+"","0" + mouth);
                        } else {
                            accountList = DBHelper.getInstance(getContext()).getFirstAccountList(getActivity(),year+"","" + mouth);
                        }
                        accountList_recycler = RcyclerUtils.listAddType(accountList);
                        DataStatus dataStatus = new DataStatus();
                        dataStatus.setAccountList(accountList);
                        dataStatus.setAccountList_recycler(accountList_recycler);
                        accountViewModel.getCurrentName().setValue(dataStatus);
                        initToolBarDate(year+"");
                    }

                }
            }
        }
    };

    /**
     * 异步通讯回传数据处理
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if("1".equals(response)) {
                db.delete("account", "_id = ?", new String[]{String.valueOf(bill_id)});
                if (mouth < 10) {
                    accountList = DBHelper.getInstance(getContext()).getFirstAccountList(getActivity(),year+"","0" + mouth);
                } else {
                    accountList = DBHelper.getInstance(getContext()).getFirstAccountList(getActivity(),year+"","" + mouth);
                }
                accountList_recycler = RcyclerUtils.listAddType(accountList);
                DataStatus dataStatus = new DataStatus();
                dataStatus.setAccountList(accountList);
                dataStatus.setAccountList_recycler(accountList_recycler);
                accountViewModel.getCurrentName().setValue(dataStatus);
                initToolBarDate(year+"");
            }
            if ("Failed".equals(response)) {
                Toast.makeText(getActivity(), "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
            }
            if ("wrong".equals(msg.obj + "")) {
                Toast.makeText(getActivity(), "网络连接失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
            if ("0".equals(response)) {
                Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
            }

        }
    };

}
