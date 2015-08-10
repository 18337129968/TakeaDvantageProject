package com.jishijiyu.takeadvantage.activity.home;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.SPUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
/**
 * 引导页
 * @author tml
 */
public class GuiActivity extends Activity {
	private Context mContext = null;
	private int[] gui_images;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gui);
		mContext = GuiActivity.this;
		initView();
	}

	private void initView() {
		gui_images = new int[] { R.drawable.guid_item1, R.drawable.guid_item2,
				R.drawable.guid_item3,R.drawable.guid_item4 };
		ViewPager pager = (ViewPager) findViewById(R.id.vp_gui_pager);
		pager.setAdapter(new MyPagerAdapter());
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			if (position == gui_images.length - 1) {
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, LoginActivity.class);
						intent.putExtra("Login", Constant.LOGIN_TO_HOME_CODE);
						startActivity(intent);
						SPUtils.put(mContext, "isFist", false);
						finish();
					}
				});
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return gui_images.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setBackgroundResource(gui_images[position]);
			container.addView(imageView);
			return imageView;
		}
	}
}
