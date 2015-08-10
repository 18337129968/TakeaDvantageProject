package com.jishijiyu.takeadvantage.activity.ernieonermb;

import com.jishijiyu.takeadvantage.utils.Constant;

public class TuoGuanRequest {
	public String c = Constant.ERNIE_TUO_GUAN;
	public Parameter p;
	
	public TuoGuanRequest() {
		// TODO Auto-generated constructor stub
		this.p = new Parameter();
	}

	public class Parameter {
		public String isLock;
		public String userId;
		public String tokenId;	
		public String roomId;	
		
	}
}
