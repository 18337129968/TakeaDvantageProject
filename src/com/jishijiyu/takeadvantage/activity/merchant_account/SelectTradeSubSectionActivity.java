package com.jishijiyu.takeadvantage.activity.merchant_account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.adapter.MyBaseAdapter;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 选择行业细分界面
 * 
 * @author shifeiyu
 * 
 */
public class SelectTradeSubSectionActivity extends HeadBaseActivity {
	GridView trade_subsection_gridview;
	List<String> list;
	Map<String, Object> map;
	int a = -1;

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
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText("选择行业细分");

	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(SelectTradeSubSectionActivity.this,
				R.layout.activity_business_trade, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		a = intent.getIntExtra("arg2", 0);
		initView();
	}

	private void initView() {
		trade_subsection_gridview = (GridView) findViewById(R.id.business_trade_gridview);
		trade_subsection_gridview
				.setAdapter(new MyGridAdapter(mContext, data()));
		trade_subsection_gridview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(
								SelectTradeSubSectionActivity.this,
								SelectBusinessTradeActivity.class);
						intent.putExtra("subName", data().get(arg2));
						int subCode = arg2 + 10;
						intent.putExtra("subCode", "" + subCode);
						setResult(RESULT_OK, intent);
						finish();
					}
				});
	}

	public List<String> data() {
		list = new ArrayList<String>();
		for (int i = 0; i + 1 < TradeData.tradeData[a].length; i++) {
			list.add(TradeData.tradeData[a][i + 1]);
		}
		return list;

	}

	private class MyGridAdapter extends MyBaseAdapter {

		public MyGridAdapter(Context context, List<?> list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View initView(int position, View convertView) {
			View v = LayoutInflater.from(mContext).inflate(
					R.layout.layout_business_trade_item, null);
			TextView tv = (TextView) v
					.findViewById(R.id.business_trade_item_text);
			tv.setText((data().get(position)));
			FrameLayout tradelayout = (FrameLayout) v
					.findViewById(R.id.trade_layout);
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) tradelayout
					.getLayoutParams();
			DisplayMetrics metric = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metric);
			int width = metric.widthPixels;
			params.width = width / 4;
			params.height = params.width / 2;
			tradelayout.setLayoutParams(params);
			return v;
		}

	}

}
