package com.jishijiyu.takeadvantage.activity.exchangemall;

import android.view.View;
import android.widget.FrameLayout;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;

/**
 * 电话卡充值详情
 * @author baohan
 */
public class PrepaidPhoneDetailsActivity extends HeadBaseActivity {

	@Override
	public void appHead(View view) {
		// TODO Auto-generated method stub
		top_text.setText(getResources().getString(R.string.phonecard));
		btn_right.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initReplaceView() {
		// TODO Auto-generated method stub
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(PrepaidPhoneDetailsActivity.this,
				R.layout.activity_phone_card_recharge, null);
		base_centent.addView(view);
		 AppManager.getAppManager().addActivity(this);

		 AppManager.getAppManager()
			.finishActivity(PrepaidPhoneDetailsActivity.this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
