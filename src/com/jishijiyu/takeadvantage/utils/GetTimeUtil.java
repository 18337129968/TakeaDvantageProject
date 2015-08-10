package com.jishijiyu.takeadvantage.utils;

import java.text.SimpleDateFormat;

/**
 * 时间戳转换
 * 
 * @author shifeiyu
 * 
 */
public class GetTimeUtil {
	public static String GetTime(long cc_time) {
		String str_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str_time = sdf.format(cc_time);
		return str_time;

	}

	public static String GetTime2(long cc_time) {
		String str_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		str_time = sdf.format(cc_time);
		return str_time;

	}

	public static String GetTime3(long cc_time) {
		String str_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		str_time = sdf.format(cc_time);
		return str_time;

	}
}
