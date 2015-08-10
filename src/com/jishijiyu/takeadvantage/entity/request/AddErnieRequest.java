package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 报名参加摇奖
 * 
 * @author zhaobin
 */
public class AddErnieRequest {
	public String c = Constant.REQUEST_ADD_ERNIE_CODE;
	public Pramater p;

	public AddErnieRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String ernieId;
		public String prizeId;
		public String userId;
		public String tokenId;
	}
}
