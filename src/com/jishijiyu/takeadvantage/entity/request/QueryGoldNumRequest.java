package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class QueryGoldNumRequest {
	public String c = Constant.USERMANAGER;
	public User p = null;

	public QueryGoldNumRequest() {
		// TODO Auto-generated constructor stub
		p = new User();
	}

	public class User {
		public String userId;
		public String tokenId;
	}
}
