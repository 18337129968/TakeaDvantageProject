package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 一元拍邀请好友返回数据
 * 
 * @author zhaobin
 * 
 */
public class InvitationFriendsResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public int pageSize;
		public List<InvitationFriendsVOList> invitationFriendsVOList;
	}

	public class InvitationFriendsVOList {
		public String friendsId;
		public String headImgUrl;
		public String nickname;
		public String userId;
	}
}
