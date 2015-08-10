package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 投放前广告返回
 * 
 * @author shifeiyu
 * 
 */
public class AdvertsingBeforeResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public List<PostAgoList> postAgoList;
	}

	public class PostAgoList {
		public long commitTime;
		public int companyId;
		public long createTime;
		public String heartMemery;
		public int id;
		public String posterImgUrl;
		public String posterSign;
		public int posterState;
		public String posterTitle;
		public int reviewState;
	}
}
