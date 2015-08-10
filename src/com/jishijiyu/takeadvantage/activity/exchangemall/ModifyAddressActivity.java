package com.jishijiyu.takeadvantage.activity.exchangemall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.LocationActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.myinformation.MoreAddressCanRemoveActivity;
import com.jishijiyu.takeadvantage.entity.request.ModifyAddressRequest;
import com.jishijiyu.takeadvantage.entity.result.Address;
import com.jishijiyu.takeadvantage.entity.result.ModifyAddressResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
/**
 * 修改收货地址
 * @author zcl
 *
 */
public class ModifyAddressActivity extends HeadBaseActivity implements
		OnClickListener {
	static final private int GET_CODE = 0;
	/** 获取数据成功 */
	private static final int LOADING_DATA_SUCCESS = 1;
	/** 获取数据失败 */
	private static final int LOADING_DATA_FAILED = 2;
	private Gson gson;
	private String result;
	private Message message;
	EditText name, phoneNum, zipCode, detail;
	TextView area_tv;
	Button save, defult;
	LinearLayout isDefultLL;
	boolean isdefult = false;
	ImageView isDefult;
	ModifyAddressRequest modifyAddressRequest = new ModifyAddressRequest();
	ModifyAddressResult modifyAddressResult = new ModifyAddressResult();
	private Boolean issuccess;
	String province = null, city = null, area = null, id = "";
	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//
//			case LOADING_DATA_SUCCESS:
//				modifyAddressResult = gson.fromJson(result,
//						ModifyAddressResult.class);
//				issuccess = modifyAddressResult.p.isSucce;
//				if (issuccess) {
//					Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
//					startForActivity(mContext,
//							MoreAddressCanRemoveActivity.class, null);
//					finish();
//				} else {
//					Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
//				}
//				init();
//				break;
//			case LOADING_DATA_FAILED:
//				Toast.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();
//				break;
//			default:
//				break;
//			}
//		};
//	};

	private void requestData(Object entry) {
		gson = new Gson();
		String request = gson.toJson(entry);
		LogUtil.i(entry + "request" + request);
		HttpConnectTool.update(request, mContext, new ConnectListener() {
			
			@Override
			public void contectSuccess(String result) {
				modifyAddressResult = gson.fromJson(result,
						ModifyAddressResult.class);
				if(modifyAddressResult.p.isTrue){
				issuccess = modifyAddressResult.p.isSucce;
				if (issuccess) {
					ToastUtils.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
					startForActivity(mContext,
							MoreAddressCanRemoveActivity.class, null);
					finish();
				} else {
					ToastUtils.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
				}
				init();	
				}else{
					Intent intent = new Intent(ModifyAddressActivity.this,LoginActivity.class);
					startActivity(intent);
				}
			}
			
			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();
				
			}
		});
		
//		result = HttpConnectUtil.updata(request, false, null, null);
//		LogUtil.i("result" + result);
//		message = Message.obtain();
//		if (!TextUtils.isEmpty(result)) {
//			message.what = LOADING_DATA_SUCCESS;
//		} else {
//			message.what = LOADING_DATA_FAILED;
//		}
//		handler.sendMessage(message);

	}

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.modify_address));
		btn_right.setVisibility(View.VISIBLE);
		btn_left.setVisibility(View.VISIBLE);
		btn_right.setText(R.string.save);
		btn_right.setGravity(Gravity.CENTER);
		btn_right.setOnClickListener(this);
		btn_left.setOnClickListener(this);
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this, R.layout.activity_modify_address, null);
		base_centent.addView(view);
		init();

	}

	private void init() {
		LinearLayout city_ll = (LinearLayout) findViewById(R.id.modify_address_ll);
		city_ll.setOnClickListener(this);
		name = (EditText) findViewById(R.id.modify_address_name_et);
		phoneNum = (EditText) findViewById(R.id.modify_address_phonenum_et);
		zipCode = (EditText) findViewById(R.id.modify_address_zipcode_et);
		detail = (EditText) findViewById(R.id.modify_address_detailaddress_et);
		area_tv = (TextView) findViewById(R.id.modify_address_tv);
//		defult = (Button) findViewById(R.id.modify_address_set_defult_btn);
//		defult.setOnClickListener(this);
		isDefult = (ImageView) findViewById(R.id.modify_address_isdefult);
		isDefultLL =  (LinearLayout) findViewById(R.id.modify_address_isdefult_ll);
		isDefultLL.setOnClickListener(this);
//		isDefult.setOnClickListener(this);
		String type = getIntent().getStringExtra(HeadBaseActivity.intentKey);
		Address data = (Address) getIntent().getSerializableExtra("address");

		if (!type.equals("") && type.equals("isdefult")) {
//			defult.setVisibility(View.VISIBLE);
		} else {
			save.setVisibility(View.VISIBLE);
		}
		if (data != null) {
			name.setText(data.name);
			phoneNum.setText(data.telephone);
			zipCode.setText(data.postalCode);
			detail.setText(data.detailedAddress);
			area_tv.setText(data.province + " " + data.city + " " + data.area);
			province = data.province;
			city = data.city;
			area = data.area;
			id = "" + data.id;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify_address_ll:
			Intent intent = new Intent(this, LocationActivity.class);
			startActivityForResult(intent, GET_CODE);
			break;
//		case R.id.modify_address_set_defult_btn:
//			   if(!isdefult){
//	            	isDefult.setBackgroundResource(R.drawable.shouhuo_btn_choose);
//	            	isdefult = true;
//	            }else{
//	            	isDefult.setBackgroundResource(R.drawable.shouhuo_btn_no_choose);
//	            	isdefult = false;
//	            }
		case R.id.modify_address_isdefult_ll:
			if(!isdefult){
				isDefult.setBackgroundResource(R.drawable.shouhuo_btn_choose);
				isdefult = true;
			}else{
				isDefult.setBackgroundResource(R.drawable.shouhuo_btn_no_choose);
				isdefult = false;
			}
				
			break;
		case R.id.btn_top_right:
			if (isNoData()) {
				return;
			}
			getData();
			requestData(modifyAddressRequest);
			//

			break;
		case R.id.btn_top_left:
			finish();
			break;
		default:
			break;
		}

	}

	private boolean isNoData() {
		if (name.getText().toString().trim().equals("")
				|| phoneNum.getText().toString().trim().equals("")
				|| area_tv.getText().toString().trim().equals("省、市、区")
				|| zipCode.getText().toString().trim().equals("")
				|| detail.getText().toString().trim().equals("")) {
			Toast.makeText(this, "请填写完整的收货信息", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	private void getData() {
		String userid = GetUserIdUtil.getUserId(this);
		if (userid == null) {
			return;
		} else {
			modifyAddressRequest.p.userId = userid;
		}
		String tokenId = GetUserIdUtil.getTokenId(this);
		modifyAddressRequest.p.tokenId = tokenId;
		modifyAddressRequest.p.name = name.getText().toString();
		modifyAddressRequest.p.telephone = phoneNum.getText().toString();
		modifyAddressRequest.p.detailedAddress = detail.getText().toString();
		modifyAddressRequest.p.postalCode = zipCode.getText().toString();
		modifyAddressRequest.p.area = area;
		modifyAddressRequest.p.city = city;
		modifyAddressRequest.p.province = province;
		modifyAddressRequest.p.id = id;
		if (isdefult) {
			modifyAddressRequest.p.type = "2";
		} else {
			modifyAddressRequest.p.type = "1";
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		final TextView area_tv = (TextView) findViewById(R.id.modify_address_tv);

		if (requestCode == GET_CODE) {

			if (resultCode == RESULT_OK) {

				if (!data.getStringExtra("province").equals("")) {
					province = data.getStringExtra("province");
					city = data.getStringExtra("city");
					area = data.getStringExtra("area");
					LogUtil.i("返回的地址--------" + province + city + area);
				}
			}
			area_tv.setText(province + " " + city + " " + area);
		}
	}
}
