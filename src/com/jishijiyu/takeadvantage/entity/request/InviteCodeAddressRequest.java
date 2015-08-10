package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 邀请好友二维码
 * 
 * @author sunaoyang
 */
public class InviteCodeAddressRequest {
	public String c = Constant.INVITE_CODE_ADDRESS;
	public Pramater p;

	public InviteCodeAddressRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
	}
}
