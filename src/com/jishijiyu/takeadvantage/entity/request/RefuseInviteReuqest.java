package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RefuseInviteReuqest {
	public String c = Constant.REFUSEINCITE;
	public Parameter p;

	public RefuseInviteReuqest() {
		// TODO Auto-generated constructor stub
		p = new Parameter();
	}

	public class Parameter {
		public String roomId;
		public String tokenId;
		public String userId;
	}
}
