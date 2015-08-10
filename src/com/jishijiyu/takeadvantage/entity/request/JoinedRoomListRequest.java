package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class JoinedRoomListRequest {
	public String c = Constant.JOINED_ROOM_LIST_REQUEST_CODE;
	public Parameter p;

	public JoinedRoomListRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String userId;
		public String page;
		public String pageSize;
	}
}
