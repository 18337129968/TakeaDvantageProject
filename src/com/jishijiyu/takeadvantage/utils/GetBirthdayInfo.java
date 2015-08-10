package com.jishijiyu.takeadvantage.utils;

public class GetBirthdayInfo {
	private static int year, month, day;

	public static int getYear(String s) {
		year = Integer.parseInt(s.substring(0, 4));
		return year;

	}

	public static int getMonth(String s) {
		if (s.length() <= 10) {
			if (s.substring(5, 7).contains("月")||
					s.substring(5, 7).contains("-")) {
				month = Integer.parseInt(s.substring(5, 6));
			} else {
				month = Integer.parseInt(s.substring(5, 7));
			}
		} else {
			month = Integer.parseInt(s.substring(5, 7));
		}
		return month;

	}

	public static int getDay(String s) {
		if (s.length() <= 10) {
			if(s.length()==8){
				day=Integer.parseInt(s.substring(7, 8));
				return day;
			}
			if (s.substring(5, 7).contains("月")||
					s.substring(5, 7).contains("-")) {
				if (s.substring(7, 9).contains("日")||
						s.substring(7, 9).contains("-")) {
					day = Integer.parseInt(s.substring(7, 8));
				}else{
					day = Integer.parseInt(s.substring(7, 9));
				}
			} else {
				if(s.length()==9){
					if(!s.substring(5, 7).contains("月")||
							!s.substring(5, 7).contains("-")){
						day = Integer.parseInt(s.substring(8, 9));
						return day;
					}
				}
				if (s.substring(8, 10).contains("日")||
						s.substring(8, 10).contains("-")) {
					day = Integer.parseInt(s.substring(8, 9));
				} else {
					day = Integer.parseInt(s.substring(8, 10));
				}
			}
		} else {
			day = Integer.parseInt(s.substring(8, 10));
		}
		return day;

	}

}
