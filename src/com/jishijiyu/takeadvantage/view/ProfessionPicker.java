package com.jishijiyu.takeadvantage.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.FileUtil;
import com.jishijiyu.takeadvantage.view.ScrollerNumberPicker.OnSelectListener;

/**
 * 城市Picker
 * 
 * @author zd   industry profession
 * 
 */
public class ProfessionPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker industryPicker;
	private ScrollerNumberPicker professionPicker;
	//private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempIndustryIndex = -1;
	private int temProfessionIndex = -1;
	//private int tempCounyIndex = -1;
	private Context context;
	private List<Professioninfo> industry_list = new ArrayList<Professioninfo>();
	private HashMap<String, List<Professioninfo>> profession_map = new HashMap<String, List<Professioninfo>>();
	//private HashMap<String, List<Professioninfo>> couny_map = new HashMap<String, List<Professioninfo>>();
	private static ArrayList<String> industry_list_code = new ArrayList<String>();
	private static ArrayList<String> profession_list_code = new ArrayList<String>();
	//private static ArrayList<String> couny_list_code = new ArrayList<String>();

	private ProfessioncodeUtil professioncodeUtil;
	private String profession_code_string;
	private String profession_string;
	private int industryPositon = 0;

	public ProfessionPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}

	public ProfessionPicker(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}

	// 获取城市信息
	private void getaddressinfo() {
		// TODO Auto-generated method stub
		// 读取城市信息string
		JSONParser parser = new JSONParser();
		String area_str = FileUtil.readAssets(context, "profession.json");
		industry_list = parser.getJSONParserResult(area_str, "area0");
		// professioncodeUtil.setIndustry_list_code(parser.industry_list_code);
		profession_map = parser.getJSONParserResultArray(area_str, "area1");
		// System.out.println("profession_mapsize" +
		// parser.profession_list_code.toString());
		// professioncodeUtil.setProfession_list_code(parser.profession_list_code);
//		couny_map = parser.getJSONParserResultArray(area_str, "area2");
		// professioncodeUtil.setCouny_list_code(parser.profession_list_code);
		// System.out.println("couny_mapsize" +
		// parser.profession_list_code.toString());
	}

	public static class JSONParser {
		public ArrayList<String> industry_list_code = new ArrayList<String>();
		public ArrayList<String> profession_list_code = new ArrayList<String>();

		public List<Professioninfo> getJSONParserResult(String JSONString, String key) {
			List<Professioninfo> list = new ArrayList<Professioninfo>();
			JsonObject result = new JsonParser().parse(JSONString)
					.getAsJsonObject().getAsJsonObject(key);

			Iterator iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
						.next();
				Professioninfo professioninfo = new Professioninfo();

				professioninfo.setProfession_name(entry.getValue().getAsString());
				professioninfo.setId(entry.getKey());
				industry_list_code.add(entry.getKey());
				list.add(professioninfo);
			}
			System.out.println(industry_list_code.size());
			return list;
		}

		public HashMap<String, List<Professioninfo>> getJSONParserResultArray(
				String JSONString, String key) {
			HashMap<String, List<Professioninfo>> hashMap = new HashMap<String, List<Professioninfo>>();
			JsonObject result = new JsonParser().parse(JSONString)
					.getAsJsonObject().getAsJsonObject(key);

			Iterator iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
						.next();
				List<Professioninfo> list = new ArrayList<Professioninfo>();
				JsonArray array = entry.getValue().getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					Professioninfo professioninfo = new Professioninfo();
					professioninfo.setProfession_name(array.get(i).getAsJsonArray().get(0)
							.getAsString());
					professioninfo.setId(array.get(i).getAsJsonArray().get(1)
							.getAsString());
					profession_list_code.add(array.get(i).getAsJsonArray().get(1)
							.getAsString());
					list.add(professioninfo);
				}
				hashMap.put(entry.getKey(), list);
			}
			return hashMap;
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.profession_picker, this);
		professioncodeUtil = ProfessioncodeUtil.getSingleton();
		// 获取控件引用
		industryPicker = (ScrollerNumberPicker) findViewById(R.id.industry);

		professionPicker = (ScrollerNumberPicker) findViewById(R.id.profession);
		//counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
		industryPicker.setData(professioncodeUtil.getIndustry(industry_list));
		industryPicker.setDefault(1);
		professionPicker.setData(professioncodeUtil.getProfession(profession_map, professioncodeUtil
				.getIndustry_list_code().get(1)));
		professionPicker.setDefault(1);
		//counyPicker.setData(professioncodeUtil.getCouny(couny_map, professioncodeUtil
//				.getProfession_list_code().get(1)));
	//	counyPicker.setDefault(1);
		industryPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				System.out.println("id-->" + id + "text----->" + text);
				if (text.equals("") || text == null)
					return;
				if (tempIndustryIndex != id) {
					System.out.println("endselect");
					String selectDay = professionPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					//String selectMonth = counyPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
					// 城市数组
					professionPicker.setData(professioncodeUtil.getProfession(profession_map,
							professioncodeUtil.getIndustry_list_code().get(id)));
					professionPicker.setDefault(1);
//					counyPicker.setData(professioncodeUtil.getCouny(couny_map,
//							professioncodeUtil.getProfession_list_code().get(1)));
//					counyPicker.setDefault(1);
					int lastDay = Integer.valueOf(industryPicker.getListSize());
					if (id > lastDay) {
						industryPicker.setDefault(lastDay - 1);
					}
				}
				tempIndustryIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub
			}
		});
		professionPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text.equals("") || text == null)
					return;
				if (temProfessionIndex != id) {
					String selectDay = industryPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
//					String selectMonth = counyPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
//					counyPicker.setData(professioncodeUtil.getCouny(couny_map,
//							professioncodeUtil.getProfession_list_code().get(id)));
//					counyPicker.setDefault(1);
					int lastDay = Integer.valueOf(professionPicker.getListSize());
					if (id > lastDay) {
						professionPicker.setDefault(lastDay - 1);
					}
				}
				temProfessionIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
//		counyPicker.setOnSelectListener(new OnSelectListener() {
//
//			@Override
//			public void endSelect(int id, String text) {
//				// TODO Auto-generated method stub
//
//				if (text.equals("") || text == null)
//					return;
//				if (tempCounyIndex != id) {
//					String selectDay = industryPicker.getSelectedText();
//					if (selectDay == null || selectDay.equals(""))
//						return;
//					String selectMonth = professionPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
//					// 城市数组
//					profession_code_string = professioncodeUtil.getCouny_list_code()
//							.get(id);
//					int lastDay = Integer.valueOf(counyPicker.getListSize());
//					if (id > lastDay) {
//						counyPicker.setDefault(lastDay - 1);
//					}
//				}
//				tempCounyIndex = id;
//				Message message = new Message();
//				message.what = REFRESH_VIEW;
//				handler.sendMessage(message);
//			}
//
//			@Override
//			public void selecting(int id, String text) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getProfession_code_string() {
		return profession_code_string;
	}

	public String getAll_string() {
		profession_string = industryPicker.getSelectedText() + "  "
				+ professionPicker.getSelectedText();
		return profession_string;
	}

	public String getIndustryCode() {
		int industryPositon = industryPicker.getSelected();
		return industry_list.get(industryPositon).getId();
	}

	public String getProfessionCode() {
		int professionPositon = professionPicker.getSelected();
		return profession_map.get(getIndustryCode()).get(professionPositon).getId();
	}

//	public String getAreaCode() {
//		int AreaPositon = counyPicker.getSelected();
//		return couny_map.get(getProfessionCode()).get(AreaPositon).getId();
//	}

	public String getIndustry_string() {
		return industryPicker.getSelectedText();
	}

	public String getProfession_string() {
		return professionPicker.getSelectedText();
	}

//	public String getArea_string() {
//		return counyPicker.getSelectedText();
//	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
