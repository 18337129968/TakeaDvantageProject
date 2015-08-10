package com.jishijiyu.takeadvantage.activity.myinformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.LocationActivity;
import com.jishijiyu.takeadvantage.activity.ProfessionActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.ChangeMyInfomationRequest;
import com.jishijiyu.takeadvantage.entity.request.MyInfoMationRequest;
import com.jishijiyu.takeadvantage.entity.request.MyInfoMationRequest.Parameter;
import com.jishijiyu.takeadvantage.entity.result.ChangeMyInfomationResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.MyInfoMationResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.FileUtil;
import com.jishijiyu.takeadvantage.utils.GetBirthdayInfo;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.SPUtils;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.view.BigImagePopupWindow;
import com.jishijiyu.takeadvantage.view.SelectBirthdayPopupWindow;
import com.jishijiyu.takeadvantage.view.SelectHeadPicPopupWindow;
import com.jishijiyu.takeadvantage.view.SelectJobPopupWindow;

/**
 * 我的基本信息 界面
 * 
 * @author shifeiyu
 * @version 2015年5月30日15:50:55
 */
public class MyBasicInfomationActivity extends HeadBaseActivity {
	private SelectHeadPicPopupWindow menuWindow;
	private SelectJobPopupWindow jobMenuWindow;
	private SelectBirthdayPopupWindow mybirthPopWindow;
	private String province, city, area, industry, profession;
	private String trade, job;
	private String headimg;
	private Message msg;
	public int year, month, day;
	private static final int GET_CODE = 0;
	private static final int MAN = 3;
	private static final int WOMEN = 4;
	private static final int SET_JOB = 5;
	private static final int NO_SET_JOB = 6;
	private static final int SET_BIRTHDAY = 7;
	private static final int GET_CODE_FOR_CITY = 8;
	private static final int GET_CODE_FOR_PROFESSION = 9;
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final int RESULT_MY = 10;// 结果
	public static final int AGIN_LOGIN = 11;

	public static final String IMAGE_UNSPECIFIED = "image/*";
	private ImageView personal_logo;
	RelativeLayout mine_head_photo_layout, mine_nickname_layout,
			mine_telnumber_layout, mine_sex_layout, mine_birthday_layout,
			mine_job_layout, mine_city_layout, mine_delivery_address_layout;
	public TextView btn_select_from_photo, btn_use_camera, btn_cancle_select,
			nickname_text, sex_text, birthday_text, job_text, city_text,
			sex_btn_man, sex_btn_women, phonenumber_text;
	AlertDialog dialog;
	LayoutInflater inflater;
	String headImageUrl;
	Bitmap bitmap;
	public static int year2, month2, day2;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MAN:
				sex_text.setText("男");
				break;
			case WOMEN:
				sex_text.setText("女");
				break;
			case SET_JOB:
				job_text.setText(job);
				break;
			case NO_SET_JOB:
				job_text.setText("未设置");
				break;
			case SET_BIRTHDAY:
				int year1 = mybirthPopWindow.year;
				int month1 = mybirthPopWindow.month;
				int day1 = mybirthPopWindow.day;
				year2 = year1;
				month2 = month1;
				day2 = day1;
				if (month1 < 10 && day1 >= 10) {
					birthday_text.setText(year1 + "-" + "0" + month1 + "-"
							+ day1);
					break;
				}
				if (day1 < 10 && month1 >= 10) {
					birthday_text.setText(year1 + "-" + month1 + "-" + "0"
							+ day1);
					break;
				}
				if (month1 < 10 && day1 < 10) {
					birthday_text.setText(year1 + "-" + "0" + month1 + "-"
							+ "0" + day1);
					break;
				} else {
					birthday_text.setText(year1 + "-" + month1 + "-" + day1);
				}
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		Intent intent1 = null;
		switch (v.getId()) {
		case R.id.mine_head_photo_layout: // 头像

			menuWindow = new SelectHeadPicPopupWindow(
					MyBasicInfomationActivity.this, itemsOnClick, 1);
			menuWindow.showAtLocation(
					MyBasicInfomationActivity.this.findViewById(R.id.personal),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.mine_nickname_layout: // 昵称
			Intent intent = new Intent(this, MyNickNameActivity.class);
			intent.putExtra("nickname", nickname_text.getText().toString());
			startActivityForResult(intent, GET_CODE);
			break;
		case R.id.btn_top_left: // 返回
			AppManager.getAppManager().finishActivity(this);
			break;
		case R.id.mine_sex_layout: // 性别
			menuWindow = new SelectHeadPicPopupWindow(
					MyBasicInfomationActivity.this, sexitemsOnClick, 1);
			menuWindow.from_pic_btn.setText("男");
			menuWindow.from_camera_btn.setText("女");
			menuWindow.showAtLocation(
					MyBasicInfomationActivity.this.findViewById(R.id.personal),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.mine_city_layout: // 地区
			intent1 = new Intent(this, LocationActivity.class);
			startActivityForResult(intent1, GET_CODE_FOR_CITY);
			break;
		case R.id.mine_delivery_address_layout: // 我的收货地址
			startForActivity(MyBasicInfomationActivity.this,
					MoreAddressCanRemoveActivity.class, null);
			break;
		case R.id.mine_job_layout: // 职业
			intent1 = new Intent(this, ProfessionActivity.class);
			startActivityForResult(intent1, GET_CODE_FOR_PROFESSION);
			break;
		// jobMenuWindow = new SelectJobPopupWindow(
		// MyBasicInfomationActivity.this, JobitemsOnClick,
		// R.id.personal);
		// jobMenuWindow.showAtLocation(
		// MyBasicInfomationActivity.this.findViewById(R.id.personal),
		// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		// break;
		case R.id.mine_birthday_layout: // 生日
			mybirthPopWindow = new SelectBirthdayPopupWindow(
					MyBasicInfomationActivity.this, birthItemClick);
			mybirthPopWindow.showAtLocation(
					MyBasicInfomationActivity.this.findViewById(R.id.personal),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			// sb = new SelectBirthday(MyBasicInfomationActivity.this);
			// sb.showAtLocation(MyBasicInfomationActivity.this.findViewById(R.id.personal),
			// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.btn_top_right: // 保存
			changeInfo();
			break;
		// case R.id.my_head_photo_image:
		// if (TextUtils.isEmpty(headImageUrl)) {
		// ToastUtils.makeText(MyBasicInfomationActivity.this,
		// "头像未设置\n无法查看大图", 0).show();
		// } else {
		// BigImagePopupWindow bigpop = new BigImagePopupWindow(
		// MyBasicInfomationActivity.this, headImageUrl, bitmap);
		// bigpop.showAtLocation(this.findViewById(R.id.personal),
		// Gravity.NO_GRAVITY, 0, 0);
		// }
		// break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		top_text.setText(R.string.mine_title);
		btn_right.setText("保存");
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setOnClickListener(this);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(MyBasicInfomationActivity.this,
				R.layout.activity_mine_basic_account_infomation, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		mine_head_photo_layout = (RelativeLayout) findViewById(R.id.mine_head_photo_layout);
		mine_nickname_layout = (RelativeLayout) findViewById(R.id.mine_nickname_layout);
		mine_telnumber_layout = (RelativeLayout) findViewById(R.id.mine_telnumber_layout);
		mine_sex_layout = (RelativeLayout) findViewById(R.id.mine_sex_layout);
		mine_birthday_layout = (RelativeLayout) findViewById(R.id.mine_birthday_layout);
		mine_job_layout = (RelativeLayout) findViewById(R.id.mine_job_layout);
		mine_city_layout = (RelativeLayout) findViewById(R.id.mine_city_layout);
		mine_delivery_address_layout = (RelativeLayout) findViewById(R.id.mine_delivery_address_layout);
		sex_text = (TextView) findViewById(R.id.sex_text);
		job_text = (TextView) findViewById(R.id.job_text);
		birthday_text = (TextView) findViewById(R.id.birthday_text);
		city_text = (TextView) findViewById(R.id.city_text);
		phonenumber_text = (TextView) findViewById(R.id.phonenumber_text);
		personal_logo = (ImageView) findViewById(R.id.my_head_photo_image);
		nickname_text = (TextView) findViewById(R.id.nickname_text);
		// personal_logo.setOnClickListener(this);
		mine_nickname_layout.setOnClickListener(this);
		mine_head_photo_layout.setOnClickListener(this);
		mine_delivery_address_layout.setOnClickListener(this);
		mine_sex_layout.setOnClickListener(this);
		mine_city_layout.setOnClickListener(this);
		mine_job_layout.setOnClickListener(this);
		mine_birthday_layout.setOnClickListener(this);

		initInfo();
		String s = birthday_text.getText().toString();
		Calendar c = Calendar.getInstance();
		if (s.equals("未设置")) {
			year2 = c.get(Calendar.YEAR);
			month2 = c.get(Calendar.MONTH) + 1;
			day2 = c.get(Calendar.DAY_OF_MONTH) - 1;
		} else {
			year2 = GetBirthdayInfo.getYear(s);
			month2 = GetBirthdayInfo.getMonth(s);
			day2 = GetBirthdayInfo.getDay(s);
		}
	}

	/**
	 * 初始化个人信息
	 */
	public void initInfo() {
		Gson gson = new Gson();
		/**
		 * 设置头像
		 */
		MyInfoMationRequest myInfoRequest = new MyInfoMationRequest();
		Parameter parameter = myInfoRequest.p;
		parameter.userId = GetUserIdUtil
				.getUserId(MyBasicInfomationActivity.this);
		parameter.tokenId = GetUserIdUtil
				.getTokenId(MyBasicInfomationActivity.this);
		String myinfoJson = gson.toJson(myInfoRequest);
		HttpConnectTool.update(myinfoJson, MyBasicInfomationActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						Gson gson = new Gson();
						MyInfoMationResult infoResult = gson.fromJson(result,
								MyInfoMationResult.class);
						com.jishijiyu.takeadvantage.entity.result.MyInfoMationResult.Parameter parameter1 = infoResult.p;
						Log.e("istrue?", "" + parameter1.isTrue);
						if (parameter1.isTrue) {
							headImageUrl = parameter1.UserExtendUser.headImgUrl;
							if (TextUtils
									.isEmpty(parameter1.UserExtendUser.headImgUrl)) {
								personal_logo
										.setBackgroundResource(R.drawable.pr);
							} else {
								ImageLoaderUtil.loadImage(
										parameter1.UserExtendUser.headImgUrl,
										personal_logo);
							}
						} else {
							ToastUtils.makeText(MyBasicInfomationActivity.this,
									R.string.again_login_text, 0).show();
							startForActivity(MyBasicInfomationActivity.this,
									LoginActivity.class, null);
							finish();
						}

					}

					@Override
					public void contectStarted() {

					}

					@Override
					public void contectFailed(String path, String request) {
						ToastUtils.makeText(MyBasicInfomationActivity.this,
								"访问服务器失败", 0).show();
						personal_logo
								.setBackgroundResource(R.drawable.pr_figure);
					}
				});
		String infoJson = FileUtil.readFile(MyBasicInfomationActivity.this,
				"InfoJson");
		if (infoJson != null && infoJson != "") {
			MyInfoMationResult infoResult = gson.fromJson(infoJson,
					MyInfoMationResult.class);

			com.jishijiyu.takeadvantage.entity.result.MyInfoMationResult.Parameter parameter1 = infoResult.p;
			/**
			 * 设置昵称
			 */
			if (!TextUtils.isEmpty(parameter1.UserExtendUser.nickname)) {
				nickname_text.setText(parameter1.UserExtendUser.nickname);
			} else {
				nickname_text.setText("未设置");
			}
			/**
			 * 设置手机号
			 */
			String json = (String) SPUtils.get(mContext,
					Constant.USER_INFO_FILE_NAME, "");
			LoginUserResult jsonObject = gson.fromJson(json,
					LoginUserResult.class);
			String phoneNumber = jsonObject.p.user.mobile;
			if (!TextUtils.isEmpty(phoneNumber)) {
				phonenumber_text.setText(phoneNumber);
			} else {
				String tel = GetUserIdUtil
						.getUserMobile(MyBasicInfomationActivity.this);
				phonenumber_text.setText(tel);
			}
			/**
			 * 设置性别
			 */
			if (parameter1.UserExtendUser.sex == 1) {
				sex_text.setText("男");
			}
			if (parameter1.UserExtendUser.sex == 2) {
				sex_text.setText("女");
			}
			/**
			 * 设置生日
			 */
			if (!TextUtils.isEmpty(parameter1.UserExtendUser.birthday)) {
				birthday_text.setText(parameter1.UserExtendUser.birthday);
			} else {
				birthday_text.setText("未设置");
			}
			/**
			 * 设置职业
			 */
			if (!TextUtils.isEmpty(parameter1.UserExtendUser.job)) {
				job_text.setText(parameter1.UserExtendUser.job);
			} else {
				job_text.setText("未设置");
			}
			/**
			 * 设置地区
			 */
			if (TextUtils.isEmpty(parameter1.UserExtendUser.province)
					&& TextUtils.isEmpty(parameter1.UserExtendUser.city)
					&& TextUtils.isEmpty(parameter1.UserExtendUser.area)) {
				province = "";
				city = "";
				area = "";
				city_text.setText("未设置");
			} else {
				if (parameter1.UserExtendUser.province.equals("北京市")
						|| parameter1.UserExtendUser.province.equals("天津市")
						|| parameter1.UserExtendUser.province.equals("上海市")
						|| parameter1.UserExtendUser.province.equals("重庆市")) {
					city_text.setText(parameter1.UserExtendUser.province
							+ parameter1.UserExtendUser.area);
					province = parameter1.UserExtendUser.province;
					area = parameter1.UserExtendUser.area;
					city = "";
				} else {
					city_text.setText(parameter1.UserExtendUser.province
							+ parameter1.UserExtendUser.city
							+ parameter1.UserExtendUser.area);
					province = parameter1.UserExtendUser.province;
					city = parameter1.UserExtendUser.city;
					area = parameter1.UserExtendUser.area;
				}

			}
		}

	}

	/**
	 * 请求修改个人信息
	 */
	public void changeInfo() {
		Gson gson = new Gson();
		String userId = GetUserIdUtil.getUserId(MyBasicInfomationActivity.this);
		ChangeMyInfomationRequest changeInfoRequest = new ChangeMyInfomationRequest();
		com.jishijiyu.takeadvantage.entity.request.ChangeMyInfomationRequest.Parameter parameter = changeInfoRequest.p;
		parameter.userId = userId;
		parameter.tokenId = GetUserIdUtil
				.getTokenId(MyBasicInfomationActivity.this);
		/**
		 * 修改头像
		 */
		if (TextUtils.isEmpty(headimg)) {
			Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir()
					+ Constant.HEAD_PIC_FILE_NAME);
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher);
				parameter.headImg = convertIconToString(bitmap);
			} else {
				parameter.headImg = convertIconToString(bitmap);
			}

		} else {
			parameter.headImg = headimg;
		}
		/**
		 * 修改昵称
		 */
		if (TextUtils.isEmpty(nickname_text.getText().toString())) {
			parameter.nickname = "未设置";
		} else {
			parameter.nickname = nickname_text.getText().toString();
		}
		/**
		 * 修改性别
		 */
		if (sex_text.getText().equals("男")) {
			parameter.sex = "1";
		}
		if (sex_text.getText().equals("女")) {
			parameter.sex = "2";
		} else {
			parameter.sex = "1";
		}
		/**
		 * 修改生日
		 */
		if (!TextUtils.isEmpty(birthday_text.getText())) {
			parameter.birthday = birthday_text.getText().toString();
		} else {
			parameter.birthday = "未设置";
		}
		/**
		 * 修改职业
		 */
		if (!TextUtils.isEmpty(job_text.getText())) {
			parameter.job = job_text.getText().toString();
		} else {
			parameter.job = "未设置";
		}
		/**
		 * 修改地区
		 */
		if (city_text.getText().toString().equals("未设置")
				|| TextUtils.isEmpty(city_text.getText())) {
			province = "";
			city = "";
			area = "";
		} else {
			parameter.province = province;
			parameter.city = city;
			parameter.area = area;
		}
		String json = gson.toJson(changeInfoRequest);
		HttpConnectTool.update(json, MyBasicInfomationActivity.this,
				new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						Gson gson = new Gson();
						ChangeMyInfomationResult changeInfoResult = gson
								.fromJson(result,
										ChangeMyInfomationResult.class);
						if (changeInfoResult.p.isTrue) {
							if (changeInfoResult.p.isSucce == true) {
								ToastUtils.makeText(
										MyBasicInfomationActivity.this,
										"保存信息成功", 0).show();
								finish();
							} else {
								ToastUtils.makeText(
										MyBasicInfomationActivity.this, "保存失败",
										0).show();
							}
						} else {
							ToastUtils.makeText(MyBasicInfomationActivity.this,
									R.string.again_login_text, 0).show();
							startForActivity(MyBasicInfomationActivity.this,
									LoginActivity.class, null);
							finish();
						}

					}

					@Override
					public void contectStarted() {

					}

					@Override
					public void contectFailed(String path, String request) {
						ToastUtils.makeText(MyBasicInfomationActivity.this,
								"访问服务器超时", 0).show();
					}
				});
	}

	/**
	 * 选择生日日期
	 */
	private OnClickListener birthItemClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.my_birth_btn_ok1:
				Calendar calendar = Calendar.getInstance();
				String s = mybirthPopWindow.wheelMain.getTime();
				mybirthPopWindow.year = GetBirthdayInfo.getYear(s);
				mybirthPopWindow.month = GetBirthdayInfo.getMonth(s);
				mybirthPopWindow.day = GetBirthdayInfo.getDay(s);
				if (mybirthPopWindow.year > calendar.get(Calendar.YEAR)) {
					ToastUtils.makeText(MyBasicInfomationActivity.this,
							"生日日期需小于今天", 0).show();
					break;
				}
				if (mybirthPopWindow.year == calendar.get(Calendar.YEAR)) {
					if (mybirthPopWindow.month > calendar.get(Calendar.MONTH)) {
						ToastUtils.makeText(MyBasicInfomationActivity.this,
								"生日日期需小于今天", 0).show();
						break;
					}
					if (mybirthPopWindow.month == calendar.get(Calendar.MONTH)) {
						if (mybirthPopWindow.day >= calendar
								.get(Calendar.DAY_OF_MONTH)) {
							ToastUtils.makeText(MyBasicInfomationActivity.this,
									"生日日期需小于今天", 0).show();
							break;
						} else {
							msg = msg.obtain();
							msg.what = SET_BIRTHDAY;
							handler.sendMessage(msg);
							mybirthPopWindow.dismiss();
						}
					} else {
						msg = msg.obtain();
						msg.what = SET_BIRTHDAY;
						handler.sendMessage(msg);
						mybirthPopWindow.dismiss();
					}
				} else {
					msg = msg.obtain();
					msg.what = SET_BIRTHDAY;
					handler.sendMessage(msg);
					mybirthPopWindow.dismiss();
				}
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 选择职业
	 */
	private OnClickListener JobitemsOnClick = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.select_job_ok_btn:
				trade = jobMenuWindow.select_trade_text.getText().toString();
				job = jobMenuWindow.select_job_text.getText().toString();
				if (TextUtils.isEmpty(trade) || TextUtils.isEmpty(job)) {
					ToastUtils.makeText(MyBasicInfomationActivity.this,
							"请选择行业", 0).show();
				} else {
					msg = msg.obtain();
					msg.what = SET_JOB;
					handler.sendMessage(msg);
					jobMenuWindow.dismiss();
				}

				break;

			default:
				break;
			}

		}
	};
	/**
	 * 判断选择性别
	 */
	private OnClickListener sexitemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			msg = msg.obtain();
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.from_pic_btn:
				msg.what = MAN;
				handler.sendMessage(msg);
				break;
			case R.id.from_camera_btn:
				msg.what = WOMEN;
				handler.sendMessage(msg);
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 判断选择按钮
	 */
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.from_pic_btn:
				Intent intent;
				intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, PHOTOZOOM);
				break;
			case R.id.from_camera_btn:
				Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intents.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(), "temp.jpg")));
				startActivityForResult(intents, PHOTOHRAPH);
				break;
			default:
				break;
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE) {
			return;
		}

		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/temp.jpg");
			startPhotoZoom(Uri.fromFile(picture));
		}

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			// startPhotoZoom(data.getData());
			String[] pojo = { MediaStore.Images.Media.DATA };
			Uri uri = data.getData();
			CursorLoader cursorLoader = new CursorLoader(this, uri, pojo, null,
					null, null);
			Cursor cursor = cursorLoader.loadInBackground();
			cursor.moveToFirst();
			startPhotoZoom(uri);

		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 100, stream);// (0 -
				bitmap = photo; // 100)压缩文件
				personal_logo.setImageBitmap(photo);
				headimg = convertIconToString(photo);
			}

		}

		if (resultCode == RESULT_OK) {
			if (requestCode == GET_CODE) {
				nickname_text.setText(data.getAction());
			} else if (requestCode == GET_CODE_FOR_CITY) {
				data.getExtras();
				province = data.getStringExtra("province");
				city = data.getStringExtra("city");
				area = data.getStringExtra("area");
				if (province.equals("北京市") || province.equals("天津市")
						|| province.equals("上海市") || province.equals("重庆市")) {
					city_text.setText(province + area);
				} else {
					city_text.setText(province + city + area);
				}
			}
		}
		if (resultCode == RESULT_OK) {
			if (requestCode == GET_CODE) {
				nickname_text.setText(data.getAction());
			} else if (requestCode == GET_CODE_FOR_PROFESSION) {
				data.getExtras();
				industry = data.getStringExtra("industry");
				profession = data.getStringExtra("profession");
				job_text.setText(profession);
			}
		}
		if (resultCode == Constant.AGAIN_LOGIN_CODE) {
			initInfo();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	/**
	 * bitmap转string
	 */

	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.PNG, 100, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}
