package com.jishijiyu.takeadvantage.activity.ernie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelAdapter;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.SensorManagerHelper;
import com.jishijiyu.takeadvantage.utils.SensorManagerHelper.OnShakeListener;
import com.jishijiyu.takeadvantage.view.JustifyTextView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 规则
 * 
 * @author zhaobin
 */
public class RuleActivity extends HeadBaseActivity {

	private JustifyTextView text = null;
	private String name = "rule";

	@Override
	public void appHead(View view) {
		top_text.setText("摇奖规则");
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	public static String getAssetString(Context context, String name) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(context
					.getAssets().open(name + ".txt")));
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

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(RuleActivity.this, R.layout.rule, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		text = (JustifyTextView) findViewById(R.id.text);
		String s = getAssetString(this, name);
		text.setText(s);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(this);
			break;

		}
	}

}
