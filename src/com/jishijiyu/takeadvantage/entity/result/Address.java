package com.jishijiyu.takeadvantage.entity.result;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Address implements Serializable {
	public String postalCode;
	public int userId;
	public int id;
	public String province;
	public String city;
	public String area;
	public String detailedAddress;
	public String name;
	public String telephone;
	public String type;
	public boolean isChecked;
}
