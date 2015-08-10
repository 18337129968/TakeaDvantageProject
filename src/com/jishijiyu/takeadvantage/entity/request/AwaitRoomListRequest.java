package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 待揭晓房间列表请求
 * 
 * @author shifeiyu
 * 
 */
public class AwaitRoomListRequest {
	public String c = Constant.AWAIT_ROOM_LIST_REQUEST;
	public Parameter p;

	public AwaitRoomListRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String page;
		public String userId;
		public String pageSize;
	}
}
