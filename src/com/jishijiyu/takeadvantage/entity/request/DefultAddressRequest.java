package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class DefultAddressRequest {
	public String c = Constant.REQUEST_DEFULT_ADDRESS_CODE;
	public Pramater p;

	public DefultAddressRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;

	}
}
