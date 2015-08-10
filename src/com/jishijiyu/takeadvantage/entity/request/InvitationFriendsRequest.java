package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 一元拍邀请好友请求
 * 
 * @author zhaobin
 * 
 */
public class InvitationFriendsRequest {
	public String c = Constant.Invitation_friends_code;
	public Parameter p;

	public InvitationFriendsRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String page;
		public String pageSize;
		public String roomId;
		public String userId;
		public String tokenId;
	}
}
