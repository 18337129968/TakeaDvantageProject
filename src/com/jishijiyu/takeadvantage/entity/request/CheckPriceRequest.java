package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 报名参加摇奖
 * 
 * @author zhaobin
 */
public class CheckPriceRequest {
	public String c = Constant.CHECK_PRICE_CODE;
	public Pramater p;

	public CheckPriceRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String ernieId;
		public String page;
		public String pageSize;
		public String userId;
		public String tokenId;
	}
}
