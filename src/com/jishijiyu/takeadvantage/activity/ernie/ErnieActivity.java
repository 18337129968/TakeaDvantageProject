package com.jishijiyu.takeadvantage.activity.ernie;

import java.lang.ref.SoftReference;
import java.util.*;

import kankan.wheel.widget.*;
import kankan.wheel.widget.adapters.AbstractWheelAdapter;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.exchangemall.FirmOrderActivity;
import com.jishijiyu.takeadvantage.entity.request.ErnieRequest;
import com.jishijiyu.takeadvantage.entity.request.LockCodeRequest;
import com.jishijiyu.takeadvantage.entity.request.PrizeDetailsRequest;
import com.jishijiyu.takeadvantage.entity.result.ErnieResult;
import com.jishijiyu.takeadvantage.entity.result.LockCodeResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.PrizeDetailsResult;
import com.jishijiyu.takeadvantage.entity.result.ShowPriceResult;
import com.jishijiyu.takeadvantage.receiver.MyReceiver;
import com.jishijiyu.takeadvantage.utils.*;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.SensorManagerHelper.OnShakeListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.*;

/**
 * 
 * 摇奖
 * 
 * @author zhaobin
 */
public class ErnieActivity extends HeadBaseActivity {
	/**
	 * 报名参加
	 */
	private Button btn_bm = null;
	/**
	 * 我的中奖
	 */
	private Button btn_myprice = null;
	private Button btn_myprice2 = null;
	private TextView price_num, price_num2, tv_price;
	private LinearLayout view = null;
	private LinearLayout center_layout = null;
	private LinearLayout layout2 = null;
	private ImageView img = null;
	private ImageView center = null;
	private ImageView img_price = null;
	private ImageView img_price2 = null;
	private ImageView img_price3 = null;
	private ImageView img_price4 = null;
	private ImageView lock1 = null;
	private ImageView lock2 = null;
	private ImageView lock3 = null;
	private TextView tv_h = null;
	private TextView tv_m = null;
	private TextView tv_s = null;
	private TextView ernieTp = null;
	private ViewGroup anim_mask_layout;// 动画层

	/**
	 * 未到报名时间
	 */
	private View view2 = null;
	private int[] end_location;
	private List<WheelView> wheelViews = null;
	private List<TextView> list1 = null;
	private List<TextView> list2 = null;
	private List<TextView> list3 = null;
	private List<TextView> list4 = null;
	private List<ImageView> lockList = null;
	private List<List<TextView>> list = null;
	private int items[] = { R.drawable.num0, R.drawable.num1, R.drawable.num2,
			R.drawable.num3, R.drawable.num4, R.drawable.num5, R.drawable.num6,
			R.drawable.num7, R.drawable.num8, R.drawable.num9 };
	private int items2[] = { R.drawable.red0, R.drawable.red1, R.drawable.red2,
			R.drawable.red3, R.drawable.red4, R.drawable.red5, R.drawable.red6,
			R.drawable.red7, R.drawable.red8, R.drawable.red9 };
	private Bitmap items3[] = new Bitmap[100];
	private Bitmap items4[] = new Bitmap[100];
	private static final int n = 300;
	private AlertDialog alertDialog = null;
	/**
	 * 一等奖
	 */
	private int n0[] = new int[6];
	/**
	 * 二等奖
	 */
	private int n1[] = new int[6];
	/**
	 * 三等奖
	 */
	private int n2[] = new int[6];
	/**
	 * 四等奖
	 */
	private int n3[] = new int[6];
	private int n4[] = { 5, 5, 5, 5, 5, 5 };
	/**
	 * 一二三四等奖信息
	 */
	private ShowPriceResult showPriceResult;
	private LoginUserResult.Ernie ernie = null;
	/**
	 * 时间
	 */
	private long tempTime = 0;
	private List<ShowPriceResult.PrizeList> prizeList = null;
	private String userId = null;
	private int[] ernieCode = null;
	private TimeTask task = null;
	/**
	 * 锁Id
	 */
	private int locks[] = { R.drawable.img_unlock_3, R.drawable.img_unlock_3,
			R.drawable.img_unlock_3 };
	int lockNum[] = { -1, -1, -1 };
	int ns = 0;
	private Gson gson = null;
	private PrizeDetailsResult.Parameter fivePriceParameter = null;
	private LoginUserResult loginUserResult = null;
	private String periods = null;
	private SensorManagerHelper managerHelper = null;
	private MyReceiver myReceiver = null;
	private boolean isShaks = false;
	SharedPreferences sp = null;

	@Override
	public void appHead(View view) {
		btn_right.setText(getResources().getString(R.string.ernie_top));
		btn_left.setOnClickListener(this);
		btn_right.setText(getResources().getString(R.string.lock_top));
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(ErnieActivity.this, R.layout.ernie, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		ernieCode = setErnieCode();
		if (sp == null) {
			sp = ErnieActivity.this.getSharedPreferences(Constant.SP_NAME,
					Context.MODE_PRIVATE);
		}
		Intent intent = getIntent();
		if (intent != null) {
			showPriceResult = (ShowPriceResult) intent
					.getSerializableExtra("showprice");
		}
		if (showPriceResult != null) {
			prizeList = showPriceResult.p.prizeList;
		}
		wheelViews = new ArrayList<WheelView>();
		initWheel(R.id.slot_1, 1);
		initWheel(R.id.slot_2, 2);
		initWheel(R.id.slot_3, 3);
		initWheel(R.id.slot_4, 4);
		initWheel(R.id.slot_5, 5);
		initWheel(R.id.slot_6, 6);
		add2Bitmap();
		initView(view);
		initOnclick();
		// locks[0] = R.drawable.img_lock_3;
		// locks[1] = R.drawable.img_unlock_2;
		// locks[2] = R.drawable.img_unlock_2;
		// lockNum[0] = 1;
	}

	public void initView(View view) {
		list = new ArrayList<List<TextView>>();
		list1 = new ArrayList<TextView>();
		list2 = new ArrayList<TextView>();
		list3 = new ArrayList<TextView>();
		list4 = new ArrayList<TextView>();
		LinearLayout view1 = (LinearLayout) view.findViewById(R.id.ernie_1);
		LinearLayout view2 = (LinearLayout) view.findViewById(R.id.ernie_2);
		LinearLayout view3 = (LinearLayout) view.findViewById(R.id.ernie_3);
		LinearLayout view4 = (LinearLayout) view.findViewById(R.id.ernie_4);
		addView(view1, 1);
		addView(view2, 2);
		addView(view3, 3);
		addView(view4, 4);
		list.add(list1);
		list.add(list2);
		list.add(list3);
		list.add(list4);
		this.view2 = view.findViewById(R.id.layout3);
		btn_bm = (Button) view.findViewById(R.id.btn_bm);
		btn_myprice = (Button) view.findViewById(R.id.btn_myprice);
		btn_myprice2 = (Button) view.findViewById(R.id.btn_myprice2);
		img_price = (ImageView) view.findViewById(R.id.img_price);
		img_price2 = (ImageView) view.findViewById(R.id.img_price2);
		img_price3 = (ImageView) view.findViewById(R.id.img_price3);
		img_price4 = (ImageView) view.findViewById(R.id.img_price4);
		tv_h = (TextView) view.findViewById(R.id.tv_h);
		tv_m = (TextView) view.findViewById(R.id.tv_m);
		tv_s = (TextView) view.findViewById(R.id.tv_s);
		tv_price = (TextView) view.findViewById(R.id.tv_price);
		ernieTp = (TextView) view.findViewById(R.id.ernie_top);
		lock1 = (ImageView) view.findViewById(R.id.lock_1);
		lock2 = (ImageView) view.findViewById(R.id.lock_2);
		lock3 = (ImageView) view.findViewById(R.id.lock_3);
		lockList = new ArrayList<ImageView>();
		lockList.add(lock1);
		lockList.add(lock2);
		lockList.add(lock3);
		center_layout = (LinearLayout) view.findViewById(R.id.center_layout);
		layout2 = (LinearLayout) view.findViewById(R.id.layout2);
		center = (ImageView) view.findViewById(R.id.center);
		price_num = (TextView) view.findViewById(R.id.price_num);
		price_num2 = (TextView) view.findViewById(R.id.price_num2);
		for (int i = 0; i < wheelViews.size(); i++) {
			WheelView wheel = wheelViews.get(i);
			int position = wheel.getCurrentItem();

			for (int j = 0; j < 4; j++) {
				TextView textView = list.get(j).get(
						Integer.parseInt(wheel.getTag().toString().trim()) - 1);
				String num = textView.getText().toString().trim();
				if (!TextUtils.isEmpty(num)
						&& Integer.parseInt(num) == position) {
					if (i + 1 == 6) {
						wheel.setViewAdapter(new SlotMachineAdapter(
								ErnieActivity.this, items4));
					} else {
						wheel.setViewAdapter(new SlotMachineAdapter(
								ErnieActivity.this, items2));
					}
					textView.setTextColor(android.graphics.Color.RED);
				} else {
					textView.setTextColor(android.graphics.Color.WHITE);
				}
			}
		}
		if (showPriceResult != null) {
			List<ShowPriceResult.PrizeList> list = showPriceResult.p.prizeList;
			if (!TextUtils.isEmpty(list.get(0).prizeImg)) {
				ImageLoaderUtil.loadImage(list.get(0).prizeImg, img_price);
			}
			if (!TextUtils.isEmpty(list.get(1).prizeImg)) {
				ImageLoaderUtil.loadImage(list.get(1).prizeImg, img_price2);
			}
			if (!TextUtils.isEmpty(list.get(2).prizeImg)) {
				ImageLoaderUtil.loadImage(list.get(2).prizeImg, img_price3);
			}
			if (!TextUtils.isEmpty(list.get(3).prizeImg)) {
				ImageLoaderUtil.loadImage(list.get(3).prizeImg, img_price4);
			}
		}
		if (myReceiver == null) {
			myReceiver = new MyReceiver() {
				@Override
				public void ernieBegintime() {
					checkUserInfo();
					init();
				}

				@Override
				public void ernieEndtime() {
				}

				@Override
				public void joinBegintime() {
				}

				@Override
				public void joinEndtime() {
				}
			};
			IntentFilter dynamic_filter = new IntentFilter();
			dynamic_filter.addAction(Constant.ernieBegintime); // 添加动态广播的Action
			dynamic_filter.addAction(Constant.ernieEndtime); // 添加动态广播的Action
			dynamic_filter.addAction(Constant.joinBegintime); // 添加动态广播的Action
			dynamic_filter.addAction(Constant.joinEndtime); // 添加动态广播的Action
			registerReceiver(myReceiver, dynamic_filter); // 注册自定义动态广播消息
		}

	}

	public void init() {
		for (int i = 0; i < 3; i++) {
			lockList.get(i).setBackgroundResource(locks[i]);
		}
		if (prizeList != null) {
			setPriceNumber();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		managerHelper = new SensorManagerHelper(ErnieActivity.this);
		userId = GetUserIdUtil.getUserId(this);
		checkUserInfo();
		getLock();
		init();
	}

	@Override
	protected void onPause() {
		super.onPause();
		managerHelper.stop();
	}

	public void initOnclick() {
		btn_bm.setOnClickListener(this);
		btn_myprice.setOnClickListener(this);
		btn_myprice2.setOnClickListener(this);
		img_price.setOnClickListener(this);
		img_price2.setOnClickListener(this);
		img_price3.setOnClickListener(this);
		img_price4.setOnClickListener(this);
	}

	@SuppressLint("ResourceAsColor")
	public void addView(LinearLayout view, int n) {

		for (int i = 0; i < 6; i++) {
			TextView textView = new TextView(this);
			textView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(android.graphics.Color.WHITE);
			switch (n) {
			case 1:
				list1.add(textView);
				break;
			case 2:
				list2.add(textView);
				break;
			case 3:
				list3.add(textView);
				break;
			case 4:
				list4.add(textView);
				break;
			}
			view.addView(textView);
		}
	}

	private void initWheel(int id, int num) {
		WheelView wheel = getWheel(id);
		if (num == 6) {
			wheel.setViewAdapter(new SlotMachineAdapter(this, items3));
		} else {
			wheel.setViewAdapter(new SlotMachineAdapter(this, items));
		}
		// wheel.setCurrentItem((int) (Math.random() * 40));
		switch (num) {
		case 1:
			wheel.setCurrentItem(ernieCode[0]);
			break;
		case 2:
			wheel.setCurrentItem(ernieCode[1]);
			break;
		case 3:
			wheel.setCurrentItem(ernieCode[2]);
			wheel.scroll(-2 * n, 3000);
			break;
		case 4:
			wheel.setCurrentItem(ernieCode[3]);
			break;
		case 5:
			wheel.setCurrentItem(ernieCode[4]);
			break;
		case 6:
			wheel.setCurrentItem(ernieCode[5]);
			// wheel.setCurrentItem(5, true);
			break;
		}
		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
		wheelViews.add(wheel);
	}

	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * wheel 转动
	 * 
	 * @param id
	 *            the wheel id
	 */
	private void mixWheel(int id, int index) {
		WheelView wheel = getWheel(id);
		if (index == 6) {
			wheel.setViewAdapter(new SlotMachineAdapter(this, items3));
		} else {
			wheel.setViewAdapter(new SlotMachineAdapter(this, items));
		}
		switch (index) {
		case 1:
			wheel.setCurrentItem(ernieCode[0]);
			wheel.scroll(-2 * n, 2000);
			break;
		case 2:
			wheel.setCurrentItem(ernieCode[1]);
			wheel.scroll(-2 * n, 2500);
			break;
		case 3:
			wheel.setCurrentItem(ernieCode[2]);
			wheel.scroll(-2 * n, 3000);
			break;
		case 4:
			wheel.setCurrentItem(ernieCode[3]);
			wheel.scroll(-2 * n, 3500);
			break;
		case 5:
			wheel.setCurrentItem(ernieCode[4]);
			wheel.scroll(-2 * n, 4000);
			break;
		case 6:
			wheel.setCurrentItem(ernieCode[5]);
			// wheel.setCurrentItem(5, true);
			wheel.scroll(-2 * n, 5000);
			break;
		}
	}

	// Wheel改变监听
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {

			}
		}
	};
	// Wheel滚动状态
	private boolean wheelScrolled = false;

	// Wheel 滚动监听
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			int position = wheel.getCurrentItem();
			int num = Integer.parseInt(wheel.getTag().toString().trim());
			if (ernieCode == n4) {
				if (num != 6) {
					wheel.setViewAdapter(new SlotMachineAdapter(
							ErnieActivity.this, items2));
				} else {
					wheel.setViewAdapter(new SlotMachineAdapter(
							ErnieActivity.this, items4));
				}
			} else {
				for (int i = 0; i < 4; i++) {
					TextView textView = list.get(i).get(num - 1);
					String n = textView.getText().toString().trim();
					if (!TextUtils.isEmpty(n)
							&& Integer.parseInt(n) == position) {
						if (num != 6) {
							wheel.setViewAdapter(new SlotMachineAdapter(
									ErnieActivity.this, items2));
						} else {
							wheel.setViewAdapter(new SlotMachineAdapter(
									ErnieActivity.this, items4));
						}
						textView.setTextColor(android.graphics.Color.RED);
						if (num <= 3) {
							int s = 0;
							for (int j = 0; j < lockNum.length; j++) {
								if (lockNum[j] >= 0) {
									s++;
								}
							}
							if (locks[num - 1] != R.drawable.img_lock_3
									&& locks[num - 1] != R.drawable.img_lock_2
									&& locks[num - 1] != R.drawable.img_lock_1) {
								switch (s) {
								case 0:
									locks[num - 1] = R.drawable.img_unlock_3;
									break;
								case 1:
									locks[num - 1] = R.drawable.img_unlock_2;
									break;
								case 2:
									locks[num - 1] = R.drawable.img_unlock_1;
									break;
								}
								lockList.get(num - 1).setBackgroundResource(
										locks[num - 1]);
								lockList.get(num - 1).setOnClickListener(
										ErnieActivity.this);
							}
						}
					} else {
						if (num <= 3) {
							lockList.get(num - 1).setOnClickListener(null);
						}
						textView.setTextColor(android.graphics.Color.WHITE);
					}
				}
				if (num == 6) {
					if (VoiceUtil.mp != null) {
						VoiceUtil.stopErnieMusic(ErnieActivity.this);
					}
				}
			}
			if (num == 6) {
				isShaks = true;
				// price = "一等奖";
				// setAnim(price, prizeList.get(0).prizeImg);
				if (isEqual(ernieCode, n0)) {
					price = "一等奖";
					setAnim(price, prizeList.get(0).prizeImg);
				} else if (isEqual(ernieCode, n1)) {
					price = "二等奖";
					setAnim(price, prizeList.get(1).prizeImg);
				} else if (isEqual(ernieCode, n2)) {
					price = "三等奖";
					setAnim(price, prizeList.get(2).prizeImg);
				} else if (isEqual(ernieCode, n3)) {
					price = "四等奖";
					setAnim(price, prizeList.get(3).prizeImg);
				} else if (isEqual(ernieCode, n4)) {
					price = "五等奖";
					if (fivePriceParameter != null
							&& !TextUtils
									.isEmpty(fivePriceParameter.Prize.prizeImg)) {
						setAnim(price, fivePriceParameter.Prize.prizeImg);
					}
				}
			}
		}
	};

	String price = null;
	int s;
	String msg = null;
	int num = 0;

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(ErnieActivity.this);
			break;
		case R.id.btn_bm:
			if (ernie != null) {
				if (Constant.time > ernie.joinEndtime) {
					ToastUtils.makeText(
							ErnieActivity.this,
							ErnieActivity.this.getResources().getString(
									R.string.join_ernie_over),
							ToastUtils.LENGTH_SHORT).show();
				} else {
					startForActivity(ErnieActivity.this,
							CheckPriceActivity.class, null);
				}
			}
			break;
		case R.id.btn_myprice:
			// startForActivity(ErnieActivity.this, MyPriceActivity.class,
			// (Serializable) winList);
			startForActivity(ErnieActivity.this, MyPriceActivity.class, null);
			break;
		case R.id.btn_myprice2:
			// startForActivity(ErnieActivity.this, MyPriceActivity.class,
			// (Serializable) winList);
			startForActivity(ErnieActivity.this, MyPriceActivity.class, null);
			break;
		case R.id.lock_1:
			num = 0;
			Lock();
			break;
		case R.id.lock_2:
			num = 1;
			Lock();
			break;
		case R.id.lock_3:
			num = 2;
			Lock();
			break;
		case R.id.btn_top_right:
			startForActivity(ErnieActivity.this, LocksActivity.class, null);
			break;
		case R.id.img_price:
			price = "一等奖";
			alertDialog = DialogUtils.showDialog(ErnieActivity.this,
					prizeList.get(0).name, prizeList.get(0).id,
					prizeList.get(0).prizeImg, null, ErnieActivity.this, false);
			break;
		case R.id.img_price2:
			price = "二等奖";
			alertDialog = DialogUtils.showDialog(ErnieActivity.this,
					prizeList.get(1).name, prizeList.get(1).id,
					prizeList.get(1).prizeImg, null, ErnieActivity.this, false);
			break;
		case R.id.img_price3:
			price = "三等奖";
			alertDialog = DialogUtils.showDialog(ErnieActivity.this,
					prizeList.get(2).name, prizeList.get(2).id,
					prizeList.get(2).prizeImg, null, ErnieActivity.this, false);
			break;
		case R.id.img_price4:
			alertDialog = DialogUtils.showDialog(ErnieActivity.this,
					prizeList.get(3).name, prizeList.get(3).id,
					prizeList.get(3).prizeImg, null, ErnieActivity.this, false);
			break;
		case R.id.btn_close:
			if (alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			break;
		}
	}

	/**
	 * Slot machine adapter
	 */
	private class SlotMachineAdapter extends AbstractWheelAdapter {
		// Image size
		final int IMAGE_WIDTH = 100;
		final int IMAGE_HEIGHT = 86;
		private int it[] = null;
		private Bitmap it2[] = null;
		// Cached images
		private List<SoftReference<Bitmap>> images;

		// Layout inflater
		private Context context;

		/**
		 * Constructor
		 */
		public SlotMachineAdapter(Context context, int it[]) {
			this.context = context;
			this.it = it;
			images = new ArrayList<SoftReference<Bitmap>>(it.length);
			for (int id : it) {
				images.add(new SoftReference<Bitmap>(loadImage(id)));
			}
		}

		/**
		 * Constructor
		 */
		public SlotMachineAdapter(Context context, Bitmap it[]) {
			this.context = context;
			this.it2 = it;
			images = new ArrayList<SoftReference<Bitmap>>(it.length);
			for (Bitmap id : it) {
				images.add(new SoftReference<Bitmap>(id));
			}
		}

		/**
		 * Loads image from resources
		 */
		private Bitmap loadImage(int id) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), id);
			// Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH,
			// IMAGE_HEIGHT, true);
			return bitmap;
		}

		@Override
		public int getItemsCount() {
			if (it != null) {
				return it.length;
			}
			return it2.length;
		}

		// Layout params for image view
		final LayoutParams params = new LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			ImageView img;
			if (cachedView != null) {
				img = (ImageView) cachedView.getTag();
			} else {
				LayoutInflater inflater = LayoutInflater.from(context);
				cachedView = inflater.inflate(R.layout.wheel_item, null);
				img = (ImageView) cachedView.findViewById(R.id.wheel_item);
				cachedView.setTag(img);
			}
			SoftReference<Bitmap> bitmapRef = images.get(index);
			Bitmap bitmap = bitmapRef.get();
			if (bitmap == null) {
				if (it != null) {
					bitmap = loadImage(it[index]);
				} else {
					bitmap = it2[index];
				}
				images.set(index, new SoftReference<Bitmap>(bitmap));
			}
			img.setImageBitmap(bitmap);
			return cachedView;
		}
	}

	private void setAnim(View v, String url, final int count) {
		anim_mask_layout = null;
		anim_mask_layout = createAnimLayout();

		int[] start_location = new int[2];
		v.getLocationInWindow(start_location);

		final View view = addViewToAnimLayout(anim_mask_layout, start_location);
		end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
		switch (count) {
		case 1:
			center.getLocationInWindow(end_location);
			break;
		case 2:
			btn_myprice.getLocationInWindow(end_location);// shopCart是那个购物车
			break;
		}
		ImageLoaderUtil.loadImage(url, img);
		// img.setBackgroundResource(R.drawable.img1);
		// 计算位移
		// int endX = end_location[0] - 130;// 动画位移的X坐标
		// int endY = end_location[1] - 120;// 动画位移的y坐标
		int endX = end_location[0] + 100;// 动画位移的X坐标
		int endY = end_location[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(800);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				img.setBackgroundResource(0);
				ErnieActivity.this.img.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				img.setBackgroundResource(0);
				ErnieActivity.this.img.setVisibility(View.GONE);
			}
		});

	}

	private void setAnim(String price, final String uri) {
		ErnieActivity.this.center_layout.setBackgroundResource(R.drawable.lock);
		ImageLoaderUtil.loadImage(uri, ErnieActivity.this.center);
		ErnieActivity.this.center.setVisibility(View.VISIBLE);
		tv_price.setText(price);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setRepeatCount(3);
		alphaAnimation.setRepeatMode(Animation.REVERSE);
		ErnieActivity.this.center_layout.setAnimation(alphaAnimation);
		alphaAnimation.start();
		alphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				ErnieActivity.this.center_layout
						.setBackgroundResource(R.drawable.lock);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tv_price.setText("");
				ErnieActivity.this.center_layout.setBackgroundResource(0);
				ErnieActivity.this.center.setVisibility(View.GONE);
				setAnim(center, uri, 2);
			}
		});
	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(ViewGroup vg, int[] location) {

		view = new LinearLayout(this);
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		view.setGravity(Gravity.CENTER);
		img = new ImageView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
		img.setLayoutParams(params);
		view.addView(img);
		vg.addView(view);
		return view;
	}

	/**
	 * 获取登录信息
	 */
	private void checkUserInfo() {

		String result = (String) SPUtils.get(mContext,
				Constant.USER_INFO_FILE_NAME, "");
		// getHttpData(userId);
		if (!TextUtils.isEmpty(result)) {
			if (gson == null) {
				gson = new Gson();
			}
			Log.e("Result", result);
			loginUserResult = gson.fromJson(result, LoginUserResult.class);
			if (loginUserResult != null) {
				if (loginUserResult.p.isSucce) {
					ernie = loginUserResult.p.ernie;
					top_text.setText("第" + ernie.periods + "期");
				}
			}
			String top = null;

			if (loginUserResult != null && ernie != null) {
				if (Constant.time >= ernie.ernieBegintime
						&& Constant.time <= ernie.ernieEndtime) {
					LoginUserResult.Enroll erEnroll = loginUserResult.p.enroll;
					if (erEnroll == null) {
						ErnieActivity.this.view2.setVisibility(View.VISIBLE);
						Constants.isApply = false;
						ErnieActivity.this.view2
								.setOnTouchListener(new OnTouchListener() {

									@Override
									public boolean onTouch(View v,
											MotionEvent event) {
										return true;
									}
								});
					} else {
						// ernieCode(userId, false);
						ErnieActivity.this.view2.setVisibility(View.GONE);
						Constants.isApply = true;
						managerHelper.stop();
						managerHelper = new SensorManagerHelper(
								ErnieActivity.this);
						managerHelper.setOnShakeListener(listener);
						isShaks = true;
						if (!TextUtils.isEmpty(loginUserResult.p.enroll.prizeId
								+ "")) {
							getFivePrice(loginUserResult.p.enroll.prizeId + "");
						}

					}
					tempTime = ernie.ernieEndtime - Constant.time;
					// top = "一等奖：摇出X，剩余X；二等奖：摇出X，剩余X；三等奖：摇出X，剩余X；四等奖：摇出X，剩余X";
					top = "摇奖中";
					Log.e("time", Constant.time + "");
					// TODO
					if (tempTime > 0) {
						if (task != null) {
							task.cancel();
						}
						task = new TimeTask(tempTime, 1000);
						task.start();
					} else {

					}
				} else {
					if (Constant.time < ernie.ernieBegintime) {
						tempTime = ernie.ernieBegintime - Constant.time;
						if (tempTime > 0) {
							if (task != null) {
								task.cancel();
							}
							task = new TimeTask(tempTime, 1000);
							task.start();
						}
					}
					managerHelper.stop();
					isShaks = false;
					tempTime = 0;
					// TODO
					ErnieActivity.this.view2.setVisibility(View.GONE);
					Date date = new Date(ernie.ernieBegintime);
					if (Constant.time > ernie.ernieBegintime) {
						top = "第" + ernie.periods + "期摇奖已经结束，敬请期待下一期摇奖!";

					} else {
						top = "今日" + date.getHours() + ":" + date.getMinutes()
								+ "开始摇奖   一等奖：" + prizeList.get(0).name
								+ ",二等奖：" + prizeList.get(1).name + ",三等奖："
								+ prizeList.get(2).name + ",四等奖："
								+ prizeList.get(3).name;
					}
				}
				if (loginUserResult.p.enroll != null) {
					Constants.isApply = true;
					btn_bm.setBackgroundResource(R.drawable.btn_reget_code);
					btn_bm.setEnabled(false);
					btn_bm.setText("我已报名");
				} else {
					btn_bm.setBackgroundResource(R.anim.btn_selector);
					btn_bm.setEnabled(true);
					Constants.isApply = false;
					btn_bm.setText("报名参加");
				}
			}
			lock1.setBackgroundResource(locks[0]);
			lock2.setBackgroundResource(locks[1]);
			lock3.setBackgroundResource(locks[2]);
			if (!TextUtils.isEmpty(top)) {
				ernieTp.setText(top);
			}

		}
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
			init();
			checkUserInfo();
			// ToastUtils.makeText(ErnieActivity.this, "摇奖已结束，请参加下一次摇奖！",
			// ToastUtils.LENGTH_SHORT).show();
			// AppManager.getAppManager().finishActivity(ErnieActivity.this);
		}

		@Override
		public void onTick(long millisUntilFinished) {

			if (millisUntilFinished != 0) {
				int hour = (int) (millisUntilFinished / 3600000);
				int minute = (int) (millisUntilFinished % 3600000 / 60000);
				int second = (int) (millisUntilFinished % 60000 / 1000);
				tv_h.setText(hour + "");
				tv_m.setText(minute + "");
				tv_s.setText(second + "");
			}
		}

	}

	/**
	 * 
	 * 图片合成
	 * 
	 * @author zhaobin
	 * @return void
	 * @throws
	 */
	private void add2Bitmap() {
		for (int i = 0; i < items.length; i++) {
			Bitmap first = BitmapFactory.decodeResource(getResources(),
					items[i]);
			for (int j = 0; j < items.length; j++) {
				Bitmap second = BitmapFactory.decodeResource(getResources(),
						items[j]);
				if (i * 10 + j < 10) {
					items3[i * 10 + j] = second;
				} else {
					Bitmap bitmap = ImageTools.add2Bitmap(first, second);
					items3[i * 10 + j] = bitmap;
				}
			}
		}
		for (int i = 0; i < items2.length; i++) {
			Bitmap first = BitmapFactory.decodeResource(getResources(),
					items2[i]);
			for (int j = 0; j < items2.length; j++) {
				Bitmap second = BitmapFactory.decodeResource(getResources(),
						items2[j]);
				if (i * 10 + j < 10) {
					items4[i * 10 + j] = second;
				} else {
					Bitmap bitmap = ImageTools.add2Bitmap(first, second);
					items4[i * 10 + j] = bitmap;
				}
			}
		}
	}

	/**
	 * 设置中奖号码
	 * 
	 * @author zhaobin
	 * @return void
	 * @throws
	 */
	private void setPriceNumber() {
		for (int i = 0; i < prizeList.size(); i++) {
			String winNumber = prizeList.get(i).winNumber;
			String n[] = winNumber.split(",");

			switch (i) {
			case 0:
				for (int j = 0; j < n.length; j++) {
					n0[j] = Integer.parseInt(n[j]);
				}
				break;
			case 1:
				for (int j = 0; j < n.length; j++) {
					n1[j] = Integer.parseInt(n[j]);
				}
				break;
			case 2:
				for (int j = 0; j < n.length; j++) {
					n2[j] = Integer.parseInt(n[j]);
				}
				break;
			case 3:
				for (int j = 0; j < n.length; j++) {
					n3[j] = Integer.parseInt(n[j]);
				}
				break;
			}
		}
		for (int i = 0; i < 6; i++) {
			list1.get(i).setText(n0[i] + "");
			list1.get(i).setTextColor(android.graphics.Color.WHITE);
			list2.get(i).setText(n1[i] + "");
			list2.get(i).setTextColor(android.graphics.Color.WHITE);
			list3.get(i).setText(n2[i] + "");
			list3.get(i).setTextColor(android.graphics.Color.WHITE);
			list4.get(i).setText(n3[i] + "");
			list4.get(i).setTextColor(android.graphics.Color.WHITE);
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
	private void lockCode(final int lockType) {
		switch (lockType) {
		case 0:
			alertDialog = DialogUtils.showDialog(ErnieActivity.this, msg,
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
								getLockCode(lockType);
								break;
							}
						}
					}, false);
			break;
		case 1:
			alertDialog = DialogUtils.showDialog(ErnieActivity.this, msg,
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
								getLockCode(lockType);
								break;
							}
						}
					}, false);
			break;
		case 2:
			alertDialog = DialogUtils.showDialog(ErnieActivity.this, msg,
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
								getLockCode(lockType);
								break;
							}
						}
					}, false);
			break;
		}
	}

	public void getLockCode(int lockType) {
		LockCodeRequest lock = new LockCodeRequest();
		LockCodeRequest.Pramater parameter = lock.p;
		parameter.loc = num + "";
		parameter.userId = GetUserIdUtil.getUserId(this);
		parameter.tokenId = GetUserIdUtil.getTokenId(this);
		if (gson == null) {
			gson = new Gson();
		}
		String json = gson.toJson(lock);
		Log.e("Request", json);
		HttpConnectTool.update(json, false, ErnieActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							Log.e("Result", result);
							LockCodeResult jsonobj = gson.fromJson(result,
									LockCodeResult.class);
							if (jsonobj.p.isTrue) {
								if (jsonobj.p.isSucce) {
									switch (num) {
									case 0:
										switch (s) {
										case 0:
											lockNum[0] = 0;
											locks[0] = R.drawable.img_lock_3;
											locks[1] = R.drawable.img_unlock_2;
											locks[2] = R.drawable.img_unlock_2;
											break;
										case 1:
											lockNum[1] = 0;
											locks[0] = R.drawable.img_lock_2;
											if (lockNum[0] == 1) {
												locks[2] = R.drawable.img_unlock_1;
											} else if (lockNum[0] == 2) {
												locks[1] = R.drawable.img_unlock_1;
											}
											break;
										case 2:
											lockNum[2] = 0;
											locks[0] = R.drawable.img_lock_1;
											break;
										}
										break;
									case 1:
										switch (s) {
										case 0:
											lockNum[0] = 1;
											locks[1] = R.drawable.img_lock_3;
											locks[0] = R.drawable.img_unlock_2;
											locks[2] = R.drawable.img_unlock_2;
											break;
										case 1:
											lockNum[1] = 1;
											locks[1] = R.drawable.img_lock_2;
											if (lockNum[0] == 1) {
												locks[2] = R.drawable.img_unlock_1;
											} else if (lockNum[0] == 2) {
												locks[0] = R.drawable.img_unlock_1;
											}
											break;
										case 2:
											lockNum[2] = 1;
											locks[1] = R.drawable.img_lock_1;
											break;
										}
										break;
									case 2:
										switch (s) {
										case 0:
											lockNum[0] = num;
											locks[num] = R.drawable.img_lock_3;
											locks[1] = R.drawable.img_unlock_2;
											locks[0] = R.drawable.img_unlock_2;
											break;
										case 1:
											lockNum[1] = num;
											locks[num] = R.drawable.img_lock_2;
											if (lockNum[0] == 1) {
												locks[0] = R.drawable.img_unlock_1;
											} else if (lockNum[0] == 2) {
												locks[1] = R.drawable.img_unlock_1;
											}
											break;
										case 2:
											lockNum[2] = num;
											locks[num] = R.drawable.img_lock_1;
											break;
										}
										break;
									}
									lock1.setBackgroundResource(locks[0]);
									lock2.setBackgroundResource(locks[1]);
									lock3.setBackgroundResource(locks[2]);
									// TODO
									saveLock();
								} else {
									ToastUtils.makeText(ErnieActivity.this,
											"锁定失败！", ToastUtils.LENGTH_SHORT)
											.show();
								}
							} else {
								IntentActivity.mIntent(ErnieActivity.this);
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

	public void Lock() {
		boolean isLocked;
		int lockType = 0;
		s = 0;
		isLocked = false;
		for (int i = 0; i < lockNum.length; i++) {
			if (lockNum[i] >= 0) {
				s++;
			}
			if (lockNum[i] == 2) {
				isLocked = true;
			}
		}
		if (!isLocked) {
			switch (locks[num]) {
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
			if (loginUserResult != null) {
				if (loginUserResult.p.user.inviteUserNum >= lockType + 1) {
					switch (lockType + 1) {
					case 1:
						msg = "需要消耗一个推荐人换取一把铜锁来锁定当前号码吗？";
						lockCode(lockType);
						break;
					case 2:
						msg = "需要消耗两个推荐人换取一把银锁来锁定当前号码吗？";
						lockCode(lockType);
						break;
					case 3:
						msg = "需要消耗三个推荐人换取一把金锁来锁定当前号码吗？";
						lockCode(lockType);
						break;
					}
				} else {
					ToastUtils.makeText(ErnieActivity.this, "当前推荐人数不足，无法换锁。",
							ToastUtils.LENGTH_SHORT).show();
				}
			}

		}

	}

	/**
	 * 
	 * 获取摇奖号码
	 * 
	 * @author zhaobin
	 * @param userId
	 *            用户id
	 * @param isShak
	 *            是否晃动
	 * @return String
	 * @throws
	 */
	private void ernieCode(String userId, final boolean isShak) {
		if (gson == null) {
			gson = new Gson();
		}
		ErnieRequest ernieRequest = new ErnieRequest();
		ernieRequest.p.userId = userId;
		ernieRequest.p.tokenId = GetUserIdUtil.getTokenId(this);
		// TODO
		String json = gson.toJson(ernieRequest);
		Log.e("Request", json);
		HttpConnectTool.update(json, false, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					Log.e("Result", result);
					isShaks = false;
					ErnieResult jsonobj = gson.fromJson(result,
							ErnieResult.class);
					if (jsonobj.p.isTrue) {
						// getLock();
						ernieCode = jsonobj.p.lotteryNum;
						if (ernieCode == null || ernieCode.length < 0) {
							ernieCode = setErnieCode();
						}
						if (!isShak) {
							for (int i = 0; i < wheelViews.size(); i++) {
								wheelViews.get(i).setCurrentItem(ernieCode[i]);
							}
						} else {
							if (VoiceUtil.isVoice(ErnieActivity.this)) {
								if (VoiceUtil.mp != null) {
									VoiceUtil
											.stopErnieMusic(ErnieActivity.this);
								}
								VoiceUtil.playErnieMusic(ErnieActivity.this);
							}
							for (int i = 0; i < 3; i++) {
								if (locks[i] != R.drawable.img_lock_3
										&& locks[i] != R.drawable.img_lock_2
										&& locks[i] != R.drawable.img_lock_1) {
									lockList.get(i).setOnClickListener(null);
									switch (i) {
									case 0:
										mixWheel(R.id.slot_1, 1);
										break;
									case 1:
										mixWheel(R.id.slot_2, 2);
										break;
									case 2:
										mixWheel(R.id.slot_3, 3);
										break;
									}
								}
							}
							mixWheel(R.id.slot_4, 4);
							mixWheel(R.id.slot_5, 5);
							mixWheel(R.id.slot_6, 6);
						}
					} else {
						IntentActivity.mIntent(ErnieActivity.this);
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

	private OnShakeListener listener = new OnShakeListener() {

		@Override
		public void onShake() {
			if (isShaks) {
				ernieCode(userId, true);
			}
		}
	};

	public int[] setErnieCode() {
		ernieCode = null;
		ernieCode = new int[6];
		boolean isStop = false;
		do {

			for (int i = 0; i < 6; i++) {
				if (i == ernieCode.length - 1) {
					ernieCode[i] = radom(0, 99);
				} else {
					ernieCode[i] = radom(0, 9);
				}
			}
			if (ernieCode != null && !isEqual(ernieCode, n0)
					&& !isEqual(ernieCode, n1) && !isEqual(ernieCode, n2)
					&& !isEqual(ernieCode, n3) && !isEqual(ernieCode, n4)) {
				isStop = true;
			}
		} while (!isStop);
		return ernieCode;
	}

	/**
	 * 
	 * 数值之间随机数
	 * 
	 * @author zhaobin
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return int
	 * @throws
	 */
	public int radom(int min, int max) {

		return (int) (Math.random() * (max - min) + min);

	}

	/**
	 * 获取报名选取的五等奖信息
	 * 
	 * @author zhaobin
	 * @param priceId
	 *            报名选取的五等奖id
	 * @return void
	 * @throws
	 */
	public void getFivePrice(String priceId) {
		PrizeDetailsRequest detailsRequest = new PrizeDetailsRequest();
		PrizeDetailsRequest.Parameter p = detailsRequest.p;
		p.prizeId = priceId;
		p.tokenId = GetUserIdUtil.getTokenId(ErnieActivity.this);
		p.userId = GetUserIdUtil.getUserId(ErnieActivity.this);
		final Gson gson = new Gson();
		String json = gson.toJson(detailsRequest);
		HttpConnectTool.update(json, false, ErnieActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						if (result != null) {
							PrizeDetailsResult jsonobj = gson.fromJson(result,
									PrizeDetailsResult.class);
							fivePriceParameter = jsonobj.p;
							if (jsonobj != null && !jsonobj.p.isTrue) {
								IntentActivity.mIntent(ErnieActivity.this);
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

	/**
	 * 保存锁的信息
	 * 
	 * @author zhaobin
	 * @return void
	 * @throws
	 */
	public void saveLock() {
		String lockm = lockNum[0] + "," + lockNum[1] + "," + lockNum[2];
		String lock = locks[0] + "," + locks[1] + "," + locks[2];

		SharedPreferences.Editor editor = sp.edit();
		editor.putString(loginUserResult.p.user.id + Constant.PERIODS,
				ernie.periods + "");
		editor.putString(loginUserResult.p.user.id + Constant.LOCK, lockm);
		editor.putString(loginUserResult.p.user.id + Constant.LOCK2, lock);
		editor.commit();
	}

	/**
	 * 获取锁的信息
	 * 
	 * @author zhaobin
	 * @return void
	 * @throws
	 */
	public void getLock() {
		String PERIODS = sp.getString(loginUserResult.p.user.id
				+ Constant.PERIODS, "0");
		String lockm = sp.getString(loginUserResult.p.user.id + Constant.LOCK,
				"-1,-1,-1");
		String lock = sp.getString(loginUserResult.p.user.id + Constant.LOCK2,
				locks[0] + "," + locks[1] + "," + locks[2]);
		if (PERIODS != null) {
			periods = PERIODS;
		}
		if (!TextUtils.isEmpty(periods)) {
			if (!periods.equals(ernie.periods)) {
				for (int i = 0; i < 3; i++) {
					locks[i] = R.drawable.img_unlock_3;
					lockNum[i] = -1;
				}
				saveLock();
			} else {
				if (lockm != null) {
					String locn[] = lockm.split(",");
					for (int i = 0; i < 3; i++) {
						lockNum[i] = Integer.parseInt(locn[i]);
					}
				}
				if (lock != null) {
					String loc[] = lock.split(",");
					for (int i = 0; i < 3; i++) {
						locks[i] = Integer.parseInt(loc[i]);
					}
				}
			}
		}
	}

	/**
	 * 清除锁的信息
	 * 
	 * @author zhaobin
	 * @return void
	 * @throws
	 */
	private void clearLock() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myReceiver != null) {
			unregisterReceiver(myReceiver);
		}
		AppManager.getAppManager().finishActivity(this);
	}

	public boolean isEqual(int[] arr1, int[] arr2) {
		boolean flag = true;
		if (arr1.length != arr2.length) {
			return false;
		} else {
			for (int i = 0; i < arr1.length; i++) {
				if (arr1[i] != arr2[i]) {
					flag = false;
				}
			}
		}
		return flag;
	}
}
