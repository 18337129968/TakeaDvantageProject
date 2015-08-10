package com.jishijiyu.takeadvantage.activity.myfriend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.dao.InviteMessgeDao;
import com.jishijiyu.takeadvantage.activity.home.HomeActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.adapter.NewFriendsMsgAdapter;
import com.jishijiyu.takeadvantage.entity.InviteMessage;
import com.jishijiyu.takeadvantage.entity.request.FriendDatalRequest;
import com.jishijiyu.takeadvantage.entity.result.FriendDatalResult;
import com.jishijiyu.takeadvantage.entity.result.FriendDatalResult.FriendDetails;
import com.jishijiyu.takeadvantage.entity.result.MerchantDatalResult;
import com.jishijiyu.takeadvantage.entity.result.MerchantDatalResult.Company;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * 
 * 新朋友
 * 
 * @author zhaobin
 * @param <T>
 */
@SuppressLint("NewApi")
public class NewFriendActivity extends HeadBaseActivity {
	private ListView listView = null;
	private Gson gson = null;
	private List<Serializable> datals = null;
	private int i = 0;
	List<InviteMessage> msgs;
	private static final int SUCCESS = 0;
	private static final int FAIL = 1;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				i++;
				int n = msgs.size();
				if (msgs.size() > i) {
					getResult(msgs.get(i).getFrom());
				} else {
					i = 0;
					// 设置adapter
					NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(
							NewFriendActivity.this, 1, msgs, datals);
					listView.setAdapter(adapter);
					// App.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME)
					// .setUnreadMsgCount(0);
				}
				break;
			case FAIL:
				break;
			}
		}
	};

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.new_friend_top));
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("NewApi")
	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(NewFriendActivity.this,
				R.layout.my_new_friend, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initview(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		datals = new ArrayList<Serializable>();
		InviteMessgeDao dao = new InviteMessgeDao(this);
		msgs = dao.getMessagesList();
		if (msgs != null && msgs.size() > 0) {
			getResult(msgs.get(i).getFrom());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initview(View view) {
		listView = (ListView) view.findViewById(R.id.new_friend_list);
		listView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				InviteMessage inviteMessage = msgs.get(arg2);
				Intent intent = new Intent();
				if (inviteMessage.getFrom().indexOf("_com") != -1) {
					intent.setClass(NewFriendActivity.this,
							MerchantDatal2Activity.class);
				} else {
					intent.setClass(NewFriendActivity.this,
							FriendDatel2Activity.class);

				}
				intent.putExtra(HeadBaseActivity.intentKey,
						(Serializable) datals.get(arg2));
				intent.putExtra(HeadBaseActivity.intentKey + 1,
						(Serializable) msgs.get(arg2));
				intent.putExtra("name", "newfriend");
				startActivity(intent);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(NewFriendActivity.this);
			break;

		}
	}

	private String id = null;

	public void getResult(String userId) {
		id = userId;
		gson = new Gson();
		FriendDatalRequest request = new FriendDatalRequest();
		if (userId.indexOf("_com") != -1) {
			request.c = Constant.MERCHANT_DATAL_CODE;
			id = userId.substring(0, userId.length() - 1);
		}
		FriendDatalRequest.Pramater pramater = request.p;
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		pramater.friendId = userId;
		String json = gson.toJson(request);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					if (id.substring(id.length() - 1, id.length()).equals(
							"_com")) {
						MerchantDatalResult datalResult = gson.fromJson(result,
								MerchantDatalResult.class);
						if (result != null) {
							if (datalResult.p.isTrue) {
								MerchantDatalResult.Company details = datalResult.p.company;
								if (details != null) {
									datals.add(details);
								}
							} else {
								IntentActivity.mIntent(NewFriendActivity.this);
							}
						}
					} else {
						FriendDatalResult datalResult = gson.fromJson(result,
								FriendDatalResult.class);
						if (datalResult != null) {
							if (datalResult.p.isTrue) {
								FriendDatalResult.FriendDetails details = datalResult.p.friendDetails;
								if (details != null) {
									datals.add(details);
								}
							} else {
								IntentActivity.mIntent(NewFriendActivity.this);
							}
						}
					}
					handler.sendEmptyMessage(SUCCESS);
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				handler.sendEmptyMessage(FAIL);
				ToastUtils.makeText(NewFriendActivity.this,
						getResources().getString(R.string.net_no),
						ToastUtils.LENGTH_LONG).show();
			}
		});
	}
}
