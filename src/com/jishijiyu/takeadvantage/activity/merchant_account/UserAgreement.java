package com.jishijiyu.takeadvantage.activity.merchant_account;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;

/**
 * 用户使用协议
 * 
 * @author sunaoyang
 * 
 */
public class UserAgreement extends HeadBaseActivity {

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
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
		top_text.setText("拍得利用户协议");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this, R.layout.user_agreement, null);
		base_centent.addView(view);
	}

}
