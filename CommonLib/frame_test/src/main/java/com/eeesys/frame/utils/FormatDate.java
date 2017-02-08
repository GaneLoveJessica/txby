package com.eeesys.frame.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式化
 * @author eeesys
 *
 */
public class FormatDate {
	
	public static String format(int date){
		if(date < 10){
			return "0" + date;
		} else {
			return String.valueOf(date);
		}
	}
	
	public static String format(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return sdf.format(date);
	}
	
	public static String format_2(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
		return sdf.format(date);
	}
	
	public static Date formatyyyy_MM_dd(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static String format(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf =new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.CHINA);
		return sdf.format(d);
	}
	
	public static String format_2(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
			Date d = sdf.parse(date);
			sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			return sdf.format(d);
		} catch (ParseException e) {
			return date;
		}

	}

	public static String tomorrow(){
		//获取当前日期
		Date date = new Date();
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String nowDate = sf.format(date);
			System.out.println(nowDate);
			//通过日历获取下一天日期
			Calendar cal = Calendar.getInstance();
			cal.setTime(sf.parse(nowDate));
			cal.add(Calendar.DAY_OF_YEAR, +1);
			String nextDate_1 = sf.format(cal.getTime());
			System.out.println(nextDate_1);
			//通过秒获取下一天日期
			long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒
			date.setTime(time * 1000);//毫秒
			String nextDate_2 = sf.format(date).toString();
			System.out.println(nextDate_1);
			return nextDate_2;
		} catch (Exception e) {
			return String.valueOf(new Date().getTime());
		}
	}
}
