package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 投放中广告返回
 * 
 * @author shifeiyu
 * 
 */
public class AdvertsingCentreResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public List<PostJustList> postJustList;
	}

	public class PostJustList {
		public int collectNum;
		public int answerNum;
		public long commitTime;
		public int companyId;
		public long createTime;
		public int id;
		public int lookNum;
		public int percent;
		public int posterExaminePerson;
		public String posterImgUrl;
		public int posterPrice;
		public int posterState;
		public String posterTitle;
		public int reviewState;
		public long reviewTime;
		public int score;
		public long shelvesTime;

	}
}
