package com.jishijiyu.takeadvantage.entity.result;

public class DefultAddressResult {
	public String c;
	public Pramater p;

	public class Pramater {
		public receiveAddress receiveAddress;
		public boolean isTrue;
	}
	public class receiveAddress{
		public String id;
		public String tokenId;
		
		public String userId;
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
