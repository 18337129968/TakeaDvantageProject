package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 商家账户信息请求
 * 
 * @author shifeiyu
 * 
 */
public class MerchantAccountRequest {
	public String c = Constant.MERCHANT_ACCOUNT_REQUEST_CODE;
	public Parameter p;

	public MerchantAccountRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
	}

}
