package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class GoldCoinRequest {
	public String c = Constant.GOLDCOIN;
	public Parameter p;

	public GoldCoinRequest() {
		// TODO Auto-generated constructor stub
		p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String amount;
	}
}
