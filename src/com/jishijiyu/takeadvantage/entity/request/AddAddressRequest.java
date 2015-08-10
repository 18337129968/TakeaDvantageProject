package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

public class AddAddressRequest {
	public String c = Constant.REQUEST_ADD_ADDRESS_CODE;
	public Pramater p;

	public AddAddressRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
		public String province;
		public String city;
		public String area;
		public String detailedAddress;
		public String name;
		public String telephone;
		public String postalCode;
		public String type;

	}
}
