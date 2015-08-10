package com.jishijiyu.takeadvantage.activity.login;

import java.util.Timer;
import java.util.TimerTask;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.home.HomeActivity;
import com.jishijiyu.takeadvantage.entity.request.InvitationCodeRequest;
import com.jishijiyu.takeadvantage.entity.request.InvitationCodeRequest.Parameter;
import com.jishijiyu.takeadvantage.entity.result.InvitationCodeResult;
import com.jishijiyu.takeadvantage.entity.result.RegisterResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.FileUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 输入邀请码界面
 * 
 * @author shifeiyu
 * @version 2015年5月29日18:50:59
 * 
 */
public class InvitationCodeActivity extends HeadBaseActivity {
	InvitationCodeRequest invitationcodeRequest;
	InvitationCodeResult invitationcodeResult;
	TextView register_done_btn;
	View invitation_skip_text;
	Timer timer;
	TimerTask task;
	EditText invitation_code_edittext;

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.register_title);
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.invitation_skip_text: // 点击“跳过”按钮
			ToastUtils.makeText(InvitationCodeActivity.this, "注册成功",
					Toast.LENGTH_SHORT).show();
			startForActivity(InvitationCodeActivity.this, LoginActivity.class,
					null);
			finish();
			break;
		case R.id.register_done_btn: // 点击“完成注册”按钮
			if (TextUtils.isEmpty(invitation_code_edittext.getText())) {
				ToastUtils.makeText(InvitationCodeActivity.this, "请输入邀请码", 0)
						.show();
			} else {
				Gson gson = new Gson();
				String userJson = FileUtil.readFile(
						InvitationCodeActivity.this,
						Constant.USER_INFO_FILE_NAME);
				RegisterResult s = gson
						.fromJson(userJson, RegisterResult.class);
				invitationcodeRequest = new InvitationCodeRequest();
				Parameter parameter = invitationcodeRequest.p;
				parameter.inviteCode = invitation_code_edittext.getText()
						.toString();
				parameter.userId = String.valueOf(s.p.user.id);
				String json = gson.toJson(invitationcodeRequest);
				HttpConnectTool.update(json, InvitationCodeActivity.this,
						new ConnectListener() {

							@Override
							public void contectSuccess(String result) {
								Gson gson = new Gson();
								InvitationCodeResult jsonObject = gson
										.fromJson(result,
												InvitationCodeResult.class);
								com.jishijiyu.takeadvantage.entity.result.InvitationCodeResult.Parameter parameter2 = jsonObject.p;
								switch (parameter2.type) {
								case 0:
									ToastUtils.makeText(
											InvitationCodeActivity.this,
											"注册成功", 0).show();
									startForActivity(
											InvitationCodeActivity.this,
											HomeActivity.class, null);
									finish();
									break;
								case 1:
									ToastUtils.makeText(
											InvitationCodeActivity.this,
											"邀请码已被使用过", 0).show();
									break;
								case 2:
									ToastUtils.makeText(
											InvitationCodeActivity.this,
											"邀请码不正确", 0).show();
									break;

								default:
									break;
								}
							}

							@Override
							public void contectStarted() {

							}

							@Override
							public void contectFailed(String path,
									String request) {
								ToastUtils.makeText(
										InvitationCodeActivity.this, "访问服务器超时",
										0).show();
							}
						});

			}
			break;
		default:
			break;
		}
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(InvitationCodeActivity.this,
				R.layout.activity_invitation_code, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		invitation_skip_text = findViewById(R.id.invitation_skip_text);
		register_done_btn = (TextView) findViewById(R.id.register_done_btn);
		invitation_code_edittext = (EditText) findViewById(R.id.invitation_code_edittext);
		invitation_skip_text.setOnClickListener(this);
		register_done_btn.setOnClickListener(this);
	}

}
