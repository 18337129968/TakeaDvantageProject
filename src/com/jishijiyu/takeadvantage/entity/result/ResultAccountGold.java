package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

import com.jishijiyu.takeadvantage.entity.result.ResultAccountIntegral.ScoreStatisticsRecord;

/**
 * 账户金币返回
 * 
 * @author baohan
 * 
 */
public class ResultAccountGold {

	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public List<GoldStatisticsRecordList> goldStatisticsRecordList;
		public GoldStatistics goldStatistics;

		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

		public List<GoldStatisticsRecordList> getGoldStatisticsRecordList() {
			return goldStatisticsRecordList;
		}

		public void setGoldStatisticsRecordList(
				List<GoldStatisticsRecordList> goldStatisticsRecordList) {
			this.goldStatisticsRecordList = goldStatisticsRecordList;
		}

		public GoldStatistics getGoldStatistics() {
			return goldStatistics;
		}

		public void setGoldStatistics(GoldStatistics goldStatistics) {
			this.goldStatistics = goldStatistics;
		}

	}

	public class GoldStatistics {
		public int advanceDelGold;
		public int buyDelGold;
		public int dollerDelGold;
		public int exchangeAddGold;
		public int exchangeDelGold;
		public int id;
		public int lookPosterAddGold;
		public int taskAddGold;
		public int userId;
	}

	public class GoldStatisticsRecordList {
		public String createTime;
		public String id;
		public String num;
		public String type;
		public String userId;

	}
}
