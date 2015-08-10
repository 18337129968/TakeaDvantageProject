package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class NewRoomRequest {
	public String c = Constant.NEWROOMATA;
	public Parameter p;

	public NewRoomRequest() {
		p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String userId;
	}
}
