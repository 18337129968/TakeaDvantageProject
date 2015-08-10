package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 添加商家号—搜索
 * 
 * @author zhaobin
 */
public class AddMerchantSearchRequest {
	public String c = Constant.ADDMERCHANT_SEARCH_CODE;
	public Pramater p;

	public AddMerchantSearchRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String keyWord;
		public String userId;
		public String tokenId;
		public String pageNum;
		public String pageSize;
	}
}
