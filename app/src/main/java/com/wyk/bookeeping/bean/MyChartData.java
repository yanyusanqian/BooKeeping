package com.wyk.bookeeping.bean;

import java.util.List;
import java.util.Map;

public class MyChartData {
    private List<Account> list;
    // 每周/月/年 记账总笔数(数据条数)
    private int num = 0;
    // 单笔最大金额
    private float maxcount = 0;
    // 总支出
    private float allcount = 0;
    // 数据上x轴每个支出(每日、每月)
    private Map<String,Float> count;

    public List<Account> getList() {
        return list;
    }

    public void setList(List<Account> list) {
        this.list = list;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(float maxcount) {
        this.maxcount = maxcount;
    }

    public float getAllcount() {
        return allcount;
    }

    public void setAllcount(float allcount) {
        this.allcount = allcount;
    }

    public Map<String, Float> getCount() {
        return count;
    }

    public void setCount(Map<String, Float> count) {
        this.count = count;
    }
}
