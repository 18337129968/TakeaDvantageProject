package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 奖品查找房间请求
 * 
 * @author shifeiyu
 * 
 */
public class PrizeSearchRoomRequest {
	public String c = Constant.PRIZE_SEARCH_ROOM_REQUEST_CODE;
	public Parameter p;

	public PrizeSearchRoomRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String prizeName;
		public String tokenId;
		public String userId;
	}
}
