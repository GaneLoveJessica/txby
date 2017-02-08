package com.tianxiabuyi.sports_medicine.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    /**
     * 时间转换
     */
    public static String getPrefix(String datetime) {
        if (datetime == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long currentSeconds = System.currentTimeMillis();

        String timeStr = "";
        long timeGap = currentSeconds - date.getTime();// 与现在时间差
        if (timeGap > 24 * 3 * 60 * 60 * 1000) {
            timeStr = getDayTime(date.getTime());
        } else if (timeGap > 24 * 2 * 60 * 60 * 1000) {
            timeStr = "前天 " + getMinTime(date.getTime());
        } else if (timeGap > 24 * 60 * 60 * 1000) {
            timeStr = "昨天 " + getMinTime(date.getTime());
        } else if (timeGap > 60 * 60 * 1000) {
            timeStr = timeGap / 3600 / 1000 + "小时前";
        } else if (timeGap > 60 * 1000) {
            timeStr = timeGap / 60 / 1000 + "分钟前";
        } else {
            timeStr = "刚刚";
        }
        return timeStr;
    }

    public static String getDayTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
        String date = format.format(new Date(time));
        if (date.substring(0, 4).equals(getCurrentYear()))
            return date.substring(5);
        else
            return date;
    }

    public static String getMinTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return format.format(new Date(time));
    }

    public static String getTodayDate(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(date);
    }

    public static String getCurrentMonth(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        return sdf.format(date);
    }

    public static String getCurrentYear() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.CHINA);
        return sdf.format(date);
    }

}
