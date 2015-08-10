package com.jishijiyu.takeadvantage.entity.result;

/**
 * 意见反馈返回
 * 
 * @author baohan
 * 
 */
public class FeedBackResult {
	public int c;
	public Parameter p;

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public Parameter getP() {
		return p;
	}

	public void setP(Parameter p) {
		this.p = p;
	}

	public class Parameter {
		public boolean isSucce;

		public boolean isSucce() {
			return isSucce;
		}

		public void setSucce(boolean isSucce) {
			this.isSucce = isSucce;
		}
		
	}

}
