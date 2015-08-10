/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jishijiyu.takeadvantage.adapter;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.dao.InviteMessgeDao;
import com.jishijiyu.takeadvantage.activity.ernie.CheckPriceActivity;
import com.jishijiyu.takeadvantage.entity.InviteMessage;
import com.jishijiyu.takeadvantage.entity.InviteMessage.InviteMesageStatus;
import com.jishijiyu.takeadvantage.entity.request.AddErnieRequest;
import com.jishijiyu.takeadvantage.entity.request.DelFriendRequest;
import com.jishijiyu.takeadvantage.entity.request.AddErnieRequest.Pramater;
import com.jishijiyu.takeadvantage.entity.request.AddFriendRequest;
import com.jishijiyu.takeadvantage.entity.result.AddErnieResult;
import com.jishijiyu.takeadvantage.entity.result.CheckPriceResult;
import com.jishijiyu.takeadvantage.entity.result.DelFriendResult;
import com.jishijiyu.takeadvantage.entity.result.FriendDatalResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.FriendDatalResult.FriendDetails;
import com.jishijiyu.takeadvantage.entity.result.MerchantDatalResult;
import com.jishijiyu.takeadvantage.entity.result.MerchantDatalResult.Company;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constants;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {
	private static final int SECCUSE = 0;
	private static final int FAILE = 1;
	private Context context;
	private InviteMessgeDao messgeDao;
	private List<Serializable> datals;
	private Gson gson = null;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SECCUSE:

				break;
			case FAILE:
				ToastUtils.makeText(context, "同意失败！", ToastUtils.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	public NewFriendsMsgAdapter(Context context, int textViewResourceId,
			List<InviteMessage> objects, List<Serializable> datals) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.datals = datals;
		messgeDao = new InviteMessgeDao(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.row_invite_msg, null);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			holder.groupContainer = (LinearLayout) convertView
					.findViewById(R.id.ll_group);
			holder.groupname = (TextView) convertView
					.findViewById(R.id.tv_groupName);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String str1 = "已同意你的好友请求";
		String str2 = "同意";

		String str3 = "请求加你为好友";
		String str4 = "申请加入群：";
		String str5 = "已同意";
		String str6 = "已拒绝";
		final InviteMessage msg = getItem(position);
		if (msg != null) {
			if (msg.getGroupId() != null) { // 显示群聊提示
				holder.groupContainer.setVisibility(View.VISIBLE);
				holder.groupname.setText(msg.getGroupName());
			} else {
				holder.groupContainer.setVisibility(View.GONE);
			}
			holder.reason.setText(msg.getReason());
			String name = "";
			String imgUrl = "";
			if (datals != null && datals.size() > 0) {
				if (msg.getFrom().indexOf("_com") != -1) {
					MerchantDatalResult.Company company = (Company) datals
							.get(position);
					if (company != null) {
						name = company.companyName;
						imgUrl = company.logoImgUrl;
					} else {
						name = msg.getFrom();
					}
				} else {

					FriendDatalResult.FriendDetails detail = (FriendDetails) datals
							.get(position);
					if (detail != null) {
						name = detail.nickname;
						imgUrl = detail.headImgUrl;
					} else {
						name = msg.getFrom();
					}
				}
			} else {
				name = msg.getFrom();
			}
			ImageLoaderUtil.loadImage(imgUrl, holder.avator);

			holder.name.setText(name);
			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			if (msg.getStatus() == InviteMesageStatus.BEAGREED) {
				holder.status.setVisibility(View.INVISIBLE);
				holder.reason.setText(str1);
			} else if (msg.getStatus() == InviteMesageStatus.BEINVITEED
					|| msg.getStatus() == InviteMesageStatus.BEAPPLYED) {
				holder.status.setVisibility(View.VISIBLE);
				holder.status.setEnabled(true);
				holder.status
						.setBackgroundResource(android.R.drawable.btn_default);
				holder.status.setText(str2);
				if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {
					if (msg.getReason() == null) {
						// 如果没写理由
						holder.reason.setText(str3);
					}
				} else { // 入群申请
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.reason.setText(str4 + msg.getGroupName());
					}
				}
				// 设置点击事件
				holder.status.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 同意别人发的好友请求
						addFriend(holder.status, msg);
					}
				});
			} else if (msg.getStatus() == InviteMesageStatus.AGREED) {
				holder.status.setText(str5);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			} else if (msg.getStatus() == InviteMesageStatus.REFUSED) {
				holder.status.setText(str6);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			}

			// 设置用户头像
		}

		return convertView;
	}

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	private void acceptInvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
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
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							button.setText(str2);
							msg.setStatus(InviteMesageStatus.AGREED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg
									.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);
							handler.sendEmptyMessage(SECCUSE);
						}
					});

				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, str3 + e.getMessage(), 1)
									.show();
						}
					});

				}
			}
		}).start();
	}

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView reason;
		Button status;
		LinearLayout groupContainer;
		TextView groupname;
		// TextView time;
	}

	private void addFriend(final Button button, final InviteMessage msg) {
		LoginUserResult login = GetUserIdUtil.getLogin(context);
		if (login == null || login.p.user == null) {
			return;
		}
		LoginUserResult.User user = login.p.user;
		AddFriendRequest addFriendRequest = new AddFriendRequest();
		AddFriendRequest.Pramater pramater = addFriendRequest.p;
		pramater.friendId = msg.getFrom();
		pramater.userId = GetUserIdUtil.getUserId(context);
		pramater.tokenId = GetUserIdUtil.getTokenId(context);
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
		HttpConnectTool.update(json, context, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					Log.e("Result", result);
					DelFriendResult addFriendResult = gson.fromJson(result,
							DelFriendResult.class);
					if (addFriendResult != null) {
						if (addFriendResult.p.isTrue) {
							if (addFriendResult.p.isSucce) {
								acceptInvitation(button, msg);
							} else {
								handler.sendEmptyMessage(FAILE);
							}
						} else {
							IntentActivity.mIntent(context);
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
