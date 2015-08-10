package com.jishijiyu.takeadvantage.entity.result;

public class AddErnieResult {
	public String c;
	public Pramater p;

	public class Pramater {
		public Enroll enroll;
		public String type;
		public boolean isTrue;
	}

	public class Enroll {
		public long createTime;
		public String ernieId;
		public String id;
		public long joinBegintime;
		public String prizeId;
		public String userId;
	}
}
