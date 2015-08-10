package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 
 * 报名参加摇奖（5等奖信息）
 * 
 * @author zhaobin
 */
public class CheckPriceResult {
	public String c = Constant.CHECK_PRICE_CODE;
	public Pramater p;

	public class Pramater {

		public List<PrizeList> prizeList;
		public String type;
		public boolean isTrue;
	}

	public class PrizeList {
		public String id;
		public String ernieId;
		public String name;
		public String prizeImg;
		public String mallId;
	}
}
