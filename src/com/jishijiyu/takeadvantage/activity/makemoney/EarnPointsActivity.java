package com.jishijiyu.takeadvantage.activity.makemoney;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.adapter.FragmentViewPagerAdapter;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.lidroid.xutils.ViewUtils;

/**
 * 赚拍币
 * 
 * @author zm
 * 
 */
public class EarnPointsActivity extends EarnPointsFM {
	private Button scoreBoard, recommendedList, integralWallList;
//	private FragmentManager manager;
//	private FragmentTransaction transaction;
	private Context mContext;
	private ViewPager viewPager;
	
	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.earnpoints));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
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
		View view = View.inflate(EarnPointsActivity.this, R.layout.earnpoints,
				null);

		mContext = EarnPointsActivity.this;
		ViewUtils.inject(this);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		viewPager = (ViewPager)findViewById(R.id.viewPager);
		scoreBoard = (Button) findViewById(R.id.scoreboard);
		recommendedList = (Button) findViewById(R.id.recommended_list);
		integralWallList = (Button) findViewById(R.id.integral_wall_list);
		integralWallList.setOnClickListener(this);
		scoreBoard.setOnClickListener(this);
		recommendedList.setOnClickListener(this);
		scoreBoard.setSelected(true);
		recommendedList.setSelected(false);
		IntegralistFragment integralistFragment = new IntegralistFragment(
				EarnPointsActivity.this, "1");
		IntegralistFragment integralistFragmentd = new IntegralistFragment(
				EarnPointsActivity.this, "2");
		IntegralWallActivity integralwall = new IntegralWallActivity(
				EarnPointsActivity.this, null);
		List<Fragment> listView = new ArrayList<Fragment>();
		listView.add(integralistFragment);
		listView.add(integralistFragmentd);
		listView.add(integralwall);
		
//		viewPager.setAdapter(new EarnPointsAdapter(getSupportFragmentManager(),listView));
//		viewPager.setCurrentItem(0);
		FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(this.getSupportFragmentManager(), viewPager,listView);
	    viewPager.setCurrentItem(0);
        adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener(){
            @Override
            public void onExtraPageSelected(int i) {
                System.out.println("Extra...i: " + i);
                switch (i) {
				case 0:
					scoreBoard.setSelected(true);
					recommendedList.setSelected(false);
					integralWallList.setSelected(false);
					break;
				case 1:
					scoreBoard.setSelected(false);
					recommendedList.setSelected(true);
					integralWallList.setSelected(false);
					break;
				case 2:
					scoreBoard.setSelected(false);
					recommendedList.setSelected(false);
					integralWallList.setSelected(true);
					break;
				default:
					break;
				}
            }
        });
//		viewPager.setOnPageChangeListener(new MyViewpagerListener());
//		manager = getFragmentManager();
//		transaction = manager.beginTransaction();
//		IntegralistFragment integralistFragment = new IntegralistFragment(
//				EarnPointsActivity.this, "1");
//		transaction.replace(R.id.earnpoints_rl, integralistFragment);
//		transaction.commit();
	}

	@Override
	public void onClick(View v) {
//		FragmentManager manager = getFragmentManager();
//		FragmentTransaction transaction = manager.beginTransaction();
		switch (v.getId()) {
		case R.id.scoreboard:
//			IntegralistFragment integralistFragment = new IntegralistFragment(
//					EarnPointsActivity.this, "1");
//			transaction.replace(R.id.earnpoints_rl, integralistFragment);
//			scoreBoard.setSelected(true);
//			recommendedList.setSelected(false);
//			integralWallList.setSelected(false);
			viewPager.setCurrentItem(0);
			break;
		case R.id.recommended_list:
//			IntegralistFragment integralistFragmentd = new IntegralistFragment(
//					EarnPointsActivity.this, "2");
//			transaction.replace(R.id.earnpoints_rl, integralistFragmentd);
//			scoreBoard.setSelected(false);
//			recommendedList.setSelected(true);
//			integralWallList.setSelected(false);
			viewPager.setCurrentItem(1);
			break;
		case R.id.integral_wall_list:

			/*
			 * Intent intent = new
			 * Intent(EarnPointsActivity.this.getApplicationContext() ,
			 * IntegralWallActivity.class); startActivity(intent);
			 */
//			IntegralWallActivity integralwall = new IntegralWallActivity(
//					EarnPointsActivity.this, null);
//			transaction.replace(R.id.earnpoints_rl, integralwall);
//			scoreBoard.setSelected(false);
//			recommendedList.setSelected(false);
//			integralWallList.setSelected(true);
			viewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
//		transaction.commit();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public class MyViewpagerListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 0:
				scoreBoard.setSelected(true);
				recommendedList.setSelected(false);
				integralWallList.setSelected(false);
				break;
			case 1:
				scoreBoard.setSelected(false);
				recommendedList.setSelected(true);
				integralWallList.setSelected(false);
				break;
			case 2:
				scoreBoard.setSelected(false);
				recommendedList.setSelected(false);
				integralWallList.setSelected(true);
				break;
			default:
				break;
			}
		}
		
	}

}
