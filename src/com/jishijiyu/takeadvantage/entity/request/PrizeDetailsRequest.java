package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 
 * 奖品详情请求数据
 * 
 * @author zhaobin
 */
public class PrizeDetailsRequest {

	private String c = Constant.PRIZE_DETAIL;

	public Parameter p;

	public PrizeDetailsRequest() {
		p = new Parameter();
	}

	public class Parameter {
		public String prizeId;
		public String tokenId;
		public String userId;
	}
}
