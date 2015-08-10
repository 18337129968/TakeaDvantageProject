package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 使用号码锁
 * 
 * @author zhaobin
 */
public class LockCodeRequest {
	public String c = Constant.LOCK_CODE;
	public Pramater p;

	public LockCodeRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String loc;
		public String userId;
		public String tokenId;
	}
}
