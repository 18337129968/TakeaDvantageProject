package com.jishijiyu.takeadvantage.activity.exchangemall;

import java.util.ArrayList;

public class ErnieGetPrizeInfoResponse {
	public String c;
	public Parameter p;

	public class Parameter {
		public ArrayList<PrizeInfo> prizeInfoList;
		public boolean isTrue;
		public String mealRecordId;
	}

	public class PrizeInfo {
		public String beginTimep;
		public String grade;
		public String headImgUrl;
		public String nickname;
		public String perios;
		public String province;
		public String winTime;
		public String countNumber;
		public String endTime;
		public String number;
		public String prizeName;
		public String startTime;
		public String roomId;
		public String roomState;
		public String nowTime;
		public String userState;
		public String isShare;
		
	}
}

