package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 兑换详情
 * 
 * @author jseven1989
 */
public class AwardingGoodsResult {
	public String c;
	public Pramater p;

	public class Pramater {
		public List<Lists> list;
	}

	public class Lists {
		public String area;
		public String city;
		public String createTime;
		public String detailedAddress;
		public String ernieId;
		public String goodsName;
		public String id;
		public String name;
		public String num;
		public String orderNum;
		public String orderTime;

		public String orderType;
		public String prizeId;
		public String province;
		public String receiveTime;
		public String score;
		public String sendTime;
		public String telephone;
		public String userId;

	}
}
