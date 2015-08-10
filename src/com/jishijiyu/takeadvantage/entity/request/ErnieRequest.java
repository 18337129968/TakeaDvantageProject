package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 摇奖
 * 
 * @author zhaobin
 */
public class ErnieRequest {
	public String c = Constant.ERNIE_CODE;
	public Pramater p;

	public ErnieRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
	}
}
