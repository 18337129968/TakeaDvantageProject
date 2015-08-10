package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class MyFriendRequest {
	public String c = Constant.MY_FRIEND_LIST_CODE;
	public Pramater p;

	public MyFriendRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String pageNum;
		public String pageSize;
		public String tokenId;
		public String userId;
	}
}
