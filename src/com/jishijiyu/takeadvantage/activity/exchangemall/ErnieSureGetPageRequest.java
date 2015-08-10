package com.jishijiyu.takeadvantage.activity.exchangemall;

import com.jishijiyu.takeadvantage.utils.Constant;

public class ErnieSureGetPageRequest {
	public String c = Constant.JOIN_YYERNIE_GET_PRIZE;
	public Pramater p;

	public ErnieSureGetPageRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String awardId;
		public String userId;
		public String tokenId;
	}
}
