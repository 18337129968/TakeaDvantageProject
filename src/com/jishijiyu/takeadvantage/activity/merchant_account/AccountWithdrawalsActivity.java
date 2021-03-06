package com.jishijiyu.takeadvantage.activity.merchant_account;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.entity.request.QueryGoldNumRequest;
import com.jishijiyu.takeadvantage.entity.request.WithdrawalsRequest;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.QueryGoldNumResult;
import com.jishijiyu.takeadvantage.entity.result.WithdrawalsResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Arith;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

/**
 * 提现
 * 
 * @author zhengjianxiong
 * 
 */
public class AccountWithdrawalsActivity extends HeadBaseActivity {
	private int userGoldNum;
	private QueryGoldNumResult queryGoldNumResult;
	TextView withdrawals_money_text, bank_text, withdrawals_fee,
			withdrawals_now_btn;
	EditText withdrawals_name_edit, withdrawals_cardcode_edit;
	LinearLayout select_bank_layout;
	public int BANK_CODE = -1;
	private String money;
	private Arith arith;
	private Message msg;
	private static final int SUCCESS = 0;
	private static final int FAIL = 1;
	private static final int EXCHANGEGOLDSUCCESS = 2;
	private static final int EXCHANGEGOLDFAIL = 3;
	private double mon;
	private AlertDialog alertDialog = null;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	String brankName = "北京银行";
	// 提现银行
	private List<String> list = new ArrayList<String>();

	private WithdrawalsResult withdrawalsResult;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				// ToastUtils.makeText(AccountWithdrawalsActivity.this,
				// "访问服务器成功!", 0).show();
				break;
			case FAIL:
				ToastUtils.makeText(AccountWithdrawalsActivity.this,
						"访问服务器失败!", 0).show();
				break;
			case EXCHANGEGOLDSUCCESS:
				ToastUtils.makeText(AccountWithdrawalsActivity.this,
						"提现成功!\n提现" + mon + "", 0).show();
				QueryGoldNum();
				// 兑换成功查询金币数量,改变登录的时候给的金币数量
				break;
			case EXCHANGEGOLDFAIL:
				ToastUtils
						.makeText(AccountWithdrawalsActivity.this, "提现失败!", 0)
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
		case R.id.withdrawals_now_btn:
			alertDialog = new DialogUtils().WithdrawalsDialog(
					AccountWithdrawalsActivity.this, this);

			break;
		case R.id.btn_yes:
			String name = withdrawals_name_edit.getText().toString();
			String brankNum = withdrawals_cardcode_edit.getText().toString();
			String brankName = "北京银行";
			if (name.equals("") && name.length() <= 0) {
				ToastUtils.makeText(AccountWithdrawalsActivity.this, "姓名必须填写!",
						0).show();
				alertDialog.dismiss();
			} else if (brankNum.equals("") && brankNum.length() <= 0) {
				ToastUtils.makeText(AccountWithdrawalsActivity.this, "卡号必须填写!",
						0).show();
				alertDialog.dismiss();
			} else {
				Withdrawals(name, brankName, brankNum, mon + "", money);
				alertDialog.dismiss();
				AccountWithdrawalsActivity.this.finish();
			}
			break;
		case R.id.btn_no:
			alertDialog.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText("账户提现");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AccountWithdrawalsActivity.this,
				R.layout.activity_account_withdrawals, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			money = bundle.getString("num");
			mon = arith.div(Double.parseDouble(money),
					Double.parseDouble("100"));
			int dataNum = Integer.parseInt(money);
		}

		initView();
		DecimalFormat decimal = new DecimalFormat("0.00");
		Intent intent = getIntent();
		String s = intent.getStringExtra("WithdrawalsCount");
		// int WithdrawalsCount = Integer.parseInt(s);
		withdrawals_money_text.setText(mon + "0");
		// 第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
		list.add("中国银行");
		list.add("工商银行");
		list.add("北京银行");
		list.add("中国建设银行");
		list.add("招商银行");
		list.add("交通银行");
		list.add("中国农业银行");
		list.add("中国光大银行");
		list.add("中国民生银行");
		list.add("浦发银行");
		list.add("中信银行");
		list.add("兴业银行");
		list.add("华夏银行");
		list.add("广发银行");
		list.add("平安银行");
		list.add("中国邮政储蓄银行");
		list.add("北京农商银行");
		// 第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		// 第三步：为适配器设置下拉列表下拉时的菜单样式。
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 第四步：将适配器添加到下拉列表上
		spinner.setAdapter(adapter);
		// 第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				/* 将所选mySpinner 的值带入myTextView 中 */
				// myTextView.setText("您选择的是：" + adapter.getItem(arg2));
				brankName = adapter.getItem(arg2);
				/* 将mySpinner 显示 */
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void initView() {
		withdrawals_money_text = (TextView) findViewById(R.id.withdrawals_money_text);
		withdrawals_fee = (TextView) findViewById(R.id.withdrawals_fee);
		withdrawals_now_btn = (TextView) findViewById(R.id.withdrawals_now_btn);
		withdrawals_name_edit = (EditText) findViewById(R.id.withdrawals_name_edit);
		withdrawals_cardcode_edit = (EditText) findViewById(R.id.withdrawals_cardcode_edit);
		select_bank_layout = (LinearLayout) findViewById(R.id.select_bank_layout);
		withdrawals_now_btn.setOnClickListener(this);
		spinner = (Spinner) findViewById(R.id.pro_spinner);
	}

	// 提现请求服务器
	private void Withdrawals(String accountName, String brandName,
			String brankCard, String feeNum, String goldNum) {
		// TODO Auto-generated method stub
		WithdrawalsRequest withdrawalsRequest = new WithdrawalsRequest();
		WithdrawalsRequest.Parameter parameter = withdrawalsRequest.p;
		parameter.userId = GetUserIdUtil
				.getUserId(AccountWithdrawalsActivity.this);
		parameter.tokenId = GetUserIdUtil
				.getTokenId(AccountWithdrawalsActivity.this);
		parameter.accountName = accountName;
		parameter.brandName = brandName;
		parameter.brandName = brankCard;
		parameter.feeNum = feeNum;
		parameter.goldNum = goldNum;
		final Gson gson = new Gson();
		String json = gson.toJson(withdrawalsRequest);
		HttpConnectTool.update(json, AccountWithdrawalsActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						// TODO Auto-generated method stub
						if (!TextUtils.isEmpty(result)) {
							withdrawalsResult = gson.fromJson(result,
									WithdrawalsResult.class);
							if (withdrawalsResult.p.isTrue) {
								msg = new Message();
								msg.what = SUCCESS;
								handler.sendMessage(msg);
								if (withdrawalsResult.p.isSucce) {
									msg = new Message();
									msg.what = EXCHANGEGOLDSUCCESS;
									handler.sendMessage(msg);
									
								} else {
									msg = new Message();
									msg.what = EXCHANGEGOLDFAIL;
									handler.sendMessage(msg);
								}
							} else {
								IntentActivity
										.mIntent(AccountWithdrawalsActivity.this);
							}
						}
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

	private void QueryGoldNum() {
		// TODO Auto-generated method stub
		QueryGoldNumRequest queryGoldNumRequest = new QueryGoldNumRequest();
		QueryGoldNumRequest.User user = queryGoldNumRequest.p;
		user.userId = GetUserIdUtil.getUserId(AccountWithdrawalsActivity.this);
		user.tokenId = GetUserIdUtil
				.getTokenId(AccountWithdrawalsActivity.this);
		final Gson gson = new Gson();
		String json = gson.toJson(queryGoldNumRequest);
		HttpConnectTool.update(json, AccountWithdrawalsActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {

						if (!TextUtils.isEmpty(result)) {
							queryGoldNumResult = gson.fromJson(result,
									QueryGoldNumResult.class);
							if (queryGoldNumResult.p.isTrue) {
								msg = new Message();
								msg.what = SUCCESS;
								handler.sendMessage(msg);
								// 获取金币数量
								userGoldNum = queryGoldNumResult.p.user.goldNum;
								LoginUserResult login = GetUserIdUtil
										.getLogin(AccountWithdrawalsActivity.this);
								login.p.user.goldNum = userGoldNum;
								GetUserIdUtil.saveLoginUserResult(
										AccountWithdrawalsActivity.this, login);
							} else {
								IntentActivity
										.mIntent(AccountWithdrawalsActivity.this);
							}
						}

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

	};
}
