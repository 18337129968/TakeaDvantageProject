package com.jishijiyu.takeadvantage.activity.myinformation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.DialogUtils;

public class SettingActivity extends HeadBaseActivity {
	LinearLayout change_pwd_layout;
	ImageView change_voice_btn;
	TextView exit_login_btn;
	boolean flag;
	Dialog dialog;
	SharedPreferences sp;
	String isVoice;
	String FILE_NAME = "SetVoiceMode";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.change_pwd_layout:
			startForActivity(SettingActivity.this,
					ChangePasswordActivity.class, null);
			break;
		case R.id.change_voice_btn:
			VoiceChange();
			isVoice();
			break;
		case R.id.exit_login_btn:
			dialog = DialogUtils.showDialog(SettingActivity.this, "确认退出？",
					new int[] { R.string.cancel, R.string.end },
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case R.id.left:
								dialog.dismiss();
								break;
							case R.id.right:
								Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
								startActivity(intent);
								dialog.dismiss();
								finish();
								break;
							default:
								break;
							}
						}
					}, false);
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
		top_text.setText("设置");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(SettingActivity.this,
				R.layout.activity_settings, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		change_pwd_layout = (LinearLayout) findViewById(R.id.change_pwd_layout);
		change_voice_btn = (ImageView) findViewById(R.id.change_voice_btn);
		exit_login_btn = (TextView) findViewById(R.id.exit_login_btn);
		change_voice_btn.setOnClickListener(this);
		change_pwd_layout.setOnClickListener(this);
		exit_login_btn.setOnClickListener(this);
		sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		isVoice = sp.getString("isVoice", "On");
		if (isVoice.equals("On")) {
			change_voice_btn.setBackgroundResource(R.drawable.on);
			flag = true;
		}
		Editor ed = sp.edit();
		ed.putString("isVoice", "On");
		ed.commit();
		isVoice();
	}

	public void isVoice() {
		if (flag) {
			if (sp == null) {
				sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
			}
			Editor ed = sp.edit();
			ed.putString("isVoice", "On");
			ed.commit();
		} else {
			if (sp == null) {
				sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
			}
			Editor ed = sp.edit();
			ed.putString("isVoice", "Off");
			ed.commit();
		}
	}

	public boolean VoiceChange() {
		if (flag) {
			change_voice_btn.setBackgroundResource(R.drawable.off);
			flag = false;
		} else {
			change_voice_btn.setBackgroundResource(R.drawable.on);
			flag = true;
		}
		return flag;

	}

}
