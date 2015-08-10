package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 一元拍邀请好友请求
 * 
 * @author zhaobin
 * 
 */
public class InvitationFriends2Request {
	public String c = Constant.Invitation_friends2_code;
	public Parameter p;

	public InvitationFriends2Request() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String roomId;
		public String userId;
		public String tokenId;
		public String friendsId;
	}
}
