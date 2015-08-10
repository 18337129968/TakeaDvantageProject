package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 查询报名者信息返回
 * 
 * @author shifeiyu
 * 
 */
public class CheckRegisterInfoResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public List<EnrollList> enrollList;
	}

	public class EnrollList {
		public long enrollTime;
		public String headImgUrl;
		public String province;
		public int userId;
		public String userName;
	}
}
