package com.wyk.bookeeping.livedate;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.wyk.bookeeping.bean.DataStatus;

public class DataStatusLiveData extends MutableLiveData<DataStatus> {

    private DataStatusLiveData() {
    }

    private static class Holder {
        public static final DataStatusLiveData INSTANCE = new DataStatusLiveData();
    }

    public static DataStatusLiveData getInstance() {
        return Holder.INSTANCE;
    }
}


/*//MutableLiveData在LiveData基础上暴露两个设值接口
class MutableLiveData<T> extends LiveData<T> {
    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }*/

//}