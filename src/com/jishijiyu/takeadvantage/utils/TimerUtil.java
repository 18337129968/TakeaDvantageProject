package com.jishijiyu.takeadvantage.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.util.Log;

public class TimerUtil {

	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_STRING = "yyyyMMddhhmmssSSS";
	public final static String DATE_SHORT_FORMAT = "yyyy-MM-dd";

	/**
	 * 格式转换 时间串转 字符
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String parseDateToString(Date date, String style) {
		SimpleDateFormat df = new SimpleDateFormat(style);
		return df.format(date);
	}

	// 输入时间戳，时间格式,返回需要类型比如输入（1433225765000,"yyyy-MM-dd"）
	public static String getTimestamp(String time, String type) {
		SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return times;
	}

	public static String timeo(String time, String type) {
		SimpleDateFormat sdr = new SimpleDateFormat(type);
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		String times = sdr.format(new Date(lcc));
		return times;

	}

	/**
	 * 比较两个日期相差多少天， 如果date1=2012-10-26<br>
	 * date2=2012-10-25 return 1
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int ComparisonOfDays(Date date1, Date date2) {
		long day = 60 * 60 * 24 * 1000;
		long d1 = date1.getTime();
		long d2 = date2.getTime();
		d1 = d1 / day;
		d2 = d2 / day;
		return Integer.parseInt((d2 - d1) + "");
	}

	/**
	 * 比较两个时间是否为同年，同月，同一天 TimeUtils.equalsDay()<BR>
	 * <P>
	 * </P>
	 * <P>
	 * Date : 2013-8-19 下午4:29:53
	 * </P>
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean equalsDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		String d1 = parseDateToString(date1, DATE_SHORT_FORMAT);
		String d2 = parseDateToString(date2, DATE_SHORT_FORMAT);
		return d1.equals(d2);
	}
}
