package com.jishijiyu.takeadvantage.activity.myinformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.ApplyForMerchantStepOneActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.ApplyForMerchantStepTwoActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.MerchantAccountActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.MerchantStateFailureActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.MerchantStateReviewActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.NotMerchantActivity;
import com.jishijiyu.takeadvantage.activity.my.AboutPaiDeLiActivity;
import com.jishijiyu.takeadvantage.activity.my.CommonQuestionActivity;
import com.jishijiyu.takeadvantage.activity.my.FeedBackActivity;
import com.jishijiyu.takeadvantage.activity.my.OrderInquiryActivity;
import com.jishijiyu.takeadvantage.entity.request.MerchantStateRequest;
import com.jishijiyu.takeadvantage.entity.request.MyInfoMationRequest;
import com.jishijiyu.takeadvantage.entity.request.MyInfoMationRequest.Parameter;
import com.jishijiyu.takeadvantage.entity.result.MerchantStateResult;
import com.jishijiyu.takeadvantage.entity.result.MyInfoMationResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.FileUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.view.BigImagePopupWindow;

/**
 * 我的信息 界面
 * 
 * @author shifeiyu
 * @version 2015年5月27日13:29:22
 */
public class MyInfomationActivity extends HeadBaseActivity {
	ImageView btn_next_arrow, mine_head_photo_pic;
	View order_search_btn, integral_statistics_btn, ticket_record_btn,
			winning_record_btn, feedback, faq_btn, about_pdl_btn,
			upgrade_business_btn;
	TextView mine_nickname_text, mine_account_number_text;
	String imageUrl;
	private int merchantState;// 商家状态
	private int reviewState;// 审核状态;
	private String reviewRemark;
	private TextView tv_merchantstate;
	private ImageView im_icon;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 进入个人信息界面
		case R.id.btn_next_arrow:
			startForActivity(MyInfomationActivity.this,
					MyBasicInfomationActivity.class, null);
			break;
		// 关于拍得利
		case R.id.about_pdl_btn:
			startForActivity(MyInfomationActivity.this,
					AboutPaiDeLiActivity.class, null);
			break;
		// 常见问题
		case R.id.faq_btn:
			startForActivity(MyInfomationActivity.this,
					CommonQuestionActivity.class, null);
			break;
		// 反馈
		case R.id.feedback_btn:
			startForActivity(MyInfomationActivity.this, FeedBackActivity.class,
					null);
			break;
		// 订单查询
		case R.id.order_search_btn:
			startForActivity(MyInfomationActivity.this,
					OrderInquiryActivity.class, null);
			break;
		// 兑换商品记录
		case R.id.ticket_record_btn:
			startForActivity(MyInfomationActivity.this,
					AwardingGoodsActivity.class, null);
			break;
		// 中奖记录
		case R.id.winning_record_btn:
			startForActivity(MyInfomationActivity.this,
					WinningPriceActivity.class, null);
			break;
		// 返回
		case R.id.btn_top_left:
			finish();
			break;
		// 设置
		case R.id.btn_top_right:
			startForActivity(MyInfomationActivity.this, SettingActivity.class,
					null);
			break;
		// 个人账户
		case R.id.integral_statistics_btn:
			startForActivity(MyInfomationActivity.this,
					IntegralStatisticsActivity.class, null);
			break;
		// 检查新版本
		// case R.id.checkver_btn:
		// new APKUpData(mContext).checkUpData(true);
		// break;
		case R.id.mine_head_photo_pic:
			if (TextUtils.isEmpty(imageUrl)) {
				ToastUtils.makeText(MyInfomationActivity.this, "头像未设置\n无法查看大图",
						0).show();
			} else {
				BigImagePopupWindow bigpop = new BigImagePopupWindow(
						MyInfomationActivity.this, imageUrl, null);
				bigpop.showAtLocation(
						this.findViewById(R.id.infomation_layout),
						Gravity.NO_GRAVITY, 0, 0);
			}
			break;
		// 升级为商家/商家管理
		case R.id.upgrade_business_btn:
			Gson gson = new Gson();
			MerchantStateRequest msq = new MerchantStateRequest();
			msq.p.userId = GetUserIdUtil.getUserId(MyInfomationActivity.this);
			msq.p.tokenId = GetUserIdUtil.getTokenId(MyInfomationActivity.this);
			final String myInfoJson = gson.toJson(msq);
			HttpConnectTool.update(myInfoJson, MyInfomationActivity.this,
					new ConnectListener() {

						@Override
						public void contectSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								Gson gson = new Gson();
								MerchantStateResult msr = gson.fromJson(result,
										MerchantStateResult.class);
								if (msr.p.isTrue) {
									reviewState = msr.p.com.reviewState;
									reviewRemark = msr.p.com.reviewRemark;
									if (reviewState == 0) {
										// // 未提交
										startForActivity(
												MyInfomationActivity.this,
												ApplyForMerchantStepOneActivity.class,
												null);
									} else if (reviewState == 1) {
										// 审核中
										startForActivity(
												MyInfomationActivity.this,
												MerchantStateReviewActivity.class,
												null);
									} else if (reviewState == 2) {
										// 审核成功
										startForActivity(
												MyInfomationActivity.this,
												MerchantAccountActivity.class,
												null);
									} else if (reviewState == 3) {
										// 审核失败
										Intent intent = new Intent(
												MyInfomationActivity.this
														.getApplicationContext(),
												MerchantStateFailureActivity.class);
										Bundle bundle = new Bundle();
										bundle.putString("reviewRemark",
												reviewRemark);
										intent.putExtras(bundle);
										startActivity(intent);
										// startForActivity(
										// MyInfomationActivity.this,
										// MerchantStateFailureActivity.class,
										// null);
									}
								} else {
									Intent intent = new Intent(
											MyInfomationActivity.this,
											LoginActivity.class);
									startActivityForResult(intent, RESULT_OK);

								}
							}

						}

						@Override
						public void contectStarted() {

						}

						@Override
						public void contectFailed(String path, String request) {
							ToastUtils.makeText(MyInfomationActivity.this,
									"访问服务器超时", 0).show();
						}
					});
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.mine_title);
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText("设置");
		btn_right.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		// 商家状态
		merchantState = GetUserIdUtil.merchantState(mContext);

		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(MyInfomationActivity.this,
				R.layout.activity_my_infomation, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		btn_next_arrow = (ImageView) findViewById(R.id.btn_next_arrow);
		mine_head_photo_pic = (ImageView) findViewById(R.id.mine_head_photo_pic);
		integral_statistics_btn = findViewById(R.id.integral_statistics_btn);
		ticket_record_btn = findViewById(R.id.ticket_record_btn);
		winning_record_btn = findViewById(R.id.winning_record_btn);
		feedback = findViewById(R.id.feedback_btn);
		faq_btn = findViewById(R.id.faq_btn);
		about_pdl_btn = findViewById(R.id.about_pdl_btn);
		mine_nickname_text = (TextView) findViewById(R.id.mine_nickname_text);
		mine_account_number_text = (TextView) findViewById(R.id.mine_account_number_text);
		order_search_btn = findViewById(R.id.order_search_btn);
		integral_statistics_btn = findViewById(R.id.integral_statistics_btn);
		upgrade_business_btn = findViewById(R.id.upgrade_business_btn);
		tv_merchantstate = (TextView) findViewById(R.id.tv_merchantstate);
		im_icon = (ImageView) findViewById(R.id.im_icon);
		if (merchantState == 0) {
			// 申请为商家
			im_icon.setBackgroundResource(R.drawable.icon_5);
			tv_merchantstate.setText("申请为商家");
		} else if (merchantState == 1) {
			// 商家页面
			im_icon.setBackgroundResource(R.drawable.icon_8);
			tv_merchantstate.setText("商家管理");
		}
		integral_statistics_btn.setOnClickListener(this);
		order_search_btn.setOnClickListener(this);
		btn_next_arrow.setOnClickListener(this);
		about_pdl_btn.setOnClickListener(this);
		faq_btn.setOnClickListener(this);
		feedback.setOnClickListener(this);
		ticket_record_btn.setOnClickListener(this);
		winning_record_btn.setOnClickListener(this);
		integral_statistics_btn.setOnClickListener(this);
		mine_head_photo_pic.setOnClickListener(this);
		upgrade_business_btn.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		RequestInfo();
	}

	/**
	 * 请求个人信息
	 */
	public void RequestInfo() {
		Gson gson = new Gson();
		MyInfoMationRequest myInfoRequest = new MyInfoMationRequest();
		final Parameter parameter = myInfoRequest.p;
		parameter.userId = GetUserIdUtil.getUserId(MyInfomationActivity.this);
		parameter.tokenId = GetUserIdUtil.getTokenId(MyInfomationActivity.this);
		final String myInfoJson = gson.toJson(myInfoRequest);
		HttpConnectTool.update(myInfoJson, MyInfomationActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						FileUtil.writeFile(MyInfomationActivity.this,
								"InfoJson", result);
						Gson gson = new Gson();
						MyInfoMationResult resultObject = gson.fromJson(result,
								MyInfoMationResult.class);
						com.jishijiyu.takeadvantage.entity.result.MyInfoMationResult.Parameter Resultparameter = resultObject.p;
						if (Resultparameter.isTrue) {
							/**
							 * 显示头像
							 */
							if (Resultparameter.UserExtendUser != null) {
								imageUrl = Resultparameter.UserExtendUser.headImgUrl;
								if (!TextUtils.isEmpty(imageUrl)) {
									ImageLoaderUtil.loadImage1(imageUrl,
											mine_head_photo_pic, true,
											MyInfomationActivity.this,
											Constant.HEAD_PIC_FILE_NAME);
								} else {
									File file = getFilesDir();
									try {
										FileOutputStream fos = new FileOutputStream(
												file
														+ Constant.HEAD_PIC_FILE_NAME);
										Bitmap bitmap = BitmapFactory
												.decodeResource(getResources(),
														R.drawable.pr);
										bitmap.compress(
												Bitmap.CompressFormat.PNG, 100,
												fos);
										Bitmap bitmap1 = BitmapFactory
												.decodeResource(getResources(),
														R.drawable.pr);
										mine_head_photo_pic
												.setImageBitmap(bitmap1);
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									}
								}
							} else {
								File file = getFilesDir();
								try {
									FileOutputStream fos = new FileOutputStream(
											file + Constant.HEAD_PIC_FILE_NAME);
									Bitmap bitmap = BitmapFactory
											.decodeResource(getResources(),
													R.drawable.pr);
									bitmap.compress(Bitmap.CompressFormat.PNG,
											100, fos);
									Bitmap bitmap1 = BitmapFactory
											.decodeResource(getResources(),
													R.drawable.pr);
									mine_head_photo_pic.setImageBitmap(bitmap1);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
							}
							/**
							 * 请求显示昵称
							 */
							if (!TextUtils
									.isEmpty(Resultparameter.UserExtendUser.nickname)) {
								mine_nickname_text
										.setText(Resultparameter.UserExtendUser.nickname);
							} else {
								mine_nickname_text.setText("未设置");
							}
							/**
							 * 请求显示账号
							 */
							String phoneNumber = GetUserIdUtil
									.getUserMobile(MyInfomationActivity.this);
							if (!TextUtils.isEmpty(phoneNumber)) {
								mine_account_number_text.setText("账号:"
										+ phoneNumber);
							} else {
								mine_account_number_text.setText("未设置");
							}
						} else {
							ToastUtils.makeText(MyInfomationActivity.this,
									R.string.again_login_text, 0).show();
							startForActivity(MyInfomationActivity.this,
									LoginActivity.class, null);
							finish();
						}

					}

					@Override
					public void contectStarted() {

					}

					@Override
					public void contectFailed(String path, String request) {
						ToastUtils.makeText(MyInfomationActivity.this,
								"访问服务器超时", 0).show();
						Bitmap bitmap1 = BitmapFactory.decodeResource(
								getResources(), R.drawable.pr_figure);
						mine_head_photo_pic.setImageBitmap(bitmap1);
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
