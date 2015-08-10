package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RoomListRequest {
	public String c = Constant.ROOM_LIST_REQUEST_CODE;
	public Parameter p;

	public RoomListRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String page;
		public String pageSize;
	}
}
