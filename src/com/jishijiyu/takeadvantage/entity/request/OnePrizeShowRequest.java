package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 一元摇奖我要晒奖
 * 
 * @author sunaoyang
 * 
 */
public class OnePrizeShowRequest {
	public String c = Constant.ONE_PRIZE_SHOW;
	public Parameter p;

	public OnePrizeShowRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String userId;
		public String awardContent;
		public String awardId;
		public String periods;
		public String awardGrade;
		public String mealType;
		public String roomType;
		public String awardName;
		public String imgUrl;
	}
}
