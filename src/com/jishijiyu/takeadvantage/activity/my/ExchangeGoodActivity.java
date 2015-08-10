package com.jishijiyu.takeadvantage.activity.my;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;

/**
 * 兑换商品
 * 
 * @author baohan
 * 
 */
public class ExchangeGoodActivity extends HeadBaseActivity {

	@Override
	public void onClick(View v) {

	}

	@Override
	public void appHead(View view) {
		// TODO Auto-generated method stub
		top_text.setText(getResources().getString(R.string.exchange_products));
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
		View view = View.inflate(ExchangeGoodActivity.this,
				R.layout.convert_commodity, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
