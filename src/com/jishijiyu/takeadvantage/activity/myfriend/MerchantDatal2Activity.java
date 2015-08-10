package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.dao.InviteMessgeDao;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.SortAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.InviteMessage;
import com.jishijiyu.takeadvantage.entity.InviteMessage.InviteMesageStatus;
import com.jishijiyu.takeadvantage.entity.request.AddFriendRequest;
import com.jishijiyu.takeadvantage.entity.request.FriendDatalRequest;
import com.jishijiyu.takeadvantage.entity.result.AddMerchantSearchResult;
import com.jishijiyu.takeadvantage.entity.result.CheckPriceResult;
import com.jishijiyu.takeadvantage.entity.result.DelFriendResult;
import com.jishijiyu.takeadvantage.entity.result.FriendDatalResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.AddFriendSearchResult.FriendList;
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
import com.jishijiyu.takeadvantage.view.CharacterParser;
import com.jishijiyu.takeadvantage.view.PinyinComparator;
import com.jishijiyu.takeadvantage.view.SideBar;
import com.jishijiyu.takeadvantage.view.SideBar.OnTouchingLetterChangedListener;
import com.jishijiyu.takeadvantage.view.SortModel;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 发消息
 * 
 * @author zhaobin
 */
@SuppressLint("NewApi")
public class MerchantDatal2Activity extends HeadBaseActivity {
	private static final int SECCUSE = 0;
	private static final int FAILE = 1;
	private Button btn_v = null;
	private Button btn_del = null;
	private ImageView img_logo = null;
	private TextView tv_name = null;
	private TextView tv_msg = null;
	private AddMerchantSearchResult.ComList comList = null;
	private MerchantDatalResult datalResult = null;
	private String name = null;
	private Gson gson = null;
	private InviteMessgeDao messgeDao;
	private InviteMessage inviteMessage = null;
	private MerchantDatalResult.Company details = null;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SECCUSE:

				break;
			case FAILE:
				ToastUtils.makeText(MerchantDatal2Activity.this, "同意失败！",
						ToastUtils.LENGTH_SHORT).show();
				break;
			}
		};
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
		View view = View.inflate(MerchantDatal2Activity.this,
				R.layout.merchant_datal, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		if (intent != null) {
			name = intent.getStringExtra("name");
			if (name != null && name.equals("addmerchant")) {
				comList = (AddMerchantSearchResult.ComList) intent
						.getSerializableExtra(HeadBaseActivity.intentKey);
			} else if (name != null && name.equals("newfriend")) {
				details = (Company) intent
						.getSerializableExtra(HeadBaseActivity.intentKey);
				inviteMessage = (InviteMessage) intent
						.getSerializableExtra(HeadBaseActivity.intentKey + 1);
			}
		}
		messgeDao = new InviteMessgeDao(this);
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
		btn_v = (Button) getView(view, R.id.btn_send_message);
		btn_del = (Button) getView(view, R.id.btn_del);
		img_logo = (ImageView) getView(view, R.id.img_logo);
		tv_name = (TextView) getView(view, R.id.tv_name);
		tv_msg = (TextView) getView(view, R.id.tv_msg);
		btn_del.setVisibility(View.GONE);
		btn_v.setText(getResources().getString(R.string.add));
		btn_v.setOnClickListener(this);

		if (name != null && name.equals("addmerchant")) {
			btn_v.setText(getResources().getString(R.string.add));
		} else if (name != null && name.equals("newfriend")) {
			btn_v.setText("同意");
		}
		if (comList != null || details != null) {
			getResult();
		}
	}

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(
					MerchantDatal2Activity.this);
			break;
		case R.id.btn_v:
			if (name != null && name.equals("addmerchant")) {
				if (comList != null) {
					Intent intent = new Intent();
					intent.setClass(MerchantDatal2Activity.this,
							AddFriend_SendMsgActivity.class);
					intent.putExtra(HeadBaseActivity.intentKey, comList);
					intent.putExtra("name", "addmerchant");
					startActivity(intent);
				}
			} else if (name != null && name.equals("newfriend")) {
				if (inviteMessage != null) {
					addFriend(inviteMessage);
				}
			}
			break;

		}
	}

	public void getResult() {
		final Gson gson = new Gson();
		FriendDatalRequest request = new FriendDatalRequest();
		request.c = Constant.MERCHANT_DATAL_CODE;
		FriendDatalRequest.Pramater pramater = request.p;
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		pramater.friendId = comList.userId;
		String json = gson.toJson(request);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					datalResult = gson.fromJson(result,
							MerchantDatalResult.class);
					if (result != null) {
						if (datalResult.p.isTrue) {
							MerchantDatalResult.Company details = datalResult.p.company;
							if (details != null) {
								ImageLoaderUtil.loadImage(details.logoImgUrl,
										img_logo);
								tv_name.setText(details.companyName);
								tv_msg.setText(details.companyDescribe);
							}
						} else {
							IntentActivity.mIntent(MerchantDatal2Activity.this);
						}
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {

			}
		});
	}

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	private void acceptInvitation(final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(this);
		String str1 = "正在同意...";
		final String str2 = "已同意";
		final String str3 = "同意失败:";
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if (msg.getGroupId() == null) // 同意好友请求
						EMChatManager.getInstance().acceptInvitation(
								msg.getFrom());
					else
						// 同意加群申请
						EMGroupManager.getInstance().acceptApplication(
								msg.getFrom(), msg.getGroupId());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							msg.setStatus(InviteMesageStatus.AGREED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg
									.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
						}
					});

				} catch (final Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(MerchantDatal2Activity.this,
									str3 + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
	}

	private void addFriend(final InviteMessage msg) {
		LoginUserResult login = GetUserIdUtil.getLogin(this);
		if (login == null || login.p.user == null) {
			return;
		}
		LoginUserResult.User user = login.p.user;
		AddFriendRequest addFriendRequest = new AddFriendRequest();
		AddFriendRequest.Pramater pramater = addFriendRequest.p;
		pramater.friendId = msg.getFrom();
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		switch (user.type) {
		case 0:
			pramater.ownerId = "";
			break;
		case 1:
			pramater.ownerId = user.id + "_com";
			break;
		}
		if (gson == null) {
			gson = new Gson();
		}
		String json = gson.toJson(addFriendRequest);
		Log.e("Request", json);
		HttpConnectTool.update(json, this, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					Log.e("Result", result);
					DelFriendResult addFriendResult = gson.fromJson(result,
							DelFriendResult.class);
					if (addFriendResult != null) {
						if (addFriendResult.p.isTrue) {
							if (addFriendResult.p.isSucce) {
								acceptInvitation(msg);
							} else {
								handler.sendEmptyMessage(FAILE);
							}
						} else {
							IntentActivity.mIntent(MerchantDatal2Activity.this);
						}
					} else {
						handler.sendEmptyMessage(FAILE);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				handler.sendEmptyMessage(FAILE);
			}
		});
	}
}
