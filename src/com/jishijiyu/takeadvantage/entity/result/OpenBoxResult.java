package com.jishijiyu.takeadvantage.entity.result;

/**
 * 开宝箱返回
 * 
 * @author shifeiyu
 * 
 */

public class OpenBoxResult {
	public String c;
	public Parameter p;

	public class Parameter {
		public boolean isSucce;
		public boolean isTrue;
		public int lock;
		public int state;
		public int type;
	}
}
