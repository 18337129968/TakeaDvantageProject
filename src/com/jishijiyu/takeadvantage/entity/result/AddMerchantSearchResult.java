package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;
import java.util.List;

import com.jishijiyu.takeadvantage.utils.Constant;

/**
 * 添加商家—搜索返回数据
 * 
 * @author zhaobin
 */
public class AddMerchantSearchResult implements Serializable {
	public String c;
	public Pramater p;

	public class Pramater {
		public boolean isTrue;
		public List<ComList> comList;
	}

	public class ComList implements Serializable {
		public String userId;
		public String companyName;
		public String logoImgUrl;
	}
}
