package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 一元摇奖晒奖详情
 * 
 * @author sunaoyang
 * 
 */
public class OnePrizeDetailsRequest {
	public String c = Constant.ONE_PRIZE_DETAILS;
	public Parameter p;

	public OnePrizeDetailsRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String id;
		public String userId;
	}
}
