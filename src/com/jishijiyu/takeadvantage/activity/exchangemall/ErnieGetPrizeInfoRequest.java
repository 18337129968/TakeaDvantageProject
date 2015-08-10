package com.jishijiyu.takeadvantage.activity.exchangemall;

import com.jishijiyu.takeadvantage.utils.Constant;

public class ErnieGetPrizeInfoRequest {
	public String c = Constant.YYERNIE_PRIZE_INFO;
	public Pramater p;

	public ErnieGetPrizeInfoRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
		public String page;
		public String pageSize;
	}
}
