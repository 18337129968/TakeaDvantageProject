package com.jishijiyu.takeadvantage.activity.merchant_account;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
 * 选择行业界面
 * 
 * @author shifeiyu
 * 
 */
public class SelectBusinessTradeActivity extends HeadBaseActivity {
	GridView business_trade_gridview;
	List<String> list;
	String tradeName = null;
	public static final int GET_SECTION_TRADE = 0;
	int tradeCode;

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
		top_text.setText("选择行业类别");
		btn_right.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(SelectBusinessTradeActivity.this,
				R.layout.activity_business_trade, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initView();
	}

	private void initView() {
		business_trade_gridview = (GridView) findViewById(R.id.business_trade_gridview);
		View v = LayoutInflater.from(mContext).inflate(
				R.layout.layout_business_trade_item, null);
		business_trade_gridview.setAdapter(new MyBaseAdapter(mContext, data()) {

			@Override
			public View initView(int position, View convertView) {
				View v = LayoutInflater.from(mContext).inflate(
						R.layout.layout_business_trade_item, null);
				TextView tv = (TextView) v
						.findViewById(R.id.business_trade_item_text);
				tv.setText(data().get(position));
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
		});
		business_trade_gridview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						tradeName = data().get(arg2);
						Intent intent = new Intent(mContext,
								SelectTradeSubSectionActivity.class);
						intent.putExtra("arg2", arg2);
						tradeCode = arg2 + 10;
						startActivityForResult(intent, GET_SECTION_TRADE);

					}
				});
	}

	public List<String> data() {
		list = new ArrayList<String>();
		for (int i = 0; i < TradeData.tradeData.length; i++) {
			list.add(TradeData.tradeData[i][0]);
		}
		return list;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_SECTION_TRADE) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(mContext,
						ApplyForMerchantStepTwoActivity.class);
				intent.putExtra("tradeName", tradeName);
				intent.putExtra("subName", data.getStringExtra("subName"));
				intent.putExtra("tradeCode", "" + tradeCode);
				intent.putExtra("subCode",
						tradeCode + data.getStringExtra("subCode"));
				setResult(RESULT_OK, intent);
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
