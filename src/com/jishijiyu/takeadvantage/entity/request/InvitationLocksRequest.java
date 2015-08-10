package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 
 * 邀请人兑换锁请求数据
 * 
 * @author zhaobin
 */
public class InvitationLocksRequest {
	public String c = Constant.INVITATION_LOCKS_CODE;
	public Pramater p;

	public InvitationLocksRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String tokenId;
		public String userId;
		public String num;

	}
}
