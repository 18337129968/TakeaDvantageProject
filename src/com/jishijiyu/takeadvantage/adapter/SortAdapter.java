package com.jishijiyu.takeadvantage.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.ernieonermb.InviteFriendsActivity;
import com.jishijiyu.takeadvantage.activity.myfriend.FriendFragmentActivity;
import com.jishijiyu.takeadvantage.activity.myfriend.MerchantCodeActivity;
import com.jishijiyu.takeadvantage.entity.request.InvitationFriends2Request;
import com.jishijiyu.takeadvantage.entity.request.InvitationFriends2Request.Parameter;
import com.jishijiyu.takeadvantage.entity.result.DelFriendResult;
import com.jishijiyu.takeadvantage.entity.result.InvitationFriendsResult;
import com.jishijiyu.takeadvantage.entity.result.InvitationFriendsResult.InvitationFriendsVOList;
import com.jishijiyu.takeadvantage.entity.result.MyFriendResult;
import com.jishijiyu.takeadvantage.entity.result.MyMerchantFriendResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.view.SortModel;

import android.R.integer;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	private int type;
	private Gson gson = null;
	private ViewHolder viewHolder = null;

	public SortAdapter(Context mContext, List<SortModel> list, int type) {
		this.mContext = mContext;
		this.list = list;
		this.type = type;
	}

	public int getCount() {
		return this.list != null ? list.size() : 0;
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		final SortModel mContent = list.get(position);
		if (view == null) {
			switch (type) {
			case InviteFriendsActivity.type:
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(
						R.layout.activity_invite_friends_listview_item, null);
				viewHolder.imageView = (ImageView) view.findViewById(R.id.img);
				viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
				viewHolder.btn_yq = (Button) view.findViewById(R.id.btn_invite);
				viewHolder.img_success = (ImageView) view
						.findViewById(R.id.img_success);
				viewHolder.tvLetter = (TextView) view
						.findViewById(R.id.catalog);
				break;

			default:
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(
						R.layout.my_friend_item, null);
				viewHolder.imageView = (ImageView) view.findViewById(R.id.img);
				viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
				viewHolder.tvLetter = (TextView) view
						.findViewById(R.id.catalog);
				break;
			}
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			if (type != InviteFriendsActivity.type) {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getSortLetters());
			} else {
				viewHolder.tvLetter.setVisibility(View.GONE);
			}

		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		String name = null;
		switch (type) {
		case FriendFragmentActivity.type:
			MyFriendResult.MyFriendsList friendsList = (MyFriendResult.MyFriendsList) list
					.get(position).getObject();
			if (friendsList != null) {
				name = friendsList.nickname;
				viewHolder.tvTitle.setText(name);
				ImageLoaderUtil.loadImage(friendsList.headImgUrl,
						viewHolder.imageView);
			}
			break;

		case MerchantCodeActivity.type:
			MyMerchantFriendResult.MyComList friendResultList = (MyMerchantFriendResult.MyComList) list
					.get(position).getObject();
			if (friendResultList != null) {
				viewHolder.tvTitle.setText(friendResultList.companyName);
				ImageLoaderUtil.loadImage(friendResultList.logoImgUrl,
						viewHolder.imageView);

			}
			break;
		case InviteFriendsActivity.type:
			final InvitationFriendsResult.InvitationFriendsVOList friendsVOList = (InvitationFriendsResult.InvitationFriendsVOList) list
					.get(position).getObject();
			if (friendsVOList != null) {
				viewHolder.tvTitle.setText(friendsVOList.nickname);
				ImageLoaderUtil.loadImage(friendsVOList.headImgUrl,
						viewHolder.imageView);
				viewHolder.btn_yq.setVisibility(View.VISIBLE);
				viewHolder.btn_yq.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						invitionFriend(viewHolder, friendsVOList,
								((InviteFriendsActivity) mContext).getRoomId()
										+ "", position);
					}
				});
			}
			break;
		}

		return view;

	}

	final static class ViewHolder {
		ImageView imageView;
		TextView tvLetter;
		TextView tvTitle;
		Button btn_yq;
		ImageView img_success;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public void refresh(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	/**
	 * 一元摇奖房间邀请加入房间
	 * 
	 * @author jseven1989
	 * @param @param holder
	 * @param @param friendsVOList
	 * @param @param roomId
	 * @return void
	 * @throws
	 */
	public void invitionFriend(final ViewHolder holder,
			final InvitationFriendsVOList friendsVOList, final String roomId,
			final int position) {
		if (gson == null) {
			gson = new Gson();
		}
		InvitationFriends2Request friendsRequest = new InvitationFriends2Request();
		Parameter pramater = friendsRequest.p;
		pramater.userId = GetUserIdUtil.getUserId(mContext);
		pramater.tokenId = GetUserIdUtil.getTokenId(mContext);
		pramater.roomId = roomId;
		pramater.friendsId = friendsVOList.friendsId;
		String json = gson.toJson(friendsRequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					DelFriendResult friendResult = gson.fromJson(result,
							DelFriendResult.class);
					if (friendResult.p.isTrue) {
						if (friendResult.p.isSucce) {
							// //
							// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
							EMConversation conversation = EMChatManager
									.getInstance().getConversation(
											friendsVOList.userId);
							// 创建一条文本消息
							EMMessage message = EMMessage
									.createSendMessage(EMMessage.Type.TXT);
							// 如果是群聊，设置chattype,默认是单聊
							message.setChatType(ChatType.Chat);
							// 设置消息body
							TextMessageBody txtBody = new TextMessageBody(
									"当前好友邀请你加入" + roomId + "号房间" + "iv");
							message.addBody(txtBody);
							// 设置接收人
							message.setReceipt(friendsVOList.userId);
							// 把消息加入到此会话对象中
							conversation.addMessage(message);
							// 发送消息
							EMChatManager.getInstance().sendMessage(message,
									new EMCallBack() {

										@Override
										public void onError(int arg0,
												final String arg1) {
											((InviteFriendsActivity) mContext)
													.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															ToastUtils
																	.makeText(
																			mContext,
																			arg1,
																			ToastUtils.LENGTH_SHORT)
																	.show();
														}
													});
										}

										@Override
										public void onProgress(int arg0,
												String arg1) {

										}

										@Override
										public void onSuccess() {
											((InviteFriendsActivity) mContext)
													.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															refresh_Del(position);
															ToastUtils
																	.makeText(
																			mContext,
																			"邀请成功！",
																			ToastUtils.LENGTH_SHORT)
																	.show();
														}
													});
										}
									});
						} else {
							ToastUtils.makeText(mContext, "邀请失败！",
									ToastUtils.LENGTH_SHORT).show();
						}
					} else {
						IntentActivity.mIntent(mContext);
					}
				} else {
					ToastUtils.makeText(mContext, "邀请失败！",
							ToastUtils.LENGTH_SHORT).show();
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(mContext, "邀请失败！", ToastUtils.LENGTH_SHORT)
						.show();
			}
		});

	}

	public void refresh_Del(int position) {
		list.remove(position);
		System.out.println("aaa");
		SortAdapter.this.notifyDataSetChanged();
	}
}