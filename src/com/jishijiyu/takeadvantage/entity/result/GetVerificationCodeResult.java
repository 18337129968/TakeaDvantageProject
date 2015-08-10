package com.jishijiyu.takeadvantage.entity.result;
/**
 * 获取验证码返回
 * @author win7
 * @version 2015年6月3日08:44:01
 */
public class GetVerificationCodeResult {
	public String c;
	public Parameter p;
	public class Parameter{
		public boolean isSucce;
		public int type;
		public int code;
	}
}
