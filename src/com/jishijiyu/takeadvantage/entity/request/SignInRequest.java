package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class SignInRequest {
	public String c=Constant.SIGN_IN;
	public Pramater p;
	
	public SignInRequest(){
		p=new Pramater();
	}
	
	public class Pramater{
		public String userId;
		public String tokenId;
	}
}
