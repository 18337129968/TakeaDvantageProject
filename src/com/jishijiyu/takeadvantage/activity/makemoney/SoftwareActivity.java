package com.jishijiyu.takeadvantage.activity.makemoney;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ernie.CheckPriceActivity;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.DownloadPriceRequest;
import com.jishijiyu.takeadvantage.entity.request.QueryGoldNumRequest;
import com.jishijiyu.takeadvantage.entity.result.DowloadPriceResult;
import com.jishijiyu.takeadvantage.entity.result.IntegralWallResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.QueryGoldNumResult;
import com.jishijiyu.takeadvantage.entity.result.IntegralWallResult.AppList;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 拍币榜列表详情
 * 
 * @author baohan
 * 
 */
public class SoftwareActivity extends HeadBaseActivity {
	private Gallery gallery;
	public ImageView img;
	public TextView title;
	public TextView click;
	public TextView download_quantity;
	public TextView type;
	public TextView zhaonum;
	public ImageView picdetails;
	public TextView description;
	public Button comfirm_btn;

	private IntegralWallResult.AppList appList = null;
	private Gson gson = null;

	@Override
	public void appHead(View view) {
		top_text.setText("软件详情");
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this, R.layout.software_details_item, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		if (intent != null) {
			appList = (AppList) intent
					.getSerializableExtra(HeadBaseActivity.intentKey);
		}
		init();
	}

	private void init() {
		img = (ImageView) findViewById(R.id.icon_iv);
		title = (TextView) findViewById(R.id.application_name);
		click = (TextView) findViewById(R.id.click);
		download_quantity = (TextView) findViewById(R.id.download_quantity);
		type = (TextView) findViewById(R.id.type);
		zhaonum = (TextView) findViewById(R.id.zhao);
		description = (TextView) findViewById(R.id.description);
		comfirm_btn = (Button) findViewById(R.id.comfirm_btn);
		gallery = (Gallery) findViewById(R.id.gallery);
		if (appList != null) {
			ImageLoaderUtil.loadImage(appList.appIco, img);
			title.setText(appList.appTitle);
			type.setText("类型：" + appList.appType);
			zhaonum.setText("大小：" + appList.appSize);
			click.setText("点击量：" + 10000);
			description.setText(appList.appDescribe);
			if (!TextUtils.isEmpty(appList.appBanner)) {
				String url[] = appList.appBanner.split("#");
				gallery.setAdapter(new ImageAdapte(this, url));
			}

		}
		comfirm_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (appList != null) {
					IntegralWallActivity integralWallActivity=new IntegralWallActivity(mContext, null);
					integralWallActivity.implementsDownLoad(appList.appApk, appList.appTitle, Long.parseLong(appList.appSize), appList.appScore);
					getPrice(appList.appScore);
				}
				ToastUtils.makeText(SoftwareActivity.this, "已开始下载，请稍后...",
						ToastUtils.LENGTH_SHORT).show();
			}
		});
	}

	private void QueryUser() {
		QueryGoldNumRequest queryGoldNumRequest = new QueryGoldNumRequest();
		QueryGoldNumRequest.User user = queryGoldNumRequest.p;
		user.userId = GetUserIdUtil.getUserId(this);
		user.tokenId = GetUserIdUtil.getTokenId(this);
		final Gson gson = new Gson();
		String json = gson.toJson(queryGoldNumRequest);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {

				if (!TextUtils.isEmpty(result)) {
					QueryGoldNumResult queryGoldNumResult = gson.fromJson(
							result, QueryGoldNumResult.class);
					if (queryGoldNumResult.p.isTrue) {
						if (queryGoldNumResult.p.user != null) {
							LoginUserResult login = GetUserIdUtil
									.getLogin(SoftwareActivity.this);
							login.p.user.score = queryGoldNumResult.p.user.score != null ? Integer
									.parseInt(queryGoldNumResult.p.user.score)
									: 0;
							GetUserIdUtil.saveLoginUserResult(
									SoftwareActivity.this, login);
						}
					} else {
						IntentActivity.mIntent(SoftwareActivity.this);
					}
				}

			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
			}
		});

	};

	public void getPrice(String appScore) {
		gson = new Gson();
		DownloadPriceRequest request = new DownloadPriceRequest();
		DownloadPriceRequest.Parameter pramater = request.p;
		pramater.appScore = appScore;
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		String json = gson.toJson(request);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					LogUtil.i("result", result);
					DowloadPriceResult dowloadPriceResult = gson.fromJson(
							result, DowloadPriceResult.class);
					if (dowloadPriceResult != null) {
						if (dowloadPriceResult.p.isTrue) {
							if (dowloadPriceResult.p.isSucce) {
								QueryUser();
							} else {
								ToastUtils.makeText(SoftwareActivity.this,
										"拍币获取失败！", ToastUtils.LENGTH_SHORT)
										.show();
							}
						} else {
							IntentActivity.mIntent(SoftwareActivity.this);
						}
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {

			}
		});
	}

	class ImageAdapte extends BaseAdapter {
		private Context context;
		private String url[];

		public ImageAdapte(Context c, String url[]) {
			context = c;
			this.url = url;
		}

		@Override
		public int getCount() {
			return url.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageview = new ImageView(context);
			ImageLoaderUtil.loadImage(url[position], imageview);
			imageview.setScaleType(ImageView.ScaleType.FIT_XY);
			imageview.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			return imageview;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(SoftwareActivity.this);
			break;
		}

	}

}
