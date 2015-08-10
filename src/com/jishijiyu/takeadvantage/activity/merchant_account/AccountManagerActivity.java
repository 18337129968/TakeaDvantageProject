package com.jishijiyu.takeadvantage.activity.merchant_account;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.myinformation.DiscussPresentActivity;
import com.jishijiyu.takeadvantage.entity.request.MerchanAccountRequest;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.MerchanAccountResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.RechargeDialogUtil;

/**
 * 账户管理
 * 
 * @author shifeiyu
 * 
 */
public class AccountManagerActivity extends HeadBaseActivity {
	TextView recharge_btn, withdrawals_btn;
	public static final int SUCCESS = 0;
	public static final int FAIL = 1;
	public Message msg;
	public String balance = null;// 账户余额
	public String consume = null;// 累计消费
	public String fetchout = null;// 累计提现
	public String supply = null;// 累计消费
	AlertDialog dialog;
	//
	public TextView tv_balance, tv_consume, tv_fetchout, tv_supply;
	public MerchanAccountResult merchanAccountResult;
	private int dataNum = 0;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				break;
			case FAIL:
				ToastUtils.makeText(AccountManagerActivity.this, "访问服务器失败!", 0)
						.show();
				break;
			default:
				break;
			}

		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.recharge_btn:
			// 充值
			dialog = RechargeDialogUtil.showRechargeDialog1(mContext, 2,
					balance, AccountManagerActivity.this);
			break;
		case R.id.dialog_ok_btn1:
			EditText et_money = (EditText) dialog.findViewById(R.id.et_money1);

			if (et_money.length() <= 0) {
				ToastUtils.makeText(AccountManagerActivity.this, "请输入充值金额!", 0)
						.show();
			} else {
				dataNum = Integer.parseInt(et_money.getText().toString());
				if (dataNum != 0) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					intent.setClass(AccountManagerActivity.this,
							MentionActivity.class);
					bundle.putString("money", et_money.getText().toString());
					intent.putExtras(bundle);
					startActivity(intent);
					dialog.dismiss();
				} else {
					dialog.dismiss();
					ToastUtils.makeText(AccountManagerActivity.this,
							"充值金额必须大于0!", 0).show();

				}
			}
			break;
		case R.id.dialog_cancel_btn1:
			dialog.dismiss();
			break;
		case R.id.withdrawals_btn:
			Intent i = new Intent(AccountManagerActivity.this,
					MentionActivity.class);
			startActivity(i);
			// 提现
			// 获取当前的金币数量
			// goldNum = login.p.user.goldNum;
			/*
			 * dialog = RechargeDialogUtil.showRechargeDialog(mContext, 1,
			 * balance, AccountManagerActivity.this);
			 */
			break;
		case R.id.dialog_ok_btn:
			EditText et_money1 = (EditText) dialog.findViewById(R.id.et_money);
			int balan = (new Double(balance)).intValue();
			int num = Integer.parseInt(et_money1.getText().toString());
			if (num > balan) {
				ToastUtils.makeText(AccountManagerActivity.this,
						"剩余金额不足，请重新输入!", 0).show();
				dialog.dismiss();
			} else {
				if (et_money1.length() <= 0) {
					ToastUtils.makeText(AccountManagerActivity.this,
							"请输入提现金额!", 0).show();
				} else {
					dataNum = Integer.parseInt(et_money1.getText().toString());
					if (dataNum != 0
							&& et_money1.getText().toString().length() > 0) {
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						intent.setClass(AccountManagerActivity.this,
								DiscussPresentActivity.class);
						bundle.putString("num", et_money1.getText().toString());
						intent.putExtras(bundle);
						startActivity(intent);
						dialog.dismiss();
						AccountManagerActivity.this.finish();
					} else {
						dialog.dismiss();
						ToastUtils.makeText(AccountManagerActivity.this,
								"请输入提现金额！", 0).show();

					}
				}
			}
			break;
		case R.id.dialog_cancel_btn:
			dialog.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		top_text.setText("账户管理");
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AccountManagerActivity.this,
				R.layout.activity_account_manage, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initView();
		initClick();
		// 查询显示金额
		MerchantAccount();
	}

	private void MerchantAccount() {
		// TODO Auto-generated method stub
		MerchanAccountRequest merchanAccountRequest = new MerchanAccountRequest();
		MerchanAccountRequest.Pramater pramater = merchanAccountRequest.p;
		pramater.userId = GetUserIdUtil.getUserId(AccountManagerActivity.this);
		pramater.tokenId = GetUserIdUtil.getUserId(AccountManagerActivity.this);
		final Gson gson = new Gson();
		String json = gson.toJson(merchanAccountRequest);
		HttpConnectTool.update(json, AccountManagerActivity.this,
				new ConnectListener() {
					@Override
					public void contectSuccess(String result) {
						// TODO Auto-generated method stub
						if (!TextUtils.isEmpty(result)) {
							merchanAccountResult = gson.fromJson(result,
									MerchanAccountResult.class);
							if (merchanAccountResult.p.isTrue) {
								balance = merchanAccountResult.p.account.balance;
								consume = merchanAccountResult.p.account.consume;
								fetchout = merchanAccountResult.p.account.fetchout;
								supply = merchanAccountResult.p.account.supply;
								tv_balance.setText("账户余额：" + balance);
								tv_consume.setText("累计充值总额：" + consume);
								tv_fetchout.setText("广告消费总额：" + fetchout);
								tv_supply.setText("累计提现总额：" + supply);
							} else {
								IntentActivity
										.mIntent(AccountManagerActivity.this);
							}
						}
						msg = new Message();
						msg.what = SUCCESS;
						handler.sendMessage(msg);
					}

					@Override
					public void contectStarted() {
						// TODO Auto-generated method stub

					}

					@Override
					public void contectFailed(String path, String request) {
						// TODO Auto-generated method stub
						msg = new Message();
						msg.what = FAIL;
						handler.sendMessage(msg);
					}
				});
	}

	private void initView() {
		recharge_btn = (TextView) findViewById(R.id.recharge_btn);
		withdrawals_btn = (TextView) findViewById(R.id.withdrawals_btn);
		tv_balance = (TextView) findViewById(R.id.tv_balance);
		tv_consume = (TextView) findViewById(R.id.tv_consume);
		tv_fetchout = (TextView) findViewById(R.id.tv_fetchout);
		tv_supply = (TextView) findViewById(R.id.tv_supply);

	}

	private void initClick() {
		recharge_btn.setOnClickListener(this);
		withdrawals_btn.setOnClickListener(this);
	}
}
