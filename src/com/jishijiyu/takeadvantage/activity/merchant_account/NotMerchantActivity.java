package com.jishijiyu.takeadvantage.activity.merchant_account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;

/**
 * 非商家界面
 * 
 * @author shifeiyu
 * 
 */
public class NotMerchantActivity extends HeadBaseActivity {
	TextView apply_for_merchant_btn;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.apply_for_merchant_btn:
			startForActivity(mContext, ApplyForMerchantStepOneActivity.class,
					null);
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText("商家管理");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(NotMerchantActivity.this,
				R.layout.activity_not_merchant, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		apply_for_merchant_btn = (TextView) findViewById(R.id.apply_for_merchant_btn);
		apply_for_merchant_btn.setOnClickListener(this);
//		// 广播
//		IntentFilter filter = new IntentFilter(
//				ApplyForMerchantSucceedActivity.actiona);
//		registerReceiver(broadcastReceiver, filter);
	}

//	// 广播
//	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			finish();
//		}
//	};
}
