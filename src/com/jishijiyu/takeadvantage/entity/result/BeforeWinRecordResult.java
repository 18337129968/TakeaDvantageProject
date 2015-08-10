package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 查询往期中奖记录返回
 * 
 * @author shifeiyu
 * 
 */
public class BeforeWinRecordResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public List<HistoryWinningList> historyWinningList;
		public boolean isSucce;
		public boolean isTrue;
	}

	public class HistoryWinningList {
		public long beginTimep;
		public int perios;
		public int roomId;
		public List<mList> list;
	}

	public class mList {
		public int grade;
		public String headImgUrl;
		public int id;
		public String nickname;
		public String province;
		public long winTime;

	}
}
