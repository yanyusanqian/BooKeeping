package com.wyk.bookeeping.bean;

import java.util.Date;

public class Account {
    // 单笔id值
    private Long id;
    // 账号id,用于连接用户表，用户仅使用本地功能时可以为空
    private Long user_id;
    // 单笔金额
    private float count;
    // 单笔类型，收入0或支出1
    private int inexType;
    // 具体类型，如出游、餐饮等
    private String detailType;
    // 具体类型图标，应为R.drawable.xx
    private int imgRes;
    // 记账时间
    private Date time;
    // 备注
    private String note;
    // 该条数据是否为title，是为1，不是为0
    private int view_type;
    // 某个日期的总支出
    private float title_expenditure;
    // 某个日期的总收入
    private float title_income;
    // 某一类型的条数
    private int num;

    public Account(){}

    public Account(String detailType,int imgRes,float count,int num) {
        this.count = count;
        this.detailType = detailType;
        this.imgRes = imgRes;
        this.num = num;
    }

    public Account(Long id, float count, int inexType, String detailType, int imgRes, Date time, String note) {
        this.id = id;
        this.count = count;
        this.inexType = inexType;
        this.detailType = detailType;
        this.imgRes = imgRes;
        this.time = time;
        this.note = note;
    }

    public Account(Long id, Long user_id, float count, int inexType, String detailType, int imgRes, Date time, String note) {
        this.id = id;
        this.user_id = user_id;
        this.count = count;
        this.inexType = inexType;
        this.detailType = detailType;
        this.imgRes = imgRes;
        this.time = time;
        this.note = note;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public int getInexType() {
        return inexType;
    }

    public void setInexType(int inexType) {
        this.inexType = inexType;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", count=" + count +
                ", inexType=" + inexType +
                ", detailType='" + detailType + '\'' +
                ", imgRes=" + imgRes +
                ", time=" + time +
                ", note='" + note + '\'' +
                ", view_type=" + view_type +
                ", title_expenditure=" + title_expenditure +
                ", title_income=" + title_income +
                '}';
    }
}
