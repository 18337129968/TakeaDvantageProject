package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class RequestOneRMBShinePrizeList {
	public String c = Constant.ONE_PRIZE_LIST;
	public Pramemter p;

	public RequestOneRMBShinePrizeList() {
		p = new Pramemter();
	}

	public class Pramemter {
		public String pageNum;
		public String pageSize;
		public String tokenId;
		public String userId;
		
	}
}
