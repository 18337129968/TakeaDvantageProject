package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;
/**
 * 用户注册请求
 * @author shifeiyu
 * @version 2015年5月29日11:08:44
 *
 */
public class RegisterRequest {
	public String c = Constant.USER_REGISTER_CODE;
	public Parameter p;
	public RegisterRequest(){
		this.p = new Parameter();
	}
	public class Parameter{
		public String code;
		public String mobile;
		public String pwd;
	}
	
}
