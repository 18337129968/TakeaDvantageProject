package com.jishijiyu.takeadvantage.entity.request;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 删除收货地址请求类
 * 
 * @author 张翠玲
 * 
 */
public class DeleteAddressRequest {
	public String c = Constant.REQUEST_DELETE_ADDRESS_CODE;
	public Pramater p;

	public DeleteAddressRequest() {
		p = new Pramater();
	}

	public class Pramater {
		public String userId;
		public String tokenId;
		public String id;

	}
}
