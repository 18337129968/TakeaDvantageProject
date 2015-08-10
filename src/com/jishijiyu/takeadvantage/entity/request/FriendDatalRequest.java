package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 好友详情返回数据
 * 
 * @author zhaobin
 */
public class FriendDatalRequest {
	public String c = Constant.FRIEND_DATAL_CODE;
	public Pramater p;

	public FriendDatalRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String friendId;
		public String userId;
		public String tokenId;
	}
}
