package com.jishijiyu.takeadvantage.activity.myinformation;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.ChangePwdRequest;
import com.jishijiyu.takeadvantage.entity.result.ChangePwdResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.EdittextUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

public class ChangePasswordActivity extends HeadBaseActivity {
	EditText old_pwd_edittext, new_pwd_edittext, new_pwd_again_edittext;
	TextView change_pwd_ok_btn;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.change_pwd_ok_btn:
			CheckPwd();
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText("修改密码");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(ChangePasswordActivity.this,
				R.layout.activity_change_pwd, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		old_pwd_edittext = (EditText) findViewById(R.id.old_pwd_edittext);
		new_pwd_edittext = (EditText) findViewById(R.id.new_pwd_edittext);
		new_pwd_again_edittext = (EditText) findViewById(R.id.new_pwd_again_edittext);
		change_pwd_ok_btn = (TextView) findViewById(R.id.change_pwd_ok_btn);
		change_pwd_ok_btn.setOnClickListener(this);
		
		EdittextUtil.edittextForPhone(old_pwd_edittext);
		EdittextUtil.edittextForPhone(new_pwd_edittext);
		EdittextUtil.edittextForPhone(new_pwd_again_edittext);
	}

	/**
	 * 检查用户输入信息
	 */
	public void CheckPwd() {
		if (TextUtils.isEmpty(old_pwd_edittext.getText())) {
			ToastUtils.makeText(ChangePasswordActivity.this, "请输入原密码", 0)
					.show();
			return;
		}
		if (old_pwd_edittext.getText().length()<6||
				old_pwd_edittext.getText().length()>30) {
			ToastUtils.makeText(ChangePasswordActivity.this, "原密码长度不符", 0)
			.show();
			return;
		}
		
		if (TextUtils.isEmpty(new_pwd_edittext.getText())) {
			ToastUtils.makeText(ChangePasswordActivity.this, "请输入新密码", 0)
					.show();
			return;
		}
		if (new_pwd_edittext.getText().length()<6||
				new_pwd_edittext.getText().length()>30) {
			ToastUtils.makeText(ChangePasswordActivity.this, "新密码长度不符", 0)
			.show();
			return;
		}
		if (TextUtils.isEmpty(new_pwd_again_edittext.getText())) {
			ToastUtils.makeText(ChangePasswordActivity.this, "请再次输入新密码", 0)
					.show();
			return;
		}
		if (!new_pwd_edittext.getText().toString()
				.equals(new_pwd_again_edittext.getText().toString())) {
			ToastUtils.makeText(ChangePasswordActivity.this, "两次输入的密码不一致", 0)
					.show();
			return;
		} else {
			Gson gson = new Gson();
			ChangePwdRequest changePwd = new ChangePwdRequest();
			com.jishijiyu.takeadvantage.entity.request.ChangePwdRequest.Parameter parameter1 = changePwd.p;
			parameter1.userId = GetUserIdUtil
					.getUserId(ChangePasswordActivity.this);
			parameter1.tokenId = GetUserIdUtil
					.getTokenId(ChangePasswordActivity.this);
			parameter1.oldPwd = old_pwd_edittext.getText().toString();
			parameter1.newPwd = new_pwd_edittext.getText().toString();
			String changePwdRqJson = gson.toJson(changePwd);
			HttpConnectTool.update(changePwdRqJson,
					ChangePasswordActivity.this, new ConnectListener() {

						@Override
						public void contectSuccess(String result) {
							Gson gson = new Gson();
							ChangePwdResult PwdResult = new ChangePwdResult();
							PwdResult = gson.fromJson(result,
									ChangePwdResult.class);
							com.jishijiyu.takeadvantage.entity.result.ChangePwdResult.Parameter parameter2 = PwdResult.p;
							if (parameter2.isTrue) {
								switch (parameter2.type) {
								case 1:
									ToastUtils.makeText(
											ChangePasswordActivity.this,
											"原密码不符合", 0).show();
									break;
								case 2:
									ToastUtils.makeText(
											ChangePasswordActivity.this,
											"新密码长度不够", 0).show();
									break;
								case 3:
									ToastUtils.makeText(
											ChangePasswordActivity.this,
											"密码修改成功", 0).show();
									finish();
									break;

								default:
									break;
								}
							}else{
								ToastUtils.makeText(ChangePasswordActivity.this, R.string.again_login_text, 0).show();
								Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
								startActivity(intent);
							}

						}

						@Override
						public void contectStarted() {

						}

						@Override
						public void contectFailed(String path, String request) {
							ToastUtils.makeText(ChangePasswordActivity.this,
									"访问服务器超时", 0).show();
						}
					});
		}
	}

}
