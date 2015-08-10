package com.jishijiyu.takeadvantage.activity.ernieonermb;

import com.jishijiyu.takeadvantage.utils.Constant;

public class JoinYYErnieRequest {
	public String c = Constant.JOIN_YYERNIE;
	public Parameter p;
	
	public JoinYYErnieRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String userId;
		public String tokenId;
		public String roomId;
		
	}
}
