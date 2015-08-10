package com.jishijiyu.takeadvantage.activity.makemoney;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.news.MyMessageActivity;
import com.jishijiyu.takeadvantage.entity.request.AdvertisingDetails;
import com.jishijiyu.takeadvantage.entity.request.AdvertisingDetails.ParameterD;
import com.jishijiyu.takeadvantage.entity.result.AdvertisingDetailsResult;
import com.jishijiyu.takeadvantage.entity.result.AdvertisingDetailsResult.poster;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.SPUtils;
import com.jishijiyu.takeadvantage.utils.String_U;

/**
 * 賺拍币看广告
 * 
 * @author zm
 * 
 */
public class WatchAdvertisementActivity extends HeadBaseActivity {
	private ImageView watchAdvertisementPicture;
	private Button consult;
	private TextView commodityDescribe, enterpriseBriefIntroduction, name,
			getIntegral;
	AdvertisingDetailsResult advertisingDetailsResult;
	poster poster;
	private Gson gson;
	private int integralistId;
	private String userId, tokenId;
	private String posterNum;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_integral:
			SimpleDateFormat systemTime = new SimpleDateFormat("yyyy年MM月dd日 ");
			Date date = new Date(System.currentTimeMillis());
			String str = systemTime.format(date);
			String day = str.substring(8, 10);
			SharedPreferences mySharedPreferences = getSharedPreferences(
					"data", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			String value = mySharedPreferences.getString("result", null);
			String userInfo = (String) SPUtils.get(
					WatchAdvertisementActivity.this,
					Constant.USER_INFO_FILE_NAME, "");
			LoginUserResult results = gson.fromJson(userInfo,
					LoginUserResult.class);
			if (results.p.user.answerTodyScore < results.p.answerMaxScore) {
				if (!TextUtils.isEmpty(value)) {
					String[] array = value.split("#");
					if (Integer.parseInt(array[0]) != Integer.parseInt(day)) {
						editor.putString("result", null);
						editor.commit();
						mIntent();
						return;
					}
					boolean is =false;
					for (int i = 1; i < array.length; i++) {

						if (String_U.equal(array[i], poster.id + "")) {
							System.out.println("广告ID："+array[i]);
							mDialog("今日已完成");
							return ;
						} else {
							is = true;

						}

				}
					if (is) {
						mIntent();
					}

				} else {
					mIntent();
				}
			} else {

				mDialog("拍币已到达上限，明日再来！");

			}

			break;
		case R.id.consult:
			startForActivity(WatchAdvertisementActivity.this,
					MyMessageActivity.class, null);
			break;
		default:
			break;
		}
	}

	public void mIntent() {
		Intent intent = new Intent();
		intent.setClass(WatchAdvertisementActivity.this,
				AnswerQuestionsActivity.class);
		intent.putExtra("integralistId", integralistId + "");
		intent.putExtra("poster", posterNum);
		startActivity(intent);
		finish();
	}

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.watch_advertisement));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void mDialog(String s) {
		DialogUtils.showDialog(this, s, new int[] { R.string.end },
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();
					}
				}, false);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(WatchAdvertisementActivity.this,
				R.layout.watch_advertisement, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		getIntegral = (TextView) findViewById(R.id.get_integral);
		watchAdvertisementPicture = (ImageView) findViewById(R.id.watch_advertisement_picture);
		name = (TextView) findViewById(R.id.name);
		integralistId = getIntent().getIntExtra("position", 0);
		posterNum = getIntent().getStringExtra("poster");
		commodityDescribe = (TextView) findViewById(R.id.commodity_describe);
		enterpriseBriefIntroduction = (TextView) findViewById(R.id.enterprise_brief_introduction);
		consult = (Button) findViewById(R.id.consult);
		userId = GetUserIdUtil.getUserId(this);
		tokenId = GetUserIdUtil.getTokenId(this);
		advertisingDetails();
	}

	public void mOnClick() {
		getIntegral.setOnClickListener(this);
		consult.setOnClickListener(this);
		name.setOnClickListener(this);
	}

	private void advertisingDetails() {
		AdvertisingDetails advertisingDetails = new AdvertisingDetails();
		advertisingDetails.setC(Constant.ADVERTISING_DETAILS_CODE);
		ParameterD parameterD = new ParameterD();
		parameterD.setPosterId(integralistId + "");
		parameterD.setTokenId(tokenId);
		parameterD.setUserId(userId);
		advertisingDetails.setP(parameterD);
		gson = new Gson();
		String json = gson.toJson(advertisingDetails);
		HttpConnectTool.update(json, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				advertisingDetailsResult = gson.fromJson(result,
						AdvertisingDetailsResult.class);
				if (advertisingDetailsResult.p.isTrue) {
				if (advertisingDetailsResult.p.poster != null) {
				
						mOnClick();
						AdvertisingDetailsResult.Parameter parameter1 = advertisingDetailsResult.p;

						poster = parameter1.poster;

						name.setText(poster.posterTitle);

						ImageLoaderUtil.loadImage(poster.posterImgUrl,
								watchAdvertisementPicture);

						commodityDescribe.setText(poster.posterDescribe);
						enterpriseBriefIntroduction.setText(""
								+ poster.companyDescribe);

					} 
				}else {
					IntentActivity.mIntent( WatchAdvertisementActivity.this);
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				Toast.makeText(mContext, request, 0).show();
			}
		});

	}
}
