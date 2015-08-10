package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

/**
 * 我的普通好友列表
 * 
 * @author zhaobin
 */
public class MyFriendResult implements Serializable {
	public int c;
	public Parameter p;

	public class Parameter implements Serializable {
		public boolean isTrue;
		public List<MyFriendsList> myFriendsList;
	}

	public class MyFriendsList implements Serializable {
		public String headImgUrl;
		public String job;
		public String nickname;
		public String province;
		public String sex;
		public String userId;
	}
}
