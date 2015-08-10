package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class PayMoneyRequest {
	public String c = Constant.NEWROOM;
	public Pramater p;

	public PayMoneyRequest() {
		// TODO Auto-generated constructor stub
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String roomIds;
		public String tokenId;
	}

}
