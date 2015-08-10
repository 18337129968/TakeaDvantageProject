package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

public class ShowPriceResult implements Serializable {
	public String c;
	public Pramater p;

	public class Pramater implements Serializable {
		public List<PrizeList> prizeList;
		public String type;
		public boolean isTrue;
	}

	public class PrizeList implements Serializable {
		public String createTime;
		public String ernieId;
		public String freight;
		public String id;
		public String mallId;
		public String name;
		public String personCount;
		public String places;
		public String price;
		public String prizeExplain;
		public String prizeGrade;
		public String prizeImg;
		public String propagateImg;
		public String score;
		public String winNumber;
	}
}
