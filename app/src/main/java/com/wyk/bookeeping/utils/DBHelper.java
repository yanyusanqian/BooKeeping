package com.wyk.bookeeping.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance = null;
    public static final String CREATE_USERDATA = "create table if not exists account" +
            "(_id integer primary key autoincrement," +
            "user_id integer," +
            "count real not null," +
            "inexType integer not null," +
            "detailType text not null," +
            "imgRes integer not null," +
            "time text not null," +
            "note text not null)";

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public DBHelper(Context context){
        super(context,"bookeeping",null,1);

    }
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERDATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
