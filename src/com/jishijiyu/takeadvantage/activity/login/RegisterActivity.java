package com.jishijiyu.takeadvantage.activity.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.my.UseraGreementActivity;
import com.jishijiyu.takeadvantage.entity.request.GetVerificationCodeRequest;
import com.jishijiyu.takeadvantage.entity.request.RegisterRequest;
import com.jishijiyu.takeadvantage.entity.result.GetVerificationCodeResult;
import com.jishijiyu.takeadvantage.entity.result.GetVerificationCodeResult.Parameter;
import com.jishijiyu.takeadvantage.entity.result.RegisterResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.EdittextUtil;
import com.jishijiyu.takeadvantage.utils.EmojiUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 用户注册界面
 * 
 * @author shifeiyu
 * @version 2015年6月11日15:31:47
 */
public class RegisterActivity extends HeadBaseActivity {
	private RegisterRequest registerrequest;
	private GetVerificationCodeRequest getVerificationRequest;
	private TextView register_nextstep_btn, register_getverification_code_btn,
			user_agreement_text, register_clear_up_btn;
	private EditText register_phone_number_edittext,
			register_verification_code_edittext,
			register_enter_password_edittext,
			register_enter_password_again_edittext;
	private Dialog dialog;
	private String PhoneNumber, Password, again_Password;
	private Message msg;
	private static final int REGISTER_SUCCESS = 0;
	private static final int REGISTER_FAIL = 1;
	private static final int UPLOADING_FAIL = 2;
	private static final int PHONE_NUMBER_ERROR = 3;
	private static final int PASSWORD_ERROR = 4;
	private static final int PASSWORD_AGAIN_ERROR = 5;
	private static final int PHONE_NUMBER_NULL = 6;
	private static final int VERIFICATION_CODE_NULL = 7;
	private static final int PASSWORD_NULL = 8;
	private static final int PASSWORD_AGAIN_NULL = 9;
	private static final int PASSWORD_NOT_SAME = 10;
	private static final int PASSWORD_SHORT = 11;
	private static final int CODE_ERROR = 12;
	private static final int CODE_LOSE = 13;
	private static final int CODE_OK = 14;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case CODE_OK:
				ToastUtils.makeText(RegisterActivity.this, "验证码已发送", 0).show();
				break;
			case CODE_ERROR:
				ToastUtils.makeText(RegisterActivity.this, "验证码错误", 0).show();
				break;
			case CODE_LOSE:
				ToastUtils.makeText(RegisterActivity.this, "无效的验证码", 0).show();
				break;
			case UPLOADING_FAIL:
				ToastUtils.makeText(RegisterActivity.this, "访问服务器超时", 0).show();
				break;
			case REGISTER_SUCCESS:
				startForActivity(mContext, InvitationCodeActivity.class, null);
				finish();
				break;
			case REGISTER_FAIL:
				ToastUtils.makeText(RegisterActivity.this, "注册失败！", 0).show();
				break;
			case PHONE_NUMBER_ERROR:
				ToastUtils.makeText(RegisterActivity.this, "手机号有误\n请重新输入", 0)
						.show();
				break;
			case PHONE_NUMBER_NULL:
				ToastUtils.makeText(RegisterActivity.this, "请输入手机号", 0).show();
				break;
			case VERIFICATION_CODE_NULL:
				ToastUtils.makeText(RegisterActivity.this, "请输入验证码", 0).show();
				break;
			case PASSWORD_ERROR:
				ToastUtils.makeText(RegisterActivity.this, "密码格式不正确，请重新输入", 0)
						.show();
				break;
			case PASSWORD_AGAIN_ERROR:
				ToastUtils
						.makeText(RegisterActivity.this, "再次输入密码不正确，请重新输入", 0)
						.show();
				break;
			case PASSWORD_NULL:
				ToastUtils.makeText(RegisterActivity.this, "请输入密码", 0).show();
				break;
			case PASSWORD_AGAIN_NULL:
				ToastUtils.makeText(RegisterActivity.this, "请再次输入密码", 0).show();
				break;
			case PASSWORD_NOT_SAME:
				ToastUtils.makeText(RegisterActivity.this, "您输入的两次密码不一致，请重新输入",
						0).show();

				break;
			case PASSWORD_SHORT:
				ToastUtils
						.makeText(RegisterActivity.this, "您输入的密码过短\n请重新输入", 0)
						.show();
				break;
			default:
				break;

			}
		}

	};

	@Override
	public void appHead(View view) {
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText(R.string.register_title);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.register_nextstep_btn: // 点击“下一步”按钮
			initRegist();
			break;
		case R.id.register_getverification_code_btn: // 点击“获取验证码”按钮
			getVerificationCode();
			break;
		case R.id.user_agreement_text: // 用户注册使用协议
			startForActivity(RegisterActivity.this,
					UseraGreementActivity.class, null);
			break;
		case R.id.register_clear_up_btn:
			register_phone_number_edittext.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(RegisterActivity.this,
				R.layout.activity_register, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		register_nextstep_btn = (TextView) view
				.findViewById(R.id.register_nextstep_btn);
		register_getverification_code_btn = (TextView) view
				.findViewById(R.id.register_getverification_code_btn);
		user_agreement_text = (TextView) findViewById(R.id.user_agreement_text);
		register_phone_number_edittext = (EditText) findViewById(R.id.register_phone_number_edittext);
		register_verification_code_edittext = (EditText) findViewById(R.id.register_verification_code_edittext);
		register_enter_password_edittext = (EditText) findViewById(R.id.register_enter_password_edittext);
		register_enter_password_again_edittext = (EditText) findViewById(R.id.register_enter_password_again_edittext);
		register_clear_up_btn = (TextView) findViewById(R.id.register_clear_up_btn);
		user_agreement_text.setOnClickListener(this);
		register_getverification_code_btn.setOnClickListener(this);
		register_nextstep_btn.setOnClickListener(this);
		register_clear_up_btn.setOnClickListener(this);

		EdittextUtil.edittextForPhone(register_phone_number_edittext);
		EdittextUtil.edittextForPhone(register_verification_code_edittext);
		EdittextUtil.edittextForPhone(register_enter_password_edittext);
		EdittextUtil.edittextForPhone(register_enter_password_again_edittext);

		register_phone_number_edittext
				.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						if (TextUtils.isEmpty(s)) {
							register_clear_up_btn.setVisibility(View.INVISIBLE);
							register_clear_up_btn.setClickable(false);
						} else {
							register_clear_up_btn.setVisibility(View.VISIBLE);
							register_clear_up_btn.setClickable(true);
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});

	}

	/**
	 * 判断获取注册信息
	 */
	private void initRegist() {
		PhoneNumber = register_phone_number_edittext.getText().toString();
		Password = EmojiUtil.filterEmoji(register_enter_password_edittext
				.getText().toString());
		again_Password = EmojiUtil
				.filterEmoji(register_enter_password_again_edittext.getText()
						.toString());
		msg = Message.obtain();
		if (TextUtils.isEmpty(PhoneNumber)) {
			msg.what = PHONE_NUMBER_NULL;
			handler.sendMessage(msg);
			return;
		}
		if (PhoneNumber.length() < 11 || !isPhone(PhoneNumber)) {
			msg.what = PHONE_NUMBER_ERROR;
			handler.sendMessage(msg);
			return;
		}
		if (TextUtils.isEmpty(register_verification_code_edittext.getText())) {
			msg.what = VERIFICATION_CODE_NULL;
			handler.sendMessage(msg);
			return;
		}
		if (register_verification_code_edittext.getText().length() < 4) {
			ToastUtils.makeText(RegisterActivity.this, "请输入4位有效验证码", 0).show();
			return;
		}
		if (TextUtils.isEmpty(Password)) {
			msg.what = PASSWORD_NULL;
			handler.sendMessage(msg);
			return;
		}
		if (Password.length() < 6) {
			msg.what = PASSWORD_SHORT;
			handler.sendMessage(msg);
			return;
		}
		if (TextUtils.isEmpty(again_Password)) {
			msg.what = PASSWORD_AGAIN_NULL;
			handler.sendMessage(msg);
			return;
		}

		if (again_Password == null || again_Password.length() < 6
				|| again_Password.length() > 30) {
			msg.what = PASSWORD_AGAIN_ERROR;
			handler.sendMessage(msg);
			return;
		}
		if (!Password.equals(again_Password)) {
			msg.what = PASSWORD_NOT_SAME;
			handler.sendMessage(msg);
			return;
		} else {
			registerrequest = new RegisterRequest();
			com.jishijiyu.takeadvantage.entity.request.RegisterRequest.Parameter parameter = registerrequest.p;
			parameter.code = register_verification_code_edittext.getText()
					.toString();
			parameter.mobile = PhoneNumber;
			parameter.pwd = Password;
			Gson gson = new Gson();
			String json = gson.toJson(registerrequest);
			HttpConnectTool.update(json, RegisterActivity.this,
					new ConnectListener() {

						@Override
						public void contectSuccess(String result) {
							System.out.println(result);
							if (!TextUtils.isEmpty(result)) {
								Gson gson = new Gson();
								RegisterResult jsonObject = gson.fromJson(
										result, RegisterResult.class);
								com.jishijiyu.takeadvantage.entity.result.RegisterResult.Parameter parameter2 = jsonObject.p;
								switch (parameter2.type) {
								case 0:
									msg.what = REGISTER_SUCCESS;
									handler.sendMessage(msg);
									break;
								case 1:
									msg.what = PHONE_NUMBER_ERROR;
									handler.sendMessage(msg);
									break;
								case 2:
									msg.what = CODE_LOSE;
									handler.sendMessage(msg);
									break;
								case 3:
									msg.what = CODE_ERROR;
									handler.sendMessage(msg);
									break;
								case 4:
									msg.what = PASSWORD_ERROR;
									handler.sendMessage(msg);
									break;
								case 5:
									msg.what = REGISTER_FAIL;
									handler.sendMessage(msg);
									break;
								default:
									break;
								}
							} else {
								ToastUtils.makeText(mContext, "连接服务器超时", 0)
										.show();
							}

						}

						@Override
						public void contectStarted() {

						}

						@Override
						public void contectFailed(String path, String request) {
							msg.what = UPLOADING_FAIL;
							handler.sendMessage(msg);
						}
					});
		}

	}

	/**
	 * 获取验证码
	 */
	public void getVerificationCode() {
		msg = msg.obtain();
		PhoneNumber = register_phone_number_edittext.getText().toString();
		if (TextUtils.isEmpty(PhoneNumber)) {
			msg.what = PHONE_NUMBER_NULL;
			handler.sendMessage(msg);
			return;
		}
		if (PhoneNumber.length() < 11 || !isPhone(PhoneNumber)) {
			msg.what = PHONE_NUMBER_ERROR;
			handler.sendMessage(msg);
			return;
		} else {

			getVerificationRequest = new GetVerificationCodeRequest();
			getVerificationRequest.p.mobile = PhoneNumber;
			getVerificationRequest.p.type = "1";
			Gson gson = new Gson();
			String json = gson.toJson(getVerificationRequest);
			HttpConnectTool.update(json, RegisterActivity.this,
					new ConnectListener() {

						@Override
						public void contectSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								Gson gson = new Gson();
								GetVerificationCodeResult jsonobj = gson
										.fromJson(result,
												GetVerificationCodeResult.class);
								Parameter parameter = jsonobj.p;
								Log.e("code", "" + parameter.code);
								switch (parameter.code) {
								case 0:
									if (parameter.isSucce) {
										Timecount time = new Timecount(59999,
												1000);
										time.start();
										ToastUtils.makeText(
												RegisterActivity.this,
												"验证码已发送", 0).show();
									} else {
										ToastUtils.makeText(
												RegisterActivity.this,
												"验证码发送失败", 0).show();
									}
									break;
								case 1:
									dialog = DialogUtils.showDialog(
											RegisterActivity.this,
											"该手机号码已经注册,请用该号码\n直接登录", new int[] {
													R.string.end,
													R.string.login },
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													switch (v.getId()) {
													case R.id.left:
														dialog.cancel();
														break;
													case R.id.right:
														startForActivity(
																RegisterActivity.this,
																LoginActivity.class,
																null);
														finish();
														break;
													default:
														break;
													}

												}
											}, false);
									break;
								}

							} else {
								ToastUtils.makeText(mContext, "连接服务器超时", 0)
										.show();
							}

						}

						@Override
						public void contectStarted() {

						}

						@Override
						public void contectFailed(String path, String request) {
							msg.what = UPLOADING_FAIL;
							handler.sendMessage(msg);
						}
					});
		}
	}

	/**
	 * 判断格式是否为手机号码
	 */
	public boolean isPhone(String inputText) {
		Pattern p = Pattern.compile("^1[3578]\\d{9}$");
		Matcher m = p.matcher(inputText);
		return m.matches();

	}

	/**
	 * 
	 * 倒计时效果
	 * 
	 */
	class Timecount extends CountDownTimer {

		public Timecount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			register_getverification_code_btn
					.setBackgroundResource(R.drawable.btn_reget_code);
			register_getverification_code_btn.setTextColor(Color
					.parseColor("#DBDBDB"));
			register_getverification_code_btn.setEnabled(false);
			register_getverification_code_btn.setText(millisUntilFinished
					/ 1000 + "秒重新获取");
		}

		@Override
		public void onFinish() {
			register_getverification_code_btn
					.setBackgroundResource(R.drawable.btn_getcode);
			register_getverification_code_btn.setTextColor(Color
					.parseColor("#E7B9B2"));
			register_getverification_code_btn.setEnabled(true);
			register_getverification_code_btn.setText("获取验证码");

		}

	}

}
