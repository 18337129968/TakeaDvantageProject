package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 加入清单请求
 * 
 * @author shifeiyu
 * 
 */
public class AddListRequest {
	public String c = Constant.JOIN_LIST_REQUEST_CODE;
	public Parameter p;

	public AddListRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String roomId;
	}
}
