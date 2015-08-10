package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

/**
 * 拍币墙返回数据
 * 
 * @author zhaobin
 */
public class IntegralWallResult implements Serializable {
	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public List<AppList> appList;
	}

	public class AppList implements Serializable {
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
