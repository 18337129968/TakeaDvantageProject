package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;
/**
 * 输入邀请码请求
 * @author shifeiyu
 * @version 2015年6月4日19:14:14
 *
 */
public class InvitationCodeRequest {
	public String c=Constant.INVITATION_CODE_REQUEST;
	public Parameter p;
	public InvitationCodeRequest(){
		this.p = new Parameter();
	}
	public class Parameter{
		public String inviteCode;
		public String userId;
	}
}
