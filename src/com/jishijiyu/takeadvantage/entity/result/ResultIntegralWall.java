package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 拍币墙列表返回
 * 
 * @author baohan
 * 
 */
public class ResultIntegralWall {

	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public List<AppList> appList;
	}

	public class AppList {
		public String appApk;
		public String appBanner;
		public String appDescribe;
		public String appIco;
		public String appMaker;
		public String appPrice;
		public String appScore;
		public String appSize;
		public String appState;
		public String appTitle;
		public String appType;
		public String createTime;
		public String endTime;
		public String id;
		public String percent;

	}
}
