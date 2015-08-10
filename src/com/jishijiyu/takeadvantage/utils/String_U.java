package com.jishijiyu.takeadvantage.utils;


/******************************************
 * 类描述：字符串工具类 类名称：String_U
 * 
 ******************************************/

public class String_U {

	private String_U() {
	}

	/**
	 * 字符串安全比较
	 * 
	 * @param lStr
	 *            左侧String
	 * @param rStr
	 *            右侧String
	 * @return boolean true 两字符串内容一致，false两字符串内容不一致
	 */
	public static boolean equal(String lStr, String rStr) {
		if (lStr == null) {
			return lStr == rStr;
		}

		return lStr.equals(rStr);
	}

}
