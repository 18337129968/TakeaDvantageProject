package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RequestNewsDes {
	public String c=Constant.REQUEST_NEWS_DES_CODE;
	public Pramater p;
	
	public RequestNewsDes(){
		p=new Pramater();
	}
	
	public class Pramater{
		public String userId;
		public String tokenId;
		public String contentId;
	}
}
