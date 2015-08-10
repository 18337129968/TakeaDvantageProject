package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.utils.AppManager;

/**
 * 一元摇奖房间复用界面
 * 
 * @author shifeiyu
 * 
 */
public class OneRmbHomeActivity extends HeadBaseActivity {
	PullToRefreshListView listview = null;
	ListView registed_listview;
	List<Map<String, Object>> list;
	View v, vv;
	Map<String, Object> map;
	LinearLayout reuse_layout1, reuse_layout2, reuse_layout3, reuse_layout4,
			reuse_layout5;
	LinearLayout reuse_meal_layout1, reuse_meal_layout2, reuse_meal_layout3,
			reuse_meal_layout4;
	TextView getprize_now_btn, winprize_state, reuse_title1, reuse_title2,
			reuse_title3, taocan_title;
	ImageView first_prize_image, second_prize_image, third_prize_image;
	ImageView meal1_prize1_img, meal2_prize1_img, meal2_prize2_img,
			meal3_prize1_img, meal3_prize2_img, meal3_prize3_img,
			meal4_prize1_img, meal4_prize2_img, meal4_prize3_img,
			meal4_prize4_img;
	TextView meal1_prize1_name, meal2_prize1_name, meal2_prize2_name,
			meal3_prize1_name, meal1_prize1_text, meal2_prize1_text,
			meal2_prize2_text, meal3_prize1_text, meal3_prize2_text,
			meal3_prize3_text, meal4_prize1_text, meal4_prize2_text,
			meal4_prize3_text, meal4_prize4_text, registed_ok_text1,
			reward_text, stay_reward_text, join_to_ernie_btn,
			meal3_prize2_name, meal3_prize3_name, meal4_prize1_name,
			meal4_prize2_name, meal4_prize3_name, meal4_prize4_name;
	ListView listview1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(OneRmbHomeActivity.this,
				R.layout.activity_yyp_home, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		v = LayoutInflater.from(mContext).inflate(
				R.layout.layout_one_rmb_ernie_room, null);
		vv = LayoutInflater.from(mContext).inflate(
				R.layout.layout_register_listview, null);
		taocan_title = (TextView) v.findViewById(R.id.taocan_title);
		registed_ok_text1 = (TextView) v.findViewById(R.id.registed_ok);
		stay_reward_text = (TextView) findViewById(R.id.stay_reward_text);
		reward_text = (TextView) findViewById(R.id.reward_text);
		join_to_ernie_btn = (TextView) findViewById(R.id.join_to_ernie_btn);
		listview = (PullToRefreshListView) findViewById(R.id.reuse_listview);
		reuse_layout1 = (LinearLayout) findViewById(R.id.reuse_layout1);
		reuse_layout2 = (LinearLayout) findViewById(R.id.reuse_layout2);
		reuse_layout3 = (LinearLayout) findViewById(R.id.reuse_layout3);
		reuse_layout4 = (LinearLayout) findViewById(R.id.reuse_layout4);
		reuse_layout5 = (LinearLayout) vv.findViewById(R.id.reuse_layout5);
		registed_listview = (ListView) vv.findViewById(R.id.registed_listview);
		winprize_state = (TextView) findViewById(R.id.winprize_state);
		getprize_now_btn = (TextView) findViewById(R.id.getprize_now_btn);
		reuse_title1 = (TextView) v.findViewById(R.id.reuse_title1);
		reuse_title2 = (TextView) v.findViewById(R.id.reuse_title2);
		reuse_title3 = (TextView) v.findViewById(R.id.reuse_title3);
		reuse_meal_layout1 = (LinearLayout) v
				.findViewById(R.id.reuse_meal_layout1);
		reuse_meal_layout2 = (LinearLayout) v
				.findViewById(R.id.reuse_meal_layout2);
		reuse_meal_layout3 = (LinearLayout) v
				.findViewById(R.id.reuse_meal_layout3);
		reuse_meal_layout4 = (LinearLayout) v
				.findViewById(R.id.reuse_meal_layout4);
		meal1_prize1_img = (ImageView) v.findViewById(R.id.meal1_prize1_img);
		meal2_prize1_img = (ImageView) v.findViewById(R.id.meal2_prize1_img);
		meal2_prize2_img = (ImageView) v.findViewById(R.id.meal2_prize2_img);
		meal3_prize1_img = (ImageView) v.findViewById(R.id.meal3_prize1_img);
		meal3_prize2_img = (ImageView) v.findViewById(R.id.meal3_prize2_img);
		meal3_prize3_img = (ImageView) v.findViewById(R.id.meal3_prize3_img);
		meal4_prize1_img = (ImageView) v.findViewById(R.id.meal4_prize1_img);
		meal4_prize2_img = (ImageView) v.findViewById(R.id.meal4_prize2_img);
		meal4_prize3_img = (ImageView) v.findViewById(R.id.meal4_prize3_img);
		meal4_prize4_img = (ImageView) v.findViewById(R.id.meal4_prize4_img);
		meal1_prize1_name = (TextView) v.findViewById(R.id.meal1_prize1_name);
		meal2_prize1_name = (TextView) v.findViewById(R.id.meal2_prize1_name);
		meal2_prize2_name = (TextView) v.findViewById(R.id.meal2_prize2_name);
		meal3_prize1_name = (TextView) v.findViewById(R.id.meal3_prize1_name);
		meal3_prize2_name = (TextView) v.findViewById(R.id.meal3_prize2_name);
		meal3_prize3_name = (TextView) v.findViewById(R.id.meal3_prize3_name);
		meal4_prize1_name = (TextView) v.findViewById(R.id.meal4_prize1_name);
		meal4_prize2_name = (TextView) v.findViewById(R.id.meal4_prize2_name);
		meal4_prize3_name = (TextView) v.findViewById(R.id.meal4_prize3_name);
		meal4_prize4_name = (TextView) v.findViewById(R.id.meal4_prize4_name);
		meal1_prize1_text = (TextView) v.findViewById(R.id.meal1_prize1_text);
		meal2_prize1_text = (TextView) v.findViewById(R.id.meal2_prize1_text);
		meal2_prize2_text = (TextView) v.findViewById(R.id.meal2_prize2_text);
		meal3_prize1_text = (TextView) v.findViewById(R.id.meal3_prize1_text);
		meal3_prize2_text = (TextView) v.findViewById(R.id.meal3_prize2_text);
		meal3_prize3_text = (TextView) v.findViewById(R.id.meal3_prize3_text);
		meal4_prize1_text = (TextView) v.findViewById(R.id.meal4_prize1_text);
		meal4_prize2_text = (TextView) v.findViewById(R.id.meal4_prize2_text);
		meal4_prize3_text = (TextView) v.findViewById(R.id.meal4_prize3_text);
		meal4_prize4_text = (TextView) v.findViewById(R.id.meal4_prize4_text);
		listview1 = listview.getListView();
		listview1.addHeaderView(v);
	}

}
