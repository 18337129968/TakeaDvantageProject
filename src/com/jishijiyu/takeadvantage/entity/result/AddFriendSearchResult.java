package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 添加好友—搜索返回数据
 * 
 * @author zhaobin
 */
public class AddFriendSearchResult implements Serializable {
	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public List<FriendList> friendList;
	}

	public class FriendList implements Serializable {
		public String headImgUrl;
		public String id;
		public String job;
		public String nickname;
		public String province;
		public String sex;
	}
}
