package com.jishijiyu.takeadvantage.activity.merchant_account;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;

/**
 * 提交成功界面
 * 
 * @author shifeiyu
 * 
 */
public class ApplyForMerchantSucceedActivity extends HeadBaseActivity {
	public static final String actiona = "jason.broadcast.action";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
		case R.id.btn_examine:
			finish();
		}

	}

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(
				R.string.choose_industry_category));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setBackgroundResource(R.drawable.btn_back);
		btn_left.setOnClickListener(this);
		top_text.setText("商家管理");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this,
				R.layout.activity_select_industry_category3, null);
		base_centent.addView(view);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				ApplyForMerchantSucceedActivity.this.finish();
			}
		}, 3000);

		Intent i = new Intent(actiona);
		mContext.sendBroadcast(i);
	}

}
