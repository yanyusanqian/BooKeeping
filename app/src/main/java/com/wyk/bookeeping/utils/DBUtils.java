package com.wyk.bookeeping.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wyk.bookeeping.bean.Account;

import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    private SQLiteDatabase db;

    /**
     * 查询YYYY年第m月的数据
     * @param context 上下文
     * @param year 年份，格式为YYYY
     * @param month 月份，格式为01-12月
     * @return
     */
    public List<Account> getMonthData(Context context,String year,String month){
        db = DBHelper.getInstance(context).getWritableDatabase();
        List<Account> list = new ArrayList<>();
        String sql = "select * from account where strftime('%Y-%m',time)='" + year+"-"+month + "' order by time desc";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getLong(0), cursor.getFloat(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getInt(5),
                        TimeUtil.string2Date(cursor.getString(6), "yyyy-MM-dd")
                        , cursor.getString(7));
                list.add(account);
            }
        }
        return list;
    }

    /**
     * 查询数据库中YYYY年第w周的数据
     * @param context 上下文
     * @param year 年份，格式为YYYY
     * @param week 月份，格式为01-53周
     * @return
     */
    public List<Account> getWeekData(Context context,String year,String week){
        db = DBHelper.getInstance(context).getWritableDatabase();
        List<Account> list = new ArrayList<>();
        String sql = "select * from account where strftime('%Y-%w',time)='" + year+"-"+week + "' order by time desc";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getLong(0), cursor.getFloat(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getInt(5),
                        TimeUtil.string2Date(cursor.getString(6), "yyyy-MM-dd")
                        , cursor.getString(7));
                list.add(account);
            }
        }
        return list;
    }

    /**
     * 查询某YYYY年数据
     * @param context 上下文
     * @param year 年份
     * @return
     */
    public List<Account> getYearData(Context context,String year){
        db = DBHelper.getInstance(context).getWritableDatabase();
        List<Account> list = new ArrayList<>();
        String sql = "select * from account where strftime('%Y',time)='" + year+ "' order by time desc";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Account account = new Account(
                        cursor.getLong(0), cursor.getFloat(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getInt(5),
                        TimeUtil.string2Date(cursor.getString(6), "yyyy-MM-dd")
                        , cursor.getString(7));
                list.add(account);
            }
        }
        return list;
    }



}
