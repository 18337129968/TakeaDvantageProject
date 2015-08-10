package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class WinningRecordGoodsRequest {
	public String c = Constant.WINNING_RECORD_GOODS_DETAILS;
	public Pramater p;

	public WinningRecordGoodsRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String winningId;
		public String tokenId;
	}
}
