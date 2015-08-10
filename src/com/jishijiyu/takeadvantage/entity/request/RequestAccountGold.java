package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 账户金币
 * 
 * @author baohan
 * 
 */
public class RequestAccountGold {
	public String c = Constant.ACCOUNGOLD;
	public Pramater p;

	public RequestAccountGold() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
        public String page;
        public String pageSize;
	}

}
