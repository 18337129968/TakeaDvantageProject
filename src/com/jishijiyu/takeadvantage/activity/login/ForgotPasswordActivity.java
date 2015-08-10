package com.jishijiyu.takeadvantage.activity.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.entity.request.ForgotPasswordRequest;
import com.jishijiyu.takeadvantage.entity.request.GetVerificationCodeRequest;
import com.jishijiyu.takeadvantage.entity.result.ForgotPasswordResult;
import com.jishijiyu.takeadvantage.entity.result.GetVerificationCodeResult;
import com.jishijiyu.takeadvantage.entity.result.GetVerificationCodeResult.Parameter;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.EdittextUtil;
import com.jishijiyu.takeadvantage.utils.EmojiUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.TaskUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 忘记密码/找回密码界面
 * 
 * @author shifeiyu
 * @version 2015年6月10日18:03:09
 * 
 */
public class ForgotPasswordActivity extends HeadBaseActivity {
	String new_pwd, again_pwd;
	GetVerificationCodeRequest getVerificationRequest;
	TextView fgpw_getverification_code_btn, fgpw_done_btn;
	EditText fgpw_phone_number_edittext, fgpw_verification_code_edittext,
			fgpw_enter_password_edittext, fgpw_enter_password_again_edittext;
	Dialog dialog;

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.forgot_password_title);
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.fgpw_getverification_code_btn: // 点击“获取验证码”按钮
			getVerificationCode();
			break;
		case R.id.fgpw_done_btn: // 点击“完成”按钮
			initUserInfo();
			break;
		default:
			break;
		}
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(ForgotPasswordActivity.this,
				R.layout.activity_forgot_password, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		fgpw_getverification_code_btn = (TextView) findViewById(R.id.fgpw_getverification_code_btn);
		fgpw_done_btn = (TextView) findViewById(R.id.fgpw_done_btn);
		fgpw_phone_number_edittext = (EditText) findViewById(R.id.fgpw_phone_number_edittext);
		fgpw_verification_code_edittext = (EditText) findViewById(R.id.fgpw_verification_code_edittext);
		fgpw_enter_password_edittext = (EditText) findViewById(R.id.fgpw_enter_password_edittext);
		fgpw_enter_password_again_edittext = (EditText) findViewById(R.id.fgpw_enter_password_again_edittext);
		fgpw_getverification_code_btn.setOnClickListener(this);
		fgpw_done_btn.setOnClickListener(this);
		EdittextUtil.edittextForPhone(fgpw_phone_number_edittext);
		EdittextUtil.edittextForPhone(fgpw_verification_code_edittext);
		EdittextUtil.edittextForPhone(fgpw_enter_password_edittext);
		EdittextUtil.edittextForPhone(fgpw_enter_password_again_edittext);
	}

	/**
	 * 判断获取注册信息
	 */
	private void initUserInfo() {

		new_pwd = EmojiUtil.filterEmoji(fgpw_enter_password_edittext.getText()
				.toString());
		again_pwd = EmojiUtil.filterEmoji(fgpw_enter_password_again_edittext
				.getText().toString());
		if (TextUtils.isEmpty(fgpw_phone_number_edittext.getText())) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "请输入手机号", 0)
					.show();
			return;
		}
		if (fgpw_phone_number_edittext.getText().length() < 11
				|| !isPhone(fgpw_phone_number_edittext.getText().toString())) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "手机号有误\n请重新输入", 0)
					.show();
			return;
		}
		if (TextUtils.isEmpty(fgpw_verification_code_edittext.getText())) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "请输入验证码", 0)
					.show();
			return;
		}
		if (fgpw_verification_code_edittext.getText().length() < 4) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "请输入4位有效验证码", 0)
					.show();
			return;
		}
		if (TextUtils.isEmpty(new_pwd)) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "请输入新密码", 0)
					.show();
			return;
		}
		if (new_pwd.length() < 6) {
			dialog = DialogUtils.showDialog(ForgotPasswordActivity.this,
					"您输入的密码过短，请重新输入", new int[] { R.string.end },
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();

						}
					}, false);
			TaskUtil.afterTimeRun(dialog, 1);
			return;
		}
		if (new_pwd.length() > 30) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "密码长度不符", 0)
					.show();
			return;
		}
		if (TextUtils.isEmpty(again_pwd)) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "请再次输入新密码", 0)
					.show();
			return;
		}

		if (again_pwd == null || again_pwd.length() < 6
				|| again_pwd.length() > 30) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "新密码长度不符", 0)
					.show();
			return;
		}
		if (!new_pwd.equals(again_pwd)) {
			dialog = DialogUtils.showDialog(ForgotPasswordActivity.this,
					"您输入的两次新密码不一致，请重新输入", new int[] { R.string.end },
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();

						}
					}, false);
			TaskUtil.afterTimeRun(dialog, 1);
			return;
		} else {
			ForgotPasswordRequest forgotpassRequest = new ForgotPasswordRequest();
			com.jishijiyu.takeadvantage.entity.request.ForgotPasswordRequest.Parameter parameter = forgotpassRequest.p;
			parameter.code = fgpw_verification_code_edittext.getText()
					.toString();
			parameter.mobile = fgpw_phone_number_edittext.getText().toString();
			parameter.newPwd = new_pwd;
			Gson gson = new Gson();
			String json = gson.toJson(forgotpassRequest);
			Log.e("Request", json);
			HttpConnectTool.update(json, ForgotPasswordActivity.this,
					new ConnectListener() {

						@Override
						public void contectSuccess(String result) {
							Gson gson = new Gson();
							ForgotPasswordResult forgotresult = gson.fromJson(
									result, ForgotPasswordResult.class);
							com.jishijiyu.takeadvantage.entity.result.ForgotPasswordResult.Parameter parameter1 = forgotresult.p;

							switch (parameter1.type) {
							case 0:
								ToastUtils.makeText(
										ForgotPasswordActivity.this, "密码修改成功",
										0).show();
								startForActivity(ForgotPasswordActivity.this,
										LoginActivity.class, null);
								finish();
								break;
							case 1:
								dialog = DialogUtils.showDialog(
										ForgotPasswordActivity.this,
										"该手机号未被注册过", new int[] { R.string.end,
												R.string.register_text },
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												switch (v.getId()) {
												case R.id.left:
													dialog.dismiss();
													break;
												case R.id.right:
													startForActivity(
															ForgotPasswordActivity.this,
															RegisterActivity.class,
															null);
													finish();
													break;
												}
											}
										}, false);

								break;
							case 2:
								ToastUtils.makeText(
										ForgotPasswordActivity.this, "无效的验证码",
										0).show();
								break;
							case 3:
								ToastUtils.makeText(
										ForgotPasswordActivity.this,
										"验证码有误,请重新输入", 0).show();
								break;
							case 4:
								ToastUtils.makeText(
										ForgotPasswordActivity.this, "新密码长度不符",
										0).show();
								break;
							default:
								break;
							}

						}

						@Override
						public void contectStarted() {

						}

						@Override
						public void contectFailed(String path, String request) {
							ToastUtils.makeText(ForgotPasswordActivity.this,
									"访问服务器失败", 0).show();
						}
					});
		}

	}

	/**
	 * 获取验证码
	 */
	public void getVerificationCode() {
		if (TextUtils.isEmpty(fgpw_phone_number_edittext.getText())) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "请输入手机号", 0)
					.show();
			return;
		}
		if (fgpw_phone_number_edittext.getText().length() < 11
				|| !isPhone(fgpw_phone_number_edittext.getText().toString())) {
			ToastUtils.makeText(ForgotPasswordActivity.this, "手机号有误\n请重新输入", 0)
					.show();
			return;
		} else {

			getVerificationRequest = new GetVerificationCodeRequest();
			getVerificationRequest.p.mobile = fgpw_phone_number_edittext
					.getText().toString();
			getVerificationRequest.p.type = "3";
			Gson gson = new Gson();
			String json = gson.toJson(getVerificationRequest);
			if (json != null) {
				Log.e("Request", json);
			}
			HttpConnectTool.update(json, ForgotPasswordActivity.this,
					new ConnectListener() {

						@Override
						public void contectSuccess(String result) {
							if (!TextUtils.isEmpty(result)) {
								Gson gson = new Gson();
								GetVerificationCodeResult jsonobj = gson
										.fromJson(result,
												GetVerificationCodeResult.class);
								Parameter parameter = jsonobj.p;
								if (parameter.code == 2) {
									dialog = DialogUtils.showDialog(
											ForgotPasswordActivity.this,
											"该手机号未被注册过", new int[] {
													R.string.end,
													R.string.register_text },
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													switch (v.getId()) {
													case R.id.left:
														dialog.dismiss();
														break;
													case R.id.right:
														startForActivity(
																ForgotPasswordActivity.this,
																RegisterActivity.class,
																null);
														finish();
														break;
													}
												}
											}, false);
								} else {
									if (parameter.isSucce == true) {
										Timecount time = new Timecount(59999,
												1000);
										time.start();
										ToastUtils.makeText(
												ForgotPasswordActivity.this,
												"验证码已发送", 0).show();
									} else {
										ToastUtils.makeText(
												ForgotPasswordActivity.this,
												"验证码发送失败", 0).show();
									}
								}
							} else {
								ToastUtils.makeText(
										ForgotPasswordActivity.this, "验证码发送失败",
										0).show();
							}

						}

						@Override
						public void contectStarted() {

						}

						@Override
						public void contectFailed(String path, String request) {
							ToastUtils.makeText(ForgotPasswordActivity.this,
									"访问服务器超时", 0).show();
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
			fgpw_getverification_code_btn
					.setBackgroundResource(R.drawable.btn_reget_code);
			fgpw_getverification_code_btn.setTextColor(Color
					.parseColor("#DBDBDB"));
			fgpw_getverification_code_btn.setEnabled(false);
			fgpw_getverification_code_btn.setText(millisUntilFinished / 1000
					+ "秒重新获取");
		}

		@Override
		public void onFinish() {
			fgpw_getverification_code_btn
					.setBackgroundResource(R.drawable.btn_getcode);
			fgpw_getverification_code_btn.setTextColor(Color
					.parseColor("#E7B9B2"));
			fgpw_getverification_code_btn.setEnabled(true);
			fgpw_getverification_code_btn.setText("获取验证码");
		}

	}

}
