package com.jishijiyu.takeadvantage.activity.ernieonermb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * @author zhengjianxiong
 * @content 邀请好友列表
 * @time 2015-7-13
 */

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.SortAdapter;
import com.jishijiyu.takeadvantage.entity.request.InvitationFriends2Request;
import com.jishijiyu.takeadvantage.entity.request.InvitationFriendsRequest;
import com.jishijiyu.takeadvantage.entity.request.InvitationFriendsRequest.Parameter;
import com.jishijiyu.takeadvantage.entity.result.DelFriendResult;
import com.jishijiyu.takeadvantage.entity.result.InvitationFriendsResult;
import com.jishijiyu.takeadvantage.entity.result.InvitationFriendsResult.InvitationFriendsVOList;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.view.CharacterParser;
import com.jishijiyu.takeadvantage.view.PinyinComparator;
import com.jishijiyu.takeadvantage.view.SideBar;
import com.jishijiyu.takeadvantage.view.SortModel;
import com.jishijiyu.takeadvantage.view.SideBar.OnTouchingLetterChangedListener;

public class InviteFriendsActivity extends HeadBaseActivity {
	private PullToRefreshListView listView = null;
	private Button btn_invite_all;
	private Intent intent;
	private SideBar sideBar = null;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private SortAdapter adapter;
	private Gson gson = null;
	private int page = 0;
	private int pageSize = 1;
	private int roomId = -1;
	private List<InvitationFriendsResult.InvitationFriendsVOList> friendsVOLists = null;
	private List<InvitationFriendsResult.InvitationFriendsVOList> friendsVOLists2 = null;
	public static final int type = 2;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(
					InviteFriendsActivity.this);
			break;
		case R.id.btn_invite_all:
			invitionFriend(friendsVOLists2, roomId);
			break;
		default:
			break;
		}
	}

	public int getRoomId() {
		return roomId;
	}

	@Override
	public void appHead(View view) {
		btn_left.setVisibility(View.VISIBLE);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		btn_left.setText("返回");
		top_text.setText("邀请好友列表");
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(InviteFriendsActivity.this,
				R.layout.activity_invite_friends, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		intent = getIntent();
		if (intent != null) {
			String room = (String) intent
					.getSerializableExtra(HeadBaseActivity.intentKey);
			if (!TextUtils.isEmpty(room)) {
				roomId = Integer.parseInt(room);
			} else {
				roomId = -1;
			}
		}
		initView(view);
		initOnclick();
	}

	public void initView(View view) {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		listView = (PullToRefreshListView) view.findViewById(R.id.listview);
		btn_invite_all = (Button) view.findViewById(R.id.btn_invite_all);
		this.sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listView.getRefreshableView().setSelection(position);
				}

			}
		});
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 0;
				friendsVOLists2 = null;
				SourceDateList = new ArrayList<SortModel>();
				getFriendList(roomId + "");
				listView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page++;
				getFriendList(roomId + "");
				listView.onRefreshComplete();
			}
		});
	}

	public void initOnclick() {
		btn_invite_all.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (roomId > 0) {
			getFriendList(roomId + "");
		} else {
			AppManager.getAppManager().finishActivity(
					InviteFriendsActivity.this);
		}
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private void getFriendList(String roomId) {
		if (gson == null) {
			gson = new Gson();
		}
		InvitationFriendsRequest friendsRequest = new InvitationFriendsRequest();
		Parameter pramater = friendsRequest.p;
		pramater.page = page + "";
		pramater.pageSize = pageSize + "";
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		pramater.roomId = roomId;
		String json = gson.toJson(friendsRequest);
		HttpConnectTool.update(json, InviteFriendsActivity.this,
				new ConnectListener() {
					@Override
					public void contectSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							InvitationFriendsResult friendResult = gson
									.fromJson(result,
											InvitationFriendsResult.class);
							if (friendResult.p.isTrue) {
								friendsVOLists = friendResult.p.invitationFriendsVOList;
								if (friendsVOLists != null
										&& friendsVOLists.size() > 0) {
									if (friendsVOLists2 == null) {
										friendsVOLists2 = friendsVOLists;
									} else {
										for (int i = 0; i < friendsVOLists
												.size(); i++) {
											friendsVOLists2.add(friendsVOLists
													.get(i));
										}
									}
									SourceDateList = new ArrayList<SortModel>();
									for (int i = 0; i < friendsVOLists2.size(); i++) {
										SortModel sortModel = new SortModel();
										sortModel.setObject(friendsVOLists2
												.get(i));
										// 汉字转换成拼音
										String pinyin = characterParser
												.getSelling(friendsVOLists2
														.get(i).nickname);
										String sortString = pinyin.substring(0,
												1).toUpperCase();

										// 正则表达式，判断首字母是否是英文字母
										if (sortString.matches("[A-Z]")) {
											sortModel.setSortLetters(sortString
													.toUpperCase());
										} else {
											sortModel.setSortLetters("#");
										}

										SourceDateList.add(sortModel);
									}
									// 根据a-z进行排序源数据
									Collections.sort(SourceDateList,
											pinyinComparator);

								}
								if (adapter == null) {
									adapter = new SortAdapter(
											InviteFriendsActivity.this,
											SourceDateList, type);
									listView.setAdapter(adapter);
								} else {
									adapter.refresh(SourceDateList);
								}
							} else {
								IntentActivity
										.mIntent(InviteFriendsActivity.this);
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

	String friendsId = "";
	boolean sendOk = true;

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
	public void invitionFriend(List<InvitationFriendsVOList> friendsVOLists,
			final int roomId) {
		if (roomId < 0) {
			ToastUtils.makeText(mContext, "房间号错误！", ToastUtils.LENGTH_SHORT)
					.show();
			return;
		}
		if (friendsVOLists == null || friendsVOLists.size() <= 0) {
			ToastUtils.makeText(mContext, "无可邀请的好友！", ToastUtils.LENGTH_SHORT)
					.show();
			return;
		}
		LoginUserResult loginUserResult = GetUserIdUtil.getLogin(this);
		for (int i = 0; i < friendsVOLists.size(); i++) {
			final InvitationFriendsVOList friendsVOList = friendsVOLists.get(i);
			// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(friendsVOList.userId);
			// 创建一条文本消息
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// 如果是群聊，设置chattype,默认是单聊
			message.setChatType(ChatType.Chat);
			// 设置消息body
			TextMessageBody txtBody = new TextMessageBody("当前好友邀请你加入" + roomId
					+ "号房间" + "iv");
			message.addBody(txtBody);
			// 设置接收人
			message.setReceipt(friendsVOList.userId);
			// 把消息加入到此会话对象中
			conversation.addMessage(message);
			// 发送消息
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onError(int arg0, final String arg1) {

					((InviteFriendsActivity) mContext)
							.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ToastUtils.makeText(mContext, arg1,
											ToastUtils.LENGTH_SHORT).show();
								}
							});
					sendOk = false;
					return;
				}

				@Override
				public void onProgress(int arg0, String arg1) {

				}

				@Override
				public void onSuccess() {
					sendOk = true;
				}
			});

		}
		if (!sendOk) {
			ToastUtils.makeText(mContext, "邀请好友失败！", ToastUtils.LENGTH_SHORT)
					.show();
			return;
		}
		for (int i = 0; i < friendsVOLists.size(); i++) {
			InvitationFriendsVOList friendsVOList = friendsVOLists.get(i);
			if (TextUtils.isEmpty(friendsId)) {
				friendsId = friendsId + friendsVOList.friendsId;
			} else {
				friendsId = friendsId + "," + friendsVOList.friendsId;
			}
		}
		if (TextUtils.isEmpty(friendsId)) {
			ToastUtils.makeText(mContext, "邀请好友失败！", ToastUtils.LENGTH_SHORT)
					.show();
			return;
		}
		if (gson == null) {
			gson = new Gson();
		}
		InvitationFriends2Request friendsRequest = new InvitationFriends2Request();
		InvitationFriends2Request.Parameter pramater = friendsRequest.p;
		pramater.userId = GetUserIdUtil.getUserId(mContext);
		pramater.tokenId = GetUserIdUtil.getTokenId(mContext);
		pramater.roomId = roomId + "";
		pramater.friendsId = friendsId;
		String json = gson.toJson(friendsRequest);
		HttpConnectTool.update(json, mContext, new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					DelFriendResult friendResult = gson.fromJson(result,
							DelFriendResult.class);
					if (friendResult.p.isTrue) {
						if (friendResult.p.isSucce) {
							listView.setAdapter(null);
							ToastUtils.makeText(mContext, "邀请好友成功！",
									ToastUtils.LENGTH_SHORT).show();
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
}
