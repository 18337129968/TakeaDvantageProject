package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class ExchangeGoldRequest {
	public String c=Constant.ACCOUNGGOLD;
	public ExchangeGold p;
    public ExchangeGoldRequest() {
		// TODO Auto-generated constructor stub
    	p =new ExchangeGold();
	}
	public class ExchangeGold {
		public String userId;
		public String goldNum;
		public String tokenId;
	}

}
