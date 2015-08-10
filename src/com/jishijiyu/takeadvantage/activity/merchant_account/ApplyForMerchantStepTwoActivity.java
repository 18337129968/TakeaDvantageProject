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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.ApplyMerchantRequest;
import com.jishijiyu.takeadvantage.entity.result.ApplyMerchantResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.view.SelectHeadPicPopupWindow;

/**
 * 申请商家第二步
 * 
 * @author shifeiyu
 * 
 */
public class ApplyForMerchantStepTwoActivity extends HeadBaseActivity {
	private SelectHeadPicPopupWindow menuWindow;
	TextView industryCategorytv;
	RelativeLayout chooseCategory;
	EditText businessProfile_et, featuresAdvantage_et, business_code_edit;
	ImageView logo_iv, document_iv, certificate_iv1;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int NONE = 0;
	public static final int GET_TRADE_CODE = 4;
	int a = 0;
	Button submit;
	View view;
	Bitmap bitmap;
	private String merchantLogo = null, merchantDocment = null, other1 = null,
			tradename = null, subname = null, tradecode = null, subcode = null;
	ApplyMerchantRequest applymerchantrequest = new ApplyMerchantRequest();
	ApplyMerchantResult applymerchantresult = new ApplyMerchantResult();
	private Gson gson;
	private Boolean issuccess;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.apply_merchant));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setBackgroundResource(R.drawable.btn_back);
		btn_left.setOnClickListener(this);

	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		view = View.inflate(this, R.layout.activity_apply_merchant_step_two,
				null);
		base_centent.addView(view);
		init();
	}

	private void init() {
		business_code_edit = (EditText) findViewById(R.id.business_code_edit);
		industryCategorytv = (TextView) findViewById(R.id.apply_merchant_industry_category_tv);
		chooseCategory = (RelativeLayout) findViewById(R.id.apply_merchant_choose_category);
		businessProfile_et = (EditText) findViewById(R.id.apply_merchant_business_profile_et);
		featuresAdvantage_et = (EditText) findViewById(R.id.apply_merchant_features_advantage_et);
		logo_iv = (ImageView) findViewById(R.id.upload_business_logo_iv1);
		document_iv = (ImageView) findViewById(R.id.upload_business_document_iv2);
		certificate_iv1 = (ImageView) findViewById(R.id.upload_registration_certificate_iv3);
		submit = (Button) findViewById(R.id.apply_merchant_submit);
		business_code_edit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		logo_iv.setOnClickListener(this);
		document_iv.setOnClickListener(this);
		certificate_iv1.setOnClickListener(this);
		chooseCategory.setOnClickListener(this);
		submit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 设置行业类别
		case R.id.apply_merchant_choose_category:
			Intent intent = new Intent(mContext,
					SelectBusinessTradeActivity.class);
			startActivityForResult(intent, GET_TRADE_CODE);
			break;
		// 上传LOGO
		case R.id.upload_business_logo_iv1:
			a = 1;
			menuWindow = new SelectHeadPicPopupWindow(this, itemsOnClick, 1);
			menuWindow.showAtLocation(ApplyForMerchantStepTwoActivity.this
					.findViewById(R.id.apply_merchant_step_two_ll),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		// 上传资质
		case R.id.upload_business_document_iv2:
			a = 2;
			menuWindow = new SelectHeadPicPopupWindow(this, itemsOnClick, 1);
			menuWindow.showAtLocation(ApplyForMerchantStepTwoActivity.this
					.findViewById(R.id.apply_merchant_step_two_ll),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		// 上传组织机构代码证/税务登记证
		case R.id.upload_registration_certificate_iv3:
			a = 3;
			menuWindow = new SelectHeadPicPopupWindow(this, itemsOnClick, 1);
			menuWindow.showAtLocation(ApplyForMerchantStepTwoActivity.this
					.findViewById(R.id.apply_merchant_step_two_ll),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		// 提交
		case R.id.apply_merchant_submit:
			checkMerchantInfo();
			break;
		case R.id.btn_top_left:
			finish();
			break;
		default:
			break;
		}
	}

	// 检查商户信息
	private void checkMerchantInfo() {
		if (TextUtils.isEmpty(industryCategorytv.getText())) {
			ToastUtils.makeText(mContext, "请选择行业类别", 0).show();
			return;
		}
		if (TextUtils.isEmpty(businessProfile_et.getText())) {
			ToastUtils.makeText(mContext, "请输入商家简介", 0).show();
			return;
		}
		if (TextUtils.isEmpty(featuresAdvantage_et.getText())) {
			ToastUtils.makeText(mContext, "特色优势不能为空", 0).show();
			return;
		}
		if (TextUtils.isEmpty(merchantLogo)) {
			ToastUtils.makeText(mContext, "请上传商家LOGO", 0).show();
			return;
		}
		if (TextUtils.isEmpty(merchantDocment)) {
			ToastUtils.makeText(mContext, "请上传营业执照", 0).show();
			return;
		}
		if (TextUtils.isEmpty(business_code_edit.getText())) {
			ToastUtils.makeText(mContext, "请输入营业执照号码", 0).show();
			return;
		} else {
			Bundle bundle = getIntent().getExtras();
			gson = new Gson();

			applymerchantrequest.p.userId = GetUserIdUtil.getUserId(mContext);
			applymerchantrequest.p.tokenId = GetUserIdUtil.getTokenId(mContext);
			applymerchantrequest.p.companyAddressProvince = bundle
					.getString("provinceCode");
			applymerchantrequest.p.companyAddressCity = bundle
					.getString("cityCode");
			applymerchantrequest.p.companyAddressArea = bundle
					.getString("areaCode");
			applymerchantrequest.p.companyAddressDetails = bundle
					.getString("detailaddress");
			applymerchantrequest.p.companyName = bundle
					.getString("merchantName");
			applymerchantrequest.p.telephone = bundle.getString("merchantNum");
			applymerchantrequest.p.companyTradeA = tradecode;
			applymerchantrequest.p.companyTradeB = subcode;
			applymerchantrequest.p.goodness = featuresAdvantage_et.getText()
					.toString().trim();
			applymerchantrequest.p.logoImgUrl = merchantLogo;
			applymerchantrequest.p.aptitudeImgUrl = merchantDocment;
			if (!TextUtils.isEmpty(other1)) {
				applymerchantrequest.p.taxationImgUrl = other1;
			} else {
				applymerchantrequest.p.taxationImgUrl = "";
			}
			applymerchantrequest.p.licenseNum = business_code_edit.getText()
					.toString().trim();
			applymerchantrequest.p.place = "13123123";

			String json = gson.toJson(applymerchantrequest);
			HttpConnectTool.update(json, mContext, new ConnectListener() {

				@Override
				public void contectSuccess(String result) {
					if (!TextUtils.isEmpty(result)) {

						applymerchantresult = gson.fromJson(result,
								ApplyMerchantResult.class);
						if (applymerchantresult.p.isTrue) {

							issuccess = applymerchantresult.p.isSucce;
							if (issuccess) {
								startForActivity(mContext,
										ApplyForMerchantSucceedActivity.class,
										null);

								finish();
							}

							init();
						} else {
							Intent intent = new Intent(
									ApplyForMerchantStepTwoActivity.this,
									LoginActivity.class);
							startActivityForResult(intent, RESULT_OK);
						}
						LogUtil.i("result" + result);
					}
				}

				@Override
				public void contectStarted() {
					// TODO Auto-generated method stub

				}

				@Override
				public void contectFailed(String path, String request) {
					// TODO Auto-generated method stub
					Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
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
		if (requestCode == GET_TRADE_CODE) {
			if (resultCode == RESULT_OK) {
				tradename = data.getStringExtra("tradeName");
				subname = data.getStringExtra("subName");
				tradecode = data.getStringExtra("tradeCode");
				subcode = data.getStringExtra("subCode");
				industryCategorytv.setText(tradename + "/" + subname);
				// industryCategorytv.setText(tradecode + "/" + subcode);
			}
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
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100, stream);// (0
																			// -
					bitmap = photo; // 100)压缩文件
					if (a == 1) {
						logo_iv.setImageBitmap(photo);
						merchantLogo = convertIconToString(photo);
					}
					if (a == 2) {
						document_iv.setImageBitmap(photo);
						merchantDocment = convertIconToString(photo);
					}
					if (a == 3) {
						certificate_iv1.setImageBitmap(photo);
						other1 = convertIconToString(photo);
					}
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
