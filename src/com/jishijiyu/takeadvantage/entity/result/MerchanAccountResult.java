package com.jishijiyu.takeadvantage.entity.result;

public class MerchanAccountResult {
	public String c;
	public Pramater p;

	public class Pramater {
		public Account account;
		public boolean isTrue;
	}

	public class Account {
		public String balance;
		public String consume;
		public String fetchout;
		public String supply;

	}

}
