package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class AcceptPrizeRequest {
	public String c = Constant.ACCEPTPRIZE_CODE;
	public Pramater p;

	public AcceptPrizeRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String id;
		public String userId;
		public String tokenId;
		public String province;
		public String city;
		public String area;
		public String detailedAddress;
		public String name;
		public String telephone;
		public String num;

	}
}
