package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 添加好友
 * 
 * @author zhaobin
 * 
 */
public class AddFriendRequest {
	public String c = Constant.add_friend_code;
	public Pramater p;

	public AddFriendRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
		public String friendId;
		public String ownerId;
	}
}
