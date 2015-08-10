package com.jishijiyu.takeadvantage.entity.result;

public class SignInResultInfo {
	public String c;
	public Pramater p;
	
	public class Pramater{
		public boolean canSign;
		public SignIn sign;
		public boolean isTrue;
	}
	public class SignIn{
		public int days;
		public int id;
		public int lastDay;
		public int userId;
	}
}
