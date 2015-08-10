package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 添加好友—搜索
 * 
 * @author zhaobin
 */
public class AddFriendSearchRequest {
	public String c = Constant.ADDFRIEND_SEARCH_CODE;
	public Pramater p;

	public AddFriendSearchRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String keyWord;
		public String userId;
		public String tokenId;
		public String pageNum;
		public String pageSize;
	}
}
