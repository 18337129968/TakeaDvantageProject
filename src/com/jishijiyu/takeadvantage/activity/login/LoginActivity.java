package com.jishijiyu.takeadvantage.activity.login;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.baidu.mapapi.map.Text;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.App;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.dao.UserDao;
import com.jishijiyu.takeadvantage.activity.home.HomeActivity;
import com.jishijiyu.takeadvantage.activity.merchant_account.MerchantAccountActivity;
import com.jishijiyu.takeadvantage.entity.User;
import com.jishijiyu.takeadvantage.entity.request.LoginUser;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult.Parameter;
import com.jishijiyu.takeadvantage.utils.*;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.view.CircularImage;

/**
 * 用户登录界面
 * 
 * @author shifeiyu
 * @version 2015年5月29日18:50:45
 * 
 */
public class LoginActivity extends HeadBaseActivity {
	private CircularImage cover_user_photo;
	private TextView forgot_password_text, register_text, login_btn;
	private EditText edittext_phone_number, edittext_password;
	private ImageView remember1;
	private SharedPreferences sp = null, sp1 = null;
	private String isMemory = "";
	private String FILE = "saveUserPwd";
	private String passWord, p;
	private String name, pwd;
	private Dialog dialog;
	private Bitmap bitmap = null;
	boolean flag = false;
	private Message msg;
	private static final int CHECK_SUCCESS = 0;
	private static final int CHECK_FAIL = 1;
	private static final int PHONE_NUMBER_ERROR = 2;
	private static final int PHONE_NUMBER_NULL = 3;
	private static final int PASSWORD_NULL = 4;
	private static final int CONNECT_FAIL = 6;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHECK_SUCCESS:
				ToastUtils.makeText(LoginActivity.this, "登录成功！", 0).show();
				remember();
				p = edittext_phone_number.getText().toString();
				if (sp1 == null) {
					sp1 = getSharedPreferences("UserPhone",
							Context.MODE_PRIVATE);
				}
				Editor ed = sp1.edit();
				ed.putString("phoneNumber", p);
				ed.commit();
				startForActivity(LoginActivity.this, HomeActivity.class, null);
				finish();
				break;
			case CHECK_FAIL:
				ToastUtils.makeText(LoginActivity.this, "用户名或密码有误", 0).show();
				break;
			case PHONE_NUMBER_ERROR:
				ToastUtils.makeText(LoginActivity.this, "请输入正确手机号码", 0).show();
				break;
			case PHONE_NUMBER_NULL:
				ToastUtils.makeText(LoginActivity.this, "请输入手机号", 0).show();
				break;
			case PASSWORD_NULL:
				ToastUtils.makeText(LoginActivity.this, "请输入密码", 0).show();
				break;
			case CONNECT_FAIL:
				ToastUtils.makeText(LoginActivity.this, "访问服务器失败", 0).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.user_login_title);
		btn_left.setVisibility(View.INVISIBLE);
		btn_right.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forgot_password_text: // 点击“忘记密码”按钮
			startForActivity(LoginActivity.this, ForgotPasswordActivity.class,
					null);
			// startForActivity(LoginActivity.this,
			// MerchantAccountActivity.class,
			// null);
			break;
		case R.id.register_text: // 点击“注册”按钮
			startForActivity(LoginActivity.this, RegisterActivity.class, null);
			break;
		case R.id.login_btn: // 点击“登录”按钮
			checkUserInfo();
			break;
		case R.id.remember_pwd_image: // 点击“记住密码” 选择框
			changeBackground();
			remember();
			break;
		default:
			break;
		}
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(LoginActivity.this, R.layout.activity_login,
				null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		forgot_password_text = (TextView) findViewById(R.id.forgot_password_text);
		register_text = (TextView) findViewById(R.id.register_text);
		edittext_phone_number = (EditText) findViewById(R.id.edittext_phone_number);
		edittext_password = (EditText) findViewById(R.id.edittext_password);
		remember1 = (ImageView) findViewById(R.id.remember_pwd_image);
		login_btn = (TextView) findViewById(R.id.login_btn);
		cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		remember1.setOnClickListener(this);
		forgot_password_text.setOnClickListener(this);
		register_text.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		EdittextUtil.edittextForPhone(edittext_phone_number);
		EdittextUtil.edittextForPhone(edittext_password);

		File file = this.getFilesDir();
		bitmap = BitmapFactory.decodeFile(file + Constant.HEAD_PIC_FILE_NAME);
		if (bitmap == null) {
			bitmap = BitmapFactory
					.decodeResource(getResources(), R.drawable.pr);
			cover_user_photo.setImageBitmap(bitmap);
		} else {
			cover_user_photo.setImageBitmap(bitmap);
		}
		sp1 = this.getSharedPreferences("UserPhone", Context.MODE_PRIVATE);
		p = sp1.getString("phoneNumber", "");
		edittext_phone_number.setText(p);

		sp = this.getSharedPreferences(FILE, Context.MODE_PRIVATE);
		isMemory = sp.getString("isMemory", "No");
		if (isMemory.equals("Yes")) {
			passWord = sp.getString("PassWord", "");
			edittext_password.setText(passWord);
			remember1.setBackgroundResource(R.drawable.remember_pwd);
			flag = true;
		} else {
			remember1.setBackgroundResource(R.drawable.no_remember_pwd);
		}
	}

	/**
	 * 检查用户信息
	 */
	private void checkUserInfo() {
		msg = Message.obtain();
		name = edittext_phone_number.getText().toString();
		pwd = EmojiUtil.filterEmoji(edittext_password.getText().toString());
		LoginUser userinfo = new LoginUser();
		com.jishijiyu.takeadvantage.entity.request.LoginUser.Parameter parameter = userinfo.p;
		parameter.mobile = name;
		parameter.pwd = edittext_password.getText().toString();
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
			dialog = DialogUtils.showDialog(LoginActivity.this, "手机号或密码不能为空",
					new int[] { R.string.end }, new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					}, false);
			return;
		}
		if (!isPhone(name) || name.length() < 11) {
			msg.what = PHONE_NUMBER_ERROR;
			handler.sendMessage(msg);
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			msg.what = PASSWORD_NULL;
			handler.sendMessage(msg);
			return;
		}
		if (pwd.length() < 6 || pwd.length() > 30) {
			ToastUtils.makeText(LoginActivity.this, "密码长度不符", 0).show();
			return;
		} else {
			login_btn.setClickable(false);
			final Gson gson = new Gson();
			String json = gson.toJson(userinfo);
			HttpConnectTool.update(json, mContext, new ConnectListener() {

				@Override
				public void contectSuccess(String result) {
					if (!TextUtils.isEmpty(result)) {
						Log.e("result", result);
						login_btn.setClickable(true);
						Constants.name = name;
						Constants.pwd = pwd;
						SPUtils.put(mContext, Constant.USER_INFO_FILE_NAME,
								result);
						LoginUserResult jsonobj = gson.fromJson(result,
								LoginUserResult.class);
						final Parameter parameter2 = jsonobj.p;
						if (parameter2.isSucce) {
							// final long start = System.currentTimeMillis();
							// // 调用sdk登陆方法登陆聊天服务器
							// EMChatManager.getInstance().login(
							// parameter2.user.id + "",
							// parameter2.user.password, new EMCallBack() {
							//
							// @Override
							// public void onSuccess() {
							//
							// // 登陆成功，保存用户名密码
							// App.getInstance().setUserName(
							// parameter2.user.id + "");
							// App.getInstance().setPassword(
							// parameter2.user.password);
							// try {
							// // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
							// // ** manually load all local groups
							// // and
							// EMGroupManager.getInstance()
							// .loadAllGroups();
							// EMChatManager.getInstance()
							// .loadAllConversations();
							// // 处理好友和群组
							// initializeContacts();
							// } catch (final Exception e) {
							// e.printStackTrace();
							// // 取好友或者群聊失败，不让进入主页面
							// runOnUiThread(new Runnable() {
							// public void run() {
							// App.getInstance().logout(
							// null);
							// }
							// });
							// Log.i("login", e.getMessage());
							// msg.what = CONNECT_FAIL;
							// handler.sendMessage(msg);
							// return;
							// }
							// // 更新当前用户的nickname
							// // 此方法的作用是在ios离线推送时能够显示用户nick
							// boolean updatenick = EMChatManager
							// .getInstance()
							// .updateCurrentUserNick(
							// App.currentUserNick
							// .trim());
							// if (!updatenick) {
							// Log.e("LoginActivity",
							// "update current user nick fail");
							// }
							msg.what = CHECK_SUCCESS;
							handler.sendMessage(msg);
							// }
							//
							// @Override
							// public void onProgress(int progress,
							// String status) {
							// }
							//
							// @Override
							// public void onError(final int code,
							// final String message) {
							// runOnUiThread(new Runnable() {
							// public void run() {
							// Toast.makeText(
							// getApplicationContext(),
							// "error:" + message,
							// Toast.LENGTH_SHORT)
							// .show();
							// }
							// });
							// msg.what = CONNECT_FAIL;
							// handler.sendMessage(msg);
							// }
							// });
							return;
						} else {

							dialog = DialogUtils.showDialog(LoginActivity.this,
									"用户名或密码有误", new int[] { R.string.end },
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									}, false);
							return;

						}
					} else {
						ToastUtils.makeText(mContext, "连接服务器失败", 0).show();
						login_btn.setClickable(true);
					}
				}

				@Override
				public void contectStarted() {

				}

				@Override
				public void contectFailed(String path, String request) {
					msg.what = CONNECT_FAIL;
					handler.sendMessage(msg);
					login_btn.setClickable(true);
				}
			});

		}

	}

	/**
	 * 延时跳转
	 * 
	 * @return
	 */
	public void intentHome() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				startForActivity(LoginActivity.this, HomeActivity.class, null);
				finish();
			}
		};
		timer.schedule(task, 500);
	}

	/**
	 * 判断格式是否为手机号码
	 */
	public boolean isPhone(String inputText) {

		Pattern p = Pattern.compile("^1[3578]\\d{9}$");
		Matcher m = p.matcher(inputText);
		return m.matches();

	}

	/**
	 * 记住密码
	 */
	public void remember() {
		if (flag) {
			if (sp == null) {
				sp = getSharedPreferences(FILE, MODE_PRIVATE);
			}
			Editor editor = sp.edit();
			editor.putString("PassWord", pwd);
			editor.putString("isMemory", "Yes");
			editor.commit();
		} else {
			if (sp == null) {
				sp = getSharedPreferences(FILE, MODE_PRIVATE);
			}
			Editor editor = sp.edit();
			editor.putString("isMemory", "No");
			editor.commit();
		}
	}

	/**
	 * 记住密码更换背景
	 */
	public boolean changeBackground() {
		if (flag) {
			remember1.setBackgroundResource(R.drawable.no_remember_pwd);
			flag = false;
		} else {
			remember1.setBackgroundResource(R.drawable.remember_pwd);
			flag = true;
		}
		return flag;
	}

	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
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
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent("Exit");
			sendBroadcast(intent);
			ToastUtils.ToastCancel(mContext);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
