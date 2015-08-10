package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RoomTypeQueryTaoCanRequest {
	public String c = Constant.ROOMTYPEQUERYTAOCAN;
	public Pramater p;

	public RoomTypeQueryTaoCanRequest() {
		// TODO Auto-generated constructor stub
		p = new Pramater();
	}

	public class Pramater {
		public String tokenId;
		public String roomTypeId;
		public String userId;
	}
}
