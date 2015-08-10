package com.jishijiyu.takeadvantage.entity.result;

public class ConfirmReceivingResult {
	public ConfirmData getP() {
		return p;
	}

	public void setP(ConfirmData p) {
		this.p = p;
	}

	private String c;
	private ConfirmData p;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public static class ConfirmData {
		private boolean isSucce;
		private boolean isTrue;

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
