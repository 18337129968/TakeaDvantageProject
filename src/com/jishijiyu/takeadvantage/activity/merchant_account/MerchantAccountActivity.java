package com.jishijiyu.takeadvantage.activity.merchant_account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.MerchantAccountRequest;
import com.jishijiyu.takeadvantage.entity.result.MerchantAccountResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 商家账户界面
 * 
 * @author shifeiyu
 * 
 */
public class MerchantAccountActivity extends HeadBaseActivity {
	TextView advertising_btn, account_manage_btn, my_advert_btn,
			merchant_name_text, review_state_text1, merchant_synopsis_text,
			browse_quantity_text, partake_task_text, my_message_btn;
	ImageView merchant_logo_img;
	String companyId = "";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:// 返回
			finish();
			break;
		case R.id.advertising_btn:// 发布广告
			Intent intentToAdvertising = new Intent(mContext,
					AdvertisingActivity.class);
			intentToAdvertising.putExtra("companyId", companyId);
			startActivity(intentToAdvertising);
			// startForActivity(mContext, AdvertisingActivity.class, null);
			break;
		case R.id.account_manage_btn:// 账户管理
			startForActivity(mContext, AccountManagerActivity.class, null);
			break;
		case R.id.my_advert_btn:// 我的广告
			// startForActivity(mContext, MyAdvertActivity.class, null);
			Intent intent = new Intent(mContext, MyAdvertActivity.class);
			intent.putExtra("companyId", companyId);
			startActivity(intent);
			break;
		// case R.id.my_message_btn:// 我的信息
		// startForActivity(mContext, MyAdvertActivity.class, null);
		// break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText("商家管理");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(MerchantAccountActivity.this,
				R.layout.activity_merchant_account, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initView();
		initBtnClick();
		getMerchantAccountInfo();
	}

	private void initView() {
		advertising_btn = (TextView) findViewById(R.id.advertising_btn);
		account_manage_btn = (TextView) findViewById(R.id.account_manage_btn);
		my_advert_btn = (TextView) findViewById(R.id.my_advert_btn);
		merchant_name_text = (TextView) findViewById(R.id.merchant_name_text);
		review_state_text1 = (TextView) findViewById(R.id.review_state_text1);
		merchant_synopsis_text = (TextView) findViewById(R.id.merchant_synopsis_text);
		browse_quantity_text = (TextView) findViewById(R.id.browse_quantity_text);
		partake_task_text = (TextView) findViewById(R.id.partake_task_text);
		merchant_logo_img = (ImageView) findViewById(R.id.merchant_logo_img);
	}

	private void initBtnClick() {
		advertising_btn.setOnClickListener(this);
		account_manage_btn.setOnClickListener(this);
		my_advert_btn.setOnClickListener(this);
	}

	private void getMerchantAccountInfo() {
		final Gson gson = new Gson();
		MerchantAccountRequest request = new MerchantAccountRequest();
		request.p.userId = GetUserIdUtil.getUserId(mContext);
		request.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		String json = gson.toJson(request);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					MerchantAccountResult mResult = gson.fromJson(result,
							MerchantAccountResult.class);
					if (mResult.p.isTrue) {
						if (mResult.p.companyAccount != null) {
							companyId = mResult.p.companyAccount.companyId + "";
							merchant_name_text
									.setText(mResult.p.companyAccount.companyName);
							merchant_synopsis_text
									.setText(mResult.p.companyAccount.companyDescribe);
							browse_quantity_text.setText("浏览量\n"
									+ mResult.p.companyAccount.lookNum);
							partake_task_text.setText("参与任务\n"
									+ mResult.p.companyAccount.answerNum);
							ImageLoaderUtil.loadImage(
									mResult.p.companyAccount.logoImgUrl,
									merchant_logo_img);
							switch (mResult.p.companyAccount.reviewState) {
							case 0:
								review_state_text1.setText("未提交审核");
								break;
							case 1:
								review_state_text1.setText("已提交待审核");
								break;
							case 2:
								review_state_text1.setText("审核成功");
								break;
							case 3:
								review_state_text1.setText("审核失败");
								break;

							default:
								break;
							}
						} else {
							ToastUtils.makeText(mContext, "请求商家数据失败", 0).show();
						}
					} else {
						ToastUtils.makeText(mContext,
								R.string.again_login_text, 0).show();
						startForActivity(mContext, LoginActivity.class, null);
						finish();
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

			}
		});
	}
}
