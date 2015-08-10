package com.jishijiyu.takeadvantage.activity.my;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
/**
 * 关于公司
 * @author baohan
 *
 */
public class AboutCompanyActivity extends HeadBaseActivity {

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void appHead(View view) {
		// TODO Auto-generated method stub
		top_text.setText(getResources().getString(R.string.about_company));
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
		// TODO Auto-generated method stub
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AboutCompanyActivity.this,
				R.layout.about_company, null);
		base_centent.addView(view);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
