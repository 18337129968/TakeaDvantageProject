package com.jishijiyu.takeadvantage.entity;

import java.io.Serializable;

import com.jishijiyu.takeadvantage.activity.SlideView;

@SuppressWarnings("serial")
public class ReceivingAddress implements Serializable {
	private boolean isdefult;
	private boolean isChecked;
	private String zipCode;
	private int userId;
	private int id;
	private String province;
	private String city;
	private String area;
	private String detailedAddress;
	private String name;
	private String telephone;
	public SlideView slideView;
	public String getArea() {
		return area;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public void setArea(String area) {
		this.area = area;
	}

	

	public boolean isIsdefult() {
		return isdefult;
	}

	public void setIsdefult(boolean isdefult) {
		this.isdefult = isdefult;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
