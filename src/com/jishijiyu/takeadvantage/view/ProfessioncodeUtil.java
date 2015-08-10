package com.jishijiyu.takeadvantage.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

/**
 * 
 * 城市代码
 * 
 * @author zd industry profession
 * 
 */
public class ProfessioncodeUtil {

	private ArrayList<String> industry_list = new ArrayList<String>();
	private ArrayList<String> profession_list = new ArrayList<String>();
//	private ArrayList<String> couny_list = new ArrayList<String>();
	public ArrayList<String> industry_list_code = new ArrayList<String>();
	public ArrayList<String> profession_list_code = new ArrayList<String>();
//	public ArrayList<String> couny_list_code = new ArrayList<String>();
	/** 单例 */
	public static ProfessioncodeUtil model;
	private ProfessioncodeUtil() {
	}

	public ArrayList<String> getIndustry_list_code() {
		return industry_list_code;
	}

	public ArrayList<String> getProfession_list_code() {
		return profession_list_code;
	}

	public void setProfession_list_code(ArrayList<String> profession_list_code) {
		this.profession_list_code = profession_list_code;
	}

//	public ArrayList<String> getCouny_list_code() {
//		return couny_list_code;
//	}
//
//	public void setCouny_list_code(ArrayList<String> couny_list_code) {
//		this.couny_list_code = couny_list_code;
//	}

	public void setIndustry_list_code(ArrayList<String> industry_list_code) {

		this.industry_list_code = industry_list_code;
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static ProfessioncodeUtil getSingleton() {
		if (null == model) {
			model = new ProfessioncodeUtil();
		}
		return model;
	}

	public ArrayList<String> getIndustry(List<Professioninfo> provice) {
		if (industry_list_code.size() > 0) {
			industry_list_code.clear();
		}
		if (industry_list.size() > 0) {
			industry_list.clear();
		}
		for (int i = 0; i < provice.size(); i++) {
			industry_list.add(provice.get(i).getProfession_name());
			industry_list_code.add(provice.get(i).getId());
		}
		return industry_list;

	}

	public ArrayList<String> getProfession(
			HashMap<String, List<Professioninfo>> professionHashMap, String provicecode) {
		if (profession_list_code.size() > 0) {
			profession_list_code.clear();
		}
		if (profession_list.size() > 0) {
			profession_list.clear();
		}
		List<Professioninfo> profession = new ArrayList<Professioninfo>();
		profession = professionHashMap.get(provicecode);
		System.out.println("profession--->" + profession.toString());
		for (int i = 0; i < profession.size(); i++) {
			profession_list.add(profession.get(i).getProfession_name());
			profession_list_code.add(profession.get(i).getId());
		}
		return profession_list;

	}

//	public ArrayList<String> getCouny(
//			HashMap<String, List<Professioninfo>> professionHashMap, String professioncode) {
//		System.out.println("professioncode" + professioncode);
//		List<Professioninfo> couny = null;
//		if (couny_list_code.size() > 0) {
//			couny_list_code.clear();
//
//		}
//		if (couny_list.size() > 0) {
//			couny_list.clear();
//		}
//		couny = new ArrayList<Professioninfo>();
//
//		couny = professionHashMap.get(professioncode);
//		System.out.println("couny--->" + couny.toString());
//		for (int i = 0; i < couny.size(); i++) {
//			couny_list.add(couny.get(i).getProfession_name());
//			couny_list_code.add(couny.get(i).getId());
//		}
//		return couny_list;
//
//	}
}
