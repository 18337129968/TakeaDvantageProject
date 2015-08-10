package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 账户拍币
 * 
 * @author baohan
 * 
 */
public class RequestAccountIntegral {
	public String c = Constant.ACCOUNTINTEGRAL;
	public Pramater p;

	public RequestAccountIntegral() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
        public String page;
        public String pageSize;
	}

}
