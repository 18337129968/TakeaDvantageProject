package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class ForgotPasswordRequest {
	public String c = Constant.FORGOTPASSWORD_REQUEST_CODE;
	public Parameter p;
	public ForgotPasswordRequest(){
		this.p = new Parameter();
	}
	public class Parameter{
		public String code;
		public String mobile;
		public String newPwd;
	}
}
