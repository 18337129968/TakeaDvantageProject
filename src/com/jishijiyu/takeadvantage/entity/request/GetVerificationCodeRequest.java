package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;
/**
 * 获取验证码请求
 * @author shifeiyu
 * @version 2015年6月3日08:43:15
 */
public class GetVerificationCodeRequest {
	public String c = Constant.GET_VERIFICATION_CODE;
	public Parameter p ;
	public  GetVerificationCodeRequest(){
		this.p = new Parameter();
	}
	public class Parameter{
		public String  mobile;
		public String type ;
	}
}
