package com.jishijiyu.takeadvantage.activity.merchant_account;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.annotation.SuppressLint;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.AdvertisingRequest;
import com.jishijiyu.takeadvantage.entity.result.AdvertsingResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.EdittextUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.view.BigImagePopupWindow;
import com.jishijiyu.takeadvantage.view.SelectHeadPicPopupWindow;

/**
 * 发布广告界面
 * 
 * @author shifeiyu
 * 
 */
public class AdvertisingActivity extends HeadBaseActivity {
	private SelectHeadPicPopupWindow menuWindow;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	int witch = 0;
	boolean flag1 = false, flag2 = false, flag3 = false;
	String isRightAnswer = null;
	String advert_pic1 = "", advert_pic2 = "", advert_pic3 = "",
			advert_pic4 = "", advert_pic5 = "", companyId = "";
	Bitmap bitmap1 = null, bitmap2 = null, bitmap3 = null, bitmap4 = null,
			bitmap5 = null;
	TextView commit_check_btn;
	EditText advert_name_edit, advert_edit, important_words, content_synopsis,
			show_phone, show_address, question_explain, answers1_edit,
			answers2_edit, answers3_edit;
	CheckBox select_show_phone, select_show_address;
	ImageView advert_add_pic_btn, advertising_img1, advertising_img2,
			advertising_img3, advertising_img4, advertising_img5,
			select_answers1, select_answers2, select_answers3;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_check_btn:
			checkAdvertInfo();
			break;
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.advert_add_pic_btn:
			witch = 6;
			menuWindow = new SelectHeadPicPopupWindow(AdvertisingActivity.this,
					itemsOnClick, 2);
			menuWindow.preview_btn.setVisibility(View.GONE);
			menuWindow.advert_delete_image.setVisibility(View.GONE);
			LayoutParams params = (LayoutParams) menuWindow.pop_layout1
					.getLayoutParams();
			params.weight = (float) 0.5;
			menuWindow.pop_layout1.setLayoutParams(params);
			menuWindow.showAtLocation(
					AdvertisingActivity.this.findViewById(R.id.advertising),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.advertising_img1:
			witch = 1;
			menuWindow = new SelectHeadPicPopupWindow(AdvertisingActivity.this,
					itemsOnClick, 2);
			menuWindow.showAtLocation(
					AdvertisingActivity.this.findViewById(R.id.advertising),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.advertising_img2:
			witch = 2;
			menuWindow = new SelectHeadPicPopupWindow(AdvertisingActivity.this,
					itemsOnClick, 2);
			menuWindow.showAtLocation(
					AdvertisingActivity.this.findViewById(R.id.advertising),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.advertising_img3:
			witch = 3;
			menuWindow = new SelectHeadPicPopupWindow(AdvertisingActivity.this,
					itemsOnClick, 2);
			menuWindow.showAtLocation(
					AdvertisingActivity.this.findViewById(R.id.advertising),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.advertising_img4:
			witch = 4;
			menuWindow = new SelectHeadPicPopupWindow(AdvertisingActivity.this,
					itemsOnClick, 2);
			menuWindow.showAtLocation(
					AdvertisingActivity.this.findViewById(R.id.advertising),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.advertising_img5:
			witch = 5;
			menuWindow = new SelectHeadPicPopupWindow(AdvertisingActivity.this,
					itemsOnClick, 2);
			menuWindow.showAtLocation(
					AdvertisingActivity.this.findViewById(R.id.advertising),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.select_answers1:
			flag1 = true;
			flag2 = false;
			flag3 = false;
			select_answers1
					.setImageResource(R.drawable.answearquestion_input_choosed);
			select_answers2
					.setImageResource(R.drawable.answearquestion_input_no_choose);
			select_answers3
					.setImageResource(R.drawable.answearquestion_input_no_choose);
			break;
		case R.id.select_answers2:
			flag1 = false;
			flag2 = true;
			flag3 = false;
			select_answers1
					.setImageResource(R.drawable.answearquestion_input_no_choose);
			select_answers2
					.setImageResource(R.drawable.answearquestion_input_choosed);
			select_answers3
					.setImageResource(R.drawable.answearquestion_input_no_choose);
			break;
		case R.id.select_answers3:
			flag1 = false;
			flag2 = false;
			flag3 = true;
			select_answers1
					.setImageResource(R.drawable.answearquestion_input_no_choose);
			select_answers2
					.setImageResource(R.drawable.answearquestion_input_no_choose);
			select_answers3
					.setImageResource(R.drawable.answearquestion_input_choosed);
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		btn_right.setTextSize(12);
		btn_right.setText("存入草稿");
		top_text.setText("发布广告");

	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(AdvertisingActivity.this,
				R.layout.activity_advertising, null);
		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		companyId = intent.getStringExtra("companyId");
		initView();
		initClick();

	}

	private void initView() {
		advert_name_edit = (EditText) findViewById(R.id.advert_name_edit);
		advert_edit = (EditText) findViewById(R.id.advert_edit);
		important_words = (EditText) findViewById(R.id.important_words);
		content_synopsis = (EditText) findViewById(R.id.content_synopsis);
		show_phone = (EditText) findViewById(R.id.show_phone);
		show_address = (EditText) findViewById(R.id.show_address);
		commit_check_btn = (TextView) findViewById(R.id.commit_check_btn);
		select_show_phone = (CheckBox) findViewById(R.id.select_show_phone);
		select_show_address = (CheckBox) findViewById(R.id.select_show_address);
		advert_add_pic_btn = (ImageView) findViewById(R.id.advert_add_pic_btn);
		advertising_img1 = (ImageView) findViewById(R.id.advertising_img1);
		advertising_img2 = (ImageView) findViewById(R.id.advertising_img2);
		advertising_img3 = (ImageView) findViewById(R.id.advertising_img3);
		advertising_img4 = (ImageView) findViewById(R.id.advertising_img4);
		advertising_img5 = (ImageView) findViewById(R.id.advertising_img5);
		question_explain = (EditText) findViewById(R.id.question_explain);
		answers1_edit = (EditText) findViewById(R.id.answers1_edit);
		answers2_edit = (EditText) findViewById(R.id.answers2_edit);
		answers3_edit = (EditText) findViewById(R.id.answers3_edit);
		select_answers1 = (ImageView) findViewById(R.id.select_answers1);
		select_answers2 = (ImageView) findViewById(R.id.select_answers2);
		select_answers3 = (ImageView) findViewById(R.id.select_answers3);
		select_answers1.setOnClickListener(this);
		select_answers2.setOnClickListener(this);
		select_answers3.setOnClickListener(this);
		EdittextUtil.editMaxLength(advert_name_edit, 20);
		EdittextUtil.editMaxLength(advert_edit, 50);
		EdittextUtil.editMaxLength(important_words, 20);
		EdittextUtil.editMaxLength(content_synopsis, 100);
		EdittextUtil.editMaxLength(show_phone, 12);
		EdittextUtil.editMaxLength(show_address, 50);
	}

	private void initClick() {
		commit_check_btn.setOnClickListener(this);
		advert_add_pic_btn.setOnClickListener(this);
		advertising_img1.setOnClickListener(this);
		advertising_img2.setOnClickListener(this);
		advertising_img3.setOnClickListener(this);
		advertising_img4.setOnClickListener(this);
		advertising_img5.setOnClickListener(this);

	}

	// 检查广告信息
	private void checkAdvertInfo() {
		if (TextUtils.isEmpty(advert_name_edit.getText())) {
			ToastUtils.makeText(mContext, "请输入广告名称", 0).show();
			return;
		}
		if (advert_pic1 == "" && advert_pic2 == "" && advert_pic3 == ""
				&& advert_pic4 == "" && advert_pic5 == "") {
			ToastUtils.makeText(mContext, "请上传至少一张广告图片", 0).show();
			return;
		}
		if (TextUtils.isEmpty(advert_edit.getText())) {
			ToastUtils.makeText(mContext, "请输入广告语", 0).show();
			return;
		}
		if (TextUtils.isEmpty(important_words.getText())) {
			ToastUtils.makeText(mContext, "请输入核心记忆词", 0).show();
			return;
		}
		if (TextUtils.isEmpty(advert_edit.getText())) {
			ToastUtils.makeText(mContext, "请输入内容简介", 0).show();
			return;
		}
		if (TextUtils.isEmpty(question_explain.getText())) {
			ToastUtils.makeText(mContext, "请输入问题描述", 0).show();
			return;
		}
		if (TextUtils.isEmpty(answers1_edit.getText())) {
			ToastUtils.makeText(mContext, "请输入选项1答案", 0).show();
			return;
		}
		if (TextUtils.isEmpty(answers2_edit.getText())) {
			ToastUtils.makeText(mContext, "请输入选项2答案", 0).show();
			return;
		}
		if (TextUtils.isEmpty(answers3_edit.getText())) {
			ToastUtils.makeText(mContext, "请输入选项3答案", 0).show();
			return;
		}
		if (flag1 == false && flag2 == false && flag3 == false) {
			ToastUtils.makeText(mContext, "请选择一项正确答案", 0).show();
			return;
		} else {
			if (flag1) {
				isRightAnswer = answers1_edit.getText().toString();
			}
			if (flag2) {
				isRightAnswer = answers2_edit.getText().toString();
			}
			if (flag3) {
				isRightAnswer = answers3_edit.getText().toString();
			}
			final Gson gson = new Gson();
			AdvertisingRequest advertRequest = new AdvertisingRequest();
			advertRequest.p.userId = GetUserIdUtil.getUserId(mContext);
			advertRequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
			advertRequest.p.posterTitle = advert_name_edit.getText().toString();
			advertRequest.p.posterImgUrl_1 = advert_pic1;
			advertRequest.p.posterImgUrl_2 = advert_pic2;
			advertRequest.p.posterImgUrl_3 = advert_pic3;
			advertRequest.p.posterImgUrl_4 = advert_pic4;
			advertRequest.p.posterImgUrl_5 = advert_pic5;
			advertRequest.p.posterSign = advert_edit.getText().toString();
			advertRequest.p.heartMemery = important_words.getText().toString();
			advertRequest.p.posterDescribe = advert_edit.getText().toString();
			advertRequest.p.questionDescribe = question_explain.getText()
					.toString();
			advertRequest.p.questionOption_1 = answers1_edit.getText()
					.toString();
			advertRequest.p.questionOption_2 = answers2_edit.getText()
					.toString();
			advertRequest.p.questionOption_3 = answers3_edit.getText()
					.toString();
			advertRequest.p.answer = isRightAnswer;
			String json = gson.toJson(advertRequest);
			HttpConnectTool.update(json, mContext, new ConnectListener() {

				@Override
				public void contectSuccess(String result) {
					if (!TextUtils.isEmpty(result)) {
						AdvertsingResult advertResult = gson.fromJson(result,
								AdvertsingResult.class);
						if (advertResult.p.isTrue) {
							if (advertResult.p.isSucce) {
								ToastUtils.makeText(mContext, "提交审核成功！", 0)
										.show();
								Intent intent = new Intent(mContext,
										MyAdvertActivity.class);
								intent.putExtra("companyId", companyId);
								startActivity(intent);
								// startForActivity(mContext,
								// MyAdvertActivity.class, null);
								finish();
							} else {
								ToastUtils.makeText(mContext, "提交审核失败", 0)
										.show();
							}
						} else {
							ToastUtils.makeText(mContext,
									R.string.again_login_text, 0).show();
							startForActivity(mContext, LoginActivity.class,
									null);
							finish();
						}
					}
				}

				@Override
				public void contectStarted() {
					// TODO Auto-generated method stub

				}

				@Override
				public void contectFailed(String path, String request) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	/**
	 * 判断选择按钮
	 */
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			// 从相册选取
			case R.id.advert_from_pic_btn:
				Intent intent;
				intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, PHOTOZOOM);
				break;
			// 相机拍摄
			case R.id.advert_from_camera_btn:
				Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intents.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(), "temp.jpg")));
				startActivityForResult(intents, PHOTOHRAPH);
				break;
			// 预览
			case R.id.preview_btn:
				switch (witch) {
				case 1:
					if (bitmap1 == null) {
						ToastUtils.makeText(AdvertisingActivity.this, "请先上传图片",
								0).show();
					} else {
						BigImagePopupWindow bigpop = new BigImagePopupWindow(
								AdvertisingActivity.this, null, bitmap1);
						bigpop.showAtLocation(AdvertisingActivity.this
								.findViewById(R.id.advertising),
								Gravity.NO_GRAVITY, 0, 0);
					}
					break;
				case 2:
					if (bitmap2 == null) {
						ToastUtils.makeText(AdvertisingActivity.this, "请先上传图片",
								0).show();
					} else {
						BigImagePopupWindow bigpop = new BigImagePopupWindow(
								AdvertisingActivity.this, null, bitmap2);
						bigpop.showAtLocation(AdvertisingActivity.this
								.findViewById(R.id.advertising),
								Gravity.NO_GRAVITY, 0, 0);
					}
					break;
				case 3:
					if (bitmap3 == null) {
						ToastUtils.makeText(AdvertisingActivity.this, "请先上传图片",
								0).show();
					} else {
						BigImagePopupWindow bigpop = new BigImagePopupWindow(
								AdvertisingActivity.this, null, bitmap3);
						bigpop.showAtLocation(AdvertisingActivity.this
								.findViewById(R.id.advertising),
								Gravity.NO_GRAVITY, 0, 0);
					}
					break;
				case 4:
					if (bitmap4 == null) {
						ToastUtils.makeText(AdvertisingActivity.this, "请先上传图片",
								0).show();
					} else {
						BigImagePopupWindow bigpop = new BigImagePopupWindow(
								AdvertisingActivity.this, null, bitmap4);
						bigpop.showAtLocation(AdvertisingActivity.this
								.findViewById(R.id.advertising),
								Gravity.NO_GRAVITY, 0, 0);
					}
					break;
				case 5:
					if (bitmap5 == null) {
						ToastUtils.makeText(AdvertisingActivity.this, "请先上传图片",
								0).show();
					} else {
						BigImagePopupWindow bigpop = new BigImagePopupWindow(
								AdvertisingActivity.this, null, bitmap5);
						bigpop.showAtLocation(AdvertisingActivity.this
								.findViewById(R.id.advertising),
								Gravity.NO_GRAVITY, 0, 0);
					}
					break;
				case 6:
					ToastUtils.makeText(AdvertisingActivity.this,
							"请点击已上传的图片预览", 0).show();
					break;

				default:
					break;
				}
				break;
			// 删除
			case R.id.advert_delete_image:
				switch (witch) {
				case 1:
					advert_pic1 = "";
					bitmap1 = null;
					advertising_img1.setVisibility(View.GONE);
					advert_add_pic_btn.setVisibility(View.VISIBLE);
					break;
				case 2:
					advert_pic2 = "";
					bitmap2 = null;
					advertising_img2.setVisibility(View.GONE);
					advert_add_pic_btn.setVisibility(View.VISIBLE);
					break;
				case 3:
					advert_pic3 = "";
					bitmap3 = null;
					advertising_img3.setVisibility(View.GONE);
					advert_add_pic_btn.setVisibility(View.VISIBLE);
					break;
				case 4:
					advert_pic4 = "";
					bitmap4 = null;
					advertising_img4.setVisibility(View.GONE);
					advert_add_pic_btn.setVisibility(View.VISIBLE);
					break;
				case 5:
					advert_pic5 = "";
					bitmap5 = null;
					advertising_img5.setVisibility(View.GONE);
					advert_add_pic_btn.setVisibility(View.VISIBLE);
					break;
				case 6:
					ToastUtils.makeText(AdvertisingActivity.this,
							"请点击已上传的图片删除", 0).show();
					break;
				default:
					break;
				}
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
				// 100)压缩文件
				switch (witch) {
				case 1:
					bitmap1 = photo;
					advert_pic1 = convertIconToString(photo);
					advertising_img1.setImageBitmap(photo);
					return;
				case 2:
					bitmap2 = photo;
					advert_pic2 = convertIconToString(photo);
					advertising_img2.setImageBitmap(photo);
					return;
				case 3:
					bitmap3 = photo;
					advert_pic3 = convertIconToString(photo);
					advertising_img3.setImageBitmap(photo);
					return;
				case 4:
					bitmap4 = photo;
					advert_pic4 = convertIconToString(photo);
					advertising_img4.setImageBitmap(photo);
					return;
				case 5:
					bitmap5 = photo;
					advert_pic5 = convertIconToString(photo);
					advertising_img5.setImageBitmap(photo);
					return;

				default:
					break;
				}
				if (advert_pic1 == "") {
					bitmap1 = photo;
					advert_pic1 = convertIconToString(photo);
					advertising_img1.setVisibility(View.VISIBLE);
					advertising_img1.setImageBitmap(photo);
					return;
				}
				if (advert_pic1 != "" && advert_pic2 == "") {
					bitmap2 = photo;
					advert_pic2 = convertIconToString(photo);
					advertising_img2.setVisibility(View.VISIBLE);
					advertising_img2.setImageBitmap(photo);
					return;
				}
				if (advert_pic2 != "" && advert_pic3 == "") {
					bitmap3 = photo;
					advert_pic3 = convertIconToString(photo);
					advertising_img3.setVisibility(View.VISIBLE);
					advertising_img3.setImageBitmap(photo);
					return;
				}
				if (advert_pic3 != "" && advert_pic4 == "") {
					bitmap4 = photo;
					advert_pic4 = convertIconToString(photo);
					advertising_img4.setVisibility(View.VISIBLE);
					advertising_img4.setImageBitmap(photo);
					return;
				}
				if (advert_pic4 != "" && advert_pic5 == "") {
					bitmap5 = photo;
					advert_pic5 = convertIconToString(photo);
					advertising_img5.setVisibility(View.VISIBLE);
					advertising_img5.setImageBitmap(photo);
					advert_add_pic_btn.setVisibility(View.GONE);
					return;
				}

			}

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

}
