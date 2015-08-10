package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 房间号查找房间请求信息
 * 
 * @author shifeiyu
 * 
 */
public class RoomNumSearchRoomRequest {
	public String c = Constant.ROOM_NUM_SEARCH_ROOM_REQUEST_CODE;
	public Parameter p;

	public RoomNumSearchRoomRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String roomNum;
	}

}
