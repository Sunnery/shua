package com.guesslive.admin.common.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUntils {
    public static String getTodayFull(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getYml(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf.parse(dateStr);
        return sdf.format(date);
    }

    public static String getToday(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getYesterDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return sdf.format(calendar.getTime());
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(getTodayFull());
//        System.out.println(getToday());
//        System.out.println(getYesterDay());
        System.out.println(getYml("2018-09-22 08:10:10"));
    }

}
