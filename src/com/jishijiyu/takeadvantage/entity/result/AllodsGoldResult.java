package com.jishijiyu.takeadvantage.entity.result;

/**
 * 提交任务返回
 * 
 * @author baohan
 * 
 */
public class AllodsGoldResult {
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
		public boolean isTrue;

		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

		public boolean isSucce() {
			return isSucce;
		}

		public void setSucce(boolean isSucce) {
			this.isSucce = isSucce;
		}

	}

}
