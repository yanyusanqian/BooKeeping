package com.wyk.bookeeping.bean;

import java.util.Date;

public class Account {
    // 单笔id值
    private Long bill_id;
    // 账号id,用于连接用户表，用户仅使用本地功能时可以为空
    private Long user_id;
    // 单笔金额
    private float bill_count;
    // 单笔类型，收入0或支出1
    private int bill_inexType;
    // 具体类型，如出游、餐饮等
    private String bill_detailType;
    // 具体类型图标，应为R.drawable.xx
    private int bill_imgRes;
    // 记账时间
    private String bill_time;
    // 备注
    private String bill_note;
    // 该条数据是否为title，是为1，不是为0
    private int view_type;
    // 某个日期的总支出
    private float title_expenditure;
    // 某个日期的总收入
    private float title_income;
    // 某一类型的条数
    private int num;

    public Account(){}

    public Account(String bill_detailType,int bill_imgRes,float bill_count, int num) {
        this.bill_count = bill_count;
        this.bill_detailType = bill_detailType;
        this.bill_imgRes = bill_imgRes;
        this.num = num;
    }


    public Account(Long bill_id, float bill_count, int bill_inexType, String bill_detailType, int bill_imgRes, String bill_time, String bill_note) {
        this.bill_id = bill_id;
        this.bill_count = bill_count;
        this.bill_inexType = bill_inexType;
        this.bill_detailType = bill_detailType;
        this.bill_imgRes = bill_imgRes;
        this.bill_time = bill_time;
        this.bill_note = bill_note;
    }

    public Account(Long bill_id, Long user_id, float bill_count, int bill_inexType, String bill_detailType, int bill_imgRes, String bill_time, String bill_note) {
        this.bill_id = bill_id;
        this.user_id = user_id;
        this.bill_count = bill_count;
        this.bill_inexType = bill_inexType;
        this.bill_detailType = bill_detailType;
        this.bill_imgRes = bill_imgRes;
        this.bill_time = bill_time;
        this.bill_note = bill_note;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public float getTitle_expenditure() {
        return title_expenditure;
    }

    public void setTitle_expenditure(float title_expenditure) {
        this.title_expenditure = title_expenditure;
    }

    public float getTitle_income() {
        return title_income;
    }

    public void setTitle_income(float title_income) {
        this.title_income = title_income;
    }

    public Long getBill_id() {
        return bill_id;
    }

    public void setBill_id(Long bill_id) {
        this.bill_id = bill_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public float getBill_count() {
        return bill_count;
    }

    public void setBill_count(float bill_count) {
        this.bill_count = bill_count;
    }

    public int getBill_inexType() {
        return bill_inexType;
    }

    public void setBill_inexType(int bill_inexType) {
        this.bill_inexType = bill_inexType;
    }

    public String getBill_detailType() {
        return bill_detailType;
    }

    public void setBill_detailType(String bill_detailType) {
        this.bill_detailType = bill_detailType;
    }

    public int getBill_imgRes() {
        return bill_imgRes;
    }

    public void setBill_imgRes(int bill_imgRes) {
        this.bill_imgRes = bill_imgRes;
    }

    public String getBill_time() {
        return bill_time;
    }

    public void setBill_time(String bill_time) {
        this.bill_time = bill_time;
    }

    public String getBill_note() {
        return bill_note;
    }

    public void setBill_note(String bill_note) {
        this.bill_note = bill_note;
    }

    @Override
    public String toString() {
        return "Account{" +
                "bill_id=" + bill_id +
                ", user_id=" + user_id +
                ", bill_count=" + bill_count +
                ", bill_inexType=" + bill_inexType +
                ", bill_detailType='" + bill_detailType + '\'' +
                ", bill_imgRes=" + bill_imgRes +
                ", bill_time=" + bill_time +
                ", bill_note='" + bill_note + '\'' +
                ", view_type=" + view_type +
                ", title_expenditure=" + title_expenditure +
                ", title_income=" + title_income +
                ", num=" + num +
                '}';
    }
}
