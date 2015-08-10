package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class SetPriceRequest {
	public String c = Constant.SET_PRICE_CODE;
	public Pramater p;

	public SetPriceRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String mobile;
		public String userId;
		public String winningRecordId;
		public String tokenId;
	}
}
