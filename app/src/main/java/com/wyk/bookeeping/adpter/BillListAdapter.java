package com.wyk.bookeeping.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillListAdapter extends BaseAdapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_ITEM = 0;
    private List<Account> accountList;
    private Context context;

    public BillListAdapter(Context context) {
        super(context);
        this.context = context;
    }


    public void notifyDataSetChanged(List<Account> accountList) {
        this.accountList = accountList;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        accountList.remove(position);
        notifyItemRemoved(position);
    }


    @SuppressLint("UseSparseArrays")
    @Override
    public int getItemCount() {
        return accountList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return accountList.get(position).getView_type();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        LayoutInflater mInflater = LayoutInflater.from(context);
        //判断viewtype类型返回不同Viewholder
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                vh = new ItemHolder(mInflater.inflate(R.layout.item_bill_list, parent, false));
                break;
            case VIEW_TYPE_TITLE:
                vh = new TitleHolder(mInflater.inflate(R.layout.item_stick_header, parent, false));
                break;
        }
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (viewType == VIEW_TYPE_ITEM) {
            ItemHolder viewHolder = (ItemHolder) holder;

            float count = accountList.get(position).getCount();
            int type = accountList.get(position).getInexType();
            if (type == 1)
                count = -count;
            viewHolder.amount_bill.setText(count + "");
            viewHolder.text_bill.setText(accountList.get(position).getNote());
            viewHolder.imageview_bill.setImageResource(accountList.get(position).getImgRes());
        } else {
            TitleHolder viewHolder = (TitleHolder) holder;
                viewHolder.tv_title_date.setText(TimeUtil.date2String(accountList.get(position+1).getTime(), "MM月dd日"));
                viewHolder.tv_title_week.setText(TimeUtil.getWeekByDate(accountList.get(position+1).getTime()));
                float expenditure = accountList.get(position).getTitle_expenditure();
                float income = accountList.get(position).getTitle_income();
                if(income !=0){
                    viewHolder.tv_title_income.setText("收入:" + income);
                }else{
                    viewHolder.tv_title_income.setText("");
                }

                if(expenditure !=0){
                    viewHolder.tv_title_expend.setText("支出:" + expenditure);
                }else{
                    viewHolder.tv_title_expend.setText("");
                    viewHolder.tv_title_expend.setVisibility(View.GONE);
                }
        }

    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        ImageView imageview_bill;
        TextView text_bill, amount_bill;

        ItemHolder(View inflate) {
            super(inflate);
            text_bill = inflate.findViewById(R.id.text_bill);
            amount_bill = inflate.findViewById(R.id.amount_bill);
            imageview_bill = inflate.findViewById(R.id.imageview_bill);
        }
    }

    private class TitleHolder extends RecyclerView.ViewHolder {
        TextView tv_title_date, tv_title_week, tv_title_income, tv_title_expend;

        TitleHolder(View inflate) {
            super(inflate);
            tv_title_date = inflate.findViewById(R.id.tv_title_date);
            tv_title_week = inflate.findViewById(R.id.tv_title_week);
            tv_title_income = inflate.findViewById(R.id.tv_title_income);
            tv_title_expend = inflate.findViewById(R.id.tv_title_expend);
        }
    }
}
