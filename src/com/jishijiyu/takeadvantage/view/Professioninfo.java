package com.jishijiyu.takeadvantage.view;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Professioninfo implements Serializable {

	private String id;
	private String profession_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProfession_name() {
		return profession_name;
	}

	public void setProfession_name(String profession_name) {
		this.profession_name = profession_name;
	}

	@Override
	public String toString() {
		return "Professioninfo [id=" + id + ", profession_name=" + profession_name + "]";
	}

}
