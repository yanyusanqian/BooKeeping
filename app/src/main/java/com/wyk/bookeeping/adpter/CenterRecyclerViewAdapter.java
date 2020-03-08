package com.wyk.bookeeping.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyk.bookeeping.R;
import com.wyk.bookeeping.bean.CenterItem;

import java.util.List;

public class CenterRecyclerViewAdapter extends RecyclerView.Adapter<CenterRecyclerViewAdapter.ViewHolder> {
    public static final int TYPE_ICONS = 1;
    public static final int TYPE_BILL = 2;
    public static final int TYPE_BUDGET = 3;
    public static final int TYPE_COMMONFUNCTIONS = 4;
    public static final int TYPE_SETTING = 5;

    private Context context;
    private List<CenterItem> centerItemList;

    public CenterRecyclerViewAdapter(Context context, List<CenterItem> centerItemList) {
        this.context = context;
        this.centerItemList = centerItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public CenterRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ICONS:
                return new IconsHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_icons, parent, false));
            case TYPE_BILL:
                return new BillHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_bill, parent, false));
            case TYPE_BUDGET:
                return new BudgetHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_budget, parent, false));
            case TYPE_COMMONFUNCTIONS:
                return new CommonFunctionsHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_commonfunctions, parent, false));
            case TYPE_SETTING:
                return new SettingHolder(LayoutInflater.from(context).inflate(R.layout.layout_center_setting, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return centerItemList.get(position).getItemType();
    }

    /**
     * 绑定控件，可以写事件和方法
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CenterRecyclerViewAdapter.ViewHolder holder, int position) {
        CenterItem centerItem = centerItemList.get(position);
        switch (centerItem.getItemType()){
            case TYPE_ICONS:
                IconsHolder iconsHolder = (IconsHolder)holder;
                break;
            case TYPE_BILL:
                BillHolder billHolder = (BillHolder)holder;
                break;
            case TYPE_BUDGET:
                BudgetHolder budgetHolder = (BudgetHolder)holder;
                break;
            case TYPE_COMMONFUNCTIONS:
                CommonFunctionsHolder commonFunctionsHolder = (CommonFunctionsHolder)holder;
                break;
            case TYPE_SETTING:
                SettingHolder settingHolder = (SettingHolder)holder;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return centerItemList.size();
    }


    class IconsHolder extends CenterRecyclerViewAdapter.ViewHolder {
        public IconsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class BillHolder extends CenterRecyclerViewAdapter.ViewHolder {
        public BillHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class BudgetHolder extends CenterRecyclerViewAdapter.ViewHolder {
        public BudgetHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class CommonFunctionsHolder extends CenterRecyclerViewAdapter.ViewHolder {
        public CommonFunctionsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class SettingHolder extends CenterRecyclerViewAdapter.ViewHolder {
        public SettingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
