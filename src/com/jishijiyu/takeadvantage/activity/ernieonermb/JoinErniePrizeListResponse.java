package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.ArrayList;

import com.jishijiyu.takeadvantage.activity.ernieonermb.GetPrizeDataResponse.Parameter;

public class JoinErniePrizeListResponse {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public long nowDate;
		ArrayList<PrizeGradeInfo> firstPrizeList;
		public dollerRoom dollerRoom;
	}

	public class PrizeGradeInfo {
		public int companyId;
		public long createTime;
		public int freight;
		public int grade;
		public int id;
		public String img;
		public String name;
		public String explain;
		public int[] num;
		public int places;
		public int price;
		public int winningCount;
	}

	public class dollerRoom {
		public int createPerson;
		public long beginTime;
		public long createTime;
		public long endTime;
		public int id;
		public int lotteryState;
		public int mealId;
		public int mealPerios;
		public int roomIssue;
		public int startErnieType;
		public int state;
		public int type;
		public int userId;
		public int joinNumber;

	}
}
