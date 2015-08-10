package com.jishijiyu.takeadvantage.activity.exchangemall;

import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.jishijiyu.takeadvantage.entity.request.AddAddressRequest;
import com.jishijiyu.takeadvantage.entity.result.AddAddressResult;
import com.jishijiyu.takeadvantage.entity.result.Address;
import com.jishijiyu.takeadvantage.utils.EdittextUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 添加收货地址
 * 
 * @author zcl
 * 
 */
public class AddAddressActivity extends HeadBaseActivity implements
		OnClickListener {
	static final private int GET_CODE = 0;
	/** 获取数据成功 */
	private static final int LOADING_DATA_SUCCESS = 1;
	/** 获取数据失败 */
	private static final int LOADING_DATA_FAILED = 2;
	private static final int PHONE_NUMBER_ERROR = 3;
	private Gson gson;
	private String result;
	private Message message;
	EditText name, phoneNum, zipCode, detail;
	TextView area_tv;
	Button save, defult;
	ImageView isDefult;
	LinearLayout isDefultLL;
	boolean isdefult = false;
	AddAddressRequest addAddressRequest = new AddAddressRequest();
	AddAddressResult addAddressResult = new AddAddressResult();
	private Boolean issuccess;
	String request, type;
	String province = null, city = null, area = null;
	private Message msg;
	private String phone;

	private void requestData(Object entry) {
		gson = new Gson();
		request = gson.toJson(entry);
		LogUtil.i(entry + "request" + request);
		// result = HttpConnectUtil.updata(request, false, null, null);
		HttpConnectTool.update(request, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				btn_right.setClickable(true);
				if (!TextUtils.isEmpty(result)) {

					addAddressResult = gson.fromJson(result,
							AddAddressResult.class);
					if (addAddressResult.p.isTrue) {

						issuccess = addAddressResult.p.isSucce;
						if (issuccess) {
							// Toast.makeText(mContext, "添加成功",
							// Toast.LENGTH_SHORT)
							// .show();
							ToastUtils.makeText(mContext, "添加成功",
									Toast.LENGTH_SHORT).show();
							if (type != null && !type.equals("")
									&& type.equals("isdefult")) {
								startForActivity(mContext,
										MoreAddressCanRemoveActivity.class,
										null);
							} else {
								startForActivity(mContext,
										MoreAddressActivity.class, null);
							}

							finish();
						} else {
							Toast.makeText(mContext, "收货地址条数已经超出限制，请删除后再做添加",
									Toast.LENGTH_SHORT).show();
						}

						init();
					} else {
						Intent intent = new Intent(AddAddressActivity.this,
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
				btn_right.setClickable(true);
				Toast.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();
			}
		});

		// message = Message.obtain();
		// if (!TextUtils.isEmpty(result)) {
		// message.what = LOADING_DATA_SUCCESS;
		// } else {
		// message.what = LOADING_DATA_FAILED;
		// }
		// handler.sendMessage(message);
	}

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.add_address));
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
		View view = View.inflate(this, R.layout.activity_add_address, null);
		base_centent.addView(view);
		type = getIntent().getStringExtra(HeadBaseActivity.intentKey);
		init();

	}

	private void init() {
		LinearLayout city = (LinearLayout) findViewById(R.id.add_address_ll);
		city.setOnClickListener(this);
		name = (EditText) findViewById(R.id.add_address_name_et);
		phoneNum = (EditText) findViewById(R.id.add_address_phonenum_et);
		phoneNum.addTextChangedListener(new TextWatcher() {
			String tmp = "";
			String digits = "/\\:*?<>|\"\n\t-+";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				phoneNum.setSelection(s.length());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				tmp = s.toString();

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.equals(tmp)) {
					return;
				}
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < str.length(); i++) {
					if (digits.indexOf(str.charAt(i)) < 0) {
						sb.append(str.charAt(i));
					}
				}
				tmp = sb.toString();
				phoneNum.setText(tmp);
			}

		});
		zipCode = (EditText) findViewById(R.id.add_address_zipcode_et);
		
		detail = (EditText) findViewById(R.id.add_address_detailaddress_et);
		
		area_tv = (TextView) findViewById(R.id.add_address_tv);
		save = (Button) findViewById(R.id.add_address_save_btn);
		defult = (Button) findViewById(R.id.add_address_set_defult_btn);
		isDefult = (ImageView) findViewById(R.id.add_address_isdefult);
		// isDefult.setOnClickListener(this);
		isDefultLL = (LinearLayout) findViewById(R.id.add_address_isdefult_ll);
		isDefultLL.setOnClickListener(this);
		save.setOnClickListener(this);
		defult.setOnClickListener(this);
		
		EdittextUtil.editMaxLength(detail, 100);
		Address data = (Address) getIntent().getSerializableExtra("address");
		LogUtil.i("-------" + type);
		// if (type != null && !type.equals("") && type.equals("isdefult")) {
		// defult.setVisibility(View.VISIBLE);
		// } else {
		// save.setVisibility(View.VISIBLE);
		// btn_right.setVisibility(View.GONE);
		// }
		if (data != null) {
			name.setText(data.name);
			phoneNum.setText(data.telephone);
			zipCode.setText(data.postalCode);
			detail.setText(data.detailedAddress);
			area_tv.setText(data.province + " " + data.city + " " + data.area);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_address_ll:
			Intent intent = new Intent(this, LocationActivity.class);
			startActivityForResult(intent, GET_CODE);
			break;
		case R.id.add_address_isdefult:

			if (!isdefult) {
				isDefult.setBackgroundResource(R.drawable.shouhuo_btn_choose);
				isdefult = true;
			} else {
				isDefult.setBackgroundResource(R.drawable.shouhuo_btn_no_choose);
				isdefult = false;
			}

			break;
		case R.id.add_address_isdefult_ll:

			if (!isdefult) {
				isDefult.setBackgroundResource(R.drawable.shouhuo_btn_choose);
				isdefult = true;
			} else {
				isDefult.setBackgroundResource(R.drawable.shouhuo_btn_no_choose);
				isdefult = false;
			}

			break;
		case R.id.btn_top_right:
			btn_right.setClickable(false);
			if (isNoData()) {
				btn_right.setClickable(true);
				return;
			}
			getData();
			requestData(addAddressRequest);
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
		String id = GetUserIdUtil.getUserId(this);
		if (id == null) {
			return;
		} else {
			addAddressRequest.p.userId = id;
		}
		String tonkenId = GetUserIdUtil.getTokenId(this);
		addAddressRequest.p.tokenId = tonkenId;
		addAddressRequest.p.name = name.getText().toString();
		addAddressRequest.p.telephone = phoneNum.getText().toString();
		addAddressRequest.p.detailedAddress = detail.getText().toString();
		addAddressRequest.p.postalCode = zipCode.getText().toString();
		addAddressRequest.p.area = area;
		addAddressRequest.p.city = city;
		addAddressRequest.p.province = province;
		if (isdefult) {
			addAddressRequest.p.type = "2";
		} else {
			addAddressRequest.p.type = "1";
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		final TextView area_tv = (TextView) findViewById(R.id.add_address_tv);

		if (requestCode == GET_CODE) {

			if (resultCode == RESULT_OK) {

				province = data.getStringExtra("province");
				city = data.getStringExtra("city");
				area = data.getStringExtra("area");

			}
			area_tv.setText(province + " " + city + " " + area);
		}
	}
}
