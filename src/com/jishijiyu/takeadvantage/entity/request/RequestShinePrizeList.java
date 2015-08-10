package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RequestShinePrizeList {
	public String c = Constant.INTEGRAL_PRIZE_LIST;
	public Pramemter p;

	public RequestShinePrizeList() {
		p = new Pramemter();
	}

	public class Pramemter {
		public String pageNum;
		public String pageSize;
		public String tokenId;
		public String userId;
		
	}
}
