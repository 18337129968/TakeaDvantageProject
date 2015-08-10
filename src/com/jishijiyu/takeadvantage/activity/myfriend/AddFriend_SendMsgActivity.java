package com.jishijiyu.takeadvantage.activity.myfriend;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMContactManager;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.entity.result.AddFriendSearchResult;
import com.jishijiyu.takeadvantage.entity.result.AddFriendSearchResult.FriendList;
import com.jishijiyu.takeadvantage.entity.result.AddMerchantSearchResult.ComList;
import com.jishijiyu.takeadvantage.utils.*;

/**
 * 
 * 添加好友—发送消息
 * 
 * @author zhaobin
 */
public class AddFriend_SendMsgActivity extends HeadBaseActivity {
	private Button btn_send = null;
	private EditText et_msg = null;
	private ImageView imageView = null;
	private TextView tv_name = null;
	private AddFriendSearchResult.FriendList friendList = null;
	private ComList comList = null;
	String name = "";

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.send_msg));
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AddFriend_SendMsgActivity.this,
				R.layout.add_friend_send_msg, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		if (intent != null) {
			String name = intent.getStringExtra("name");
			if (name != null && name.equals("addfriend")) {
				friendList = (FriendList) intent
						.getSerializableExtra(HeadBaseActivity.intentKey);
			}
			if (name != null && name.equals("addmerchant")) {
				comList = (ComList) intent
						.getSerializableExtra(HeadBaseActivity.intentKey);
			}
		}
		initview(view);
	}

	private void initview(View view) {
		this.btn_send = (Button) view.findViewById(R.id.btn_add);
		this.et_msg = (EditText) view.findViewById(R.id.et_msg);
		this.imageView = (ImageView) view.findViewById(R.id.img_url);
		this.tv_name = (TextView) view.findViewById(R.id.tv_name);
		btn_send.setOnClickListener(this);
		if (friendList != null) {
			ImageLoaderUtil.loadImage(friendList.headImgUrl, imageView);
			name = friendList.nickname;
		}
		if (comList != null) {
			ImageLoaderUtil.loadImage(comList.logoImgUrl, imageView);
			name = comList.companyName;
		}
		tv_name.setText(name);
	}

	@Override
	public void onClick(View v) {
		boolean isLocked;
		switch (v.getId()) {
		case R.id.btn_top_left:
			AppManager.getAppManager().finishActivity(
					AddFriend_SendMsgActivity.this);
			break;
		case R.id.btn_add:
			if (!TextUtils.isEmpty(name)) {
				if (friendList != null) {
					addContact(friendList.id);
				}
				if (comList != null) {
					addContact(comList.userId);
				}

			}
			break;
		}
	}

	/**
	 * 添加contact
	 * 
	 * @param view
	 */
	public void addContact(final String userName) {
		String name = App.getInstance().getUserName();
		if (App.getInstance().getUserName()
				.equals(friendList.id == null ? "" : friendList.id)) {

			ToastUtils.makeText(this, "不能添加自己！", ToastUtils.LENGTH_SHORT)
					.show();
			return;
		}

		if (App.getInstance().getContactList().containsKey(name)) {
			// 提示已在好友列表中，无需添加
			ToastUtils.makeText(this, "已在好友列表中，无需添加！", ToastUtils.LENGTH_SHORT)
					.show();
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					// demo写死了个reason，实际应该让用户手动填入
					String s = et_msg.getText().toString().trim();
					// TODO 好友分类
					EMContactManager.getInstance().addContact(friendList.id, s);
					runOnUiThread(new Runnable() {
						public void run() {
							ToastUtils.makeText(AddFriend_SendMsgActivity.this,
									"发送成功！", ToastUtils.LENGTH_SHORT).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							LogUtil.i("addContact", e.getMessage());
							ToastUtils.makeText(AddFriend_SendMsgActivity.this,
									"发送失败：" + e.getMessage(),
									ToastUtils.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
}
