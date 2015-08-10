package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RequestNews {
	public String c=Constant.REQUEST_NEWS_CODE;
	public Pramater p;
	
	public RequestNews(){
		p=new Pramater();
	}
	
	public class Pramater{
		public String userId;
		public String tokenId;
	}
}
