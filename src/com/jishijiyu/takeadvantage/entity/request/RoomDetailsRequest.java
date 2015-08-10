package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 房间详情请求
 * 
 * @author shifeiyu
 * 
 */
public class RoomDetailsRequest {
	public String c = Constant.ROOM_DETAILS_REQUEST_CODE;
	public Parameter p;

	public RoomDetailsRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String roomId;
		public String page;
		public String pageSize;
	}
}
