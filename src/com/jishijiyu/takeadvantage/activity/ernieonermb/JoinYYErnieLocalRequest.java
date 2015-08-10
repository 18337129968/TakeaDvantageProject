package com.jishijiyu.takeadvantage.activity.ernieonermb;

import com.jishijiyu.takeadvantage.utils.Constant;

public class JoinYYErnieLocalRequest {
	public String c = Constant.JOIN_YYERNIE_LOCAL;
	public Parameter p;
	
	public JoinYYErnieLocalRequest() {
		// TODO Auto-generated constructor stub
		this.p = new Parameter();
	}

	public class Parameter {
		public String lock;
		public String userId;
		public String tokenId;	
		public String roomId;	
		
	}
}
