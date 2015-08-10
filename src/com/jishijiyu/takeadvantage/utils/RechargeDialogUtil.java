package com.jishijiyu.takeadvantage.utils;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.login.RegisterActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.AccountRechargeActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.AccountWithdrawalsActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class RechargeDialogUtil {
	public static AlertDialog dialog = null;
	public static View view = null;
	public static TextView title_text, have_gold_count, dialog_cancel_btn,
			dialog_ok_btn, dialog_cancel_btn1, dialog_ok_btn1;
	public static EditText edit_count;

	public static AlertDialog showRechargeDialog(final Context context,
			int forWhat, String haveGolds, OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(
				R.layout.layout_recharge_dialog, null);
		title_text = (TextView) view.findViewById(R.id.dialog_title_text);
		have_gold_count = (TextView) view.findViewById(R.id.have_gold_count);
		dialog_cancel_btn = (TextView) view
				.findViewById(R.id.dialog_cancel_btn);
		dialog_ok_btn = (TextView) view.findViewById(R.id.dialog_ok_btn);
		edit_count = (EditText) view.findViewById(R.id.et_money);
		dialog_cancel_btn.setOnClickListener(clickListener);
		dialog_ok_btn.setOnClickListener(clickListener);
		have_gold_count.setText("当前剩余金额：" + haveGolds);
		switch (forWhat) {
		// 提现
		case 1:
			title_text.setText("提现金额");
			edit_count.setHint("请输入提现金额");
			dialog_ok_btn.setText("提现");

			break;
		// 充值
		case 2:
			title_text.setText("充值金额");
			edit_count.setHint("请输入充值金额");
			dialog_ok_btn.setText("充值");
			break;

		default:
			break;
		}
		dialog_cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog = new AlertDialog.Builder(context).show();
		dialog.setContentView(view);
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return dialog;

	}

	public static AlertDialog showRechargeDialog1(final Context context,
			int forWhat, String haveGolds, OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(
				R.layout.layout_recharge_dialog1, null);
		title_text = (TextView) view.findViewById(R.id.dialog_title_text);
		have_gold_count = (TextView) view.findViewById(R.id.have_gold_count);
		dialog_cancel_btn1 = (TextView) view
				.findViewById(R.id.dialog_cancel_btn1);
		dialog_ok_btn1 = (TextView) view.findViewById(R.id.dialog_ok_btn1);
		edit_count = (EditText) view.findViewById(R.id.et_money1);
		have_gold_count.setText("当前剩余金额：" + haveGolds);
		dialog_cancel_btn1.setOnClickListener(clickListener);
		dialog_ok_btn1.setOnClickListener(clickListener);
		switch (forWhat) {
		// 提现
		case 1:
			title_text.setText("提现金额");
			edit_count.setHint("请输入提现金额");
			dialog_ok_btn1.setText("提现");

			break;
		// 充值
		case 2:
			title_text.setText("充值金额");
			edit_count.setHint("请输入充值金额");
			dialog_ok_btn1.setText("充值");
			break;

		default:
			break;
		}
		dialog = new AlertDialog.Builder(context).show();
		dialog.setContentView(view);
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return dialog;

	}

}
