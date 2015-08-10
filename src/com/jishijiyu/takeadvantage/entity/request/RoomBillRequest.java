package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RoomBillRequest {
	public String c = Constant.ROOM_BILL_REQUEST_CODE;
	public Parameter p;
	
	public RoomBillRequest() {
		this.p = new Parameter();
	}

	public class Parameter{
		public String userId;
		public String tokenId;
	}
}
