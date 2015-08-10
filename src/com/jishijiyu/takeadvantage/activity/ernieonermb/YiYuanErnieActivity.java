package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ernie.LocksActivity;
import com.jishijiyu.takeadvantage.activity.ernieonermb.JoinErniePrizeListResponse.PrizeGradeInfo;
import com.jishijiyu.takeadvantage.activity.paymoney.PayOrderActivity;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.DensityUtils;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.SensorManagerHelper;
import com.jishijiyu.takeadvantage.utils.SensorManagerHelper.OnShakeListener;

/**
 * 
 * 一元摇奖
 * 
 * @author zhaobin
 */
@SuppressLint("ResourceAsColor")
public class YiYuanErnieActivity extends HeadBaseActivity {
	private int imgId[] = { R.id.yiyuan_img_1, R.id.yiyuan_img_2,
			R.id.yiyuan_img_3, R.id.yiyuan_img_4, R.id.yiyuan_img_5,
			R.id.yiyuan_img_6, R.id.yiyuan_img_7, R.id.yiyuan_img_8,
			R.id.yiyuan_img_9, R.id.yiyuan_img_10, R.id.yiyuan_img_11,
			R.id.yiyuan_img_12, R.id.yiyuan_img_13, R.id.yiyuan_img_14,
			R.id.yiyuan_img_15, R.id.yiyuan_img_16, R.id.yiyuan_img_17,
			R.id.yiyuan_img_18, R.id.yiyuan_img_19, R.id.yiyuan_img_20 };
	private int codeImgId[] = { R.id.yiyuna_code_1, R.id.yiyuna_code_2,
			R.id.yiyuna_code_3, R.id.yiyuna_code_4, R.id.yiyuna_code_5,
			R.id.yiyuna_code_6 };
	private int imgbgId[] = { R.drawable.bg_img_1, R.drawable.bg_img_2,
			R.drawable.bg_img_3, R.drawable.bg_img_4, R.drawable.bg_img_5,
			R.drawable.bg_img_6, R.drawable.bg_img_7, R.drawable.bg_img_8,
			R.drawable.bg_img_9, R.drawable.bg_img_10 };
	private int lockImgId[] = { R.id.lock_1, R.id.lock_2, R.id.lock_3 };
	/**
	 * 锁Id
	 */
	private int locks[] = { R.drawable.img_unlock_3, R.drawable.img_unlock_3,
			R.drawable.img_unlock_3 };
	int lockNum[] = { -1, -1, -1 };
	private TextView tv_h, tv_m, tv_s;
	private LinearLayout point = null;
	private List<ImageView> imgs;
	private List<ImageView> ernieCodes = null;
	private List<ImageView> lockViews = null;
	private List<View> lockLayouts = null;
	private LinearLayout lock_layout_1 = null;
	private LinearLayout lock_layout_2 = null;
	private LinearLayout lock_layout_3 = null;
	private LinearLayout lock_layout_4 = null;
	private LinearLayout lock_layout_5 = null;
	private LinearLayout lock_layout_6 = null;
	private LinearLayout center_layout = null;
	private ImageView center = null, img_1 = null, shake_image = null;
	private int num = 10;
	private boolean isStop = false;
	private MyThread thread = null;
	private int ernieSize = 0;
	private int ernie[] = { 2, 3, 4, 8, 4, 19 };
	private ViewPager pager = null;
	private int selectedPosition = 0;
	private MyPagerAdapter mAdapter = null;
	private SensorManagerHelper managerHelper = null;
	private boolean isShaks = false;
	private List<int[]> ernieCodeLists = null;
	private AlertDialog alertDialog = null;
	private int[] winningNum;
	private String startErnieType;
	private String roomId;
	private ArrayList<PrizeGradeInfo> prizeInfo = new ArrayList<JoinErniePrizeListResponse.PrizeGradeInfo>();
	private int[] saveLoclaNum = { 100, 100, 100 };
	private int[] resultErnieCode;
	private int localCount = 0;
	private long ernieFinishedTime;
	private int shakeCount = 1;
	private boolean isShakeStop = true;

	@Override
	public void appHead(View view) {
		btn_right.setText(getResources().getString(R.string.ernie_top));
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(getResources().getString(R.string.lock_top));
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		top_text.setText("摇奖");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(YiYuanErnieActivity.this,
				R.layout.yiyuan_ernie, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		intent = this.getIntent();
		if (intent != null) {
			roomId = (String) intent
					.getSerializableExtra(HeadBaseActivity.intentKey);
		}
		imgs = new ArrayList<ImageView>();
		ernieCodes = new ArrayList<ImageView>();
		lockViews = new ArrayList<ImageView>();
		lockLayouts = new ArrayList<View>();
		initview(view);
		// long tempTime = 1000000000;

	}
	@Override
	protected void onResume() {
		super.onResume();
		getErniePrizeRequest();
		managerHelper = new SensorManagerHelper(YiYuanErnieActivity.this);
		isShaks = true;
		managerHelper.setOnShakeListener(listener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		managerHelper.stop();
		isShaks = false;
	}

	// 设置手臂晃动动画
	private void shakeAnim() {
		RotateAnimation anim = new RotateAnimation(0, 30, 0, 100);
		anim.setDuration(200);
		anim.setRepeatCount(-1);
		anim.setRepeatMode(2);
		shakeCount++;
		img_1.startAnimation(anim);
	}

	// 设置手臂晃动停止动画
	private boolean stopShakeAnim() {
		RotateAnimation anim = new RotateAnimation(0, 30, 0, 100);
		anim.setDuration(200);
		anim.setRepeatCount(0);
		anim.setRepeatMode(2);
		img_1.startAnimation(anim);
		isShakeStop = true;
		return isShakeStop;
	}

	private void initview(View view) {

		for (int i = 0; i < imgId.length; i++) {
			imgs.add((ImageView) getView(view, imgId[i]));
		}
		for (int i = 0; i < codeImgId.length; i++) {
			ernieCodes.add((ImageView) getView(view, codeImgId[i]));
		}
		for (int i = 0; i < lockImgId.length; i++) {
			lockViews.add((ImageView) getView(view, lockImgId[i]));
		}
		img_1 = (ImageView) findViewById(R.id.img_1);
		shake_image = (ImageView) findViewById(R.id.shake_image);
		shake_image.setOnClickListener(this);
		tv_h = (TextView) view.findViewById(R.id.tv_h);
		tv_m = (TextView) view.findViewById(R.id.tv_m);
		tv_s = (TextView) view.findViewById(R.id.tv_s);
		pager = (ViewPager) findViewById(R.id.vp_gui_pager);
		center_layout = (LinearLayout) view.findViewById(R.id.center_layout);
		lock_layout_1 = (LinearLayout) view.findViewById(R.id.lock_layout_1);
		lock_layout_2 = (LinearLayout) view.findViewById(R.id.lock_layout_2);
		lock_layout_3 = (LinearLayout) view.findViewById(R.id.lock_layout_3);
		lock_layout_4 = (LinearLayout) view.findViewById(R.id.lock_layout_4);
		lock_layout_5 = (LinearLayout) view.findViewById(R.id.lock_layout_5);
		lock_layout_6 = (LinearLayout) view.findViewById(R.id.lock_layout_6);

		lockLayouts.add(lock_layout_1);
		lockLayouts.add(lock_layout_2);
		lockLayouts.add(lock_layout_3);
		lockLayouts.add(lock_layout_4);
		lockLayouts.add(lock_layout_5);
		lockLayouts.add(lock_layout_6);
		center = (ImageView) view.findViewById(R.id.center);

		// 为锁注册监听器
		// ImageView image_lock1=(ImageView)
		// findViewById(R.id.lock_1);image_lock1.setOnClickListener(this);
		// ImageView image_lock2=(ImageView)
		// findViewById(R.id.lock_2);image_lock2.setOnClickListener(this);
		// ImageView image_lock3=(ImageView)
		// findViewById(R.id.lock_3);image_lock3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(YiYuanErnieActivity.this);
			break;
		case R.id.btn_top_right:
			startForActivity(YiYuanErnieActivity.this, LocksActivity.class,
					null);
			break;
		case R.id.lock_1:
			num1 = 1;
			if (saveLoclaNum[0] == 100) {
				System.out.println("执行锁1");
				Lock(ernie, 0);
			} else {
				if(GetUserIdUtil.copperLockNum(mContext)-1>=0 && GetUserIdUtil.silverLockNum(mContext)-1>=0 && GetUserIdUtil.goldLockNum(mContext)-1>=0 || saveLoclaNum[1] !=100){
					Toast.makeText(this, "该锁已经锁住，无需再次点击", 1).show();
				}else{
					Toast.makeText(this, "您锁的数量不足..", 1).show();
				}
				
			}

			break;
		case R.id.lock_2:
			num1 = 1;
			if (saveLoclaNum[1] == 100) {
				Lock(ernie, 1);
				System.out.println("执行锁2");
			} else {
				if(GetUserIdUtil.copperLockNum(mContext)-1>=0 && GetUserIdUtil.silverLockNum(mContext)-1>=0 && GetUserIdUtil.goldLockNum(mContext)-1>=0 || saveLoclaNum[1] !=100){
					Toast.makeText(this, "该锁已经锁住，无需再次点击", 1).show();
				}else{
					Toast.makeText(this, "您锁的数量不足..", 1).show();
				}
			}

			break;
		case R.id.lock_3:
			num1 = 2;
			if (saveLoclaNum[2] == 100) {
				Lock(ernie, 2);
				System.out.println("执行锁3");
			} else {
				if(GetUserIdUtil.copperLockNum(mContext)-1>=0 && GetUserIdUtil.silverLockNum(mContext)-1>=0 && GetUserIdUtil.goldLockNum(mContext)-1>=0 || saveLoclaNum[2] !=100){
					Toast.makeText(this, "该锁已经锁住，无需再次点击", 1).show();
				}else{
					Toast.makeText(this, "您锁的数量不足..", 1).show();
				}
			}

			break;
		case R.id.shake_image: // 点击摇一摇图片

			if (isShakeStop) {
				shakeAnim();
				ernieSize = 0;
				if (saveLoclaNum[0] == 100) {
					ernieCodes.get(0).setBackgroundResource(0);
				}
				if (saveLoclaNum[1] == 100) {
					ernieCodes.get(1).setBackgroundResource(0);
				}
				if (saveLoclaNum[2] == 100) {
					ernieCodes.get(2).setBackgroundResource(0);
				}
				ernieCodes.get(4).setBackgroundResource(0);
				ernieCodes.get(5).setBackgroundResource(0);
				ernieCodes.get(3).setBackgroundResource(0);
				shakeErnie(null, false);
				isShakeStop = false;
			} else {
				Toast.makeText(this, "亲，请耐心等一下哦..", 1).show();
			}
			break;
		}
	}

	// 摇奖动画
	private class MyThread extends Thread {
		public MyThread() {
			// System.out.println("从Thread开启线程成功");
			isShaks = false;
			ernieSize++;
			isStop = false;

		}

		public void Stop() {
			isStop = true;

			// System.out.println("从Thread走stop方法");
		}

		@Override
		public void run() {
			// System.out.println("从Thread中走run方法成功");
			super.run();
			do {
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				num++;
				if (num >= 20) {
					num = 0;
				}
				Message msg = new Message();
				msg.what = 0;
				msg.arg1 = num;
				handler.sendMessage(msg);
				if (num == ernie[ernieSize - 1]) {
					isStop = true;
					// System.out.println("num == ernie[ernieSize - 1：走这里成功"+num);
					num = ernie[ernieSize - 1];
				}
			} while (!isStop);
		}
	}

	private String IsSureGetPrize() {
		// System.out.println("有用处prizeInfo:" + prizeInfo);
		// 一二三四等奖号码
		String firstPrize = "";
		String secondPrize = "";
		String thridPrize = "";
		String fourPrize = "";
		// 用户摇奖号码(包含用锁)的结果号码
		String userErnieNum = "";
		for (int i = 0; i < ernie.length; i++) {
			for (int j = 0; j < saveLoclaNum.length; j++) {
				if (saveLoclaNum[j] != 100) {
					ernie[j] = saveLoclaNum[j];
				}
			}
			userErnieNum = userErnieNum + ernie[i];
		}
		for (int i = 0; i < prizeInfo.size(); i++) {
			int[] prizeNum = prizeInfo.get(i).num;
			for (int j = 0; j < prizeNum.length; j++) {
				if (i == 0) {
					firstPrize = firstPrize + prizeNum[j];
					// System.out.println("firstPrize:"+firstPrize);
				} else if (i == 1) {
					secondPrize = secondPrize + prizeNum[j];
					// System.out.println("secondPrize:"+secondPrize);

				} else if (i == 2) {
					thridPrize = thridPrize + prizeNum[j];
					// System.out.println("thridPrize:"+thridPrize);
				} else if (i == 3) {
					fourPrize = fourPrize + prizeNum[j];
					// System.out.println("fourPrize:"+fourPrize);
				}
			}
		}
		System.out.println("一等奖号码：" + firstPrize);
		System.out.println("二等奖号码：" + secondPrize);
		System.out.println("三等奖号码：" + thridPrize);
		System.out.println("四等奖号码：" + fourPrize);
		for (int i = 0; i < ernie.length; i++) {
			System.out.println("我的摇奖号：" + ernie[i]);
		}
		// 比较用户是否中奖
		if (firstPrize.equals(userErnieNum) && !TextUtils.isEmpty(firstPrize)
				&& !TextUtils.isEmpty(userErnieNum)) {
			if (prizeInfo.get(0).places > 0) {
				showPrice(null);
				System.out.println("一等奖号码：" + firstPrize);
				return "恭喜您，获得一等奖大奖";
			} else {
				return "很遗憾，获奖名额已完，下次记得快点哦";
			}

		} else if (secondPrize.equals(userErnieNum)
				&& !TextUtils.isEmpty(secondPrize)
				&& !TextUtils.isEmpty(userErnieNum)) {
			if (prizeInfo.get(1).places > 0) {
				showPrice(null);
				System.out.println("二等奖号码：" + secondPrize);
				return "恭喜您，获得二等奖大奖";
			} else {
				return "很遗憾，获奖名额已完，下次记得快点哦";
			}
		} else if (thridPrize.equals(userErnieNum)
				&& !TextUtils.isEmpty(thridPrize)
				&& !TextUtils.isEmpty(userErnieNum)) {
			if (prizeInfo.get(2).places > 0) {
				showPrice(null);
				System.out.println("二等奖号码：" + thridPrize);
				return "恭喜您，获得三等奖大奖";
			} else {
				return "很遗憾，获奖名额已完，下次记得快点哦";
			}

		} else if (fourPrize.equals(userErnieNum)
				&& !TextUtils.isEmpty(fourPrize)
				&& !TextUtils.isEmpty(userErnieNum)) {
			if (prizeInfo.get(3).places > 0) {
				showPrice(null);
				System.out.println("四等奖号码：" + fourPrize);
				return "恭喜您，获得四等奖大奖";
			} else {
				return "很遗憾，获奖名额已完，下次记得快点哦";
			}
		}

		return "亲，请不要灰心，再接再厉";
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				num = msg.arg1;

				if (num == ernie[ernieSize - 1]) {
					// 改动
					setAnim(imgs.get(num), 0);
				}
				imgs.get(num).setVisibility(View.VISIBLE);
				if (num == 0) {
					imgs.get(19).setVisibility(View.INVISIBLE);
				} else {
					imgs.get(num - 1).setVisibility(View.INVISIBLE);
				}
				break;
			default:
				break;
			}
		};
	};

	private void setAnim(final View view, final int n) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		System.out.println("从这里走");
		alphaAnimation.setDuration(100);
		alphaAnimation.setRepeatCount(3);
		alphaAnimation.setRepeatMode(Animation.REVERSE);
		view.setAnimation(alphaAnimation);
		alphaAnimation.start();
		alphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				switch (n) {
				case 1:
					view.setVisibility(View.VISIBLE);
					break;
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				switch (n) {
				case 0:
					// System.out.println("case0:"+ernieSize);
					// if(ernieSize==0){
					// ernieSize++;
					// }
					ernieCodes.get(ernieSize - 1).setBackgroundResource(
							imgbgId[num > 10 ? num - 10 : num]);
					if (ernieSize < ernie.length) {
						// System.out.println("if內ernieSize:"+ernieSize);
						isStop = false;
						boolean isLock = true;
						for (int i = 0; i < prizeInfo.size(); i++) {
							// int n = ernieCodeLists.get(i)[ernieSize - 1];
							// if (n == num) {
							// if (ernieSize <= 3) {
							// System.out.println("走ernieSize小于3的部分");
							// int s = 0;
							// for (int j = 0; j < lockNum.length; j++) {
							// if (lockNum[j] >= 0) {
							// s++;
							// }
							// }
							// if (locks[ernieSize - 1] != R.drawable.img_lock_3
							// && locks[ernieSize - 1] != R.drawable.img_lock_2
							// && locks[ernieSize - 1] != R.drawable.img_lock_1)
							// {
							// switch (s) {
							// case 0:
							// locks[ernieSize - 1] = R.drawable.img_unlock_3;
							// break;
							// case 1:
							// locks[ernieSize - 1] = R.drawable.img_unlock_2;
							// break;
							// case 2:
							// locks[ernieSize - 1] = R.drawable.img_unlock_1;
							// break;
							// }
							// lockViews.get(ernieSize - 1)
							// .setBackgroundResource(
							// locks[ernieSize - 1]);
							// isLock = true;
							// }
							// }
							// }
							if (isLock && ernieSize <= 3) {
								// System.out.println("isLock中的ernieSize:"+ernieSize);
								lockViews.get(ernieSize - 1)
										.setOnClickListener(
												YiYuanErnieActivity.this);
								lockLayouts.get(ernieSize - 1)
										.setBackgroundColor(
												R.color.lock_bg_color);
							} else {
								if (ernieSize <= 3) {
									lockViews.get(ernieSize - 1)
											.setOnClickListener(null);
								}
								lockLayouts.get(ernieSize - 1)
										.setBackgroundColor(0);
							}
						}
						thread = new MyThread();
						thread.start();
					} else {
						isShaks = true;
						if (ernieSize == 6) {
							ernieSize = 0;
							// 中奖效果
							String resultString = IsSureGetPrize();
							Toast.makeText(YiYuanErnieActivity.this,
									resultString, 1).show();
						}
						stopShakeAnim();
					}

					break;
				case 1:
					center.setBackgroundResource(0);
					view.setVisibility(View.GONE);
					break;
				}
			}
		});
	}

	/**
	 * 计时器
	 * 
	 * @author zhaobin
	 */
	class TimeTask extends CountDownTimer {

		public TimeTask(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			tv_h.setText("00");
			tv_m.setText("00");
			tv_s.setText("00");
		}

		@Override
		public void onTick(long ernieFinishedTime) {
			// System.out.println("millisUntilFinished:"+millisUntilFinished);
			if (ernieFinishedTime != 0) {
				int hour = (int) (ernieFinishedTime / 3600000);
				int minute = (int) (ernieFinishedTime % 3600000 / 60000);
				int second = (int) (ernieFinishedTime % 60000 / 1000);
				String hourStr = hour + "";
				String minuteStr = minute + "";
				String secondStr = second + "";

				if (hourStr.length() == 1) {
					hourStr = "0" + hourStr;
				}
				if (minuteStr.length() == 1) {
					minuteStr = "0" + minuteStr;
				}
				if (secondStr.length() == 1) {
					secondStr = "0" + secondStr;
				}
				tv_h.setText(hourStr);
				tv_m.setText(minuteStr);
				tv_s.setText(secondStr);
			}
		}

	}

	// 设置倒计时件
	public void setUnitTimeStop(long ernieFinishedTime) {

	}

	private void initAutoViewPager() {
		if (prizeInfo.size() <= 0) {

			return;
		}
		point = (LinearLayout) findViewById(R.id.point);
		point.removeAllViews();
		View pointView;
		LayoutParams params;
		for (int i = 0; i < prizeInfo.size(); i++) {
			pointView = new View(mContext);
			pointView.setBackgroundResource(R.drawable.indicator);
			int w = DensityUtils.dp2px(mContext, 10);
			int h = DensityUtils.dp2px(mContext, 10);
			params = new LayoutParams(w, h);
			if (i != 0) {
				params.setMargins(15, 0, 0, 0);
			}
			pointView.setLayoutParams(params);
			pointView.setEnabled(false);
			point.addView(pointView);
		}
		selectedPosition = 0;
		point.getChildAt(selectedPosition).setEnabled(true);
		pager.setAdapter(new MyPagerAdapter());
		if (mAdapter == null) {
			mAdapter = new MyPagerAdapter();
			pager.setAdapter(mAdapter);
			pager.setOnPageChangeListener(new MyOnPageChangeListener());
			pager.setOnPageChangeListener(new MyOnPageChangeListener());
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			LogUtil.i("position", position + "'");
			selectedPosition = position;
			for (int i = 0; i < prizeInfo.size(); i++) {
				if (selectedPosition != i) {
					point.getChildAt(i).setEnabled(false);
				}
			}
			point.getChildAt(selectedPosition).setEnabled(true);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (prizeInfo != null) {
				return prizeInfo.size();
			}
			return 0;
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
			ViewHolder holde = null;
			holde = new ViewHolder();
			View view = LayoutInflater.from(YiYuanErnieActivity.this).inflate(
					R.layout.yiyuan_ernie_viewpeger, null);
			holde.imageViews = new ArrayList<ImageView>();
			holde.imgPrice = (ImageView) view.findViewById(R.id.img_url);
			holde.tvPrice = (TextView) view.findViewById(R.id.tv_price);
			holde.tvPriceNum = (TextView) view.findViewById(R.id.tv_price_num);
			holde.tvPriceEndNum = (TextView) view
					.findViewById(R.id.tv_price_surplus);
			holde.imageView1 = (ImageView) view.findViewById(R.id.img_price_1);
			holde.imageView2 = (ImageView) view.findViewById(R.id.img_price_2);
			holde.imageView3 = (ImageView) view.findViewById(R.id.img_price_3);
			holde.imageView4 = (ImageView) view.findViewById(R.id.img_price_4);
			holde.imageView5 = (ImageView) view.findViewById(R.id.img_price_5);
			holde.imageView6 = (ImageView) view.findViewById(R.id.img_price_6);
			holde.imageViews.add(holde.imageView1);
			holde.imageViews.add(holde.imageView2);
			holde.imageViews.add(holde.imageView3);
			holde.imageViews.add(holde.imageView4);
			holde.imageViews.add(holde.imageView5);
			holde.imageViews.add(holde.imageView6);
			container.addView(view);
			PrizeGradeInfo prizeGradeInfo = prizeInfo.get(position);
			if (prizeGradeInfo != null) {
				// if (!TextUtils.isEmpty(list.get(position).imgUrl)) {
				// ImageLoaderUtil.loadImage(list.get(position).imgUrl,
				// holde.imgPrice);
				// }
				String grade = "";
				switch (prizeGradeInfo.grade) {
				case 1:
					grade = "一等奖";
					break;
				case 2:
					grade = "二等奖";
					break;
				case 3:
					grade = "三等奖";
					break;
				case 4:
					grade = "四等奖";
					break;
				}
				if (prizeGradeInfo != null) {
					System.out.println(".........>>>");
					ImageLoaderUtil.loadImage(prizeGradeInfo.img,
							holde.imgPrice);
					holde.tvPrice.setText(grade);
					holde.tvPriceNum.setText(prizeGradeInfo.winningCount + "");
					holde.tvPriceEndNum.setText(prizeGradeInfo.places + "");
					int[] num = prizeGradeInfo.num;
					for (int i = 0; i < holde.imageViews.size(); i++) {
						// System.out.println("num:" + num);
						int code = num[i];
						holde.imageViews.get(i).setBackgroundResource(
								imgbgId[code > 10 ? code - 10 : code]);
					}
				}
			}
			return view;
		}

		public class ViewHolder {
			ImageView imgPrice;
			TextView tvPrice;
			TextView tvPriceNum;
			TextView tvPriceEndNum;
			List<ImageView> imageViews;
			ImageView imageView1;
			ImageView imageView2;
			ImageView imageView3;
			ImageView imageView4;
			ImageView imageView5;
			ImageView imageView6;
		}
	}

	public class ErniePrice {
		public String imgUrl;
		public int price;
		public int priceNum;
		public int priceCount;
		public int ernieCode[];
	}

	private OnShakeListener listener = new OnShakeListener() {

		@Override
		public void onShake() {
			if (isShaks) {
				ernieSize = 0;
				shakeErnie(null, false);
			}
		}
	};

	private void shakeErnie(String userId, final boolean isShak) {
		if (gson == null) {
			gson = new Gson();
		}
		System.out.println("走shakeErnie这里");
		isShaks = false;
		JoinYYErnieRequest joinYYErnieRequest = new JoinYYErnieRequest();
		joinYYErnieRequest.p.userId = GetUserIdUtil.getUserId(mContext);
		joinYYErnieRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		joinYYErnieRequest.p.roomId = roomId;
		String json = gson.toJson(joinYYErnieRequest);
		System.out.println("json:" + json);
		Log.e("Request", json);
		HttpConnectTool.update(json, false, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				System.out.println("摇动手机，返回的result：" + result);
				if (!TextUtils.isEmpty(result)) {
					Log.e("Result", result);
					JoinYYErnieResponse joinYYErnieResponse = gson.fromJson(
							result, JoinYYErnieResponse.class);
					if (joinYYErnieResponse.p.isTrue) {
						
						startErnieType = joinYYErnieResponse.p.startErnieType;
						// System.out.println("赋值结果：" + startErnieType);
						if (!TextUtils.isEmpty(startErnieType)) {
							switch (Integer.parseInt(startErnieType)) {
							case 1:// 不在摇奖时间之内
								ToastUtils.makeText(YiYuanErnieActivity.this,
										"摇奖还未开始!", ToastUtils.LENGTH_SHORT)
										.show();
								// stopShakeAnim();
								// new Thread(new Runnable() {
								//
								// @Override
								// public void run() {
								// try {
								// Thread.sleep(2000);
								// } catch (InterruptedException e) {
								// e.printStackTrace();
								// }
								// isShaks = true;
								// }
								// }).start();
								break;
							case 2:// 没有报名
								stopShakeAnim();
								ToastUtils
										.makeText(YiYuanErnieActivity.this,
												"您未报名，不能参与摇奖!",
												ToastUtils.LENGTH_SHORT).show();
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										isShaks = true;
									}
								}).start();
								break;
							case 3:// 没有获取用户的摇奖信息（可能服务器报错）
								stopShakeAnim();
								ToastUtils
										.makeText(YiYuanErnieActivity.this,
												"没有获取用户的摇奖信息!",
												ToastUtils.LENGTH_SHORT).show();
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										isShaks = true;
									}
								}).start();
								break;
							case 4:// 没有获取到当前房间的摇奖信息（可能服务器报错）
								stopShakeAnim();
								ToastUtils.makeText(YiYuanErnieActivity.this,
										"没有获取到当前房间的摇奖信息!",
										ToastUtils.LENGTH_SHORT).show();
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										isShaks = true;
									}
								}).start();
								break;
							case 5:// 没有奖品了
								stopShakeAnim();
								ToastUtils.makeText(YiYuanErnieActivity.this,
										"摇奖已经结束，请加入别的房间进行摇奖!",
										ToastUtils.LENGTH_SHORT).show();
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										isShaks = true;
									}
								}).start();
								break;
							case 6:// 添加中奖纪录服务器报错了
								stopShakeAnim();
								ToastUtils.makeText(YiYuanErnieActivity.this,
										"中奖纪录添加添加失败!", ToastUtils.LENGTH_SHORT)
										.show();
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											Thread.sleep(2000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										isShaks = true;
									}
								}).start();
								break;
							case 0:
								// for (int i = 0; i < 3; i++) {
								// if (locks[i] != R.drawable.img_lock_3
								// && locks[i] != R.drawable.img_lock_2
								// && locks[i] != R.drawable.img_lock_1) {
								// lockViews.get(i).setOnClickListener(
								// null);
								// switch (i) {
								// case 0:
								// ernieSize = 1;
								// break;
								// case 1:
								// ernieSize = 2;
								// break;
								// case 2:
								// ernieSize = 3;
								// break;
								// }
								// }
								// }
								for (int i = 0; i < joinYYErnieResponse.p.winningNum.length; i++) {
									ernie[i] = joinYYErnieResponse.p.winningNum[i];
									System.out.println(ernie[i]);
								}

								for (int i = 0; i < ernie.length; i++) {
									for (int j = 0; j < saveLoclaNum.length; j++) {
										if (saveLoclaNum[j] != 100) {
											ernie[j] = saveLoclaNum[j];
										}
									}
								}
								// 测试中奖
								// int[]arr={4,6,1,0,1,5};
								// ernie=arr;

								// System.out.println("joinYYErnieResponse.p.winningNum.length:"+joinYYErnieResponse.p.winningNum.length);
								thread = new MyThread();
								thread.start();
								break;
							}
							// getLock();
							// ernieCode = jsonobj.p.lotteryNum;
							// if (ernieCode == null || ernieCode.length < 0) {
							// ernieCode = setErnieCode();
							// }
							// if (!isShak) {
							// for (int i = 0; i < wheelViews.size(); i++) {
							// wheelViews.get(i).setCurrentItem(ernieCode[i]);
							// }
							// } else {
							// if (VoiceUtil.isVoice(YiYuanErnieActivity.this))
							// {
							// if (VoiceUtil.mp != null) {
							// VoiceUtil
							// .stopErnieMusic(YiYuanErnieActivity.this);
							// }
							// VoiceUtil.playErnieMusic(YiYuanErnieActivity.this);
							// }
						}
					} else {
						IntentActivity.mIntent(YiYuanErnieActivity.this);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {

			}
		});
	}

	public void showPrice(String url) {
		center_layout.setBackgroundResource(R.drawable.lock);
		center_layout.setVisibility(View.VISIBLE);
		center.setBackgroundResource(R.drawable.phone);
		setAnim(center_layout, 1);
	}

	int s;
	String msg = null;
	int num1 = 0;
	private Gson gson;
	private Intent intent;
	private ImageView image_shake;

	public void Lock(int[] ernieArry, int lockId) {
		// System.out.println("进入锁的方法....");
		boolean isLocked;
		int lockType = 0;
		s = 0;
		isLocked = false;
		if (!isLocked) {
			switch (locks[num1]) {
			case R.drawable.img_unlock_3:
				lockType = 0;
				break;
			case R.drawable.img_unlock_2:
				lockType = 1;
				break;
			case R.drawable.img_unlock_1:
				lockType = 2;
				break;
			}
			// if (loginUserResult != null) {
			// if (loginUserResult.p.user.inviteUserNum >= lockType + 1) {
			switch (lockType + 1) {
			case 1:
				msg = "您确定要用锁锁住吗？";
				lockCode(ernieArry, lockId);
				break;
			case 2:
				msg = "您确定要用锁锁住吗？";
				lockCode(ernieArry, lockId);
				break;
			case 3:
				msg = "您确定要用锁锁住吗？";
				lockCode(ernieArry, lockId);
				break;
			}
			// } else {
			// ToastUtils.makeText(ErnieActivity.this, "当前推荐人数不足，无法换锁。",
			// ToastUtils.LENGTH_SHORT).show();
			// }
			// }
		}
	}

	/**
	 * 使用号码锁
	 * 
	 * @author zhaobin
	 * @param loc
	 *            要锁的号码的位置下标，从0开始
	 * @return boolean
	 * @throws
	 */
	private void lockCode(int[] lockArray, final int lockId) {
		switch (lockId) {
		case 0:
			alertDialog = DialogUtils.showDialog(YiYuanErnieActivity.this, msg,
					new int[] { R.string.ext, R.string.end },
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case R.id.left:
								alertDialog.dismiss();
								break;
							case R.id.right:
								alertDialog.dismiss();

								localCount++;
								System.out.println("savaLoclaNum:"
										+ saveLoclaNum[lockId]);

								if (saveLoclaNum[1] == 100) {
									// 1没锁
									if (saveLoclaNum[2] == 100) {
										// 1，2都没锁

										if (getcopperLockNum()) {
											locks[0] = R.drawable.img_lock_3;
											locks[1] = R.drawable.img_unlock_2;
											locks[2] = R.drawable.img_unlock_2;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									} else {
										// 1没锁，2锁了

										if (getsilverLockNum()) {
											locks[0] = R.drawable.img_lock_2;
											locks[1] = R.drawable.img_unlock_1;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									}
								} else {
									// 1锁了
									if (saveLoclaNum[2] == 100) {

										if (getsilverLockNum()) {
											locks[0] = R.drawable.img_lock_2;
											locks[2] = R.drawable.img_unlock_1;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									} else {
										// 1锁了2锁了
										if (getGoldLocalNum()) {
											locks[0] = R.drawable.img_lock_1;
											lockViews.get(0)
													.setBackgroundResource(
															locks[0]);
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									}
								}
								break;
							}
						}
					}, false);
			break;
		case 1:
			alertDialog = DialogUtils.showDialog(YiYuanErnieActivity.this, msg,
					new int[] { R.string.ext, R.string.end },
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case R.id.left:
								alertDialog.dismiss();
								break;
							case R.id.right:
								alertDialog.dismiss();
								// getLockCode(lockId);

								localCount++;
								System.out.println("savaLoclaNum:"
										+ saveLoclaNum[lockId]);
								if (saveLoclaNum[0] == 100) {
									// 0没锁
									if (saveLoclaNum[2] == 100) {
										// 0，2都没锁

										if (getcopperLockNum()) {
											locks[1] = R.drawable.img_lock_3;
											locks[0] = R.drawable.img_unlock_2;
											locks[2] = R.drawable.img_unlock_2;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									} else {
										// 0没锁，2锁了

										if (getsilverLockNum()) {
											locks[1] = R.drawable.img_lock_2;
											locks[0] = R.drawable.img_unlock_1;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									}
								} else {
									// 0锁了
									if (saveLoclaNum[2] == 100) {

										if (getsilverLockNum()) {
											locks[1] = R.drawable.img_lock_2;
											locks[2] = R.drawable.img_unlock_1;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									} else {
										// 1锁了2锁了

										if (getGoldLocalNum()) {
											locks[1] = R.drawable.img_lock_1;
											lockViews.get(1)
													.setBackgroundResource(
															locks[1]);
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									}
								}
								break;
							}
						}
					}, false);
			break;
		case 2:
			alertDialog = DialogUtils.showDialog(YiYuanErnieActivity.this, msg,
					new int[] { R.string.ext, R.string.end },
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case R.id.left:
								alertDialog.dismiss();
								break;
							case R.id.right:
								alertDialog.dismiss();
								localCount++;
								System.out.println("savaLoclaNum:"
										+ saveLoclaNum[lockId]);
								if (saveLoclaNum[0] == 100) {
									// 0没锁
									if (saveLoclaNum[1] == 100) {
										// 0，1都没锁

										if (getcopperLockNum()) {
											locks[2] = R.drawable.img_lock_3;
											locks[0] = R.drawable.img_unlock_2;
											locks[1] = R.drawable.img_unlock_2;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									} else {
										// 0没锁，1锁了

										if (getsilverLockNum()) {
											locks[2] = R.drawable.img_lock_2;
											locks[0] = R.drawable.img_unlock_1;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									}
								} else {
									// 0锁了,1没锁 改动地方
									if (saveLoclaNum[1] == 100) {

										if (getsilverLockNum()) {
											locks[2] = R.drawable.img_lock_2;
											locks[1] = R.drawable.img_unlock_1;
											for (int i = 0; i < lockViews
													.size(); i++) {
												lockViews.get(i)
														.setBackgroundResource(
																locks[i]);
											}
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									} else {
										// 0锁了1锁了
										if (getGoldLocalNum()) {
											locks[2] = R.drawable.img_lock_1;
											lockViews.get(2)
													.setBackgroundResource(
															locks[2]);
											saveLoclaNum[lockId] = ernie[lockId];
											getLockCode(lockId);
										}
									}
								}

								break;
							}
						}
					}, false);
		}
	}

	// 判断锁的数量，不足提示，充足就锁定成功，向服务器发送请求
	// 金锁数量的判断
	public boolean getGoldLocalNum() {
		int goldLockNum = GetUserIdUtil.goldLockNum(mContext);
		if (goldLockNum >=1) {
			goldLockNum = goldLockNum - 1;
			LoginUserResult loginUserResult = GetUserIdUtil.getLogin(mContext);
			loginUserResult.p.user.goldLockNum = goldLockNum;
			GetUserIdUtil.saveLoginUserResult(mContext, loginUserResult);
			return true;
		} else {
			Toast.makeText(mContext, "亲，您金锁的数量不足哦..", 1).show();
			return false;
		}

	}

	// 银锁锁数量的判断
	public boolean getsilverLockNum() {
		int silverLockNum = GetUserIdUtil.silverLockNum(mContext);
		System.out.println("当前铜锁数量：" + silverLockNum);
		if (silverLockNum >=1) {
			silverLockNum = silverLockNum - 1;
			System.out.println("减去后银锁数量：" + silverLockNum);
			LoginUserResult loginUserResult = GetUserIdUtil.getLogin(mContext);
			loginUserResult.p.user.silverLockNum = silverLockNum;
			GetUserIdUtil.saveLoginUserResult(mContext, loginUserResult);
			return true;
		} else {
			Toast.makeText(mContext, "亲，您银锁的数量不足哦..", 1).show();
			return false;
		}

	}

	// 铜锁锁数量的判断
	public boolean getcopperLockNum() {
		int copperLockNum = GetUserIdUtil.copperLockNum(mContext);
		System.out.println("当前铜锁数量：" + copperLockNum);
		if (copperLockNum >=1) {
			copperLockNum = copperLockNum - 1;
			System.out.println("减去后铜锁数量：" + copperLockNum);
			LoginUserResult loginUserResult = GetUserIdUtil.getLogin(mContext);
			loginUserResult.p.user.copperLockNum = copperLockNum;
			GetUserIdUtil.saveLoginUserResult(mContext, loginUserResult);
			return true;
		} else {
			Toast.makeText(mContext, "亲，您铜锁的数量不足哦..", 1).show();
			return false;
		}

	}

	public void getLockCode(int lockId) {
		// LockCodeRequest lock = new LockCodeRequest();
		// LockCodeRequest.Pramater parameter = lock.p;
		System.out.println("执行从服务端获取锁的方法....");
		JoinYYErnieLocalRequest joinYYErnieLocalRequest = new JoinYYErnieLocalRequest();
		joinYYErnieLocalRequest.p.lock =lockId+"";
		joinYYErnieLocalRequest.p.userId = GetUserIdUtil.getUserId(this);
		joinYYErnieLocalRequest.p.tokenId = GetUserIdUtil.getTokenId(this);
		joinYYErnieLocalRequest.p.roomId = roomId;
		if (gson == null) {
			gson = new Gson();
		}
		String json = gson.toJson(joinYYErnieLocalRequest);
		Log.e("Request", json);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				// TODO Auto-generated method stub
				System.out.println("localResult:" + result);
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void getErniePrizeRequest() {
		ernieCodeLists = null;
		JoinErniePrizeListRequest joinErniePrizeListRequest = new JoinErniePrizeListRequest();
		joinErniePrizeListRequest.p.userId = GetUserIdUtil.getUserId(mContext);
		joinErniePrizeListRequest.p.tokenId = GetUserIdUtil
				.getTokenId(mContext);
		intent = this.getIntent();
		if (intent != null) {
			roomId = intent.getStringExtra("RoomId");
			System.out.println("roomId" + roomId);
		}
		joinErniePrizeListRequest.p.roomId = roomId;
		System.out.println("roomId:" + roomId);
		if (gson == null) {
			gson = new Gson();
		}
		String jsonString = gson.toJson(joinErniePrizeListRequest);
		HttpConnectTool.update(jsonString, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					System.out.println("取得奖品列表信息：" + result);
					JoinErniePrizeListResponse joinErniePrizeListResponse = gson
							.fromJson(result, JoinErniePrizeListResponse.class);
					if (joinErniePrizeListResponse.p.isTrue) {
						ernieCodeLists = new ArrayList<int[]>();
						for (int i = 0; i < joinErniePrizeListResponse.p.firstPrizeList
								.size(); i++) {
							// String
							// imgUri=joinErniePrizeListResponse.p.firstPrizeList.get(i).img;
							// ImageLoaderUtil.loadImage(imgUri, imgP)
							ernieCodeLists
									.add(joinErniePrizeListResponse.p.firstPrizeList
											.get(i).num);
							prizeInfo
									.add(joinErniePrizeListResponse.p.firstPrizeList
											.get(i));

							// System.out.println("joinErniePrizeListResponse:"
							// + joinErniePrizeListResponse.p.firstPrizeList
							// .get(0).name);
						}
						if (prizeInfo != null && prizeInfo.size() > 0) {
							initAutoViewPager();
						}
						long nowTime = joinErniePrizeListResponse.p.nowDate;
						System.out.println("摇奖开始时间：" + nowTime);
						long endTime = joinErniePrizeListResponse.p.dollerRoom.endTime;
						System.out.println("摇奖结束时间：" + endTime);
						ernieFinishedTime = endTime - nowTime;
						System.out.println("获取的从服务端ernieFinishedTime:"
								+ ernieFinishedTime);
						// System.out.println("prizeInfo:" + prizeInfo);
						// setUnitTimeStop(ernieFinishedTime);
						TimeTask task = new TimeTask(ernieFinishedTime, 1000);
						task.start();
					} else {
						IntentActivity.mIntent(YiYuanErnieActivity.this);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {

			}
		});
	}
}
