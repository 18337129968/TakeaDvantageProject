package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 已参与房间详情请求
 * 
 * @author shifeiyu
 * 
 */
public class JoinedRoomInfoRequest {
	public String c = Constant.JOINED_ROOM_INFO_REQUEST;
	public Parameter p;

	public JoinedRoomInfoRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String roomId;
	}

}
