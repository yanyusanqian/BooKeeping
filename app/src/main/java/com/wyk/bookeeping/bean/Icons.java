package com.wyk.bookeeping.bean;

import java.io.Serializable;

public class Icons implements Serializable {
    private int drawableid;
    private String title;
    // 是否为标题，是为1，否为0
    private int istitle;

    public Icons(String title, int istitle) {
        this.title = title;
        this.istitle = istitle;
    }

    public Icons(int drawableid, int istitle) {
        this.drawableid = drawableid;
        this.istitle = istitle;
    }

    public Icons(int drawableid, String title) {
        this.drawableid = drawableid;
        this.title = title;
    }

    public int getDrawableid() {
        return drawableid;
    }

    public void setDrawableid(int drawableid) {
        this.drawableid = drawableid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIstitle() {
        return istitle;
    }

    public void setIstitle(int istitle) {
        this.istitle = istitle;
    }
}
