package com.jishijiyu.takeadvantage.activity.ernie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.paymoney.PayOrderActivity;
import com.jishijiyu.takeadvantage.activity.paymoney.PayOrderSKActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;

/**
 * 
 * 兑换锁
 * 
 */
public class LocksActivity extends HeadBaseActivity {
	/**
	 * 分享币个数
	 */
	private TextView tv_share_num;
	/**
	 * 金锁个数
	 */
	private TextView tv_gold_lock_num;
	/**
	 * 铜锁
	 */
	private Button btn_gold_lock;
	/**
	 * 银锁个数
	 */
	private TextView tv_silver_lock_num;
	/**
	 * 铜锁
	 */
	private Button btn_silver_lock;
	/**
	 * 铜锁个数
	 */
	private TextView tv_bronze_lock_num;
	/**
	 * 铜锁
	 */
	private Button btn_bronze_lock;

	// private AlertDialog alertDialog = null;
	// private int num = 1;
	// private int inviteUserNum = 0;
	// private Gson gson = null;
	private int goldNum = 0;
	private int goldLockNum = 0;
	private int silverLockNum = 0;
	private int copperLockNum = 0;

	// private int locktype = 0;
	// private int exchangetype = 0;
	// private static final int NUM_ONE = 1;// 兑换数量初始化
	// private static final int GOLD_LOCK = 1;// 金锁
	// private static final int SILVER_LOCK = 2;// 银锁
	// private static final int COPPER_LOCK = 3;// 铜锁
	// private static final int INVITE_EXCHANGE = 1;// 邀请人数兑换
	// private static final int GOLD_EXCHANGE = 2;// 金币兑换
	// private static final int INVITE_NUM = 4;// 邀请人数换金锁
	// private static final int GOLD_NUM = 50;// 50金币换金锁
	// private static final int SILVER_NUM = 30;// 30金币换银锁
	// private static final int COPPER_NUM = 10;// 10金币换铜锁

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.lock_top));
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(LocksActivity.this, R.layout.lock, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		goldNum = GetUserIdUtil.goldNum(this);
		goldLockNum = GetUserIdUtil.goldLockNum(this);
		silverLockNum = GetUserIdUtil.silverLockNum(this);
		copperLockNum = GetUserIdUtil.copperLockNum(this);
		initView(view);
		initData();
		initOnclick();
	}

	@Override
	protected void onResume() {
		initReplaceView();
		super.onResume();

	}

	public void initView(View view) {
		tv_share_num = (TextView) view.findViewById(R.id.tv_share_num);
		tv_gold_lock_num = (TextView) view.findViewById(R.id.tv_gold_lock_num);
		tv_silver_lock_num = (TextView) view
				.findViewById(R.id.tv_silver_lock_num);
		tv_bronze_lock_num = (TextView) view
				.findViewById(R.id.tv_bronze_lock_num);
		btn_gold_lock = (Button) view.findViewById(R.id.btn_gold_lock);
		btn_silver_lock = (Button) view.findViewById(R.id.btn_silver_lock);
		btn_bronze_lock = (Button) view.findViewById(R.id.btn_bronze_lock);

	}

	public void initOnclick() {
		btn_gold_lock.setOnClickListener(this);
		btn_silver_lock.setOnClickListener(this);
		btn_bronze_lock.setOnClickListener(this);
	}

	public void initData() {
		tv_share_num.setText(goldNum + "");
		tv_gold_lock_num.setText(goldLockNum + "");
		tv_silver_lock_num.setText(silverLockNum + "");
		tv_bronze_lock_num.setText(copperLockNum + "");
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		Bundle bundle = null;
		// GoldLocksRequest request = null;
		// TextView tv_lock_invitaion_num = null;
		// TextView edt_lock_num = null;
		// TextView tv_lock_num = null;
		// TextView tv_text = null;
		// TextView tv_text2 = null;
		// TextView tv_lock_rule = null;
		// Button btn_gold = null;
		// Button btn_people = null;
		// Button btn_gold_conversion = null;
		// Button btn_invitation_conversion = null;
		// String s = null;
		// String n = null;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(LocksActivity.this);
			break;
		case R.id.btn_gold_lock:
			// alertDialog = DialogUtils.showLockDialog(LocksActivity.this,
			// inviteUserNum, goldNum, this);
			intent = new Intent(mContext, PayOrderActivity.class);
			startActivity(intent);
			// locktype = GOLD_LOCK;
			// exchangetype = INVITE_EXCHANGE;
			break;
		case R.id.btn_silver_lock:
			// alertDialog = DialogUtils.showLockDialog2(LocksActivity.this,
			// goldNum, this);
			// locktype = SILVER_LOCK;
			// exchangetype = GOLD_EXCHANGE;
			bundle = new Bundle();
			bundle.putString("lock", "2");
			intent = new Intent(mContext, PayOrderSKActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.btn_bronze_lock:
			// alertDialog = DialogUtils.showLockDialog3(LocksActivity.this,
			// goldNum, this);
			// locktype = COPPER_LOCK;
			// exchangetype = GOLD_EXCHANGE;
			bundle = new Bundle();
			bundle.putString("lock", "3");
			intent = new Intent(mContext, PayOrderSKActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		// case R.id.btn_add:
		// btn_gold_conversion = (Button) alertDialog
		// .findViewById(R.id.btn_gold_conversion);
		// tv_lock_num = (TextView) alertDialog.findViewById(R.id.tv_lock_num);
		// edt_lock_num = (TextView) alertDialog
		// .findViewById(R.id.edt_lock_num);
		// s = edt_lock_num.getText().toString().trim();
		// n = tv_lock_num.getText().toString().trim();
		// if (!TextUtils.isEmpty(s)) {
		// num = Integer.parseInt(s);
		// num += 1;
		// edt_lock_num.setText(num + "");
		// }
		// break;
		// case R.id.btn_del:
		// btn_gold_conversion = (Button) alertDialog
		// .findViewById(R.id.btn_gold_conversion);
		// tv_lock_num = (TextView) alertDialog.findViewById(R.id.tv_lock_num);
		// edt_lock_num = (TextView) alertDialog
		// .findViewById(R.id.edt_lock_num);
		// s = edt_lock_num.getText().toString().trim();
		// n = tv_lock_num.getText().toString().trim();
		// if (!TextUtils.isEmpty(s)) {
		// num = Integer.parseInt(s);
		// if (num > NUM_ONE) {
		// num -= 1;
		// }
		// edt_lock_num.setText(num + "");
		// }
		// break;
		// case R.id.btn_gold_conversion:
		// tv_lock_num = (TextView) alertDialog.findViewById(R.id.tv_lock_num);
		// edt_lock_num = (TextView) alertDialog
		// .findViewById(R.id.edt_lock_num);
		// n = tv_lock_num.getText().toString().trim();
		// s = edt_lock_num.getText().toString().trim();
		// if (!TextUtils.isEmpty(s)) {
		// num = Integer.parseInt(s);
		// if (Integer.parseInt(s) <= Integer.parseInt(n)) {
		// if (exchangetype == INVITE_EXCHANGE) {
		// invitation_conversion();
		// } else if (exchangetype == GOLD_EXCHANGE) {
		// gold_conversion();
		// }
		// if (alertDialog != null) {
		// alertDialog.dismiss();
		// }
		// } else {
		// if (exchangetype == INVITE_EXCHANGE) {
		// ToastUtils.makeText(LocksActivity.this, "邀请人数不足！",
		// ToastUtils.LENGTH_SHORT).show();
		// } else if (exchangetype == GOLD_EXCHANGE) {
		// ToastUtils.makeText(LocksActivity.this, "金币数量不足！",
		// ToastUtils.LENGTH_SHORT).show();
		// }
		// if (alertDialog != null) {
		// alertDialog.dismiss();
		// }
		// }
		// }
		// break;
		// case R.id.btn_exit:
		// if (alertDialog != null) {
		// alertDialog.dismiss();
		// }
		// break;
		// case R.id.btn_people:
		// tv_lock_invitaion_num = (TextView) alertDialog
		// .findViewById(R.id.tv_lock_invitaion_num);
		// edt_lock_num = (TextView) alertDialog
		// .findViewById(R.id.edt_lock_num);
		// tv_lock_num = (TextView) alertDialog.findViewById(R.id.tv_lock_num);
		// tv_text = (TextView) alertDialog.findViewById(R.id.tv_text);
		// tv_text2 = (TextView) alertDialog.findViewById(R.id.tv_text2);
		// tv_lock_rule = (TextView) alertDialog
		// .findViewById(R.id.tv_lock_rule);
		// btn_gold = (Button) alertDialog.findViewById(R.id.btn_gold);
		// btn_people = (Button) alertDialog.findViewById(R.id.btn_people);
		// btn_gold_conversion = (Button) alertDialog
		// .findViewById(R.id.btn_gold_conversion);
		// tv_text.setText("当前邀请好友");
		// tv_text2.setText("人");
		// tv_lock_rule.setText("把(" + INVITE_NUM + "人=1把)");
		// btn_gold_conversion.setText("邀请兑换");
		// tv_lock_invitaion_num.setText(inviteUserNum + "");
		// if (inviteUserNum > 0) {
		// tv_lock_num.setText(inviteUserNum / INVITE_NUM + "");
		// } else {
		// tv_lock_num.setText("0");
		// }
		// btn_people.setBackgroundResource(R.drawable.btn_headabove_light);
		// btn_gold.setBackgroundResource(R.drawable.btn_headabove_dark);
		// exchangetype = INVITE_EXCHANGE;
		// num = NUM_ONE;
		// edt_lock_num.setText(num + "");
		// break;
		// case R.id.btn_gold:
		// tv_lock_invitaion_num = (TextView) alertDialog
		// .findViewById(R.id.tv_lock_invitaion_num);
		// edt_lock_num = (TextView) alertDialog
		// .findViewById(R.id.edt_lock_num);
		// tv_lock_num = (TextView) alertDialog.findViewById(R.id.tv_lock_num);
		// tv_text = (TextView) alertDialog.findViewById(R.id.tv_text);
		// tv_text2 = (TextView) alertDialog.findViewById(R.id.tv_text2);
		// tv_lock_rule = (TextView) alertDialog
		// .findViewById(R.id.tv_lock_rule);
		// btn_gold = (Button) alertDialog.findViewById(R.id.btn_gold);
		// btn_people = (Button) alertDialog.findViewById(R.id.btn_people);
		// btn_gold_conversion = (Button) alertDialog
		// .findViewById(R.id.btn_gold_conversion);
		// tv_text.setText("当前剩余金币");
		// tv_text2.setText("金币");
		// tv_lock_rule.setText("把(" + GOLD_NUM + "人=1把)");
		// btn_gold_conversion.setText("金币兑换");
		// tv_lock_invitaion_num.setText(goldNum + "");
		// if (goldNum > 0) {
		// tv_lock_num.setText(goldNum / GOLD_NUM + "");
		// } else {
		// tv_lock_num.setText("0");
		// }
		// btn_people.setBackgroundResource(R.drawable.btn_headabove_dark);
		// btn_gold.setBackgroundResource(R.drawable.btn_headabove_light);
		// exchangetype = GOLD_EXCHANGE;
		// num = NUM_ONE;
		// edt_lock_num.setText(num + "");
		// break;
		}
	}

	// /**
	// * 推荐人数兑换
	// *
	// * @author zhaobin
	// * @return void
	// * @throws
	// */
	// public void invitation_conversion() {
	// gson = new Gson();
	// InvitationLocksRequest invitationLocksRequest = new
	// InvitationLocksRequest();
	// InvitationLocksRequest.Pramater pramater = invitationLocksRequest.p;
	// pramater.userId = GetUserIdUtil.getUserId(this);
	// pramater.tokenId = GetUserIdUtil.getTokenId(this);
	// pramater.num = num + "";
	// String json = gson.toJson(invitationLocksRequest);
	// HttpConnectTool.update(json, this, new ConnectListener() {
	//
	// @Override
	// public void contectSuccess(String result) {
	// if (!TextUtils.isEmpty(result)) {
	// InvitationLocksResult invitationLocksResult = gson
	// .fromJson(result, InvitationLocksResult.class);
	// if (invitationLocksResult != null) {
	// if (invitationLocksResult.p.isTrue) {
	// if (invitationLocksResult.p.isSucce) {
	// if (alertDialog != null) {
	// alertDialog.dismiss();
	// }
	// ToastUtils.makeText(LocksActivity.this,
	// "兑换成功！", ToastUtils.LENGTH_SHORT)
	// .show();
	// goldLockNum = goldLockNum + num;
	// inviteUserNum = inviteUserNum - num
	// * INVITE_NUM;
	// LoginUserResult loginUserResult = GetUserIdUtil
	// .getLogin(LocksActivity.this);
	// loginUserResult.p.user.goldLockNum = goldLockNum;
	// loginUserResult.p.user.inviteUserNum = inviteUserNum;
	//
	// GetUserIdUtil.saveLoginUserResult(
	// LocksActivity.this, loginUserResult);
	// initOnclick();
	//
	// } else {
	// ToastUtils.makeText(LocksActivity.this,
	// "兑换失败！", ToastUtils.LENGTH_SHORT)
	// .show();
	// }
	// } else {
	// IntentActivity.mIntent(LocksActivity.this);
	// }
	// } else {
	// ToastUtils.makeText(LocksActivity.this, "兑换失败！",
	// ToastUtils.LENGTH_SHORT).show();
	// }
	// } else {
	// ToastUtils.makeText(LocksActivity.this, "兑换失败！",
	// ToastUtils.LENGTH_SHORT).show();
	// }
	// }
	//
	// @Override
	// public void contectStarted() {
	//
	// }
	//
	// @Override
	// public void contectFailed(String path, String request) {
	// ToastUtils.makeText(LocksActivity.this, "兑换失败！",
	// ToastUtils.LENGTH_SHORT).show();
	// }
	// });
	// }

	// /**
	// * 金币兑换锁
	// *
	// * @author sunaoyang
	// * @return void
	// * @throws
	// */
	// public void gold_conversion() {
	// gson = new Gson();
	// GoldLocksRequest goldLocksRequest = new GoldLocksRequest();
	// GoldLocksRequest.Pramater pramater = goldLocksRequest.p;
	// pramater.userId = GetUserIdUtil.getUserId(this);
	// pramater.tokenId = GetUserIdUtil.getTokenId(this);
	// pramater.lock = locktype + "";
	// pramater.num = num + "";
	// String json = gson.toJson(goldLocksRequest);
	// HttpConnectTool.update(json, this, new ConnectListener() {
	//
	// @Override
	// public void contectSuccess(String result) {
	// if (!TextUtils.isEmpty(result)) {
	// GoldLocksResult goldLocksResult = gson.fromJson(result,
	// GoldLocksResult.class);
	// if (goldLocksResult != null) {
	// if (goldLocksResult.p.isTrue) {
	// if (goldLocksResult.p.isSucce) {
	// if (alertDialog != null) {
	// alertDialog.dismiss();
	// }
	// ToastUtils.makeText(LocksActivity.this,
	// "兑换成功！", ToastUtils.LENGTH_SHORT)
	// .show();
	// if (locktype == GOLD_LOCK) {
	// goldLockNum = goldLockNum + num;
	// goldNum = goldNum - num * GOLD_NUM;
	// } else if (locktype == SILVER_LOCK) {
	// silverLockNum = silverLockNum + num;
	// goldNum = goldNum - num * SILVER_NUM;
	// } else if (locktype == COPPER_LOCK) {
	// copperLockNum = copperLockNum + num;
	// goldNum = goldNum - num * COPPER_NUM;
	// }
	//
	// LoginUserResult loginUserResult = GetUserIdUtil
	// .getLogin(LocksActivity.this);
	// loginUserResult.p.user.goldLockNum = goldLockNum;
	// loginUserResult.p.user.silverLockNum = silverLockNum;
	// loginUserResult.p.user.copperLockNum = copperLockNum;
	// loginUserResult.p.user.goldNum = goldNum;
	//
	// GetUserIdUtil.saveLoginUserResult(
	// LocksActivity.this, loginUserResult);
	// initOnclick();
	//
	// } else {
	// ToastUtils.makeText(LocksActivity.this,
	// "兑换失败！", ToastUtils.LENGTH_SHORT)
	// .show();
	// }
	// } else {
	// IntentActivity.mIntent(LocksActivity.this);
	// }
	// } else {
	// ToastUtils.makeText(LocksActivity.this, "兑换失败！",
	// ToastUtils.LENGTH_SHORT).show();
	// }
	// } else {
	// ToastUtils.makeText(LocksActivity.this, "兑换失败！",
	// ToastUtils.LENGTH_SHORT).show();
	// }
	// }
	//
	// @Override
	// public void contectStarted() {
	//
	// }
	//
	// @Override
	// public void contectFailed(String path, String request) {
	// ToastUtils.makeText(LocksActivity.this, "兑换失败！",
	// ToastUtils.LENGTH_SHORT).show();
	// }
	// });
	// }

	// public GoldLocksRequest getGoldLocksRequest(String lock, int num) {
	// GoldLocksRequest goldLocksRequest = new GoldLocksRequest();
	// goldLocksRequest.p.tokenId = GetUserIdUtil.getTokenId(this);
	// goldLocksRequest.p.userId = GetUserIdUtil.getUserId(this);
	// goldLocksRequest.p.lock = lock;
	// goldLocksRequest.p.num = num + "";
	// return goldLocksRequest;
	// }
}
