package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 拍币摇奖我要晒奖
 * 
 * @author sunaoyang
 * 
 */
public class IntegerPrizeShowRequest {
	public String c = Constant.INTEGRAL_PRIZE_SHOW;
	public Parameter p;

	public IntegerPrizeShowRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String awardId;
		public String tokenId;
		public String userId;
		public String awardContent;
		public String imgUrl;
		public String periods;
		public String awardGrade;
		public String awardName;
	}
}
