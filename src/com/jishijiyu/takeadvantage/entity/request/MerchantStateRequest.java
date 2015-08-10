package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 商家审核状态
 * 
 * @author sunaoyang
 * 
 */
public class MerchantStateRequest {
	public String c = Constant.MERCHANT_STATE;
	public Pramater p;

	public MerchantStateRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
	}
}
