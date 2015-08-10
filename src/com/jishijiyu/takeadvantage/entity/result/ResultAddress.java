package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;



/**
 * 收货地址列表返回类
 * @author win7
 *
 */
public class ResultAddress {
	public String c;
	public Pramater p;

	public class Pramater {
		public List<Address> addrList;
		public boolean isTrue;
	}
}