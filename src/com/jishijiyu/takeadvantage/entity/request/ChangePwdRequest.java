package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class ChangePwdRequest {
	public String c = Constant.CHANGE_PWD_REQUEST_CODE;
	public Parameter p;
	
	public ChangePwdRequest(){
		this.p = new Parameter();
	}
	
	public class Parameter{
		public String newPwd;
		public String oldPwd;
		public String userId;
		public String tokenId;
	}
}
