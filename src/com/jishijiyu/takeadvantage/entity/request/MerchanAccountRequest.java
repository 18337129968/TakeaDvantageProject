package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class MerchanAccountRequest {
	public String c = Constant.MERCHANTACCOUNT;
	public Pramater p;

	public MerchanAccountRequest() {
		// TODO Auto-generated constructor stub
		p = new Pramater();
	}

	public class Pramater {
		public String tokenId;
		public String userId;
	}
}
