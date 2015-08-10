package com.jishijiyu.takeadvantage.entity.request;

public class PrizeDetailRequest {
	private String c;
	private Parameter p;

	public PrizeDetailRequest() {
		// TODO Auto-generated constructor stub
		p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String packageId;
		public String userId;

	}

}
