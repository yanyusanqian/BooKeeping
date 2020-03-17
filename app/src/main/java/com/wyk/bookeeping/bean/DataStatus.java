package com.wyk.bookeeping.bean;

import java.util.List;

public class DataStatus {
    private List<Account> accountList;
    private List<Account> accountList_recycler;

    public List<Account> getAccountList_recycler() {
        return accountList_recycler;
    }

    public void setAccountList_recycler(List<Account> accountList_recycler) {
        this.accountList_recycler = accountList_recycler;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
