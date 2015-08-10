package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 查询报名者信息请求
 * 
 * @author shifeiyu
 * 
 */
public class CheckRegisterInfoRequest {
	public String c = Constant.CHECK_REGISTER_INFO_REQUEST;
	public Parameter p;

	public CheckRegisterInfoRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String userId;
		public String page;
		public String pageSize;
		public String roomId;
	}
}
