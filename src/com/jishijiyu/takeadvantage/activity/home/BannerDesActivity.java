package com.jishijiyu.takeadvantage.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ernie.ErnieActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.LogUtil;

public class BannerDesActivity extends HeadBaseActivity {
	private Context mContext;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(BannerDesActivity.this);
			break;

		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_right.setText(getResources().getString(R.string.ernie_top));
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(BannerDesActivity.this,
				R.layout.activity_banner_des, null);
		mContext = BannerDesActivity.this;
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initData();
	}

	private void initData() {
		final String mUrl = getIntent().getStringExtra("LinkUrl");
		final WebView webView = (WebView) findViewById(R.id.wv);
		MyWebViewClient client = new MyWebViewClient();
		client.shouldOverrideUrlLoading(webView, mUrl);
		webView.setWebViewClient(new MyWebViewClient());

	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			LogUtil.i(url + "---" + view);
			return true;
		}

	}

}
