package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;

/**
 * 商家好友详情
 * 
 * @author zhaobin
 */
public class MerchantDatalResult {
	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public Company company;
	}

	public class Company implements Serializable {
		public String userId;
		public String logoImgUrl;
		public String companyDescribe;
		public String companyName;
	}
}
