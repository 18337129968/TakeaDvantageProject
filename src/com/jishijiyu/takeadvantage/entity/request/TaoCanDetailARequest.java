package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class TaoCanDetailARequest {
	public String c = Constant.TAOCANDETAIL;
	public Parameter p;

	public TaoCanDetailARequest() {
		p = new Parameter();
	}

	public class Parameter {
		public String tokenId;
		public String packageId;
		public String userId;
	}
}
