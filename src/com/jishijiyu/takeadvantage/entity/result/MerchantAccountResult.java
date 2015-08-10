package com.jishijiyu.takeadvantage.entity.result;

public class MerchantAccountResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public CompanyAccount companyAccount;
		public boolean isTrue;
	}

	public class CompanyAccount {
		public int answerNum;
		public String companyName;
		public String logoImgUrl;
		public int lookNum;
		public int reviewState;
		public int companyId;
		public String companyDescribe;
	}
}
