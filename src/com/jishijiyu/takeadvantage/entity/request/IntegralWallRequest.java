package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 拍币墙
 * 
 * @author zhaobin
 */
public class IntegralWallRequest {
	public String c = Constant.INTEGRALWALLCODE;
	public Pramater p;

	public IntegralWallRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String tokenId;
		public String userId;
		public String page;
		public String pageSize;

	}
}
