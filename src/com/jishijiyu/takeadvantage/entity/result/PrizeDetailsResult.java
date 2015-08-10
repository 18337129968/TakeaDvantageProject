package com.jishijiyu.takeadvantage.entity.result;

/**
 * 
 * 奖品详情返回数据
 * 
 * @author zhaobin
 */
public class PrizeDetailsResult {
	public String c;

	public Parameter p;

	public class Parameter {
		public Prize Prize;
		public boolean isTrue;
	}

	public class Prize {
		public float createTime;
		public String ernieId;
		public String prizeExplain;
		public String freight;
		public String prizeImg;
		public String id;
		public String name;
		public String mallId;
		public String supportCompany;
		public String price;
	}
}
