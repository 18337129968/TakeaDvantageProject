package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 往期中奖记录请求
 * 
 * @author shifeiyu
 * 
 */
public class BeforeWinRecordRequest {
	public String c = Constant.BEFORE_WIN_RECORD_REQUEST;
	public Parameter p;

	public BeforeWinRecordRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String packageId;
		public String page;
		public String pageSize;
		public String prizeId;
		public String userId;
		public String tokenId;
	}
}
