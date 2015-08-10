package com.jishijiyu.takeadvantage.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.merchant_account.AccountRechargeActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.AccountWithdrawalsActivity;
import com.jishijiyu.takeadvantage.entity.request.PrizeDetailsRequest;
import com.jishijiyu.takeadvantage.entity.result.PrizeDetailsResult;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

/**
 * SD卡相关的辅助类
 * 
 * @author zhaobin
 * 
 */
public class DialogUtils {

	private static AlertDialog alertDialog = null;
	private static View view = null;
	public static boolean flag;
	public static EditText edit_home_code;

	/**
	 * 自定义dialog
	 * 
	 * @author zhaobin
	 * @param context
	 * @param msg
	 *            显示的文字
	 * @param btnBgId
	 *            dialog中按鈕背景图片ID，如果只有一个则数组中只存一个
	 * @param clickListener
	 *            按钮点击事件
	 * @param isImg
	 *            是否有图片true：有，false：无
	 * @return AlertDialog
	 * @throws
	 */
	public static AlertDialog showDialog(Context context, CharSequence msg,
			int btnName[], OnClickListener clickListener, boolean isImg) {
		if (isImg) {
			view = LayoutInflater.from(context).inflate(R.layout.dialog2, null);

		} else {
			switch (btnName.length) {
			case 1:
				view = LayoutInflater.from(context).inflate(R.layout.dialog4,
						null);
				break;
			case 2:
				view = LayoutInflater.from(context).inflate(R.layout.dialog,
						null);
				break;
			}
		}

		TextView message = (TextView) view.findViewById(R.id.dialog_text);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		Button left = (Button) view.findViewById(R.id.left);
		Button right = (Button) view.findViewById(R.id.right);
		message.setText(msg);
		switch (btnName.length) {
		case 1:
			left.setText(context.getResources().getString(btnName[0]));
			left.setOnClickListener(clickListener);
			break;
		case 2:
			left.setText(context.getResources().getString(btnName[0]));
			right.setText(context.getResources().getString(btnName[1]));
			left.setOnClickListener(clickListener);
			right.setOnClickListener(clickListener);
			break;
		}
		alertDialog = new AlertDialog.Builder(context).show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	/**
	 * 选择奖品
	 * 
	 * @author zhaobin
	 * @param context
	 * @param title
	 *            显示的文字
	 * @param msg
	 *            显示的文字
	 * @param btnBgId
	 *            dialog中按鈕背景图片ID，如果只有一个则数组中只存一个
	 * @param clickListener
	 *            按钮点击事件
	 * @return AlertDialog
	 * @throws
	 */
	public static AlertDialog showDialog(Context context, CharSequence title,
			CharSequence msg, int btnBgId[], OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(R.layout.dialog1, null);

		TextView message = (TextView) view.findViewById(R.id.dialog_text);
		TextView tv_title = (TextView) view.findViewById(R.id.title_text);
		Button left = (Button) view.findViewById(R.id.left);
		Button right = (Button) view.findViewById(R.id.right);
		message.setText(msg);
		tv_title.setText(title);
		switch (btnBgId.length) {
		case 1:
			right.setVisibility(View.GONE);
			left.setText(context.getResources().getString(btnBgId[0]));
			left.setOnClickListener(clickListener);
			break;
		case 2:
			left.setText(context.getResources().getString(btnBgId[0]));
			right.setText(context.getResources().getString(btnBgId[1]));
			left.setOnClickListener(clickListener);
			right.setOnClickListener(clickListener);
			break;
		}
		alertDialog = new AlertDialog.Builder(context).show();
		alertDialog.setContentView(view);
		return alertDialog;
	}

	/**
	 * 
	 * 赠送弹出框
	 * 
	 * @author zhaobin
	 * @param context
	 * @param clickListener
	 * @return AlertDialog
	 * @throws
	 */
	public static AlertDialog showDialog(Context context,
			OnClickListener clickListener) {
		view = LayoutInflater.from(context)
				.inflate(R.layout.dialog_phone, null);

		EditText message = (EditText) view.findViewById(R.id.editText);
		Button btn = (Button) view.findViewById(R.id.btn);
		Button close = (Button) view.findViewById(R.id.close);
		btn.setOnClickListener(clickListener);
		close.setOnClickListener(clickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	/**
	 * 设置diaolog标题
	 * 
	 * @author zhaobin
	 * @param @param titles
	 * @return void
	 * @throws
	 */
	public static void setTitle(CharSequence titles) {
		TextView title = (TextView) view.findViewById(R.id.title_text);
		if (!TextUtils.isEmpty(titles)) {
			title.setText(titles);
		}
	}

	public static AlertDialog showDialog(final Context context,
			final CharSequence name, String prizeId, String url,
			final CharSequence btnName, final OnClickListener clickListener,
			boolean isShow) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.dialog3, null);
		ImageView img = (ImageView) view.findViewById(R.id.picture);
		ImageLoaderUtil.loadImage(url, img);
		final TextView title = (TextView) view.findViewById(R.id.title);
		final TextView sellMerchant = (TextView) view
				.findViewById(R.id.sell_merchant_textview);
		final TextView marketPrice = (TextView) view
				.findViewById(R.id.market_price_textview);
		final Button consult = (Button) view.findViewById(R.id.consult);
		final Button btnClose = (Button) view.findViewById(R.id.btn_close);
		consult.setOnClickListener(clickListener);
		btnClose.setOnClickListener(clickListener);
		consult.setText(btnName);
		PrizeDetailsRequest detailsRequest = new PrizeDetailsRequest();
		PrizeDetailsRequest.Parameter p = detailsRequest.p;
		p.prizeId = prizeId;
		p.tokenId = GetUserIdUtil.getTokenId(context);
		p.userId = GetUserIdUtil.getUserId(context);
		final Gson gson = new Gson();
		String json = gson.toJson(detailsRequest);
		HttpConnectTool.update(json, false, context, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (result != null) {
					PrizeDetailsResult jsonobj = gson.fromJson(result,
							PrizeDetailsResult.class);
					PrizeDetailsResult.Prize prize = jsonobj.p.Prize;
					String names = null;
					if (prize != null) {
						names = prize.name + " " + prize.prizeExplain;
						if (!TextUtils.isEmpty(prize.supportCompany)) {
							sellMerchant.setText(prize.supportCompany);
						} else {
							sellMerchant.setText("无");
						}
						marketPrice.setText("¥" + prize.price);
					}
					title.setText(names);
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {

			}
		});

		if (!isShow) {
			consult.setVisibility(View.INVISIBLE);
		}
		alertDialog = new AlertDialog.Builder(context).show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	// /**
	// *
	// * 兑换锁弹出框
	// *
	// * @author zhaobin
	// * @param context
	// * @param clickListener
	// * @return AlertDialog
	// * @throws
	// */
	// public static AlertDialog showLockDialog(Context context,
	// int inversion_num, int gold_num, OnClickListener clickListener) {
	// view = LayoutInflater.from(context).inflate(R.layout.lock_dialog, null);
	// TextView tv_lock_invitaion_num = (TextView) view
	// .findViewById(R.id.tv_lock_invitaion_num);
	// TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
	// TextView tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
	// TextView tv_lock_num = (TextView) view.findViewById(R.id.tv_lock_num);
	// TextView tv_lock_rule = (TextView) view.findViewById(R.id.tv_lock_rule);
	// Button btn_gold = (Button) view.findViewById(R.id.btn_gold);
	// Button btn_people = (Button) view.findViewById(R.id.btn_people);
	// Button btn_add = (Button) view.findViewById(R.id.btn_add);
	// Button btn_del = (Button) view.findViewById(R.id.btn_del);
	// final TextView edt_lock_num = (TextView) view
	// .findViewById(R.id.edt_lock_num);
	// Button btn_exit = (Button) view.findViewById(R.id.btn_exit);
	// Button btn_gold_conversion = (Button) view
	// .findViewById(R.id.btn_gold_conversion);
	// // Button btn_invitation_conversion = (Button) view
	// // .findViewById(R.id.btn_invitation_conversion);
	// tv_lock_rule.setText("把(" + Constant.rule_num + Constant.rule + ")");
	// btn_add.setOnClickListener(clickListener);
	// btn_del.setOnClickListener(clickListener);
	// btn_gold.setOnClickListener(clickListener);
	// btn_people.setOnClickListener(clickListener);
	// btn_exit.setOnClickListener(clickListener);
	// btn_gold_conversion.setOnClickListener(clickListener);
	// if (inversion_num > 0) {
	// if ((inversion_num / Constant.rule_num) > 0) {
	// // btn_invitation_conversion.setOnClickListener(clickListener);
	// }
	// tv_lock_invitaion_num.setText(inversion_num + "");
	// tv_lock_num.setText((inversion_num / Constant.rule_num) + "");
	// } else {
	// // btn_invitation_conversion.setOnClickListener(null);
	// }
	// alertDialog = new AlertDialog.Builder(context).create();
	// alertDialog.show();
	// alertDialog.setContentView(view);
	// alertDialog.getWindow().clearFlags(
	// WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	// | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	// alertDialog.getWindow().setSoftInputMode(
	// WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	// return alertDialog;
	// }
	//
	// /**
	// *
	// * 兑银换弹出框
	// *
	// * @author sunaoyang
	// * @param context
	// * @param clickListener
	// * @return AlertDialog
	// * @throws
	// */
	// public static AlertDialog showLockDialog2(Context context, int goldNum,
	// OnClickListener clickListener) {
	// view = LayoutInflater.from(context)
	// .inflate(R.layout.lock_dialog2, null);
	// TextView tv_lock_invitaion_num = (TextView) view
	// .findViewById(R.id.tv_lock_invitaion_num);
	// TextView tv_lock_num = (TextView) view.findViewById(R.id.tv_lock_num);
	// TextView tv_lock_rule = (TextView) view.findViewById(R.id.tv_lock_rule);
	// Button btn_add = (Button) view.findViewById(R.id.btn_add);
	// Button btn_del = (Button) view.findViewById(R.id.btn_del);
	// final TextView edt_lock_num = (TextView) view
	// .findViewById(R.id.edt_lock_num);
	// Button btn_exit = (Button) view.findViewById(R.id.btn_exit);
	// Button btn_gold_conversion = (Button) view
	// .findViewById(R.id.btn_gold_conversion);
	// Button btn_invitation_conversion = (Button) view
	// .findViewById(R.id.btn_invitation_conversion);
	// // tv_lock_rule.setText("把(" + Constant.rule_num + Constant.rule + ")");
	// btn_add.setOnClickListener(clickListener);
	// btn_del.setOnClickListener(clickListener);
	// btn_exit.setOnClickListener(clickListener);
	// // btn_gold_conversion.setOnClickListener(clickListener);
	// if (goldNum > 0) {
	// if ((goldNum / Constant.rule_num3) > 0) {
	// btn_gold_conversion.setOnClickListener(clickListener);
	// }
	// tv_lock_invitaion_num.setText(goldNum + "");
	// tv_lock_num.setText((goldNum / Constant.rule_num3) + "");
	// } else {
	// btn_gold_conversion.setOnClickListener(null);
	// }
	// alertDialog = new AlertDialog.Builder(context).create();
	// alertDialog.show();
	// alertDialog.setContentView(view);
	// alertDialog.getWindow().clearFlags(
	// WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	// | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	// alertDialog.getWindow().setSoftInputMode(
	// WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	// return alertDialog;
	// }
	//
	// /**
	// *
	// * 兑铜换弹出框
	// *
	// * @author sunaoyang
	// * @param context
	// * @param clickListener
	// * @return AlertDialog
	// * @throws
	// */
	// public static AlertDialog showLockDialog3(Context context, int goldNum,
	// OnClickListener clickListener) {
	// view = LayoutInflater.from(context)
	// .inflate(R.layout.lock_dialog3, null);
	// TextView tv_lock_invitaion_num = (TextView) view
	// .findViewById(R.id.tv_lock_invitaion_num);
	// TextView tv_lock_num = (TextView) view.findViewById(R.id.tv_lock_num);
	// TextView tv_lock_rule = (TextView) view.findViewById(R.id.tv_lock_rule);
	// Button btn_add = (Button) view.findViewById(R.id.btn_add);
	// Button btn_del = (Button) view.findViewById(R.id.btn_del);
	// final TextView edt_lock_num = (TextView) view
	// .findViewById(R.id.edt_lock_num);
	// Button btn_exit = (Button) view.findViewById(R.id.btn_exit);
	// Button btn_gold_conversion = (Button) view
	// .findViewById(R.id.btn_gold_conversion);
	// Button btn_invitation_conversion = (Button) view
	// .findViewById(R.id.btn_invitation_conversion);
	// // tv_lock_rule.setText("把(" + Constant.rule_num + Constant.rule + ")");
	// btn_add.setOnClickListener(clickListener);
	// btn_del.setOnClickListener(clickListener);
	// btn_exit.setOnClickListener(clickListener);
	// // btn_gold_conversion.setOnClickListener(clickListener);
	// if (goldNum > 0) {
	// if ((goldNum / Constant.rule_num4) > 0) {
	// btn_gold_conversion.setOnClickListener(clickListener);
	// }
	// tv_lock_invitaion_num.setText(goldNum + "");
	// tv_lock_num.setText((goldNum / Constant.rule_num4) + "");
	// } else {
	// btn_gold_conversion.setOnClickListener(null);
	// }
	// alertDialog = new AlertDialog.Builder(context).create();
	// alertDialog.show();
	// alertDialog.setContentView(view);
	// alertDialog.getWindow().clearFlags(
	// WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	// | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	// alertDialog.getWindow().setSoftInputMode(
	// WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	// return alertDialog;
	// }

	/**
	 * 自定义dialog
	 * 
	 * @author zhuxiaoguang
	 * @param context
	 * @param msg
	 *            显示的文字
	 * @param btnBgId
	 *            dialog中按鈕背景图片ID，如果只有一个则数组中只存一个
	 * @param clickListener
	 *            按钮点击事件
	 * @return AlertDialog
	 * @throws
	 */
	public static AlertDialog showCheckboxDialog(Context context,
			CharSequence title, CharSequence msg, CharSequence checkboxtext,
			int btnBgId[], OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(R.layout.dialog_checkbox,
				null);

		TextView message = (TextView) view.findViewById(R.id.dialog_text);
		TextView tv_title = (TextView) view.findViewById(R.id.title_text);
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.dialog_checkbox);
		Button left = (Button) view.findViewById(R.id.left);
		Button right = (Button) view.findViewById(R.id.right);
		message.setText(msg);
		tv_title.setText(title);
		checkbox.setText(checkboxtext);
		switch (btnBgId.length) {
		case 1:
			right.setVisibility(View.GONE);
			left.setText(context.getResources().getString(btnBgId[0]));
			left.setOnClickListener(clickListener);
			break;
		case 2:
			left.setText(context.getResources().getString(btnBgId[0]));
			right.setText(context.getResources().getString(btnBgId[1]));
			left.setOnClickListener(clickListener);
			right.setOnClickListener(clickListener);
			break;
		}
		alertDialog = new AlertDialog.Builder(context).show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	/**
	 * 提现
	 * 
	 * @author shifeiyu
	 * @param context
	 * @param title
	 *            标题(无的为null)
	 * @param forWhat
	 *            1:提现，2:充值
	 * @param haveGolds
	 * @param onClickListener
	 * @return AlertDialog
	 * @throws
	 */
	public static AlertDialog showRechargeDialog(final Context context,
			String title, int forWhat, int haveGolds,
			OnClickListener onClickListener) {
		view = LayoutInflater.from(context).inflate(R.layout.recharge_dialog,
				null);
		TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
		if (!TextUtils.isEmpty(title)) {
			tv_top.setVisibility(View.VISIBLE);
			tv_top.setText(title);
		}

		TextView title_text = (TextView) view
				.findViewById(R.id.dialog_title_text);
		TextView have_gold_count = (TextView) view
				.findViewById(R.id.have_gold_count);
		TextView dialog_cancel_btn = (TextView) view
				.findViewById(R.id.dialog_cancel_btn);
		TextView dialog_ok_btn = (TextView) view
				.findViewById(R.id.dialog_ok_btn);
		EditText edit_count = (EditText) view.findViewById(R.id.edit_count);
		have_gold_count.setText("当前金币" + haveGolds);
		switch (forWhat) {
		// 提现
		case 1:
			title_text.setText("100金币=1元");
			edit_count.setHint("请输入提现金额");
			dialog_ok_btn.setText("提现");
			dialog_ok_btn.setOnClickListener(onClickListener);
			break;
		// 充值
		case 2:
			title_text.setText("1元=100金币");
			edit_count.setHint("请输入充值金额");
			dialog_ok_btn.setText("充值");
			dialog_ok_btn.setOnClickListener(onClickListener);
			break;
		case 3:
			title_text.setText("1金币=1拍币");
			edit_count.setHint("请输入兑换的拍币数量");
			dialog_ok_btn.setText("兑换");
			dialog_ok_btn.setOnClickListener(onClickListener);
			break;
		case 4:
			title_text.setText("1金币=1拍币");
			edit_count.setHint("请输入兑换的拍币数量");
			dialog_ok_btn.setText("兑换");
			dialog_ok_btn.setOnClickListener(onClickListener);
			break;
		default:
			break;
		}
		dialog_cancel_btn.setOnClickListener(onClickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;

	}

	public static AlertDialog showRechargeDialog1(final Context context,
			String title, int forWhat, int haveGolds,
			OnClickListener onClickListener) {
		view = LayoutInflater.from(context).inflate(R.layout.recharge_dialog1,
				null);
		TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
		if (!TextUtils.isEmpty(title)) {
			tv_top.setVisibility(View.VISIBLE);
			tv_top.setText(title);
		}

		TextView title_text = (TextView) view
				.findViewById(R.id.dialog_title_text);
		TextView have_gold_count = (TextView) view
				.findViewById(R.id.have_gold_count);
		TextView dialog_cancel_btn = (TextView) view
				.findViewById(R.id.dialog_cancel_btn1);
		TextView dialog_ok_btn = (TextView) view
				.findViewById(R.id.dialog_ok_btn1);
		EditText edit_count = (EditText) view.findViewById(R.id.edit_count1);
		have_gold_count.setText("当前金币" + haveGolds);
		switch (forWhat) {
		// 提现
		case 1:
			title_text.setText("100金币=1元");
			edit_count.setHint("请输入提现金额");
			dialog_ok_btn.setText("提现");
			dialog_ok_btn.setOnClickListener(onClickListener);
			break;
		// 充值
		case 2:
			title_text.setText("1元=100金币");
			edit_count.setHint("请输入充值金额");
			dialog_ok_btn.setText("充值");
			dialog_ok_btn.setOnClickListener(onClickListener);
			break;
		case 3:
			title_text.setText("1金币=1拍币");
			edit_count.setHint("请输入兑换的拍币数量");
			dialog_ok_btn.setText("兑换");
			dialog_ok_btn.setOnClickListener(onClickListener);
			break;
		case 4:
			title_text.setText("1金币=1拍币");
			edit_count.setHint("请输入兑换的拍币数量");
			dialog_ok_btn.setOnClickListener(onClickListener);
			break;
		default:
			break;
		}
		dialog_cancel_btn.setOnClickListener(onClickListener);
		alertDialog = new AlertDialog.Builder(context).show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;

	}

	/**
	 * 兑换锁弹出框
	 * 
	 * @author baohan
	 * @param context
	 * @param count
	 * @param goldLock
	 * @param silverLock
	 * @param copperLock
	 * @param onClickListener
	 * @return
	 */
	public static AlertDialog exchangeLockDialog(final Context context,
			int getlock, int count, int goldLock, int silverLock,
			int copperLock, int witch, OnClickListener onClickListener) {
		view = LayoutInflater.from(context).inflate(
				R.layout.achieve_lock_dialog, null);

		ImageView lockImage = (ImageView) view.findViewById(R.id.lock_image);

		TextView haveGoldCount = (TextView) view
				.findViewById(R.id.have_gold_count);

		TextView numberHandful = (TextView) view
				.findViewById(R.id.number_handful);
		TextView numberHandful2 = (TextView) view
				.findViewById(R.id.number_handful2);
		TextView numberHandful3 = (TextView) view
				.findViewById(R.id.number_handful3);
		TextView CancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
		switch (witch) {
		case 1:
			lockImage.setImageResource(R.drawable.img_lock_1);
			haveGoldCount.setText("祝贺你获得" + count + "把金锁");
			numberHandful.setText((goldLock + count) + "把");
			numberHandful2.setText(silverLock + "把");
			numberHandful3.setText(copperLock + "把");
			break;
		case 2:
			lockImage.setImageResource(R.drawable.img_lock_2);
			haveGoldCount.setText("祝贺你获得" + count + "把银锁");
			numberHandful.setText(goldLock + "把");
			numberHandful2.setText((silverLock + count) + "把");
			numberHandful3.setText(copperLock + "把");
			break;
		case 3:
			lockImage.setImageResource(R.drawable.img_lock_3);
			haveGoldCount.setText("祝贺你获得" + count + "把铜锁");
			numberHandful.setText(goldLock + "把");
			numberHandful2.setText(silverLock + "把");
			numberHandful3.setText((copperLock + count) + "把");
			break;

		default:
			break;
		}

		CancelBtn.setOnClickListener(onClickListener);
		alertDialog = new AlertDialog.Builder(context).show();
		alertDialog.setContentView(view);
		return alertDialog;

	}

	/**
	 * 宝箱弹出框
	 * 
	 * @author baohan
	 * @param context
	 * @param image
	 * @param onClickListener
	 * @return
	 */
	public static AlertDialog TreasureBoxDialog(final Context context,
			int image, OnClickListener onClickListener) {
		view = LayoutInflater.from(context).inflate(
				R.layout.display_treasure_box, null);
		ImageView treasureBox = (ImageView) view
				.findViewById(R.id.treasure_box);

		treasureBox.setOnClickListener(onClickListener);
		alertDialog = new AlertDialog.Builder(context).show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;

	}

	/**
	 * 快速查找房间
	 * 
	 * @author shifeiyu
	 * @param context
	 * @param onClickListener
	 * @return AlertDialog
	 * @throws
	 */
	public static AlertDialog searchHomeDialog(Context context,
			OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(
				R.layout.layout_search_home_dialog, null);

		edit_home_code = (EditText) view.findViewById(R.id.edit_home_code);
		TextView search_home_cancel_btn;
		TextView search_home_join_btn;
		search_home_cancel_btn = (TextView) view
				.findViewById(R.id.search_home_cancel_btn);
		search_home_join_btn = (TextView) view
				.findViewById(R.id.search_home_join_btn);
		EdittextUtil.editMaxLength(edit_home_code, 8);
		search_home_cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();

			}
		});
		search_home_join_btn.setOnClickListener(clickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	/**
	 * * 支付对话框
	 * 
	 * @author zhengjianxiong
	 * @param context
	 * @param onClickListener
	 * @return AlertDialog
	 * @throws
	 * 
	 */

	public static AlertDialog PayMoneyDialog(Context context,
			OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(R.layout.pay_home_dialog,
				null);
		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.is_check);
		Button btn_yes = (Button) view.findViewById(R.id.btn_yes);
		btn_yes.setOnClickListener(clickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	/**
	 * 托管按钮dialog
	 * 
	 * @author shifeiyu
	 * @param context
	 * @param onClickListener
	 * @return AlertDialog
	 * @throws
	 */
	public static AlertDialog showTrusteeshipDialog(Context context,
			OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(
				R.layout.layout_trusteeship_dialog, null);
		flag = true;
		TextView trusteeship_cancel_btn, trusteeship_ok_btn;
		final ImageView trusteeship_check_img;
		trusteeship_check_img = (ImageView) view
				.findViewById(R.id.trusteeship_check_img);
		trusteeship_cancel_btn = (TextView) view
				.findViewById(R.id.trusteeship_cancel_btn);
		trusteeship_ok_btn = (TextView) view
				.findViewById(R.id.trusteeship_ok_btn);

		trusteeship_cancel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();

			}
		});
		trusteeship_check_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag) {
					trusteeship_check_img
							.setBackgroundResource(R.drawable.no_remember_pwd);
					flag = false;
				} else {
					trusteeship_check_img
							.setBackgroundResource(R.drawable.remember_pwd);
					flag = true;
				}
			}
		});
		trusteeship_ok_btn.setOnClickListener(clickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	/**
	 * 
	 * //提现弹出框
	 * 
	 * @author zhengjianxiong
	 */
	public static AlertDialog WithdrawalsDialog(Context context,
			OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(
				R.layout.withdrawals_dialog, null);
		TextView btn_yes = (TextView) view.findViewById(R.id.btn_yes);
		TextView btn_no = (TextView) view.findViewById(R.id.btn_no);
		btn_yes.setOnClickListener(clickListener);
		btn_no.setOnClickListener(clickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	/**
	 * 
	 * //弹出晒奖对话框
	 * 
	 * @author zhangxin
	 */
	public static AlertDialog showShowPrize(Context context,
			OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(R.layout.dialog_showprize,
				null);
		TextView btn_yes = (TextView) view.findViewById(R.id.btn_show);
		TextView btn_no = (TextView) view.findViewById(R.id.btn_cancel);
		btn_yes.setOnClickListener(clickListener);
		btn_no.setOnClickListener(clickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		return alertDialog;
	}

	/**
	 * 
	 * //支付对话框
	 * 
	 * @author sunaoyang
	 */
	public static AlertDialog payLockDialog(Context context,
			OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(R.layout.dialog_paylock,
				null);
		TextView btn_yes = (TextView) view.findViewById(R.id.btn_yes);
		TextView btn_no = (TextView) view.findViewById(R.id.btn_no);
		btn_yes.setOnClickListener(clickListener);
		btn_no.setOnClickListener(clickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

	/**
	 * 
	 * //商家管理对话框
	 * 
	 * @author sunaoyang
	 */
	public static AlertDialog merchantDialog(Context context,
			OnClickListener clickListener) {
		view = LayoutInflater.from(context).inflate(R.layout.dialog_merchant,
				null);
		TextView btn_yes = (TextView) view.findViewById(R.id.btn_yes);
		TextView btn_no = (TextView) view.findViewById(R.id.btn_no);
		btn_yes.setOnClickListener(clickListener);
		btn_no.setOnClickListener(clickListener);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.show();
		alertDialog.setContentView(view);
		alertDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		alertDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return alertDialog;
	}

}
