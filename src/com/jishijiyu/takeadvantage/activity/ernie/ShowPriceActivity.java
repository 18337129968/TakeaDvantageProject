package com.jishijiyu.takeadvantage.activity.ernie;

import java.util.List;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.entity.request.ShowPriceRequest;
import com.jishijiyu.takeadvantage.entity.request.ShowPriceRequest.Pramater;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.ShowPriceResult;
import com.jishijiyu.takeadvantage.entity.result.ShowPriceResult.PrizeList;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.SPUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class ShowPriceActivity extends Activity {
	private String ernieId = null;
	private ShowPriceResult priceResult = null;
	private List<PrizeList> list = null;
	private ViewPager pager = null;
	private String code = null;
	private boolean isSet = false;
	private Gson gson = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_price);
		ernieId = GetUserIdUtil.getErnieId(this);
		// ernieId = "1";
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		if (intent != null) {
			code = intent.getSerializableExtra(HeadBaseActivity.intentKey) != null ? intent
					.getSerializableExtra(HeadBaseActivity.intentKey)
					.toString() : null;
			if (code == null) {
				isSet = intent.getExtras() != null ? intent.getExtras()
						.getBoolean("success") : false;
			}
		}
		// String result = (String) SPUtils.get(this,
		// Constant.USER_INFO_FILE_NAME, "");
		// // getHttpData(userId);
		// if (!TextUtils.isEmpty(result)) {
		// if (gson == null) {
		// gson = new Gson();
		// }
		// Log.e("Result", result);
		// LoginUserResult loginUserResult = gson.fromJson(result,
		// LoginUserResult.class);
		// if (loginUserResult != null) {
		// if (loginUserResult.p.isSucce) {
		// if (loginUserResult.p.enroll != null) {
		//
		// String prizeShow = (String) SPUtils
		// .get(ShowPriceActivity.this,
		// Constant.PRIZE_SHOW, "");
		// if (!TextUtils.isEmpty(prizeShow)) {
		//
		// } else {
		//
		// }
		// return;
		// }
		// }
		// }
		// }

		getResult();
		// initView();
	}

	private void initView() {
		if (priceResult != null) {
			int type = Integer.parseInt(priceResult.p.type);
			switch (type) {
			case 0:
				list = priceResult.p.prizeList;
				break;
			default:
				list = null;
				break;
			}
		}
		pager = (ViewPager) findViewById(R.id.vp_gui_pager);
		pager.setAdapter(new MyPagerAdapter());
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void getResult() {
		final Gson gson = new Gson();
		ShowPriceRequest showPriceRequest = new ShowPriceRequest();
		Pramater pramater = showPriceRequest.p;
		pramater.ernieId = ernieId;
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		String json = gson.toJson(showPriceRequest);
		Log.e("Request", json);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					Log.e("Result", result);
					priceResult = gson.fromJson(result, ShowPriceResult.class);
					if (priceResult != null) {
						if (!priceResult.p.isTrue) {
							IntentActivity.mIntent(ShowPriceActivity.this);
							return;
						} else {
							SPUtils.put(ShowPriceActivity.this,
									Constant.PRIZE_SHOW, result);
							if ((!TextUtils.isEmpty(code) && code
									.equals("TaskActivity")) || isSet) {
								startActivityForIntent(priceResult);
								return;
							}
						}
					}
				}
				initView();
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				AppManager.getAppManager().finishActivity(
						ShowPriceActivity.this);
			}
		});
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (position == 3) {

			}
		}

		@Override
		public void onPageSelected(int position) {
			if (position == 3) {
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list == null ? 4 : list.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = LayoutInflater.from(ShowPriceActivity.this).inflate(
					R.layout.show_price_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.num);
			imageView.setScaleType(ScaleType.FIT_XY);
			if (!TextUtils.isEmpty(list.get(position).propagateImg)) {
				ImageLoaderUtil.loadImage(list.get(position).propagateImg,
						imageView);
			}
			// else {
			// imageView.setBackgroundResource(num[position]);
			// }
			container.addView(view);
			if (position == list.size() - 1) {
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivityForIntent(priceResult);
					}
				});
			}
			return view;
		}
	}

	private void startActivityForIntent(ShowPriceResult showPriceResult) {
		Intent intent = new Intent(ShowPriceActivity.this, ErnieActivity.class);
		intent.putExtra("showprice", showPriceResult);
		ShowPriceActivity.this.startActivity(intent);
		AppManager.getAppManager().finishActivity(ShowPriceActivity.this);
	}
}
