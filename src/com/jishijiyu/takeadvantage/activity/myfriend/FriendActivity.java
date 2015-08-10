package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.HX.DemoHXSDKHelper;
import com.jishijiyu.takeadvantage.HX.HXSDKHelper;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.dao.InviteMessgeDao;
import com.jishijiyu.takeadvantage.activity.dao.UserDao;
import com.jishijiyu.takeadvantage.activity.my.FriendRequestFragmentActivity;
import com.jishijiyu.takeadvantage.entity.InviteMessage;
import com.jishijiyu.takeadvantage.entity.InviteMessage.InviteMesageStatus;
import com.jishijiyu.takeadvantage.entity.User;
import com.jishijiyu.takeadvantage.utils.*;

/**
 * 
 * 好友
 * 
 * @author zhaobin
 */
public class FriendActivity extends HeadBaseActivity implements EMEventListener {
	private static final String TAG = "FriendActivity";
	// 未读消息textview
	private TextView unreadLabel;
	// 未读通讯录textview
	private TextView unreadAddressLable;

	private Button btn_news = null;
	private Button btn_friend = null;
	private Button btn_spread = null;
	private FragmentTransaction fragmentTransaction = null;
	private int btnId = R.id.btn_news;
	private FriendFragmentActivity friendFragmentActivity = null;
	private ChatHistoryFragment chatHistoryFragment = null;
	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;
	private MyConnectionListener connectionListener = null;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
						false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			App.getInstance().logout(null);
			finish();
			IntentActivity.mIntent(FriendActivity.this);
			return;
		} else if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			finish();
			IntentActivity.mIntent(FriendActivity.this);
			return;
		}
	}

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.friend_top));
		btn_left.setOnClickListener(this);
		btn_right.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("NewApi")
	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(FriendActivity.this, R.layout.friend, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		initview(view);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@SuppressLint("NewApi")
	private void initview(View view) {
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		this.btn_news = (Button) view.findViewById(R.id.btn_news);
		this.btn_friend = (Button) view.findViewById(R.id.btn_friend);
		this.btn_spread = (Button) view.findViewById(R.id.btn_spread);
		this.btn_news.setOnClickListener(this);
		this.btn_friend.setOnClickListener(this);
		this.btn_spread.setOnClickListener(this);
		fragmentTransaction = getFragmentManager().beginTransaction();
		chatHistoryFragment = new ChatHistoryFragment();
		fragmentTransaction.add(R.id.frame, chatHistoryFragment);
		fragmentTransaction.commit();
		init();
	}

	private void init() {
		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// 注册一个监听连接状态的listener

		connectionListener = new MyConnectionListener();
		EMChatManager.getInstance().addConnectionListener(connectionListener);

		// 内部测试方法，请忽略
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(FriendActivity.this);
			break;
		case R.id.btn_news:
			if (btnId != R.id.btn_news) {
				btnId = R.id.btn_news;
				btn_news.setBackgroundResource(R.drawable.btn_headabove_dark);
				btn_friend
						.setBackgroundResource(R.drawable.btn_headabove_light);
				btn_spread
						.setBackgroundResource(R.drawable.btn_headabove_light);
				fragmentTransaction = getFragmentManager().beginTransaction();
				chatHistoryFragment = new ChatHistoryFragment();
				fragmentTransaction.replace(R.id.frame, chatHistoryFragment);
				fragmentTransaction.commit();
			}
			break;
		case R.id.btn_friend:
			if (btnId != R.id.btn_friend) {
				btnId = R.id.btn_friend;
				btn_friend.setBackgroundResource(R.drawable.btn_headabove_dark);
				btn_news.setBackgroundResource(R.drawable.btn_headabove_light);
				btn_spread
						.setBackgroundResource(R.drawable.btn_headabove_light);
				friendFragmentActivity = new FriendFragmentActivity();
				fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.frame, friendFragmentActivity);
				fragmentTransaction.commit();
			}
			break;
		case R.id.btn_spread:
			if (btnId != R.id.btn_spread) {
				btnId = R.id.btn_spread;
				btn_spread.setBackgroundResource(R.drawable.btn_headabove_dark);
				btn_friend
						.setBackgroundResource(R.drawable.btn_headabove_light);
				btn_news.setBackgroundResource(R.drawable.btn_headabove_light);
				FriendRequestFragmentActivity friendRequestActivity = new FriendRequestFragmentActivity();
				fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.frame, friendRequestActivity);
				fragmentTransaction.commit();
			}
			break;
		}
	}

	/**
	 * 监听事件
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: // 普通消息
		{
			EMMessage message = (EMMessage) event.getData();
			// 提示新消息
			HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
			refreshUI();
			break;
		}
		case EventOfflineMessage: {
			refreshUI();
			break;
		}
		case EventConversationListChanged: {
			refreshUI();
			break;
		}
		default:
			break;
		}
	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// 刷新bottom bar消息未读数
				updateUnreadLabel();
				if (btnId == R.id.btn_news) {
					// 当前页面如果为聊天历史页面，刷新此页面
					if (chatHistoryFragment != null) {
						chatHistoryFragment.refresh();
					}
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (connectionListener != null) {
			EMChatManager.getInstance().removeConnectionListener(
					connectionListener);
		}

	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		for (EMConversation conversation : EMChatManager.getInstance()
				.getAllConversations().values()) {
			if (conversation.getType() == EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount
						+ conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}

	/***
	 * 好友变化listener
	 * 
	 */
	public class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = App.getInstance().getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			if (btnId == R.id.btn_friend) {
				if (friendFragmentActivity != null) {
					friendFragmentActivity.refresh();
				}
			}

		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = App.getInstance().getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					// TODO
					// 如果正在与此用户的聊天页面
					// String st10 = getResources().getString(
					// R.string.have_you_removed);
					// if (ChatActivity.activityInstance != null
					// && usernameList
					// .contains(ChatActivity.activityInstance
					// .getToChatUsername())) {
					// Toast.makeText(
					// MainActivity.this,
					// ChatActivity.activityInstance
					// .getToChatUsername() + st10, 1).show();
					// ChatActivity.activityInstance.finish();
					// }
					// updateUnreadLabel();
					// // 刷新ui
					if (friendFragmentActivity != null) {
						friendFragmentActivity.refresh();
					}
					if (chatHistoryFragment != null) {
						chatHistoryFragment.refresh();
					}
				}
			});

		}

		@Override
		public void onContactInvited(String username, String reason) {

			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			LogUtil.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			LogUtil.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {

			// 参考同意，被邀请实现此功能,demo未实现
			LogUtil.d(username, username + "拒绝了你的好友请求");
		}

	}

	/**
	 * set head
	 * 
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(null);

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
		if (btnId == R.id.btn_friend) {
			if (friendFragmentActivity != null) {
				friendFragmentActivity.refresh();
			}
		}
	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		LogUtil.i(TAG, msg.toString());
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = App.getInstance().getContactList()
				.get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					// unreadAddressLable.setText(String.valueOf(count));
					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (App.getInstance().getContactList()
				.get(Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = App.getInstance().getContactList()
					.get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 连接监听listener
	 * 
	 */
	public class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			boolean groupSynced = HXSDKHelper.getInstance()
					.isGroupsSyncedWithServer();
			boolean contactSynced = HXSDKHelper.getInstance()
					.isContactsSyncedWithServer();

			// in case group and contact were already synced, we supposed to
			// notify sdk we are ready to receive the events
			if (groupSynced && contactSynced) {
				new Thread() {
					@Override
					public void run() {
						HXSDKHelper.getInstance().notifyForRecevingEvents();
					}
				}.start();
			} else {
				if (!groupSynced) {
					asyncFetchGroupsFromServer();
				}

				if (!contactSynced) {
					asyncFetchContactsFromServer();
				}

				if (!HXSDKHelper.getInstance().isBlackListSyncedWithServer()) {
					asyncFetchBlackListFromServer();
				}
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO
					// chatHistoryFragment.errorItem.setVisibility(View.GONE);
				}

			});
		}

		@Override
		public void onDisconnected(final int error) {
			final String st1 = "连接不到聊天服务器";
			final String st2 = "当前网络不可用，请检查网络设置";
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						ToastUtils.makeText(FriendActivity.this, "帐号已经被移除!",
								ToastUtils.LENGTH_SHORT).show();
						isCurrentAccountRemoved = true;
						// showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						ToastUtils.makeText(FriendActivity.this, "帐号在其他设备登陆!",
								ToastUtils.LENGTH_SHORT).show();
						isConflict = true;
						IntentActivity.mIntent(FriendActivity.this);
					} else {
						// chatHistoryFragment.errorItem
						// .setVisibility(View.VISIBLE);
						if (NetUtils.hasNetwork(FriendActivity.this)) {
							ToastUtils.makeText(FriendActivity.this, st1,
									ToastUtils.LENGTH_SHORT).show();
							// chatHistoryFragment.errorText.setText(st1);
						} else {
							ToastUtils.makeText(FriendActivity.this, st2,
									ToastUtils.LENGTH_SHORT).show();
							// chatHistoryFragment.errorText.setText(st2);
						}
					}
				}

			});
		}
	}

	static void asyncFetchGroupsFromServer() {
		HXSDKHelper.getInstance().asyncFetchGroupsFromServer(new EMCallBack() {

			@Override
			public void onSuccess() {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(true);

				if (HXSDKHelper.getInstance().isContactsSyncedWithServer()) {
					HXSDKHelper.getInstance().notifyForRecevingEvents();
				}
			}

			@Override
			public void onError(int code, String message) {
				HXSDKHelper.getInstance().noitifyGroupSyncListeners(false);
			}

			@Override
			public void onProgress(int progress, String status) {

			}

		});
	}

	static void asyncFetchContactsFromServer() {
		HXSDKHelper.getInstance().asyncFetchContactsFromServer(
				new EMValueCallBack<List<String>>() {

					@Override
					public void onSuccess(List<String> usernames) {
						Context context = HXSDKHelper.getInstance()
								.getAppContext();

						System.out.println("----------------"
								+ usernames.toString());
						EMLog.d("roster", "contacts size: " + usernames.size());
						Map<String, User> userlist = new HashMap<String, User>();
						for (String username : usernames) {
							User user = new User();
							user.setUsername(username);
							setUserHearder(username, user);
							userlist.put(username, user);
						}
						// 添加user"申请与通知"
						User newFriends = new User();
						newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
						String strChat = "申请与通知";
						newFriends.setNick(strChat);

						userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
						// 添加"群聊"
						User groupUser = new User();
						String strGroup = "群聊";
						groupUser.setUsername(Constant.GROUP_USERNAME);
						groupUser.setNick(strGroup);
						groupUser.setHeader("");
						userlist.put(Constant.GROUP_USERNAME, groupUser);

						// 添加"聊天室"
						User chatRoomItem = new User();
						String strChatRoom = "聊天室";
						chatRoomItem.setUsername(Constant.CHAT_ROOM);
						chatRoomItem.setNick(strChatRoom);
						chatRoomItem.setHeader("");
						userlist.put(Constant.CHAT_ROOM, chatRoomItem);

						// 添加"Robot"
						User robotUser = new User();
						String strRobot = "Robot";
						robotUser.setUsername(Constant.CHAT_ROBOT);
						robotUser.setNick(strRobot);
						robotUser.setHeader("");
						userlist.put(Constant.CHAT_ROBOT, robotUser);

						// 存入内存
						App.getInstance().setContactList(userlist);
						// 存入db
						UserDao dao = new UserDao(context);
						List<User> users = new ArrayList<User>(userlist
								.values());
						dao.saveContactList(users);

						HXSDKHelper.getInstance().notifyContactsSyncListener(
								true);

						if (HXSDKHelper.getInstance()
								.isGroupsSyncedWithServer()) {
							HXSDKHelper.getInstance().notifyForRecevingEvents();
						}

					}

					@Override
					public void onError(int error, String errorMsg) {
						HXSDKHelper.getInstance().notifyContactsSyncListener(
								false);
					}

				});
	}

	static void asyncFetchBlackListFromServer() {
		HXSDKHelper.getInstance().asyncFetchBlackListFromServer(
				new EMValueCallBack<List<String>>() {

					@Override
					public void onSuccess(List<String> value) {
						EMContactManager.getInstance().saveBlackList(value);
						HXSDKHelper.getInstance().notifyBlackListSyncListener(
								true);
					}

					@Override
					public void onError(int error, String errorMsg) {
						HXSDKHelper.getInstance().notifyBlackListSyncListener(
								false);
					}

				});
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	private static void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

		// unregister this event listener when this activity enters the
		// background
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.pushActivity(this);

		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] {
						EMNotifierEvent.Event.EventNewMessage,
						EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventConversationListChanged });
	}

	@Override
	protected void onStop() {
		EMChatManager.getInstance().unregisterEventListener(this);
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
