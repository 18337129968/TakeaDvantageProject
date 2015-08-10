package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 拍币墙列表
 * 
 * @author baohan
 * 
 */
public class RequestIntegralWall {
	public String c = Constant.INTEGRALWALLLIST;
	public Pramater p;

	public RequestIntegralWall() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;

	}

}
