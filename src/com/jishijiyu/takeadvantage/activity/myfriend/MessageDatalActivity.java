package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.SortAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.result.CheckPriceResult;
import com.jishijiyu.takeadvantage.entity.result.MyFriendResult.MyFriendsList;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.view.CharacterParser;
import com.jishijiyu.takeadvantage.view.PinyinComparator;
import com.jishijiyu.takeadvantage.view.SideBar;
import com.jishijiyu.takeadvantage.view.SideBar.OnTouchingLetterChangedListener;
import com.jishijiyu.takeadvantage.view.SortModel;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * 
 * 发消息
 * 
 * @author zhaobin
 */
@SuppressLint("NewApi")
public class MessageDatalActivity extends HeadBaseActivity {
	private Intent intent = null;
	private String nickname = null;

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("NewApi")
	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View
				.inflate(MessageDatalActivity.this, R.layout.chat, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		intent = getIntent();
		if (intent != null) {
			nickname = (String) intent
					.getSerializableExtra(HeadBaseActivity.intentKey);
		}
		initview(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initview(View view) {
		if (!TextUtils.isEmpty(nickname)) {
			top_text.setText(nickname);
		}
	}

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager()
					.finishActivity(MessageDatalActivity.this);
			break;

		}
	}

}
