package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class ChangeMyInfomationRequest {
	
	public String c = Constant.CHANGE_MY_INFOMATION_REQUEST_CODE;
	public Parameter p ;
	public ChangeMyInfomationRequest(){
		this.p = new Parameter();
	}
	public class Parameter{
		public String area;
		public String birthday;
		public String city;
		public String headImg;
		public String job;
		public String nickname;
		public String province;
		public String sex;
		public String userId;
		public String tokenId;
	}
}
