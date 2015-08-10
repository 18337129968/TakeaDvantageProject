package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 请求申请为商家
 * 
 * @author shifeiyu
 * 
 */
public class ApplyMerchantRequest {
	public String c = Constant.APPLY_MERCHANT_REQUEST_CODE;
	public Parameter p;

	public ApplyMerchantRequest() {
		this.p = new Parameter();
	}

	public class Parameter {
		public String companyAddressArea;
		public String companyAddressCity;
		public String companyAddressDetails;
		public String companyAddressProvince;
		public String companyName;
		public String companyTradeA;
		public String companyTradeB;
		public String goodness;
		public String telephone;
		public String logoImgUrl;
		public String aptitudeImgUrl;
		public String tokenId;
		public String userId;
		public String taxationImgUrl;
		public String licenseNum;
		public String place;

	}
}
