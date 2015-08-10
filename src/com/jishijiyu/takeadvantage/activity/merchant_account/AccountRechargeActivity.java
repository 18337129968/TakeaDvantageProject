package com.jishijiyu.takeadvantage.activity.merchant_account;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 充值
 * 
 * @author shifeiyu
 * 
 */
public class AccountRechargeActivity extends HeadBaseActivity {
	RadioButton recharge_rb1, recharge_rb2, recharge_rb3;
	LinearLayout alipay_layout, wxpay_layout, unionpay_layout;
	ArrayList<View> list;
	TextView recharge_money_text, recharge_now_btn;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.alipay_layout:
			recharge_rb1.setChecked(true);
			recharge_rb2.setChecked(false);
			recharge_rb3.setChecked(false);
			break;
		case R.id.wxpay_layout:
			recharge_rb1.setChecked(false);
			recharge_rb2.setChecked(true);
			recharge_rb3.setChecked(false);
			break;
		case R.id.unionpay_layout:
			recharge_rb1.setChecked(false);
			recharge_rb2.setChecked(false);
			recharge_rb3.setChecked(true);
			break;
		case R.id.recharge_now_btn:
			if (recharge_rb1.isChecked()) {
				ToastUtils.makeText(mContext, "支付宝支付", 0).show();
			}
			if (recharge_rb2.isChecked()) {
				ToastUtils.makeText(mContext, "微信支付", 0).show();
			}
			if (recharge_rb3.isChecked()) {
				ToastUtils.makeText(mContext, "银联支付", 0).show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText("账户充值");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AccountRechargeActivity.this,
				R.layout.activity_account_recharge, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initView();
		DecimalFormat decimal = new DecimalFormat("0.00");
		Intent intent = getIntent();
		String s = intent.getStringExtra("RechargeCount");
		int RechargeCount = Integer.parseInt(s);
		recharge_money_text.setText(decimal.format(RechargeCount));

	}

	@SuppressLint("NewApi")
	private void initView() {
		recharge_rb1 = (RadioButton) findViewById(R.id.recharge_rb1);
		recharge_rb2 = (RadioButton) findViewById(R.id.recharge_rb2);
		recharge_rb3 = (RadioButton) findViewById(R.id.recharge_rb3);
		alipay_layout = (LinearLayout) findViewById(R.id.alipay_layout);
		wxpay_layout = (LinearLayout) findViewById(R.id.wxpay_layout);
		unionpay_layout = (LinearLayout) findViewById(R.id.unionpay_layout);
		recharge_money_text = (TextView) findViewById(R.id.recharge_money_text);
		recharge_now_btn = (TextView) findViewById(R.id.recharge_now_btn);
		recharge_now_btn.setOnClickListener(this);
		alipay_layout.setOnClickListener(this);
		wxpay_layout.setOnClickListener(this);
		unionpay_layout.setOnClickListener(this);
		recharge_rb1.setChecked(true);
	}

}
