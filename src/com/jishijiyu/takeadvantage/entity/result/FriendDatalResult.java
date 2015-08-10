package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 添加好友—搜索返回数据
 * 
 * @author zhaobin
 */
public class FriendDatalResult implements Serializable {
	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public FriendDetails friendDetails;
	}

	public class FriendDetails implements Serializable {
		public String exchange;
		public String headImgUrl;
		public String nickname;
		public String score;
		public String task;
		public String userId;
		public String wins;
	}
}
