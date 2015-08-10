package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RequestNewsDelete {
	public String c=Constant.DELETE_NEWS_CODE;
	public Pramater p;
	
	public RequestNewsDelete(){
		p=new Pramater();
	}
	
	public class Pramater{
		public String userId;
		public String tokenId;
		public String contentId;
	}
}
