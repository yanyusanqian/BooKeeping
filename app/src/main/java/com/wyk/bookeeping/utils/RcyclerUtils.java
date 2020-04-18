package com.wyk.bookeeping.utils;

import android.util.Log;

import com.wyk.bookeeping.bean.Account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RcyclerUtils {

    public static List<Account> listAddType(List<Account> accountlist) {
        int VIEW_TITLE = 1;
        int VIEW_ITEM = 0;
        List<Account> list = accountlist;

        List<Map<String, Object>> title;
        if (list.size() != 0) {
            int day = getDateDay(list.get(0).getTime());
            int num = 0;
            float it_expenditure = 0;
            float it_income = 0;
            int sum = 0;
            title = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("adress", 0);
            title.add(map);

            for (int i = 0; i < list.size(); i++) {
                int item_day = getDateDay(list.get(i).getTime());
                float count = list.get(i).getCount();
                list.get(i).setView_type(VIEW_ITEM);

                if (day == item_day) {
                    num++;
                    if (i == list.size() - 1) {
                        if (list.get(i).getInexType() == 1) {
                            title.get(title.size() - 1).put("ex", it_expenditure + count);
                            title.get(title.size() - 1).put("in", it_income);
                        } else {
                            title.get(title.size() - 1).put("ex", it_expenditure);
                            title.get(title.size() - 1).put("in", it_income + count);
                        }
                        if (num == 1&&list.size()!=1) {
                            map = new HashMap<>();
                            map.put("adress", num + sum + title.size());
                            title.add(map);

                            if (list.get(i).getInexType() == 1) {
                                title.get(title.size() - 1).put("ex", it_expenditure + count);
                                title.get(title.size() - 1).put("in", it_income);
                            } else {
                                title.get(title.size() - 1).put("ex", it_expenditure);
                                title.get(title.size() - 1).put("in", it_income + count);
                            }
                        }

                        Log.i("LIST adress", "" + title.get(title.size() - 1).get("adress"));

                    }
                } else {
                    title.get(title.size() - 1).put("ex", it_expenditure);
                    title.get(title.size() - 1).put("in", it_income);

                    map = new HashMap<>();
                    map.put("adress", num + sum + title.size());
                    title.add(map);

                    sum += num;
                    it_income = 0;
                    it_expenditure = 0;
                    day = item_day;
                    num = 1;

                    if (i == list.size() - 1) {
                        Log.i("LIST adress", "" + title.get(title.size() - 1).get("adress"));
                        if (list.get(i).getInexType() == 1) {
                            title.get(title.size() - 1).put("ex", it_expenditure + count);
                            title.get(title.size() - 1).put("in", it_income);
                        } else {
                            title.get(title.size() - 1).put("ex", it_expenditure);
                            title.get(title.size() - 1).put("in", it_income + count);
                        }
                    }
                }

                if (list.get(i).getInexType() == 1) {
                    it_expenditure += count;
                } else {
                    it_income += count;
                }
            }

            Log.i("LIST TITLE:", title.toString());

            for (int i = 0; i < title.size(); i++) {

                Account account = new Account();

                Log.i("LIST ex", (Float) title.get(i).get("ex") + "");
                account.setTitle_expenditure(Float.parseFloat(title.get(i).get("ex").toString()));
                account.setTitle_income(Float.parseFloat(title.get(i).get("in").toString()));
                account.setView_type(VIEW_TITLE);
                list.add(Integer.parseInt(title.get(i).get("adress").toString()), account);
            }

            for (int i = 0; i < list.size(); i++) {
                Log.i("LIST::", list.get(i).toString());
            }

        }
        return list;
    }

    public static int getDateDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
