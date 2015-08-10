package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

public class TaoCanDetailResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isTrue;
		public List<prizeCompanyList> prizeCompanyList;
	}

	public class prizeCompanyList {
		public String companyId;
		public String companyName;
		public long createTime;
		public String freight;
		public String grade;
		public String id;
		public String imgUrl;
		public String name;
		public String places;
		public String price;
		public String prizeExplain;
	}
}
