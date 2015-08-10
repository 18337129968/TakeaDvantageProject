package com.jishijiyu.takeadvantage.activity.myinformation;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.EdittextUtil;

/**
 * 昵称 界面
 * 
 * @author shifeiyu
 * @version 2015年5月27日13:12:03
 */
public class MyNickNameActivity extends HeadBaseActivity {
	EditText nickname_edittext;
	TextView nickname_redhint_text, btn_nickname_save;
	ImageView btn_nickname_clearup;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.btn_nickname_clearup:
			nickname_edittext.setText("");
			break;
		case R.id.btn_nickname_save:
			setResult(RESULT_OK, (new Intent()).setAction(nickname_edittext
					.getText().toString()));
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.nickname_title);
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(MyNickNameActivity.this,
				R.layout.activity_my_nickname, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		nickname_edittext = (EditText) findViewById(R.id.nickname_edittext);
		nickname_redhint_text = (TextView) findViewById(R.id.nickname_redhint_text);
		btn_nickname_clearup = (ImageView) findViewById(R.id.btn_nickname_clearup);
		btn_nickname_save = (TextView) findViewById(R.id.btn_nickname_save);
		btn_nickname_clearup.setOnClickListener(this);
		btn_nickname_save.setOnClickListener(this);
		Intent intent = getIntent();
		String nickname = intent.getStringExtra("nickname");
		if (nickname.equals("未设置")) {
			nickname_edittext.setText("");
		} else {
			nickname_edittext.setText(nickname);
		}
		showredhinttext();
		if (nickname_edittext.getText().length() < 2) {
			btn_nickname_save.setClickable(false);
		} else {
			nickname_redhint_text.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 设置是否显示 提示昵称长度信息
	 */
	public void showredhinttext() {
		nickname_edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				count = nickname_edittext.getText().length();
				if (count < 2) {
					nickname_redhint_text.setVisibility(View.VISIBLE);
					btn_nickname_save.setClickable(false);
				} else {
					nickname_redhint_text.setVisibility(View.INVISIBLE);
					btn_nickname_save.setClickable(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}
}
