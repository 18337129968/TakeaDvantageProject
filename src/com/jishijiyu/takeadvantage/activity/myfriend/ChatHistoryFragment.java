package com.jishijiyu.takeadvantage.activity.myfriend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.dao.InviteMessgeDao;
import com.jishijiyu.takeadvantage.activity.db.DBDao;
import com.jishijiyu.takeadvantage.activity.db.Note;
import com.jishijiyu.takeadvantage.adapter.ChatAllHistoryAdapter;
import com.jishijiyu.takeadvantage.entity.User;
import com.jishijiyu.takeadvantage.entity.request.MyFriendRequest;
import com.jishijiyu.takeadvantage.entity.result.MyFriendResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntentActivity;

/**
 * 
 * 消息
 * 
 * @author zhaobin
 */
@SuppressLint("NewApi")
public class ChatHistoryFragment extends Fragment {
	private InputMethodManager inputMethodManager;
	private Map<String, User> contactList;
	private ChatAllHistoryAdapter adapter;
	private boolean hidden;
	private LinearLayout layout_merchant_history = null;
	private ListView listView = null;
	private int n = -1;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();
	private List<EMConversation> conversationList1 = new ArrayList<EMConversation>();
	private List<EMConversation> conversationList2 = new ArrayList<EMConversation>();
	private int type = 0;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_new_friend, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	public void init() {
		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		layout_merchant_history = (LinearLayout) getActivity().findViewById(
				R.id.layout_merchant_history);
		this.listView = (ListView) getActivity().findViewById(
				R.id.new_friend_list);
		// 注册上下文菜单
		registerForContextMenu(listView);

		layout_merchant_history.setVisibility(View.VISIBLE);
		layout_merchant_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 商家信息
				type = 1;
				adapter = new ChatAllHistoryAdapter(getActivity(), 1,
						conversationList2);
			}
		});
		type = 0;
		// contact list
		conversationList.addAll(loadConversationsWithRecentChat());
		adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList1);
		// adapter = new ChatHistoryAdapter(getActivity(), 1,
		// loadUsersWithRecentChat());
		// 设置adapter
		listView.setAdapter(adapter);
		final String st = "不能和自己聊天";
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EMConversation conversation = adapter.getItem(position);
				String username = conversation.getUserName();
				if (username.equals(App.getInstance().getUserName()))
					Toast.makeText(getActivity(), st, 0).show();
				else {
					// 进入聊天页面
					Intent intent = new Intent(getActivity(),
							ChatActivity.class);
					if (conversation.isGroup()) {
						if (conversation.getType() == EMConversationType.ChatRoom) {
							// it is group chat
							intent.putExtra("chatType",
									ChatActivity.CHATTYPE_CHATROOM);
							intent.putExtra("groupId", username);
						} else {
							// it is group chat
							intent.putExtra("chatType",
									ChatActivity.CHATTYPE_GROUP);
							intent.putExtra("groupId", username);
						}

					} else {
						// it is single chat
						// it is single chat
						// intent.putExtra(HeadBaseActivity.intentKey,
						// conversation.get);
						// it is single chat
						intent.putExtra(HeadBaseActivity.intentKey, username);
						intent.putExtra(HeadBaseActivity.intentKey + 1,
								username);
					}
					startActivity(intent);
				}
			}
		});

		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// if (arg2 == 0) {
		// n = 1;
		// if (n < 0) {
		// list.remove(0);
		// adapter.notifyDataSetChanged();
		// }
		// } else {
		// Intent intent = new Intent(ChatHistoryFragment.this
		// .getActivity(), ChatActivity.class);
		// startActivity(intent);
		// }
		// }
		// });
		// listView.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// if (arg2 == 0) {
		// Intent intent = new Intent(ChatHistoryFragment.this
		// .getActivity(), FriendDatelActivity.class);
		// startActivity(intent);
		// } else {
		// Intent intent = new Intent(ChatHistoryFragment.this
		// .getActivity(), AddFriend_SendMsgActivity.class);
		// startActivity(intent);
		// }
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		//
		// }
		// });

	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList1.clear();
		conversationList2.clear();
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		if (adapter != null)
			switch (type) {
			case 0:
				adapter.refresh(conversationList1);
				break;
			case 1:
				adapter.refresh(conversationList2);
				break;
			}
	}

	// /**
	// * 获取有聊天记录的users和groups
	// *
	// * @param context
	// * @return
	// */
	// private List<EMContact> loadUsersWithRecentChat() {
	// List<EMContact> resultList = new ArrayList<EMContact>();
	// // 获取有聊天记录的users，不包括陌生人
	// for (User user : contactList.values()) {
	// EMConversation conversation = EMChatManager.getInstance()
	// .getConversation(user.getUsername());
	// if (conversation.getMsgCount() > 0) {
	// resultList.add(user);
	// }
	// }
	// for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
	// EMConversation conversation = EMChatManager.getInstance()
	// .getConversation(group.getGroupId());
	// if (conversation.getMsgCount() > 0) {
	// resultList.add(group);
	// }
	//
	// }
	//
	// // 排序
	// sortUserByLastChatTime(resultList);
	// return resultList;
	// }

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					// if(conversation.getType() !=
					// EMConversationType.ChatRoom){
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
					// }
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		for (int i = 0; i < list.size(); i++) {
			EMConversation conversation = list.get(i);
			if (conversation.getUserName().lastIndexOf("_com") == -1) {
				conversationList1.add(conversation);
			} else {
				conversationList2.add(conversation);
			}
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;
						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}

				});
	}

	// /**
	// * 根据最后一条消息的时间排序
	// *
	// * @param usernames
	// */
	// private void sortUserByLastChatTime(List<EMContact> contactList) {
	// Collections.sort(contactList, new Comparator<EMContact>() {
	// @Override
	// public int compare(final EMContact user1, final EMContact user2) {
	// EMConversation conversation1 = EMChatManager.getInstance()
	// .getConversation(user1.getUsername());
	// EMConversation conversation2 = EMChatManager.getInstance()
	// .getConversation(user2.getUsername());
	//
	// EMMessage user2LastMessage = conversation2.getLastMessage();
	// EMMessage user1LastMessage = conversation1.getLastMessage();
	// if (user2LastMessage.getMsgTime() == user1LastMessage
	// .getMsgTime()) {
	// return 0;
	// } else if (user2LastMessage.getMsgTime() > user1LastMessage
	// .getMsgTime()) {
	// return 1;
	// } else {
	// return -1;
	// }
	// }
	//
	// });
	// }

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
		getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
		// }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		boolean handled = false;
		boolean deleteMessage = false;
		if (item.getItemId() == R.id.delete_message) {
			deleteMessage = true;
			handled = true;
		} else if (item.getItemId() == R.id.delete_conversation) {
			deleteMessage = false;
			handled = true;
		}
		EMConversation tobeDeleteCons = adapter
				.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
		// 删除此会话
		EMChatManager.getInstance().deleteConversation(
				tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(),
				deleteMessage);
		InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
		inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
		adapter.remove(tobeDeleteCons);
		adapter.notifyDataSetChanged();

		// 更新消息未读数
		((FriendActivity) getActivity()).updateUnreadLabel();

		return handled ? true : super.onContextItemSelected(item);
	}

	
}
