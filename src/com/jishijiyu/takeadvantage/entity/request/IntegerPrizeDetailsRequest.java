package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 拍币摇奖晒奖详情
 * 
 * @author sunaoyang
 * 
 */
public class IntegerPrizeDetailsRequest {
	public String c = Constant.INTEGRAL_PRIZE_DETAILS;
	public Parameter p;

	public IntegerPrizeDetailsRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String id;
		public String userId;
	}
}
