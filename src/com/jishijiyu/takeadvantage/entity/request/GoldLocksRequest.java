package com.jishijiyu.takeadvantage.entity.request;

import java.io.Serializable;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 
 * 金币兑换锁请求数据
 * 
 * @author zhaobin
 */
public class GoldLocksRequest implements Serializable {
	public String c = Constant.GOLD_LOCKS_CODE;
	public Pramater p;

	public GoldLocksRequest() {
		p = new Pramater();
	}

	public class Pramater implements Serializable {
		public String tokenId;
		public String userId;
		public String num;
		public String lock;
	}
}
