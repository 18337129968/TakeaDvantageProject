package com.jishijiyu.takeadvantage.activity.signin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.SignInRequest;
import com.jishijiyu.takeadvantage.entity.request.SignInRequestInfo;
import com.jishijiyu.takeadvantage.entity.result.SignInResult;
import com.jishijiyu.takeadvantage.entity.result.SignInResultInfo;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntegralOperationfUtil;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 签到
 * 
 * @author tml
 */
public class SignInActivity extends Activity {

	/** popupwindow 销毁 */
	private static final int POP_DISMISS = 0;
	/** 显示加载动画 */
	private static final int LOADING_VIEW = 1;
	/** 获取数据成功 */
	private static final int LOADING_VIEW_SUCCESS = 2;
	/** 获取数据失败 */
	private static final int LOADING_VIEW_FAILED = 3;
	/** 已经签到 */
	private static final int ALREAD_SIGN_IN = 4;
	/** 累计签到 */
	private static final int ADD_SIGN_IN = 5;
	/** 重新签到 */
	private static final int AFRESH_SIGN_IN = 6;

	@ViewInject(R.id.img_btn_top_left)
	private Button img_btn_top_left;
	@ViewInject(R.id.img_btn_top_right)
	private Button img_btn_top_right;
	@ViewInject(R.id.top_text)
	private TextView top_text;
	@ViewInject(R.id.btn_sign_in)
	private Button btn_sign_in;
	@ViewInject(R.id.iv_sign_in_add7)
	private Button iv_sign_in_add7;
	@ViewInject(R.id.iv_sign_in_branch6)
	private ImageView iv_sign_in_branch6;
	@ViewInject(R.id.iv_sign_in_branch_gray6)
	private ImageView iv_sign_in_branch_gray6;
	@ViewInject(R.id.iv_sign_in_add6)
	private Button iv_sign_in_add6;
	@ViewInject(R.id.iv_sign_in_branch5)
	private ImageView iv_sign_in_branch5;
	@ViewInject(R.id.iv_sign_in_branch_gray5)
	private ImageView iv_sign_in_branch_gray5;
	@ViewInject(R.id.iv_sign_in_add5)
	private Button iv_sign_in_add5;
	@ViewInject(R.id.iv_sign_in_branch4)
	private ImageView iv_sign_in_branch4;
	@ViewInject(R.id.iv_sign_in_branch_gray4)
	private ImageView iv_sign_in_branch_gray4;
	@ViewInject(R.id.iv_sign_in_add4)
	private Button iv_sign_in_add4;
	@ViewInject(R.id.iv_sign_in_branch3)
	private ImageView iv_sign_in_branch3;
	@ViewInject(R.id.iv_sign_in_branch_gray3)
	private ImageView iv_sign_in_branch_gray3;
	@ViewInject(R.id.iv_sign_in_add3)
	private Button iv_sign_in_add3;
	@ViewInject(R.id.iv_sign_in_branch2)
	private ImageView iv_sign_in_branch2;
	@ViewInject(R.id.iv_sign_in_branch_gray2)
	private ImageView iv_sign_in_branch_gray2;
	@ViewInject(R.id.iv_sign_in_add2)
	private Button iv_sign_in_add2;
	@ViewInject(R.id.iv_sign_in_branch1)
	private ImageView iv_sign_in_branch1;
	@ViewInject(R.id.iv_sign_in_branch_gray1)
	private ImageView iv_sign_in_branch_gray1;
	@ViewInject(R.id.iv_sign_in_add1)
	private Button iv_sign_in_add1;
	private Context mContext;
	private int days = 0;
	private long lastSignDate;
	/**
	 * true 可以签到 false 不可以签到
	 * */
	private boolean signIn = false;
	private boolean isIn = false;
	private PopupWindow popupWindow;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case POP_DISMISS:
				popupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};
	private SignInResultInfo signInResultInfo;
	private Gson gson;
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		mContext = SignInActivity.this;
		ViewUtils.inject(this);
		img_btn_top_left.setVisibility(View.VISIBLE);
		top_text.setText("今日签到");
		img_btn_top_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getData();
	}

	private void getData() {
		gson = new Gson();
		SignInRequestInfo signInRequest = new SignInRequestInfo();
		signInRequest.p.userId = GetUserIdUtil.getUserId(mContext);
		signInRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		String request = gson.toJson(signInRequest);
		HttpConnectTool.update(request, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String resultString) {
				if (!isIn) {
					signInResultInfo = gson.fromJson(resultString,
							SignInResultInfo.class);
					LogUtil.i("isTrue"+signInResultInfo.p.isTrue);
					if (signInResultInfo.p.isTrue) {
						days = signInResultInfo.p.sign.days;
						signIn = signInResultInfo.p.canSign;
						LogUtil.i("days" + days + "------" + "signIn:"
								+ signInResultInfo.p.canSign);
						initData();
						isIn = true;
					} else {
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivityForResult(intent, Constant.AGAIN_LOGIN_CODE);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String requestJson) {
				btn_sign_in.setClickable(false);
				Toast.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void initData() {
		addCound();
		if (!signIn) {
			isSignIn();
		} else {

			btn_sign_in.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SignInRequest signInRequest = new SignInRequest();
					signInRequest.p.userId = GetUserIdUtil.getUserId(mContext);
					signInRequest.p.tokenId=GetUserIdUtil.getTokenId(mContext);
					String request = gson.toJson(signInRequest);
					HttpConnectTool.update(request, mContext,
							new ConnectListener() {

								@Override
								public void contectSuccess(String resultString) {
									SignInResult signInResult = gson.fromJson(
											resultString, SignInResult.class);
									LogUtil.i("signInResult.p.isSucce:"
											+ signInResult.p.isSucce);
									if (signInResult.p.isTrue) {
										if (signInResult.p.isSucce) {
											showPop();
											LogUtil.i("days++" + days);
											days++;
											addScore(days);
											signIn = false;
											addCound();
											isSignIn();
											
										} else {
											Toast.makeText(mContext, "请再次尝试签到",
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Intent intent = new Intent(mContext,
												LoginActivity.class);
										startActivityForResult(intent, Constant.AGAIN_LOGIN_CODE);
									}
								}

								@Override
								public void contectStarted() {

								}

								@Override
								public void contectFailed(String path,
										String requestJson) {

								}
							});
				}
			});
		}
	}

	protected void addScore(int day) {
		switch (day) {
		case 1:
			IntegralOperationfUtil.addIntegral(mContext, 1);
			break;
		case 2:
			IntegralOperationfUtil.addIntegral(mContext, 1);
			break;
		case 3:
			IntegralOperationfUtil.addIntegral(mContext, 2);
			break;
		case 4:
			IntegralOperationfUtil.addIntegral(mContext, 2);
			break;
		case 5:
			IntegralOperationfUtil.addIntegral(mContext, 3);
			break;
		case 6:
			IntegralOperationfUtil.addIntegral(mContext, 3);
			break;
		case 7:
			IntegralOperationfUtil.addIntegral(mContext, 5);
			break;
		default:
			break;
		}
	}

	protected void showPop() {
		View view = View.inflate(mContext, R.layout.pop_sign_in, null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.showAsDropDown(top_text);
		Message message = handler.obtainMessage();
		message.what = POP_DISMISS;
		handler.sendMessageDelayed(message, 1000);
	}

	protected void addCound() {
		switch (days) {
		case 1:
			signIn1(false);
			break;
		case 2:
			signIn1(true);
			signIn2(false);
			break;
		case 3:
			signIn1(true);
			signIn2(true);
			signIn3(false);
			break;
		case 4:
			signIn1(true);
			signIn2(true);
			signIn3(true);
			signIn4(false);
			break;
		case 5:
			signIn1(true);
			signIn2(true);
			signIn3(true);
			signIn4(true);
			signIn5(false);
			break;
		case 6:
			signIn1(true);
			signIn2(true);
			signIn3(true);
			signIn4(true);
			signIn5(true);
			signIn6(false);
			break;
		case 7:
			signIn1(true);
			signIn2(true);
			signIn3(true);
			signIn4(true);
			signIn5(true);
			signIn6(true);
			signIn7();
			break;
		default:
			break;
		}
	}

	private void isSignIn() {
		LogUtil.i("signIn:" + signIn);
		if (!signIn) {
			btn_sign_in.setText("已签到");
			btn_sign_in.setClickable(false);
		}
	}

	private void signIn7() {
		iv_sign_in_add7.setBackgroundResource(R.drawable.sign_in_yes);
	}

	private void signIn6(boolean b) {
		iv_sign_in_branch6.setVisibility(View.VISIBLE);
		if (b) {
			iv_sign_in_branch6
					.setBackgroundResource(R.drawable.sign_branch_left);
		}
		iv_sign_in_branch_gray6.setVisibility(View.INVISIBLE);
		iv_sign_in_add6.setBackgroundResource(R.drawable.sign_in_yes);
	}

	private void signIn5(boolean b) {
		iv_sign_in_branch5.setVisibility(View.VISIBLE);
		if (b) {
			iv_sign_in_branch5
					.setBackgroundResource(R.drawable.sign_branch_right);
		}
		iv_sign_in_branch_gray5.setVisibility(View.INVISIBLE);
		iv_sign_in_add5.setBackgroundResource(R.drawable.sign_in_yes);
	}

	private void signIn4(boolean b) {
		iv_sign_in_branch4.setVisibility(View.VISIBLE);
		if (b) {
			iv_sign_in_branch4
					.setBackgroundResource(R.drawable.sign_branch_left);
		}
		iv_sign_in_branch_gray4.setVisibility(View.INVISIBLE);
		iv_sign_in_add4.setBackgroundResource(R.drawable.sign_in_yes);
	}

	private void signIn3(boolean b) {
		iv_sign_in_branch3.setVisibility(View.VISIBLE);
		if (b) {
			iv_sign_in_branch3
					.setBackgroundResource(R.drawable.sign_branch_right);
		}
		iv_sign_in_branch_gray3.setVisibility(View.INVISIBLE);
		iv_sign_in_add3.setBackgroundResource(R.drawable.sign_in_yes);
	}

	private void signIn2(boolean b) {
		iv_sign_in_branch2.setVisibility(View.VISIBLE);
		if (b) {
			iv_sign_in_branch2
					.setBackgroundResource(R.drawable.sign_branch_left);
		}
		iv_sign_in_branch_gray2.setVisibility(View.INVISIBLE);
		iv_sign_in_add2.setBackgroundResource(R.drawable.sign_in_yes);
	}

	private void signIn1(boolean b) {
		iv_sign_in_branch1.setVisibility(View.VISIBLE);
		if (b) {
			iv_sign_in_branch1
					.setBackgroundResource(R.drawable.sign_branch_right);
		}
		iv_sign_in_branch_gray1.setVisibility(View.INVISIBLE);
		iv_sign_in_add1.setBackgroundResource(R.drawable.sign_in_yes);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode==Constant.AGAIN_LOGIN_CODE) {
			getData();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
