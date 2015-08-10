package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class SignInRequestInfo {
	public String c;
	public Pramater p;
	
	public SignInRequestInfo(){
		c=Constant.SIGN_IN_INFO;
		p=new Pramater();
	}
	
	public class Pramater{
		public String userId;
		public String tokenId;
	}
}
