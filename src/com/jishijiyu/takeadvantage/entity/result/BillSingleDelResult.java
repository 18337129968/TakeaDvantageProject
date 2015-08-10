package com.jishijiyu.takeadvantage.entity.result;

import com.jishijiyu.takeadvantage.utils.Constant;

public class BillSingleDelResult {
	public String c = Constant.BILL_SINGLE_DELET_CODE;
	public Parameter p;
	
	public class Parameter{
		public boolean isSucce;
		public boolean isTrue;
	}
}
