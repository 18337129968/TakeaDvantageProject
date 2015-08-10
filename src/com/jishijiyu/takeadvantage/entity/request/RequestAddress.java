package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;
/**
 * 收货地址列表请求类
 * @author win7
 *
 */
public class RequestAddress {
	public String c = Constant.REQUEST_ADDRESS_CODE;
	public Pramater p;

	public RequestAddress() {
		p = new Pramater();
	}

	public class Pramater {
		public String 	userId;
		public String tokenId;
	
	}
}
