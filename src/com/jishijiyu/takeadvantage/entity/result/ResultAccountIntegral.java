package com.jishijiyu.takeadvantage.entity.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 账户拍币返回
 * 
 * @author baohan
 * 
 */
public class ResultAccountIntegral {

	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public List<ScoreStatisticsRecord> scoreStatisticsRecord;
		public ScoreStatistics scoreStatistics;
	}

	public class ScoreStatisticsRecord {
		public long createTime;
		public String id;
		public String type;
		public String userId;
		public String num;
	}

	public class ScoreStatistics {
		public int exchangeAddScore;
		public int exchangeDelScore;
		public int firstDelScore;
		public int id;
		public int inviteUserAddScore;
		public int lookPosterAddScore;
		public int lotteryDelScore;
		public int signAddScore;
		public int systemAddScore;
		public int taskAddScore;
		public int userId;
	}
}
