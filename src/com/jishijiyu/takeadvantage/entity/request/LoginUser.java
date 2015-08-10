package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;
/**
 * 用户登录请求
 * @author shifeiyu
 * @version 2015年5月29日11:07:42
 *
 */
public class LoginUser {
	public String c = Constant.USER_LOGIN_CODE;
	public Parameter p ;
	public LoginUser(){
		this.p = new Parameter();
	}
	public class Parameter{
		public String pwd;
		public String mobile ;
	}
}
