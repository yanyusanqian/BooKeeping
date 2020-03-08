package com.wyk.bookeeping.bean;

import java.io.Serializable;

public class Icons implements Serializable {
    private int drawableid;
    private String title;

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
}
