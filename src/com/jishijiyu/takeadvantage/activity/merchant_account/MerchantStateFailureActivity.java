package com.jishijiyu.takeadvantage.activity.merchant_account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;

/**
 * 审核失败界面
 * 
 * @author shifeiyu
 * 
 */
public class MerchantStateFailureActivity extends HeadBaseActivity {

	private Button btn_examine;
	private TextView tv_examine2;
	private String reviewRemark;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;

		case R.id.btn_examine:
			startForActivity(MerchantStateFailureActivity.this,
					ApplyForMerchantStepOneActivity.class, null);
			finish();
			break;
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
				R.layout.activity_select_industry_category, null);
		base_centent.addView(view);
		Bundle bundle = getIntent().getExtras();
		reviewRemark = bundle.getString("reviewRemark");
		init();
	}

	private void init() {
		btn_examine = (Button) findViewById(R.id.btn_examine);
		tv_examine2 = (TextView) findViewById(R.id.tv_examine2);
		btn_examine.setOnClickListener(this);
		tv_examine2.setText(reviewRemark);
	}

}
