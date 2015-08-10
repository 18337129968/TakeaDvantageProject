package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RequestUpdataMyPrize {
	public String c=Constant.UPDATA_MY_PRIZE;
	public Parameter p;
	
	public RequestUpdataMyPrize(){
		p=new Parameter();
	}
	
	public class Parameter {
		public String awardContent;
		public String awardImg;
		public String title;
		public String userId;
		public String tokenId;
	}
}
