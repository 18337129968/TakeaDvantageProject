package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class MyInfoMationRequest {
	public String c = Constant.MYINFOMATION_REQUEST_CODE;
	public Parameter p;
	public MyInfoMationRequest(){
		this.p = new Parameter();
	}
	public class Parameter{
		public String userId;
		public String tokenId;
	}
}
