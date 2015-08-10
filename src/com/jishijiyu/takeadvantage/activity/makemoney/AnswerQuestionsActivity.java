package com.jishijiyu.takeadvantage.activity.makemoney;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.ShowGoldAnimActivity;
import com.jishijiyu.takeadvantage.activity.exchangemall.FirmOrderActivity;
import com.jishijiyu.takeadvantage.entity.request.AdvertisingDetails;
import com.jishijiyu.takeadvantage.entity.request.AdvertisingDetails.ParameterD;
import com.jishijiyu.takeadvantage.entity.request.RequestAnswerToIntegral;
import com.jishijiyu.takeadvantage.entity.request.RequestAnswerToIntegral.ParameteSr;
import com.jishijiyu.takeadvantage.entity.result.AdvertisingResult;
import com.jishijiyu.takeadvantage.entity.result.ConfirmReceivingResult;
import com.jishijiyu.takeadvantage.receiver.MyReceiver;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntegralOperationfUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.jishijiyu.takeadvantage.utils.String_U;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 做任务、赚拍币界面
 * 
 * @author zm
 * @version 2015年5月26日14:57:46
 */
public class AnswerQuestionsActivity extends HeadBaseActivity {
	private RadioGroup rgp_select;
	private RadioButton rbn_one, rbn_two, rbn_three;
	private ImageView imv_imageView;
	private TextView reward_integral_text, tv_title;
	String[] questArray;
	private String mResult = null;
	private TextView ok_btn;
	private AdvertisingResult jsonobj;
	Dialog dialog;
	private String userId, tokenId;
	ShareUtil share;
	private String id;
	private String poster;
	String[] information;
	View view;

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.doing_task_title);
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(
						AnswerQuestionsActivity.this);
			}
		});
	}

	/**
	 * 注册控件信息
	 */
	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		view = View.inflate(AnswerQuestionsActivity.this,
				R.layout.activity_answer_questions, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		share = new ShareUtil(this, mContext);
		id = getIntent().getStringExtra("integralistId");
		poster = getIntent().getStringExtra("poster");
		userId = GetUserIdUtil.getUserId(AnswerQuestionsActivity.this);
		tokenId = GetUserIdUtil.getTokenId(AnswerQuestionsActivity.this);
		rgp_select = (RadioGroup) view.findViewById(R.id.rgp_select);
		rbn_one = (RadioButton) view.findViewById(R.id.rbn_one);
		rbn_two = (RadioButton) view.findViewById(R.id.rbn_two);
		rbn_three = (RadioButton) view.findViewById(R.id.rbn_three);
		imv_imageView = (ImageView) view.findViewById(R.id.imv_imageView);
		reward_integral_text = (TextView) view
				.findViewById(R.id.reward_integral_text);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		ok_btn = (TextView) view.findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(this);
		getHttpData();

	}

	SimpleDateFormat systemTime = new SimpleDateFormat("yyyy年MM月dd日 ");
	Date date = new Date(System.currentTimeMillis());
	String str = systemTime.format(date);
	String day = str.substring(8, 10);

	public void getTime() {
		SharedPreferences mySharedPreferences = getSharedPreferences("data",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		String data = mySharedPreferences.getString("result", null);

		if (data == null) {
			editor.putString("result", day + "#" + id);
		} else {
			information = data.split("#");
			if (Integer.parseInt(information[0]) != Integer.parseInt(day)) {
				editor.putString("result", day + "#" + id);
			} else {
				editor.putString("result", data + "#" + id);

			}
		}
		editor.commit();

	}

	public void getHttpData() {
		AdvertisingDetails advertisingDetails = new AdvertisingDetails();
		advertisingDetails.setC(Constant.ADVERTISING_DETAILS_CODE);
		ParameterD parameterD = new ParameterD();
		parameterD.setPosterId(id);
		parameterD.setTokenId(tokenId);
		parameterD.setUserId(userId);
		advertisingDetails.setP(parameterD);
		final Gson gson = new Gson();
		String json = gson.toJson(advertisingDetails);
		// String result = HttpConnectUtil.updata(json, false, this, null);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				System.out.println("答题页面第一个接口："+result);
				if (!TextUtils.isEmpty(result)) {
					jsonobj = gson.fromJson(result, AdvertisingResult.class);
					if (jsonobj.getP().isTrue()) {
						if (jsonobj.getP().getPoster() != null) {

							ImageLoaderUtil.loadImage(jsonobj.getP()
									.getPoster().getPosterImgUrl(),
									imv_imageView);
							if (jsonobj.getP().getPoster().getQuestionOption()
									.indexOf("#") != -1) {
								questArray = jsonobj.getP().getPoster()
										.getQuestionOption().split("#");
							}
							rbn_one.setText(questArray[0]);
							rbn_two.setText(questArray[1]);
							rbn_three.setText(questArray[2]);
							tv_title.setText(jsonobj.getP().getPoster()
									.getQuestionDescribe());
							reward_integral_text.setText("悬赏拍币：" + poster
									+ "拍币");
						}
					} else {
						IntentActivity.mIntent(AnswerQuestionsActivity.this);
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

		rgp_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.rbn_one:
					if (rbn_one.isChecked()) {
						mResult = "A";
					}
					break;
				case R.id.rbn_two:
					if (rbn_two.isChecked()) {
						mResult = "B";
					}
					break;
				case R.id.rbn_three:
					if (rbn_three.isChecked()) {
						mResult = "C";
					}
					break;

				default:
					break;
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.ok_btn: // 点击“确定”按钮
			if (jsonobj.getP().getPoster() != null) {
				if (mResult != null) {
					if (String_U.equal(mResult, jsonobj.getP().getPoster()
							.getAnswer())) {
						postHttpData();
					} else {
						ToastUtils.makeText(AnswerQuestionsActivity.this,
								"答案错误，请重新选择", Toast.LENGTH_SHORT).show();
					}
				} else {
					ToastUtils.makeText(AnswerQuestionsActivity.this, "请选择答案！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				ToastUtils.makeText(AnswerQuestionsActivity.this,
						"题目获取失败请稍后在再试", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}

	};

	public void postHttpData() {
		RequestAnswerToIntegral advertisingDetails = new RequestAnswerToIntegral();
		advertisingDetails.setC(Constant.ANSWER_TO_INTEGRAL);
		ParameteSr parameter = new ParameteSr();
		parameter.setPosterId(id);
		parameter.setAnswer(mResult);
		System.out.println("mResult:"+mResult);
		parameter.setUserId(userId);
		parameter.setTokenId(tokenId);
		advertisingDetails.setP(parameter);
		final Gson gson = new Gson();
		String json = gson.toJson(advertisingDetails);
		System.out.println("答题发送的请求："+json);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				System.out.println("答题获取拍币："+result);
				if (!TextUtils.isEmpty(result)) {
					ConfirmReceivingResult confirmReceivingResult = gson
							.fromJson(result, ConfirmReceivingResult.class);
					if (confirmReceivingResult.getP().isSucce()) {
						//答题成功执行方法
						ShowGoldAnimActivity activity = new ShowGoldAnimActivity(
								mContext);
						activity.showPopWindows(AnswerQuestionsActivity.this,
								view, poster, false);
						IntegralOperationfUtil.addAnswerIntegral(
								AnswerQuestionsActivity.this,
								Integer.parseInt(poster));
						dialog = DialogUtils.showDialog(
								AnswerQuestionsActivity.this, "恭喜你获得" + poster
										+ "拍币", "分享即可获赠拍币", new int[] {
										R.string.continue_answer_questions,
										R.string.share_friend },
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										switch (v.getId()) {
										case R.id.left:
											getTime();
											dialog.cancel();
											finish();
											break;
										case R.id.right:
											share.postShare();
											dialog.cancel();
											MyReceiver myReceiver = new MyReceiver() {
												public void dynamic() {
													finish();
												};
											};
											IntentFilter dynamic_filter = new IntentFilter();
											dynamic_filter.addAction(Constant.DYNAMICACTION); //
									
											// 添加动态广播的Action
											registerReceiver(myReceiver, dynamic_filter); // 注册自定义动态广播消息
											//finish();
											break;
										default:
											break;
										}
									}
								});
						dialog.setCanceledOnTouchOutside(false);
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
