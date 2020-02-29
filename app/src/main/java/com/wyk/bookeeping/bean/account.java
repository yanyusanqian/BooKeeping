package com.wyk.bookeeping.bean;

import java.util.Date;

public class account {
    // 单笔id值
    private Long id;
    // 账号id,用于连接用户表，用户仅使用本地功能时可以为空
    private Long user_id;
    // 单笔金额
    private float count;
    // 单笔类型，收入或支出
    private int inexType;
    // 具体类型，如出游、餐饮等
    private String detailType;
    // 具体类型图标，应为R.drawable.xx
    private int imgRes;
    // 记账时间
    private Date time;
    // 备注
    private String note;

    public account(Long id, float count, int inexType, String detailType, int imgRes, Date time, String note) {
        this.id = id;
        this.count = count;
        this.inexType = inexType;
        this.detailType = detailType;
        this.imgRes = imgRes;
        this.time = time;
        this.note = note;
    }

    public account(Long id, Long user_id, float count, int inexType, String detailType, int imgRes, Date time, String note) {
        this.id = id;
        this.user_id = user_id;
        this.count = count;
        this.inexType = inexType;
        this.detailType = detailType;
        this.imgRes = imgRes;
        this.time = time;
        this.note = note;
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
}
