package com.jishijiyu.takeadvantage.utils;

/**
 * 根据mealtype获得总参与人数
 * 
 * @author shifeiyu
 * 
 */
public class GetMealCountUtil {
	// 获取mealtype总参与人数
	public static int GetMealCount(int mealType) {
		int a = 0;
		switch (mealType) {
		case 5:
			a = 500;
			break;
		case 6:
			a = 1000;
			break;
		case 7:
			a = 2000;
			break;
		case 8:
			a = 5000;
			break;

		default:
			break;
		}
		return a;
	}

	// 获取奖级
	public static String GetGrade(int grade) {
		String a = "";
		switch (grade) {
		case 1:
			a = "一等奖";
			break;
		case 2:
			a = "二等奖";
			break;
		case 3:
			a = "三等奖";
			break;
		case 4:
			a = "四等奖";
			break;

		default:
			break;
		}
		return a;
	}
}
