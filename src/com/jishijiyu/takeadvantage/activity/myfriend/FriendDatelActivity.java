package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.easemob.chat.EMContactManager;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.dao.InviteMessgeDao;
import com.jishijiyu.takeadvantage.activity.dao.UserDao;
import com.jishijiyu.takeadvantage.adapter.SortAdapter;
import com.jishijiyu.takeadvantage.entity.User;
import com.jishijiyu.takeadvantage.entity.request.DelFriendRequest;
import com.jishijiyu.takeadvantage.entity.request.FriendDatalRequest;
import com.jishijiyu.takeadvantage.entity.result.DelFriendResult;
import com.jishijiyu.takeadvantage.entity.result.FriendDatalResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.MyFriendResult;
import com.jishijiyu.takeadvantage.entity.result.AddFriendSearchResult.FriendList;
import com.jishijiyu.takeadvantage.entity.result.MyFriendResult.MyFriendsList;
import com.jishijiyu.takeadvantage.utils.AppManager;
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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 好友
 * 
 * @author zhaobin
 */
public class FriendDatelActivity extends HeadBaseActivity {
	private Button btn_v = null;
	private Button btn_del = null;
	private ImageView headImg = null;
	private TextView tv_name = null;
	private TextView tv_msg = null;
	private TextView tv_dh = null;
	private TextView tv_jf = null;
	private TextView tv_zj = null;
	private TextView tv_rw = null;
	private MyFriendResult.MyFriendsList myFriendsList = null;
	private FriendDatalResult datalResult = null;
	private Intent intent = null;
	private Gson gson = null;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.friend_datel_top));
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(FriendDatelActivity.this,
				R.layout.my_friend_datel, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		intent = getIntent();
		if (intent != null) {
			myFriendsList = (MyFriendsList) intent
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
		headImg = (ImageView) getView(view, R.id.headimg);
		tv_name = (TextView) getView(view, R.id.tv_name);
		tv_msg = (TextView) getView(view, R.id.tv_msg);
		tv_dh = (TextView) getView(view, R.id.tv_dh);
		tv_jf = (TextView) getView(view, R.id.tv_jf);
		tv_zj = (TextView) getView(view, R.id.tv_zj);
		tv_rw = (TextView) getView(view, R.id.tv_rw);
		btn_v = (Button) getView(view, R.id.btn_send_message);
		btn_del = (Button) getView(view, R.id.btn_del);
		btn_v.setOnClickListener(this);
		btn_del.setOnClickListener(this);
		if (myFriendsList != null) {
			getResult();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(FriendDatelActivity.this);
			break;
		case R.id.btn_send_message:
			if (datalResult.p.friendDetails != null) {
				Intent intent = new Intent(FriendDatelActivity.this,
						ChatActivity.class);
				intent.putExtra(HeadBaseActivity.intentKey,
						datalResult.p.friendDetails.nickname);
				intent.putExtra(HeadBaseActivity.intentKey + 1,
						datalResult.p.friendDetails.userId);
				startActivity(intent);
			}
			break;
		case R.id.btn_del:
			if (datalResult.p.friendDetails != null) {
				delFriend(datalResult.p.friendDetails.userId);
			}
			break;
		}
	}

	public void getResult() {
		gson = new Gson();
		FriendDatalRequest request = new FriendDatalRequest();
		FriendDatalRequest.Pramater pramater = request.p;
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		pramater.friendId = myFriendsList.userId;
		String json = gson.toJson(request);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					datalResult = gson
							.fromJson(result, FriendDatalResult.class);
					if (datalResult != null) {
						if (datalResult.p.isTrue) {
							FriendDatalResult.FriendDetails details = datalResult.p.friendDetails;
							if (details != null) {
								ImageLoaderUtil.loadImage(details.headImgUrl,
										headImg);
								tv_name.setText(details.nickname);
								tv_msg.setText("adsfasdf");
								tv_dh.setText(details.exchange);
								tv_jf.setText(details.score);
								tv_zj.setText(details.wins);
								tv_rw.setText(details.task);
							}
						} else {
							IntentActivity.mIntent(FriendDatelActivity.this);
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

	private void delFriend(final String userId) {
		gson = new Gson();
		LoginUserResult login = GetUserIdUtil.getLogin(this);
		if (login == null || login.p.user == null) {
			return;
		}
		LoginUserResult.User user = login.p.user;
		DelFriendRequest request = new DelFriendRequest();
		DelFriendRequest.Pramater pramater = request.p;
		pramater.userId = user.id + "";
		pramater.tokenId = login.p.tokenId;
		pramater.friendId = userId;
		switch (user.type) {
		case 0:
			pramater.ownerId = "";
			break;
		case 1:
			pramater.ownerId = user.id + "_com";
			break;
		}
		String json = gson.toJson(request);
		HttpConnectTool.update(json, this, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					DelFriendResult delFriendResult = gson.fromJson(result,
							DelFriendResult.class);
					if (delFriendResult != null) {
						if (delFriendResult.p.isTrue) {
							if (delFriendResult.p.isSucce) {
								try {
									// 删除此联系人
									deleteContact(userId);
									// 删除相关的邀请消息
									InviteMessgeDao dao = new InviteMessgeDao(
											FriendDatelActivity.this);
									dao.deleteMessage(userId);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								ToastUtils.makeText(FriendDatelActivity.this,
										"删除失败！", ToastUtils.LENGTH_SHORT)
										.show();
							}
						} else {
							IntentActivity.mIntent(FriendDatelActivity.this);
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
	 * 删除联系人
	 * 
	 * @param toDeleteUser
	 */
	public void deleteContact(final String userId) {
		String st1 = "正在删除...";
		final String st2 = "删除失败:";
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().deleteContact(userId);
					// 删除db和内存中此用户的数据
					UserDao dao = new UserDao(FriendDatelActivity.this);
					dao.deleteContact(userId);
					App.getInstance().getContactList().remove(userId);
					runOnUiThread(new Runnable() {
						public void run() {
							ToastUtils.makeText(FriendDatelActivity.this,
									"删除成功！", ToastUtils.LENGTH_SHORT).show();
						}
					});
					AppManager.getAppManager().finishActivity(
							FriendDatelActivity.this);
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(FriendDatelActivity.this,
									st2 + e.getMessage(), 1).show();
						}
					});

				}

			}
		}).start();

	}

}
