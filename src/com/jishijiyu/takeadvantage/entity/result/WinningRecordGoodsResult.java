package com.jishijiyu.takeadvantage.entity.result;

public class WinningRecordGoodsResult {
	public String c;
	public Pramater p;

	public class Pramater {
		public WinningDetails WinningDetails;
        public boolean isTrue;
		public class WinningDetails {
			public String name;
			public int prizeGrade;
			public String score;
			public String prizeExplain;
			public long lotteryTime;
			public String prizeImg;
			public String winningId;
			public long receiveTime;
			public int state;
		}
	}
}
