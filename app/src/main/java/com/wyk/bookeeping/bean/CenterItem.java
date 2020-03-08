package com.wyk.bookeeping.bean;

public class CenterItem {
    private int itemType;
    private int spanSize;
    private String string;
    public boolean isShow;
    public int count;

    public CenterItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public CenterItem(int itemType, int spanSize, String string) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }
}
