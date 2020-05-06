package com.wyk.bookeeping.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.wyk.bookeeping.bean.Account;
import com.wyk.bookeeping.bean.MyChartData;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance = null;
    private SQLiteDatabase db;
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

    public DBHelper(Context context) {
        super(context, "bookeeping", null, 1);

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

    /**
     * 同步数据
     */
    public void InsertAllAccount(Context context, List<Account> accounts) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        StringBuilder sql = new StringBuilder();
        if (accounts.size() == 0)
            return ;
        else {
            if (accounts.size() == 1) {
                Log.i("Insert","1");
                Account account = accounts.get(0);
                // 创建新的ContentValues对象，将数据存入其中
                ContentValues values = new ContentValues();
                values.put("_id", account.getBill_id());
                values.put("user_id", account.getUser_id());
                values.put("count", account.getBill_count());
                values.put("inexType", account.getBill_inexType());
                values.put("detailType", account.getBill_detailType());
                values.put("imgRes", account.getBill_imgRes());
                values.put("time", account.getBill_time());
                values.put("note", account.getBill_note());
                // 插入数据
                db.insert("account", null, values);
            } else {
                Log.i("Insert","2");
                Account account = accounts.get(0);
                sql = new StringBuilder("insert into account(_id,user_id,count,inexType,detailType,imgRes,time,note) " +
                        "select " + account.getBill_id() + "," + account.getUser_id() + "," + account.getBill_count() +
                        "," + account.getBill_inexType() + ",'" + account.getBill_detailType() + "'," + account.getBill_imgRes() +
                        ",'" + account.getBill_time()+ "','" + account.getBill_note() + "' ");
                for (int i = 1; i < accounts.size(); i++) {
                    Account account1 = accounts.get(i);
                    sql.append("union all select ").append(account1.getBill_id()).append(",")
                            .append(account1.getUser_id()).append(",").append(account1.getBill_count())
                            .append(",").append(account1.getBill_inexType()).append(",'")
                            .append(account1.getBill_detailType()).append("',").append(account1.getBill_imgRes())
                            .append(",'").append(account1.getBill_time()).append("','").
                            append(account1.getBill_note()).append("' ");
                }
                db.execSQL(sql.toString());
            }
        }
    }

    public void deleteAccount(Context context){
        db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM account");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'account'");
    }

    /**
     * 查询记账总数
     */
    public String getAllAccountNum(Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        String sql = "select count(*) from account";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor.getString(0) != null) {
            return cursor.getString(0);
        }
        return "0";
    }

    /**
     * 按月份查询
     */
    public List<Account> getFirstAccountList(Context context, String year, String month) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        List<Account> accountList = new ArrayList<>();
        String sql = "select * from account where strftime('%Y-%m',time)='" + year + "-" + month + "' order by time desc,_id desc";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getLong(0), cursor.getFloat(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getInt(5),
                        cursor.getString(6)
                        , cursor.getString(7));
                accountList.add(account);
            }
        }
        return accountList;
    }

    /**
     * 查询所有Account数据
     */
    public List<Account> getAllAccountList(Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        List<Account> accountList = new ArrayList<>();
        String sql = "select * from account";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getLong(0), cursor.getFloat(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getInt(5),
                       cursor.getString(6)
                        , cursor.getString(7));
                accountList.add(account);
            }
        }
        return accountList;
    }

    public Map<String, Float> getInExCount(Context context) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        Map<String, Float> map = new HashMap<>();
        int year = TimeUtil.getNowDateYear();
        String month = TimeUtil.getNowDateMonth_String();
        String sql = "select SUM(count) from account where strftime('%Y-%m',time)='" + year + "-" + month + "' and inexType=1;";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor.getString(0) != null) {
            map.put("ex", Float.parseFloat(cursor.getString(0)));
        }
        String sql2 = "select SUM(count) from account where strftime('%Y-%m',time)='" + year + "-" + month + "' and inexType=0;";
        Cursor cursor2 = db.rawQuery(sql2, null);
        cursor2.moveToFirst();
        if (cursor2.getString(0) != null) {
            map.put("in", Float.parseFloat(cursor2.getString(0)));
        }
        return map;
    }

    /**
     * 查询数据库中YYYY年第w周的数据
     *
     * @param context 上下文
     * @param year    年份，格式为YYYY
     * @param week    周，格式为01-53周，但数据库查询时周从00开始，所以做了一层处理
     * @return
     */
    public MyChartData getWeekData(Context context, String year, String week, int inextype) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        int t = Integer.parseInt(week) - 1;
        if (t < 10) {
            week = "0" + t;
        } else {
            week = "" + t;
        }
        List<Account> accountList = new ArrayList<>();
        MyChartData myChartData = new MyChartData();
        Map<String, Float> map = new HashMap<>();
        String sql = "select strftime('%m-%d',time),SUM(count) from account where strftime('%Y-%W',time)='" +
                year + "-" + week + "' and inexType=" + inextype + " group by strftime('%d',  time)";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                map.put(cursor.getString(0), Float.valueOf(cursor.getString(1)));
            }
            myChartData.setCount(map);
        }

        String sql2 = "select detailType,imgRes,sum(count),count(*) from account where strftime('%Y-%W',time)='" + year + "-" + week + "' and inexType=" +
                inextype + " group by detailType";
        cursor = db.rawQuery(sql2, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getString(0), cursor.getInt(1), cursor.getFloat(2),
                        cursor.getInt(3));
                accountList.add(account);
            }
            myChartData.setList(accountList);
        }

        String sql3 = "select COUNT(*),SUM(count),MAX(count) from account where strftime('%Y-%W',time)='" +
                year + "-" + week + "'  and inexType=" + inextype + " order by time";
        cursor = db.rawQuery(sql3, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            if (cursor.getString(1) != null) {
                myChartData.setNum(cursor.getInt(0));
                myChartData.setAllcount(cursor.getFloat(1));
                myChartData.setMaxcount(cursor.getFloat(2));
            }
        }
        return myChartData;
    }

    /**
     * 查询数据库中YYYY年第m月的数据
     *
     * @param context 上下文
     * @param year    年份，格式为YYYY
     * @param month   月，格式为01-12月
     * @return
     */
    public MyChartData getMonthData(Context context, String year, String month, int inextype) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        List<Account> accountList = new ArrayList<>();
        MyChartData myChartData = new MyChartData();
        Map<String, Float> map = new HashMap<>();
        String sql = "select strftime('%m-%d',time),SUM(count) from account where strftime('%Y-%m',time)='" +
                year + "-" + month + "' and inexType=" + inextype + " group by strftime('%m-%d',  time)";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                map.put(cursor.getString(0), Float.valueOf(cursor.getString(1)));
            }
            myChartData.setCount(map);
        }

        String sql2 = "select detailType,imgRes,sum(count),count(*) from account where strftime('%Y-%m',time)='"
                + year + "-" + month + "' and inexType=" + inextype + " group by detailType";
        cursor = db.rawQuery(sql2, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getString(0), cursor.getInt(1), cursor.getFloat(2),
                        cursor.getInt(3));
                accountList.add(account);
            }
            myChartData.setList(accountList);
        }

        String sql3 = "select COUNT(*),SUM(count),MAX(count) from account where strftime('%Y-%m',time)='" +
                year + "-" + month + "'  and inexType=" + inextype + " order by time";
        cursor = db.rawQuery(sql3, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            if (cursor.getString(1) != null) {
                myChartData.setNum(cursor.getInt(0));
                myChartData.setAllcount(cursor.getFloat(1));
                myChartData.setMaxcount(cursor.getFloat(2));
            }
        }
        return myChartData;
    }

    /**
     * 查询某YYYY年数据
     *
     * @param context 上下文
     * @param year    年份
     * @return
     */
    public MyChartData getYearData(Context context, String year, int inextype) {
        db = DBHelper.getInstance(context).getWritableDatabase();
        List<Account> accountList = new ArrayList<>();
        MyChartData myChartData = new MyChartData();
        Map<String, Float> map = new HashMap<>();
        String sql = "select strftime('%m',time),SUM(count) from account where strftime('%Y',time)='" +
                year + "' and inexType=" + inextype + " group by strftime('%m',  time)";
        Log.i("TAG", "SQL:" + sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                map.put(cursor.getString(0), Float.valueOf(cursor.getString(1)));
            }
            myChartData.setCount(map);
        }

        String sql2 = "select detailType,imgRes,sum(count),count(*) from account where strftime('%Y',time)='"
                + year + "'  and inexType=" + inextype + " group by detailType";
        cursor = db.rawQuery(sql2, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getString(0), cursor.getInt(1), cursor.getFloat(2),
                        cursor.getInt(3));
                accountList.add(account);
            }
            myChartData.setList(accountList);
        }

        String sql3 = "select COUNT(*),SUM(count),MAX(count) from account where strftime('%Y',time)='" +
                year + "' and inexType=" + inextype + " order by time";
        cursor = db.rawQuery(sql3, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            if (cursor.getString(1) != null) {
                myChartData.setNum(cursor.getInt(0));
                myChartData.setAllcount(cursor.getFloat(1));
                myChartData.setMaxcount(cursor.getFloat(2));
            }
        }
        return myChartData;
    }
}
