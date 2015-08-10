package com.jishijiyu.takeadvantage.entity.result;

/**
 * 广告详情返回
 * 
 * @author baohan
 * 
 */
public class AdvertisingDetailsResult {
	public int c;
	public Parameter p;

	public class Parameter {
		public poster poster;
		public boolean isTrue;
	}

	public class poster {
		public String answer;
		public int answerNum;
		public String companyDescribe;
		public int id;
		public int lookNum;
		public String posterDescribe;
		public String posterImgUrl;
		public int posterPrice;
		public String posterTitle;
		public String questionDescribe;
		public String questionOption;
		public String questionType;
		public boolean isTrue;
		
	}
}
