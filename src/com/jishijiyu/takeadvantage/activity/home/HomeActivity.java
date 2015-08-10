package com.jishijiyu.takeadvantage.activity.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.activity.Main1Activity;
import com.jishijiyu.takeadvantage.activity.ernie.ShowPriceActivity;
import com.jishijiyu.takeadvantage.activity.ernieonermb.OneRmbErnieActivity;
import com.jishijiyu.takeadvantage.activity.exchangemall.ExchangemallActivity;
import com.jishijiyu.takeadvantage.activity.makemoney.EarnPointsActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.MerchantAccountActivity;
import com.jishijiyu.takeadvantage.activity.myfriend.FriendActivity;
import com.jishijiyu.takeadvantage.activity.myinformation.MyInfomationActivity;
import com.jishijiyu.takeadvantage.activity.mytask.TaskActivity;
import com.jishijiyu.takeadvantage.activity.news.MyMessageActivity;
import com.jishijiyu.takeadvantage.activity.paymoney.PayOrderActivity;
import com.jishijiyu.takeadvantage.activity.showprize.ShinePrizeActivity;
import com.jishijiyu.takeadvantage.activity.signin.SignInActivity;
import com.jishijiyu.takeadvantage.entity.request.RequestNews;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult.Enroll;
import com.jishijiyu.takeadvantage.entity.result.ResultNews;
import com.jishijiyu.takeadvantage.entity.result.UserNotice;
import com.jishijiyu.takeadvantage.utils.APKUpData;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DensityUtils;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.SPUtils;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.view.MyAni;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 首页
 * 
 * @author tml
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HomeActivity extends Activity {

	@ViewInject(R.id.unread_msg_number)
	private TextView unread_msg_number;
	private Context mContext;
	@ViewInject(R.id.s9)
	private RelativeLayout s1;
	@ViewInject(R.id.s1)
	private RelativeLayout s2;
	@ViewInject(R.id.s10)
	private RelativeLayout s3;
	@ViewInject(R.id.s4)
	private RelativeLayout s4;
	@ViewInject(R.id.s3)
	private RelativeLayout s5;
	@ViewInject(R.id.s2)
	private RelativeLayout s6;
	@ViewInject(R.id.s5)
	private RelativeLayout s7;
	@ViewInject(R.id.s6)
	private RelativeLayout s8;
	@ViewInject(R.id.s7)
	private RelativeLayout s9;
	@ViewInject(R.id.s8)
	private RelativeLayout s10;
	@ViewInject(R.id.ll_home_pager_point)
	private LinearLayout ll_home_pager_point;
	@ViewInject(R.id.btn_top_left)
	private Button btn_top_left;
	@ViewInject(R.id.btn_top_right)
	private Button btn_top_right;
	@ViewInject(R.id.tv_news_number)
	private TextView tv_news_number;
	@ViewInject(R.id.iv_s9)
	private ImageView iv_s1;
	@ViewInject(R.id.iv_s1)
	private ImageView iv_s2;
	@ViewInject(R.id.iv_s10)
	private ImageView iv_s3;
	@ViewInject(R.id.iv_s4)
	private ImageView iv_s4;
	@ViewInject(R.id.iv_s3)
	private ImageView iv_s5;
	@ViewInject(R.id.iv_s2)
	private ImageView iv_s6;
	@ViewInject(R.id.iv_s5)
	private ImageView iv_s7;
	@ViewInject(R.id.iv_s6)
	private ImageView iv_s8;

	@ViewInject(R.id.iv_s8_2)
	private RelativeLayout iv_s8_2;
	@ViewInject(R.id.iv_s7)
	private ImageView iv_s9;
	@ViewInject(R.id.iv_s8)
	private ImageView iv_s10;
	@ViewInject(R.id.tv_integral_balance_count)
	TextView tv_integral_balance_count;
	@ViewInject(R.id.tv_integral_today_count)
	TextView tv_integral_today_count;
	@ViewInject(R.id.tv_integral_total_count)
	TextView tv_integral_total_count;
	@ViewInject(R.id.tv_s9)
	TextView tv_s1;
	@ViewInject(R.id.tv_s1)
	TextView tv_s2;
	@ViewInject(R.id.tv_s10)
	TextView tv_s3;
	@ViewInject(R.id.tv_s4)
	TextView tv_s4;
	@ViewInject(R.id.tv_s3)
	TextView tv_s5;
	@ViewInject(R.id.tv_s2)
	TextView tv_s6;
	@ViewInject(R.id.tv_s5)
	TextView tv_s7;
	@ViewInject(R.id.tv_s6)
	TextView tv_s8;
	@ViewInject(R.id.tv_s7)
	TextView tv_s9;
	@ViewInject(R.id.tv_s8)
	TextView tv_s10;
	@ViewInject(R.id.img_s9)
	ImageView img_s1;
	@ViewInject(R.id.img_s1)
	ImageView img_s2;
	@ViewInject(R.id.img_s10)
	ImageView img_s3;
	@ViewInject(R.id.img_s4)
	ImageView img_s4;
	@ViewInject(R.id.img_s3)
	ImageView img_s5;
	@ViewInject(R.id.img_s2)
	ImageView img_s6;
	@ViewInject(R.id.img_s5)
	ImageView img_s7;
	@ViewInject(R.id.img_s6)
	ImageView img_s8;
	@ViewInject(R.id.img_s7)
	ImageView img_s9;
	@ViewInject(R.id.img_s8)
	ImageView img_s10;
	private static final int BANNER_CODE = 0;
	private static final int TIMER_CODE = 1;
	private ArrayList<RelativeLayout> vList;
	private int count;
	private MyAni myAni;
	private int size;
	private long mExitTime;
	private float startX;
	private float endX;
	private float moveX;
	private float lastStartX;
	private AutoHandler mHandler;
	private ViewPager home_pager;
	private MyPagerAdapter mAdapter;
	private int selectedPosition;
	private LoginUserResult userResult;
	private BroadcastReceiver receiver;
	private IntentFilter filter;
	private long ernieBegintime;
	private long ernieEndtime;
	private long joinBegintime;
	private long joinEndtime;
	private ObjectAnimator rotate;
	private boolean getenter_for_state;
	private Enroll enroll;
	private Gson gson;
	private ArrayList<UserNotice> tempList;
	private long time;
	private AutoPlayRunnable playRunnable;
	private AutoTimeRunnable timeRunnable;
	private AlertDialog alertDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ViewUtils.inject(this);
		mContext = HomeActivity.this;
		new APKUpData(mContext).checkUpData(false);
		if (mHandler == null) {
			mHandler = new AutoHandler();
		}
		getUserInfo();
		GetUserIdUtil.getEnterForState(mContext);
		getenter_for_state = (Boolean) SPUtils.get(mContext,
				Constant.GETENTER_FOR_STATE, false);
		registerBroadCast();
		myAni = new MyAni(mContext);
		initHead();
		// textAnimation();
		initAutoViewPager();
		initDialView();
		// checkText();
		tv_integral_balance_count.setText("" + userResult.p.user.score);
		tv_integral_today_count.setText("" + userResult.p.user.todayScore);
		tv_integral_total_count.setText("" + userResult.p.user.goldNum);
	}

	// public void onRefresh() {
	// int count = getUnreadMsgCountTotal();
	// if (count > 0) {
	// unread_msg_number.setVisibility(View.VISIBLE);
	// } else {
	// count = getUnreadAddressCountTotal();
	// if (count > 0) {
	// unread_msg_number.setVisibility(View.VISIBLE);
	// } else {
	// unread_msg_number.setVisibility(View.INVISIBLE);
	// }
	// }
	// }

	// /**
	// * 获取未读申请与通知消息
	// *
	// * @return
	// */
	// public int getUnreadAddressCountTotal() {
	// int unreadAddressCountTotal = 0;
	// if (App.getInstance().getContactList()
	// .get(Constant.NEW_FRIENDS_USERNAME) != null)
	// unreadAddressCountTotal = App.getInstance().getContactList()
	// .get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
	// return unreadAddressCountTotal;
	// }

	// /**
	// * 获取未读消息数
	// *
	// * @return
	// */
	// public int getUnreadMsgCountTotal() {
	// int unreadMsgCountTotal = 0;
	// int chatroomUnreadMsgCount = 0;
	// unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
	// for (EMConversation conversation : EMChatManager.getInstance()
	// .getAllConversations().values()) {
	// if (conversation.getType() == EMConversationType.ChatRoom)
	// chatroomUnreadMsgCount = chatroomUnreadMsgCount
	// + conversation.getUnreadMsgCount();
	// }
	// return unreadMsgCountTotal - chatroomUnreadMsgCount;
	// }

	/**
	 * userInfoChanged 接收用户拍币变化的广播 Exit 接收重新登陆，返回键退出的广播
	 */
	private void registerBroadCast() {
		filter = new IntentFilter();
		filter.addAction("userInfoChanged");
		filter.addAction("Exit");
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("userInfoChanged")) {
					getUserInfo();
					tv_integral_balance_count.setText(""
							+ userResult.p.user.score);
					tv_integral_today_count.setText(""
							+ userResult.p.user.todayScore);
					tv_integral_total_count.setText(""
							+ userResult.p.user.totalScore);
				} else if (intent.getAction().equals("Exit")) {
					AppManager.getAppManager().addActivity(HomeActivity.this);
					AppManager.getAppManager().AppExit(mContext);
				}
			}
		};
		registerReceiver(receiver, filter);

	}

	private void initHead() {
		btn_top_left.setVisibility(View.VISIBLE);
		btn_top_right.setVisibility(View.VISIBLE);
		btn_top_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 签到
				Intent intent = new Intent(mContext, SignInActivity.class);
				startActivity(intent);
			}
		});
		btn_top_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MyMessageActivity.class);
				startActivity(intent);
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// @SuppressLint("NewApi")
	// private void textAnimation() {
	// rotate = ObjectAnimator.ofFloat(tv_centre_title, "TranslationX", 4, -8,
	// 8, 4, 0);
	// rotate.setDuration(120);
	// rotate.setRepeatCount(1);
	// rotate.setRepeatMode(Animation.REVERSE);
	// }
	/**
	 * 转盘
	 */
	private void initDialView() {
		// s1 = (RelativeLayout) findViewById(R.id.s1);
		// s2 = (RelativeLayout) findViewById(R.id.s2);
		// s3 = (RelativeLayout) findViewById(R.id.s3);
		// s4 = (RelativeLayout) findViewById(R.id.s4);
		// s5 = (RelativeLayout) findViewById(R.id.s5);
		// s6 = (RelativeLayout) findViewById(R.id.s6);
		// s7 = (RelativeLayout) findViewById(R.id.s7);
		// s8 = (RelativeLayout) findViewById(R.id.s8);
		// s9 = (RelativeLayout) findViewById(R.id.s9);
		// s10 = (RelativeLayout) findViewById(R.id.s10);
		initData();
		// // btn_centre_title.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// itemClick();
		// }
		// });
	}

	/**
	 * 获得用户信息
	 */
	private void getUserInfo() {
		tempList = new ArrayList<UserNotice>();
		String userInfo = (String) SPUtils.get(mContext,
				Constant.USER_INFO_FILE_NAME, "");
		if (!TextUtils.isEmpty(userInfo)) {
			Gson gson = new Gson();
			userResult = gson.fromJson(userInfo, LoginUserResult.class);
			if (userResult.p.ernie != null) {

				if (userResult.p.ernie.ernieBegintime > 0) {
					ernieBegintime = userResult.p.ernie.ernieBegintime;
					ernieEndtime = userResult.p.ernie.ernieEndtime;
					joinBegintime = userResult.p.ernie.joinBegintime;
					joinEndtime = userResult.p.ernie.joinEndtime;
					enroll = userResult.p.enroll;
					time = userResult.p.nowTime;
					mHandler.removeMessages(TIMER_CODE);
					mHandler.removeCallbacks(timeRunnable);
					Message timeMessage = Message.obtain();
					timeMessage.what = TIMER_CODE;
					mHandler.sendMessage(timeMessage);
				}
			}
		} else {
			finish();
		}
	}

	private void initData() {
		iv_s1.setOnClickListener(clickListener);
		iv_s2.setOnClickListener(clickListener);
		iv_s3.setOnClickListener(clickListener);
		iv_s4.setOnClickListener(clickListener);
		iv_s5.setOnClickListener(clickListener);
		iv_s6.setOnClickListener(clickListener);
		iv_s7.setOnClickListener(clickListener);
		iv_s8.setOnClickListener(clickListener);
		iv_s9.setOnClickListener(clickListener);
		iv_s10.setOnClickListener(clickListener);
		count = 0;
		myAni.myAnimation((Object) s1, 0f, -45f, 1f, 0.729f);
		myAni.myAnimation((Object) s2, 0f, -30f, 1f, 0.81f);
		myAni.myAnimation((Object) s3, 0f, -15f, 1f, 0.9f);
		myAni.myAnimation((Object) s5, 0f, 15f, 1f, 0.9f);
		myAni.myAnimation((Object) s6, 0f, 30f, 1f, 0.81f);
		myAni.myAnimation((Object) s7, 0f, 45f, 1f, 0.729f);
		myAni.myAnimation((Object) s8, 0f, 60f, 1f, 0.729f);
		myAni.myAnimation((Object) s9, 0f, 60f, 1f, 0.729f);
		myAni.myAnimation((Object) s10, 0f, 60f, 1f, 0.729f);
		vList = new ArrayList<RelativeLayout>();
		vList.add(s1);
		vList.add(s2);
		vList.add(s3);
		vList.add(s4);
		vList.add(s5);
		vList.add(s6);
		vList.add(s7);
		vList.add(s8);
		vList.add(s9);
		vList.add(s10);
		size = vList.size();
	}

	/**
	 * 向左滑动
	 */
	protected void leftShift() {
		AnimationSet aniSet = new AnimationSet(true);
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get(count % size), -45f, -315f, 0.6515f, 0.6515f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 1) % size), -30f, -45f, 0.81f,
				0.729f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 2) % size), -15f, -30f, 0.90f,
				0.81f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 3) % size), 0f, -15f, 1f, 0.9f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 4) % size), 15f, 0f, 0.9f, 1f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 5) % size), 30f, 15f, 0.81f, 0.9f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 6) % size), 45f, 30f, 0.729f,
				0.729f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 7) % size), 60f, 60f, 0.6515f,
				0.729f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 8) % size), 100f, 80f, 0.5f, 0.5f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 9) % size), 100f, 80f, 0.5f, 0.5f));
		aniSet.start();
		vList.get((count + 3) % size).isClickable();
		count++;
		exchangeCentreTitle();
	}

	/**
	 * 向右滑动
	 */
	protected void rightShift() {
		AnimationSet aniSet = new AnimationSet(true);
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get(count % size), -45f, -30f, 0.729f, 0.81f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 1) % size), -30f, -15f, 0.81f, 0.9f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 2) % size), -15f, 0f, 0.9f, 1f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 3) % size), 0f, 15f, 1f, 0.9f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 4) % size), 15f, 30f, 0.9f, 0.81f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 5) % size), 30f, 45f, 0.81f, 0.729f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 6) % size), 45f, 315f, 0.729f,
				0.729f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 7) % size), 60f, 60f, 0.6515f,
				0.6515f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 8) % size), 75f, 75f, 0.6515f,
				0.6515f));
		aniSet.addAnimation((Animation) myAni.myAnimation(
				(Object) vList.get((count + 9) % size), 90f, 90f, 0.6515f,
				0.6515f));
		aniSet.start();
		count = count + 9;
		exchangeCentreTitle();
	}

	/**
	 * 这是转轮盘修改 显示的方法
	 */
	private void exchangeCentreTitle() {
		switch ((count + 3) % size) {
		case 0:
			// tv_centre_title.setText("我 要 兑 换");
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s1.setVisibility(View.INVISIBLE);
			iv_s1.setVisibility(View.VISIBLE);
			tv_s2.setVisibility(View.VISIBLE);
			iv_s2.setVisibility(View.INVISIBLE);
			tv_s10.setVisibility(View.VISIBLE);
			iv_s10.setVisibility(View.INVISIBLE);
			iv_s8_2.setVisibility(View.INVISIBLE);
			break;
		case 1:
			// tv_centre_title.setText("处 理 任 务");
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s2.setVisibility(View.INVISIBLE);
			iv_s2.setVisibility(View.VISIBLE);
			tv_s1.setVisibility(View.VISIBLE);
			iv_s1.setVisibility(View.INVISIBLE);
			tv_s3.setVisibility(View.VISIBLE);
			iv_s3.setVisibility(View.INVISIBLE);
			break;
		case 2:
			// tv_centre_title.setText("我 要 赚 钱");
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s3.setVisibility(View.INVISIBLE);
			iv_s3.setVisibility(View.VISIBLE);
			tv_s2.setVisibility(View.VISIBLE);
			iv_s2.setVisibility(View.INVISIBLE);
			tv_s4.setVisibility(View.VISIBLE);
			iv_s4.setVisibility(View.INVISIBLE);
			break;
		case 3:
			// checkText();
			// tv_count_down.setVisibility(View.VISIBLE);
			tv_s4.setVisibility(View.INVISIBLE);
			iv_s4.setVisibility(View.VISIBLE);
			tv_s3.setVisibility(View.VISIBLE);
			iv_s3.setVisibility(View.INVISIBLE);
			tv_s5.setVisibility(View.VISIBLE);
			iv_s5.setVisibility(View.INVISIBLE);
			break;
		case 4:
			// tv_centre_title.setText("我 要 晒 奖");
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s5.setVisibility(View.INVISIBLE);
			iv_s5.setVisibility(View.VISIBLE);
			tv_s4.setVisibility(View.VISIBLE);
			iv_s4.setVisibility(View.INVISIBLE);
			tv_s6.setVisibility(View.VISIBLE);
			iv_s6.setVisibility(View.INVISIBLE);
			break;
		case 5:
			// tv_centre_title.setText("进 入 邀 请");
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s6.setVisibility(View.INVISIBLE);
			iv_s6.setVisibility(View.VISIBLE);
			tv_s5.setVisibility(View.VISIBLE);
			iv_s5.setVisibility(View.INVISIBLE);
			tv_s7.setVisibility(View.VISIBLE);
			iv_s7.setVisibility(View.INVISIBLE);
			break;
		case 6:
			// tv_centre_title.setText("我 的 账 号");
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s7.setVisibility(View.INVISIBLE);
			iv_s7.setVisibility(View.VISIBLE);
			tv_s6.setVisibility(View.VISIBLE);
			iv_s6.setVisibility(View.INVISIBLE);
			tv_s8.setVisibility(View.VISIBLE);
			iv_s8.setVisibility(View.INVISIBLE);

			break;
		case 7:
			// tv_centre_title.setText("一元拍");
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s8.setVisibility(View.INVISIBLE);
			iv_s8.setVisibility(View.VISIBLE);

			tv_s7.setVisibility(View.VISIBLE);
			iv_s7.setVisibility(View.INVISIBLE);
			tv_s9.setVisibility(View.VISIBLE);
			iv_s9.setVisibility(View.INVISIBLE);
			break;
		case 8:
			// tv_centre_title.setText(getResources()
			// .getString(R.string.earn_gold));
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s9.setVisibility(View.INVISIBLE);
			iv_s9.setVisibility(View.VISIBLE);
			tv_s8.setVisibility(View.VISIBLE);
			iv_s8.setVisibility(View.INVISIBLE);
			tv_s10.setVisibility(View.VISIBLE);
			iv_s10.setVisibility(View.INVISIBLE);
			iv_s8_2.setVisibility(View.INVISIBLE);
			break;
		case 9:
			// tv_centre_title.setText(getResources().getString(
			// R.string.earn_integral));
			// tv_count_down.setVisibility(View.INVISIBLE);
			tv_s10.setVisibility(View.INVISIBLE);
			iv_s10.setVisibility(View.VISIBLE);
			iv_s8_2.setVisibility(View.VISIBLE);
			tv_s9.setVisibility(View.VISIBLE);
			iv_s9.setVisibility(View.INVISIBLE);
			tv_s1.setVisibility(View.VISIBLE);
			iv_s1.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 跳转界面操作在这里执行，对号入座
	 */
	protected void itemClick() {

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.iv_s1:
				// 兑换商城
				intent = new Intent(mContext, ExchangemallActivity.class);
				break;
			case R.id.iv_s2:
				// 赚取金币
				intent = new Intent(mContext, Main1Activity.class);
				break;
			case R.id.iv_s3:
				// 赚取拍币
				intent = new Intent(mContext, EarnPointsActivity.class);
				break;
			case R.id.iv_s4:
				// 我的任务
				intent = new Intent(mContext, TaskActivity.class);
				break;
			case R.id.iv_s5:
				// 开心摇奖
				if (getenter_for_state) {
					intent = new Intent(mContext, ShowPriceActivity.class);
					intent.putExtra("success", true);
				} else {
					intent = new Intent(mContext, ShowPriceActivity.class);
				}
				break;
			case R.id.iv_s6:
				// 一元摇奖
				intent = new Intent(mContext, OneRmbErnieActivity.class);
				break;
			case R.id.iv_s7:
				// 用户晒奖
				intent = new Intent(mContext, ShinePrizeActivity.class);
				break;
			case R.id.iv_s8:
				// 我的好友
				// intent = new Intent(mContext, FriendActivity.class);
				break;
			case R.id.iv_s9:
				// 个人中心
				intent = new Intent(mContext, MyInfomationActivity.class);
				break;
			case R.id.iv_s10:
				if (userResult != null && userResult.p.user != null
						&& userResult.p.user.type != 0) {
					intent = new Intent(mContext, MerchantAccountActivity.class);
				} else {
					alertDialog = DialogUtils.merchantDialog(HomeActivity.this,
							this);
				}
				break;
			case R.id.btn_yes:
				intent = new Intent(mContext, MyInfomationActivity.class);
				alertDialog.dismiss();
				break;
			case R.id.btn_no:
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				break;
			}
			if (intent == null) {
				return;
			}
			startActivity(intent);
		}
	};

	/**
	 * 获取最新消息
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// TODO
		// onRefresh();
		tempList.clear();
		ArrayList<UserNotice> list = GetUserIdUtil.getList(mContext);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				tempList.add(list.get(i));
			}
		}
		getenter_for_state = (Boolean) SPUtils.get(mContext,
				Constant.GETENTER_FOR_STATE, false);
		gson = new Gson();
		RequestNews requestNews = new RequestNews();
		requestNews.p.userId = GetUserIdUtil.getUserId(mContext);
		requestNews.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		String request = gson.toJson(requestNews);
		HttpConnectTool.update(request, false, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				ResultNews resultNews = gson.fromJson(result, ResultNews.class);
				if (resultNews.p.isTrue) {
					if (resultNews.p.userNoticeList != null) {

						if (resultNews.p.userNoticeList.size() > 0) {
							for (int i = 0; i < resultNews.p.userNoticeList
									.size(); i++) {
								tempList.add(resultNews.p.userNoticeList.get(i));
							}
							GetUserIdUtil.saveList(mContext, tempList);
						}
						int tempCount = 0;
						for (int i = 0; i < tempList.size(); i++) {
							if (tempList.get(i).state == 1) {
								tempCount++;
							}
							GetUserIdUtil.saveList(mContext, tempList);
						}
						if (tempCount > 0) {
							tv_news_number.setVisibility(View.VISIBLE);
							tv_news_number.setText("" + tempCount);
						} else {
							tv_news_number.setVisibility(View.INVISIBLE);
							tv_news_number.setText("");
						}
					}
				} else {
					IntentActivity.mIntent(mContext);
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

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			// if (startX == 0) {
			// startX = lastStartX;
			// }
			// endX = ev.getX();
			// if (Math.abs(endX - startX) > 100) {
			// moveX = endX - startX;
			// lastStartX = endX;
			// moveX = lastStartX - startX;
			// }

			break;
		case MotionEvent.ACTION_UP:
			endX = ev.getX();
			moveX = endX - startX;
			break;
		}

		if (moveX > 50) {
			rightShift();
		} else if (moveX < -50) {
			leftShift();
		} else {
			return false;
		}
		startX = 0;
		endX = 0;
		moveX = 0;
		return super.onTouchEvent(ev);
	}

	/**
	 * 轮播图
	 */
	private void initAutoViewPager() {
		home_pager = (ViewPager) findViewById(R.id.vp_home_pager);
		if (userResult.p.bannerList.size() <= 0) {

			return;
		}

		ll_home_pager_point = (LinearLayout) findViewById(R.id.ll_home_pager_point);
		ll_home_pager_point.removeAllViews();
		View pointView;
		LayoutParams params;
		for (int i = 0; i < userResult.p.bannerList.size(); i++) {
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
			ll_home_pager_point.addView(pointView);
		}
		selectedPosition = 0;
		ll_home_pager_point.getChildAt(selectedPosition).setEnabled(true);
		home_pager.setAdapter(new MyPagerAdapter());
		if (mAdapter == null) {
			mAdapter = new MyPagerAdapter();
			home_pager.setAdapter(mAdapter);
			home_pager
					.setCurrentItem(userResult.p.bannerList.indexOf(0) * 1000);
			home_pager.setOnPageChangeListener(new MyOnPageChangeListener());
		} else {
			mAdapter.notifyDataSetChanged();
		}

		mHandler.removeCallbacks(playRunnable);
		mHandler.removeMessages(BANNER_CODE);
		mHandler.postDelayed(new AutoPlayRunnable(), 3000);

	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
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
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);

			ImageLoaderUtil
					.loadImage(
							userResult.p.bannerList.get(position
									% userResult.p.bannerList.size()).imgUrl,
							imageView);

			imageView.setOnTouchListener(new PagerTouchListener(
					userResult.p.bannerList.get(position
							% userResult.p.bannerList.size()).linkUrl));
			container.addView(imageView);
			return imageView;
		}

	}

	private class AutoHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BANNER_CODE:
				if (userResult.p.bannerList.size() > 1) {
					int currentItem = home_pager.getCurrentItem() + 1;
					home_pager.setCurrentItem(currentItem);
				}
				// rotate.start();
				playRunnable = new AutoPlayRunnable();
				postDelayed(playRunnable, 3000);
				break;
			case TIMER_CODE:
				time += 1000;
				Constant.time = time;
				Intent intent = new Intent();
				if (time / 1000 == ernieBegintime / 1000) {
					intent.setAction(Constant.ernieBegintime);
					sendBroadcast(intent);
				} else if (time / 1000 == ernieEndtime / 1000) {
					intent.setAction(Constant.ernieEndtime);
					sendBroadcast(intent);
				} else if (time / 1000 == joinBegintime / 1000) {
					intent.setAction(Constant.joinBegintime);
					sendBroadcast(intent);
				} else if (time / 1000 == joinEndtime / 1000) {
					intent.setAction(Constant.joinEndtime);
					sendBroadcast(intent);
				}
				timeRunnable = new AutoTimeRunnable();
				postDelayed(timeRunnable, 1000);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 轮播图定时器
	 */
	private class AutoPlayRunnable implements Runnable {

		@Override
		public void run() {
			Message message = mHandler.obtainMessage();
			message.what = BANNER_CODE;
			mHandler.sendMessage(message);
		}
	}

	/**
	 * 摇奖时间定时器
	 */
	private class AutoTimeRunnable implements Runnable {

		@Override
		public void run() {
			Message message = mHandler.obtainMessage();
			message.what = TIMER_CODE;
			mHandler.sendMessage(message);
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			ll_home_pager_point.getChildAt(selectedPosition).setEnabled(false);
			ll_home_pager_point.getChildAt(
					position % userResult.p.bannerList.size()).setEnabled(true);
			selectedPosition = position % userResult.p.bannerList.size();
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}

	private class PagerTouchListener implements OnTouchListener {

		private int downX;
		private int downY;
		private long downTime;
		private String mLinkUrl;

		public PagerTouchListener(String linkUrl) {
			mLinkUrl = linkUrl;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mHandler.removeCallbacks(playRunnable);
				mHandler.removeMessages(BANNER_CODE);
				downX = (int) event.getX();
				downY = (int) event.getY();
				downTime = System.currentTimeMillis();
				break;
			case MotionEvent.ACTION_CANCEL:
				mHandler.postDelayed(new AutoPlayRunnable(), 3000);
				break;
			case MotionEvent.ACTION_UP:
				mHandler.postDelayed(new AutoPlayRunnable(), 3000);
				int upX = (int) event.getX();
				int upY = (int) event.getY();

				if (downX == upX && downY == upY) {
					long upTime = System.currentTimeMillis();

					long time = upTime - downTime;
					if (time < 500) {
						pagerClickListener(v, mLinkUrl);
					}
				}
				break;
			default:
				break;
			}
			return true;
		}
	}

	/**
	 * viewpager item 点击事件
	 * 
	 * @param v
	 *            对应item
	 */
	public void pagerClickListener(View v, final String mLinkUrl) {

		Intent intent = new Intent(mContext, BannerDesActivity.class);
		intent.putExtra("LinkUrl", mLinkUrl);
		startActivity(intent);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		if (userResult.p.bannerList.size() > 0) {
			mHandler.removeCallbacksAndMessages(null);
		}
		super.onDestroy();
	}

}
