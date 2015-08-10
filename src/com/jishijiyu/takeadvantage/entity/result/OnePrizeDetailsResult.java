package com.jishijiyu.takeadvantage.entity.result;

import android.R.integer;

/**
 * 一元摇奖晒奖详情
 * 
 * @author sunaoyang
 * 
 */
public class OnePrizeDetailsResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public ShowAward showAward;
		public boolean isTrue;
	}

	public class ShowAward {
		public String awardContent;
		public Integer awardGrade;
		public String awardName;
		public String imgUrl;
		public String mobile;
		public String nikName;
		public String periods;
		public String mealType;
		public String roomType;
	}
}
