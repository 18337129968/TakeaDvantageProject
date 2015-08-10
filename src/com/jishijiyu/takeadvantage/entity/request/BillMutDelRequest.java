package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class BillMutDelRequest {
	
	public String c = Constant.BILL_MUT_DELET_CODE;
	public Parameter p;
	
	public BillMutDelRequest() {
		p = new Parameter();
	}

	public class Parameter{
		public String inventoryIds;
		public String tokenId;
		public String userId;
	}
}
