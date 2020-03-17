package com.wyk.bookeeping.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luo
 * @date 2017/8/21
 */
public class TimeUtil {
    public static String[] WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    public static final int WEEKDAYS = 7;

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getLastDateStr(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("MM月dd日");
        Date beginDate = new Date();
        Calendar calendar = Calendar.getInstance();
        //calendar.set(2017, 0, 7);
        calendar.setTime(beginDate);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate) + "      " + getWeekByDate(endDate);
    }

    public static Date getLastDate(int distanceDay) {
        Date beginDate = new Date();
        Calendar calendar = Calendar.getInstance();
        //calendar.set(2017, 0, 7);
        calendar.setTime(beginDate);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + distanceDay);
        Date endDate = calendar.getTime();
        return endDate;
    }

    /*public static int getDateDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }*/

    /**
     * 根据date获取前几天或后几天
     *
     * @param date
     * @param distanceDay
     * @return
     */
    public static Date getDistanceDate(Date date, int distanceDay) {
        //Date beginDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + distanceDay);
        Date endDate = calendar.getTime();
        return endDate;
    }

    /**
     * 获取当前月几个月之前或之后的月份
     *
     * @param date
     * @param distanceDay 如一个月之前传-1，一个月之后传1
     * @return
     */
    public static Date getMonthAgo(Date date, int distanceDay) {
        Calendar calendar = Calendar.getInstance();
        //calendar.set(2017, 0, 7);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, distanceDay);
        Date endDate = calendar.getTime();
        return endDate;
    }


    //获取某个日期的开始时间
    public static Date getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    //获取某个日期的结束时间
    public static Date getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取当前周第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取当前周最后一天
     *
     * @param date
     * @return
     */
    public static Date getEndDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFirstDayOfWeek(date));
        cal.add(Calendar.DAY_OF_WEEK, 5);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }


//    /**
//     * 获取当前周第一天
//     * @param date
//     * @return
//     */
//    public static Date getFirstDayOfWeek(Date date) {
//        Calendar cDay = Calendar.getInstance();
//        cDay.setTime(date);
//        cDay.set(Calendar.HOUR, 0);
//        cDay.set(Calendar.MINUTE, 0);
//        final int lastDay = cDay.getActualMinimum(Calendar.DAY_OF_WEEK);
//        Date lastDate = cDay.getTime();
//        lastDate.setDate(lastDay);
//        return lastDate;
//    }
//
//    /**
//     * 获取当前周最后一天
//     * @param date
//     * @return
//     */
//    public static Date getEndDayOfWeek(Date date) {
//        Calendar cDay = Calendar.getInstance();
//        cDay.setTime(date);
//        cDay.set(Calendar.HOUR, 23);
//        cDay.set(Calendar.MINUTE, 59);
//        final int lastDay = cDay.getActualMaximum(Calendar.DAY_OF_WEEK);
//        Date lastDate = cDay.getTime();
//        lastDate.setDate(lastDay);
//        return lastDate;
//    }

    /**
     * 获取当前月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.HOUR_OF_DAY, 0);
        cDay.set(Calendar.MINUTE, 0);
        final int lastDay = cDay.getActualMinimum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    /**
     * 获取当前月最后一天
     *
     * @param date
     * @return
     */
    public static Date getEndDayOfMonth(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.HOUR_OF_DAY, 23);
        cDay.set(Calendar.MINUTE, 59);
        final int lastDay = cDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    /**
     * 获取当前月第一天
     *
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(int month) {
        Calendar cDay = Calendar.getInstance();
        cDay.set(Calendar.MONTH, month);
        cDay.set(Calendar.HOUR_OF_DAY, 0);
        cDay.set(Calendar.MINUTE, 0);
        final int lastDay = cDay.getActualMinimum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    /**
     * 获取当前月最后一天
     *
     * @param month
     * @return
     */
    public static Date getEndDayOfMonth(int month) {
        Calendar cDay = Calendar.getInstance();
        cDay.set(Calendar.MONTH, month);
        cDay.set(Calendar.HOUR_OF_DAY, 23);
        cDay.set(Calendar.MINUTE, 59);
        final int lastDay = cDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    /**
     * 获取当前年第一天
     *
     * @param year
     * @return
     */
    public static Date getFirstDayOfYear(int year) {
        Calendar cDay = Calendar.getInstance();
        cDay.set(Calendar.YEAR, year);
        cDay.set(Calendar.MONTH, 0);
        cDay.set(Calendar.HOUR_OF_DAY, 0);
        cDay.set(Calendar.MINUTE, 0);
        final int lastDay = cDay.getActualMinimum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    /**
     * 获取当前年最后一天
     *
     * @param year
     * @return
     */
    public static Date getEndDayOfYear(int year) {
        Calendar cDay = Calendar.getInstance();
        cDay.set(Calendar.YEAR, year);
        cDay.set(Calendar.MONTH, 11);
        cDay.set(Calendar.HOUR_OF_DAY, 23);
        cDay.set(Calendar.MINUTE, 59);
        final int lastDay = cDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date lastDate = cDay.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    /**
     * 获取date在一年中的天数
     *
     * @param date
     * @return
     */
    public static int getDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取date在一月中的天数
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取date中的月份，从0开始
     *
     * @param date
     * @return
     */
    public static int getDateMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取当前时间
     * yyyy-MM-dd日   HH:mm
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date();//获取当前时间
        return formatter.format(curDate);
    }


    public static int getNowDateHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前月份，月份从0开始
     *
     * @return
     */
    public static int getNowDateMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static String getNowDateMonth_String() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        if(month<10){
            return "0"+month;
        }
        return ""+month;

    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getNowDateYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getNowDateMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取当前日期在一年中的哪一个轴
     * 一年有52个周，该函数的取值范围是1-52
     * 所以当一年中最后的几天超过52周，进入第53周时，将以下一年的第一周来计算
     *
     * @param date
     * @return 返回当前日期在一年中的哪一个周
     */
    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 通过一年中的第几周获取date
     *
     * @param week
     * @return
     */
    public static Date getDateByWeek(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        return calendar.getTime();
    }


    /**
     * calendar从0开始计算月份，所以加1
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取传入日期的年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 日期转星期
     *
     * @param date
     * @return
     */
    public static String getWeekByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }

        return WEEK[dayIndex - 1];
    }

    /**
     * 获取本年的开始时间
     *
     * @param date
     * @return
     */
    public static Date getBeginDayOfYear(Date date) {
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(date);
        cDay.set(Calendar.YEAR, cDay.get(Calendar.YEAR));
        cDay.set(Calendar.MONTH, Calendar.JANUARY);
        cDay.set(Calendar.DATE, 1);
        cDay.set(Calendar.HOUR_OF_DAY, 0);
        cDay.set(Calendar.MINUTE, 0);
        return cDay.getTime();
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     *
     * @param dateStr
     * @return
     */
    public static Date string2Date(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);// 严格解析
            d = formater.parse(dateStr);
        } catch (Exception e) {
            d = null;
        } finally {
            formater = null;
        }
        return d;
    }

    /**
     * 把日期转换为字符串
     *
     * @param date
     * @param format exsample: yyyy-MM-dd日   HH:mm
     * @return
     */
    public static String date2String(Date date, String format) {
        if (date == null) {
            return "";
        }
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            result = formater.format(date);
        } catch (Exception e) {
            result = "";
        } finally {
            formater = null;
        }
        return result;
    }

    public static List<String> getBetweenDateList_week(Map<String, String> map) {
        Date maxdate = TimeUtil.string2Date(map.get("MAX"), "yyyy-MM-dd");//最近日期
        Date mindate = TimeUtil.string2Date(map.get("MIN"), "yyyy-MM-dd");//最远日期
        int maxweek = TimeUtil.getWeekOfYear(maxdate);
        int minweek = TimeUtil.getWeekOfYear(mindate);
        int maxyear = TimeUtil.getYear(maxdate);
        int minyear = TimeUtil.getYear(mindate);
        List<String> list = new ArrayList<>();
        if (maxyear != minyear) {
            for (int i = 1; i <= maxweek; i++) {
                if (i < 10)
                    list.add("0" + i + "周");
                else
                    list.add(i + "周");
            }
        }else{
            for (int i = minweek; i <= maxweek; i++) {
                if (i < 10)
                    list.add("0" + i + "周");
                else
                    list.add(i + "周");
            }
        }
        return list;
    }

    public  static List<String> getBetweenDateList_month(Map<String,String> map){
        Date maxdate = TimeUtil.string2Date(map.get("MAX"), "yyyy-MM-dd");//最近日期
        Date mindate = TimeUtil.string2Date(map.get("MIN"), "yyyy-MM-dd");//最远日期
        int maxmonth = TimeUtil.getMonth(maxdate);
        int minmonth = TimeUtil.getMonth(mindate);
        int maxyear = TimeUtil.getYear(maxdate);
        int minyear = TimeUtil.getYear(mindate);

        List<String> list = new ArrayList<>();
        if (maxyear != minyear) {
            for (int i = 1; i <= maxmonth; i++) {
                if (i < 10)
                    list.add("0" + i + "月");
                else
                    list.add(i + "月");
            }
        }else{
            for (int i = minmonth; i <= maxmonth; i++) {
                if (i < 10)
                    list.add("0" + i + "月");
                else
                    list.add(i + "月");
            }
        }
        return list;
    }

    public  static List<String> getBetweenDateList_year(Map<String,String> map){
        Date maxdate = TimeUtil.string2Date(map.get("MAX"), "yyyy-MM-dd");//最近日期
        Date mindate = TimeUtil.string2Date(map.get("MIN"), "yyyy-MM-dd");//最远日期
        int maxyear = TimeUtil.getYear(maxdate);
        int minyear = TimeUtil.getYear(mindate);

        List<String> list = new ArrayList<>();
        for(int i = minyear;i<maxyear;i++){
            list.add(i+"年");
        }
        return list;
    }

}
