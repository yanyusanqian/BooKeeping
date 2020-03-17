package com.wyk.bookeeping.livedate;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.DataStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class AccountViewModel extends ViewModel {
    private MutableLiveData<DataStatus> currentName;
    private MutableLiveData<List<Account>> allaccount;
    private MutableLiveData<Map<String, String>> date;

    public MutableLiveData<DataStatus> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<DataStatus>();
        }
        return currentName;
    }

    public MutableLiveData<List<Account>> getAllaccount() {
        if (allaccount == null) {
            allaccount = new MutableLiveData<List<Account>>();
        }
        return allaccount;
    }

    public MutableLiveData<Map<String, String>> getDate() {
        if(date == null){
            date = new MutableLiveData<Map<String,String>>();
        }
        return date;
    }
}
