package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 删除好友
 * 
 * @author zhaobin
 */
public class DelFriendRequest {
	public String c = Constant.delFriend_CODE;
	public Pramater p;

	public DelFriendRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
		public String friendId;
		public String ownerId;
	}
}
