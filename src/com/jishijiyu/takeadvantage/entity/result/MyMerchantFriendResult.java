package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

/**
 * 我的商家好友列表
 * 
 * @author zhaobin
 */
public class MyMerchantFriendResult implements Serializable {
	public int c;
	public Parameter p;

	public class Parameter implements Serializable {
		public boolean isTrue;
		public List<MyComList> myComList;
	}

	public class MyComList implements Serializable {
		public String companyName;
		public String logoImgUrl;
		public String userId;
	}
}
