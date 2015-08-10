package com.jishijiyu.takeadvantage.activity.my;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;

/**
 * 关于产品
 * 
 * @author baohan
 * 
 */
public class AboutProductActivity extends HeadBaseActivity {
	private TextView aboutProductText;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void appHead(View view) {
		// TODO Auto-generated method stub
		top_text.setText(getResources().getString(R.string.about_product));
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
		View view = View.inflate(AboutProductActivity.this,
				R.layout.about_product, null);
		base_centent.addView(view);
	//	String result = getAssetString("about_product.txt", this);
	//	TextView aboutProduct = (TextView) findViewById(R.id.about_product_text);
	//	aboutProduct.setText(result);
		// AppManager.getAppManager().addActivity(this);

		// AppManager.getAppManager().finishActivity(AboutProductActivity.this);
	}

	public static String getAssetString(String asset, Context context) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(context
					.getAssets().open(asset)));
			String line = null;
			StringBuilder builder = new StringBuilder();
			while (null != (line = bufferedReader.readLine())) {
				builder.append(line).append("\n");
			}
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			bufferedReader = null;
		}
		return "";
	}

	public static void main(String[] args) {

	}

}
