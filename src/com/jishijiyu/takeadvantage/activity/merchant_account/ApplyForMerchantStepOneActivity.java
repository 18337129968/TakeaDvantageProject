package com.jishijiyu.takeadvantage.activity.merchant_account;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.LocationActivity;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.EdittextUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 申请商家第一步
 * 
 * @author shifeiyu
 * 
 */
public class ApplyForMerchantStepOneActivity extends HeadBaseActivity {
	private final int GET_CODE_FOR_CITY = 0;
	private Button next;
	private EditText merchantName, merchantNum, merchantAddressDetail;
	private TextView merchantAddress, apply_merchant_address_tv,
			user_agreement;
	private RelativeLayout chooseCity, location;
	private String province, city, area, provinceCode, cityCode, areaCode;
	private Dialog dialog;
	private CheckBox ok_radio;
	private boolean check_radio = false;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.apply_merchant));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setBackgroundResource(R.drawable.btn_back);
		btn_left.setOnClickListener(this);
	}

	private void init() {
		apply_merchant_address_tv = (TextView) findViewById(R.id.apply_merchant_address_tv);
		merchantAddress = (TextView) findViewById(R.id.apply_merchant_address_tv);
		user_agreement = (TextView) findViewById(R.id.user_agreement);
		merchantNum = (EditText) findViewById(R.id.apply_merchant_phonenum_et);
		merchantName = (EditText) findViewById(R.id.apply_merchant_name_et);
		merchantAddressDetail = (EditText) findViewById(R.id.add_address_detailaddress_et);
		next = (Button) findViewById(R.id.apply_merchant_next);
		chooseCity = (RelativeLayout) findViewById(R.id.apply_merchant_choose_city);
		location = (RelativeLayout) findViewById(R.id.merchant_location);
		ok_radio = (CheckBox) findViewById(R.id.ok_radio);
		user_agreement.setOnClickListener(this);
		ok_radio.setOnClickListener(this);
		next.setOnClickListener(this);
		chooseCity.setOnClickListener(this);
		location.setOnClickListener(this);
		EdittextUtil.editMaxLength(merchantName, 30);
		EdittextUtil.editMaxLength(merchantNum, 12);
		EdittextUtil.editMaxLength(merchantAddressDetail, 50);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this,
				R.layout.activity_apply_merchant_step_one, null);
		base_centent.addView(view);
		init();
		// 广播
		IntentFilter filter = new IntentFilter(
				ApplyForMerchantSucceedActivity.actiona);
		registerReceiver(broadcastReceiver, filter);
	}

	// 检查商家信息
	public void checkMerchantInfo() {
		if (TextUtils.isEmpty(merchantName.getText())) {
			ToastUtils.makeText(mContext, "请填写商家名称", 0).show();
			return;
		}
		if (TextUtils.isEmpty(merchantName.getText())) {
			ToastUtils.makeText(mContext, "请填写商家名称", 0).show();
			return;
		}
		if (TextUtils.isEmpty(merchantNum.getText())) {
			ToastUtils.makeText(mContext, "请填写联系电话", 0).show();
			return;
		}
		if (TextUtils.isEmpty(merchantAddress.getText())) {
			ToastUtils.makeText(mContext, "请选择商家地址", 0).show();
			return;
		}
		if (TextUtils.isEmpty(merchantAddressDetail.getText())) {
			ToastUtils.makeText(mContext, "请填写详细地址", 0).show();
			return;
		}
		if (!check_radio) {
			ToastUtils.makeText(mContext, "请同意拍得利《用户使用协议》", 0).show();
			return;

		} else {
			Bundle bundle = new Bundle();
			bundle.putString("merchantName", merchantName.getText().toString());
			bundle.putString("merchantNum", merchantNum.getText().toString());
			bundle.putString("province", province);
			bundle.putString("city", city);
			bundle.putString("area", area);
			bundle.putString("provinceCode", provinceCode);
			bundle.putString("cityCode", cityCode);
			bundle.putString("areaCode", areaCode);
			bundle.putString("detailaddress", merchantAddressDetail.getText()
					.toString());
			Intent intent = new Intent(mContext,
					ApplyForMerchantStepTwoActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		// 商家定位
		case R.id.merchant_location:
			dialog = DialogUtils.showDialog(mContext, "拍得利正在读取你的地理位置",
					new int[] { R.string.buyunxu, R.string.yunxu },
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							switch (v.getId()) {
							case R.id.left:
								dialog.cancel();
								break;
							case R.id.right:
								dialog.cancel();
								Intent intent = new Intent(
										ApplyForMerchantStepOneActivity.this,
										MerchantMapActivity.class);
								intent.putExtra("merchantName", merchantName
										.getText().toString());
								startActivity(intent);
								break;
							default:
								break;
							}
						}
					}, false);
			break;
		// 下一步
		case R.id.apply_merchant_next:
			checkMerchantInfo();
			break;
		// 商家地址
		case R.id.apply_merchant_choose_city:
			intent = new Intent(this, LocationActivity.class);
			startActivityForResult(intent, GET_CODE_FOR_CITY);
			break;
		// 返回
		case R.id.btn_top_left:
			finish();
			break;
		// 同意协议
		case R.id.ok_radio:
			if (!check_radio) {
				check_radio = true;
				ok_radio.setChecked(true);
			} else {
				check_radio = false;
				ok_radio.setChecked(false);
			}
			break;
		// 查看协议
		case R.id.user_agreement:
			intent = new Intent(this, UserAgreement.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_CODE_FOR_CITY) {
			if (resultCode == RESULT_OK) {
				province = data.getStringExtra("province");
				city = data.getStringExtra("city");
				area = data.getStringExtra("area");
				provinceCode = data.getStringExtra("provinceCode");
				cityCode = data.getStringExtra("cityCode");
				areaCode = data.getStringExtra("areaCode");
				if (province.equals("北京市") || province.equals("天津市")
						|| province.equals("上海市") || province.equals("重庆市")) {
					city = "";
					apply_merchant_address_tv.setText(province + area);
				} else {
					apply_merchant_address_tv.setText(province + city + area);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 广播
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			finish();
		}
	};
}
