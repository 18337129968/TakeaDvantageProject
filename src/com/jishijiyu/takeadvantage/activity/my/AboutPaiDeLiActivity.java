package com.jishijiyu.takeadvantage.activity.my;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.exchangemall.ExchangemallActivity;
import com.jishijiyu.takeadvantage.activity.exchangemall.PrepaidPhoneDetailsActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;

/**
 * 关于拍得利
 * 
 * @author baohan
 * 
 */
public class AboutPaiDeLiActivity extends HeadBaseActivity {
	private RelativeLayout aboutProductLayout, aboutCompanyLayout,
			aboutAgreementLayout;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.about_paideli));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AboutPaiDeLiActivity.this,
				R.layout.about_paideli, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		aboutProductLayout = (RelativeLayout) findViewById(R.id.about_product_layout);
		aboutCompanyLayout = (RelativeLayout) findViewById(R.id.about_company_layout);
		aboutAgreementLayout = (RelativeLayout) findViewById(R.id.about_agreement_layout);
		aboutProductLayout.setOnClickListener(this);
		aboutCompanyLayout.setOnClickListener(this);
		aboutAgreementLayout.setOnClickListener(this);

		// AppManager.getAppManager()
		// .finishActivity(AboutPaiDeLiActivity.this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_product_layout:
			startForActivity(AboutPaiDeLiActivity.this,
					AboutProductActivity.class, null);
			break;
		case R.id.about_company_layout:
			startForActivity(AboutPaiDeLiActivity.this,
					AboutCompanyActivity.class, null);

			break;

		case R.id.about_agreement_layout:
			startForActivity(AboutPaiDeLiActivity.this,
					UseraGreementActivity.class, null);
			break;
		default:
			break;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
