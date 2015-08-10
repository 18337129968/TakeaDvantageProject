package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class BillSingleDelRequest {
	public String c = Constant.BILL_SINGLE_DELET_CODE;
	public Parameter p;
	
	
	public BillSingleDelRequest() {
		p = new Parameter();
	}


	public class Parameter{
		public String inventoryId;
		public String tokenId;
		public String userId;
	}
}
