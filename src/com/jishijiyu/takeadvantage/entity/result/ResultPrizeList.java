package com.jishijiyu.takeadvantage.entity.result;

import java.util.ArrayList;

public class ResultPrizeList {
	public String c;
	public Parameter p;

	public class Parameter {
		public ArrayList<PrizeInfo> saPointsList;
		public ArrayList<PrizeInfo> saOneList;
		public boolean isTrue;
	}

	public class PrizeInfo {
		public String awardContent;
		public String id;
		public String imgUrl;
		public String awardName;
		public String nikName;
		public Integer awardGrade;
	}

}
