package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class AcceptOrderRequest {
	public String c = Constant.ACCEPT_ORDER_CODE;
	public Pramater p;

	public AcceptOrderRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String goodsId;
		public String tokenId;
		public String userId;
		public String province;
		public String city;
		public String area;
		public String detailedAddress;
		public String name;
		public String telephone;
		public String num;

	}
}
