package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 
 * 显示1234等奖请求信息
 * 
 * @author zhaobin
 */
public class ShowPriceRequest {
	public String c = Constant.SHOW_PRICE_CODE;
	public Pramater p;

	public ShowPriceRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String ernieId;
		public String userId;
		public String tokenId;
	}
}
