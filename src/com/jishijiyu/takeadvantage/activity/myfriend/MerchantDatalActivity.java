package com.jishijiyu.takeadvantage.activity.myfriend;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.dao.InviteMessgeDao;
import com.jishijiyu.takeadvantage.activity.dao.UserDao;
import com.jishijiyu.takeadvantage.entity.request.DelFriendRequest;
import com.jishijiyu.takeadvantage.entity.request.FriendDatalRequest;
import com.jishijiyu.takeadvantage.entity.result.DelFriendResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.MerchantDatalResult;
import com.jishijiyu.takeadvantage.entity.result.MyMerchantFriendResult.MyComList;
import com.jishijiyu.takeadvantage.utils.*;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

/**
 * 
 * 商家详情
 * 
 * @author zhaobin
 */
public class MerchantDatalActivity extends HeadBaseActivity {
	private Button btn_v = null;
	private Button btn_del = null;
	private ImageView img_logo = null;
	private TextView tv_name = null;
	private TextView tv_msg = null;
	private MyComList comList = null;
	private MerchantDatalResult datalResult = null;
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
		View view = View.inflate(MerchantDatalActivity.this,
				R.layout.merchant_datal, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		intent = getIntent();
		if (intent != null) {
			comList = (MyComList) intent
					.getSerializableExtra(HeadBaseActivity.intentKey);
		}
		initview(view);
	}

	private void initview(View view) {
		btn_v = (Button) getView(view, R.id.btn_send_message);
		btn_del = (Button) getView(view, R.id.btn_del);
		img_logo = (ImageView) getView(view, R.id.img_logo);
		tv_name = (TextView) getView(view, R.id.tv_name);
		tv_msg = (TextView) getView(view, R.id.tv_msg);
		btn_v.setOnClickListener(this);
		btn_del.setOnClickListener(this);
		if (comList != null) {
			getResult();
		}
	}

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(
					MerchantDatalActivity.this);
			break;
		case R.id.btn_send_message:
			if (datalResult.p.company != null) {
				Intent intent = new Intent(this, ChatActivity.class);
				intent.putExtra(HeadBaseActivity.intentKey,
						datalResult.p.company.companyName);
				intent.putExtra(HeadBaseActivity.intentKey + 2,
						datalResult.p.company.userId);
				startActivity(intent);
			}
			break;
		case R.id.btn_del:
			if (datalResult.p.company != null) {
				delFriend(datalResult.p.company.userId);
			}
			break;
		}
	}

	public void getResult() {
		gson = new Gson();
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
							IntentActivity.mIntent(MerchantDatalActivity.this);
						}
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(MerchantDatalActivity.this,
						getResources().getString(R.string.net_no),
						ToastUtils.LENGTH_LONG).show();
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
		pramater.friendId = userId + "_com";
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
									deleteContact(userId + "_com");
									// 删除相关的邀请消息
									InviteMessgeDao dao = new InviteMessgeDao(
											MerchantDatalActivity.this);
									dao.deleteMessage(userId);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								ToastUtils.makeText(MerchantDatalActivity.this,
										"删除失败！", ToastUtils.LENGTH_SHORT)
										.show();
							}
						} else {
							IntentActivity.mIntent(MerchantDatalActivity.this);
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
					UserDao dao = new UserDao(MerchantDatalActivity.this);
					dao.deleteContact(userId);
					App.getInstance().getContactList().remove(userId);
					runOnUiThread(new Runnable() {
						public void run() {
							ToastUtils.makeText(MerchantDatalActivity.this,
									"删除成功！", ToastUtils.LENGTH_SHORT).show();
						}
					});
					AppManager.getAppManager().finishActivity(
							MerchantDatalActivity.this);
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MerchantDatalActivity.this,
									st2 + e.getMessage(), 1).show();
						}
					});

				}

			}
		}).start();

	}

}
