package com.jishijiyu.takeadvantage.activity.exchangemall;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.widget.CustomShareBoard;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.RequestAddress;
import com.jishijiyu.takeadvantage.entity.result.Address;
import com.jishijiyu.takeadvantage.entity.result.ResultAddress;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;

/**
 * 
 * @author shifeiyu
 * @category 更多收货地址
 * 
 */
public class MoreAddressActivity extends HeadBaseActivity implements
		OnClickListener {
	/** 获取数据成功 */
	private static final int LOADING_DATA_SUCCESS = 2;
	/** 获取数据失败 */
	private static final int LOADING_DATA_FAILED = 3;
	private Gson gson;
	private String result;
	private Message message;
	RequestAddress requestAddress = new RequestAddress();
	ResultAddress resultAddress = new ResultAddress();
	List<Address> list = new ArrayList<Address>();
	private MyAdapter<Address> addressListviewAdapter = null;
	Address address;
	public String postalCode;
	public int userId;
	public int id;
	public String tokenId;
	public String province;
	public String city;
	public String area;
	public String detailedAddress;
	public String name;
	public String telephone;
	public String type;
	public Boolean isCheack;
	public String unit;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.more_address));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void initReplaceView() {
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this, R.layout.more_address, null);
		base_centent.addView(view);
		unit = getIntent().getStringExtra(HeadBaseActivity.intentKey);
		setdata();
		init();
		// requestData(requestAddress);
	}

	private void init() {
		ListView lv = (ListView) findViewById(R.id.add_address_lv);
		LinearLayout add = (LinearLayout) findViewById(R.id.more_address_add_ll);
		Button confirm = (Button) findViewById(R.id.more_address_confirm);
		addressListviewAdapter = new MyAdapter<Address>(this, list,
				R.layout.more_address_lv_item) {
			@Override
			public void convert(ViewHolder helper, int posittion, Address item) {
				// ImageView iv = (ImageView)
				// findViewById(R.id.more_address_lv_item_deflt_img);
				helper.setText(R.id.more_address_lv_item_name_tv, item.name);
				helper.setText(R.id.more_address_lv_item_phonenum_tv,
						item.telephone);
				helper.setText(R.id.more_address_lv_item_address,
						item.detailedAddress);

				if (item.type.equals("2")) {
					helper.setVisibility(R.id.more_address_lv_item_tv_defult,
							View.VISIBLE);
				} else {
					helper.setVisibility(R.id.more_address_lv_item_tv_defult,
							View.GONE);
				}
				if (item.isChecked) {
					helper.setImageViewResource(
							R.id.more_address_lv_item_deflt_img,
							R.drawable.shouhuo_btn_choose);
					helper.setImageResource(R.id.more_address_lv_item_ll,
							R.drawable.shouhuo_choose_dizhi);
				} else {
					helper.setImageViewResource(
							R.id.more_address_lv_item_deflt_img,
							R.drawable.shouhuo_btn_no_choose);
					helper.setImageResource(R.id.more_address_lv_item_ll,
							R.drawable.shouhuo_unchoose_dizhi);

				}
				notifyDataSetChanged();

			}

		};
		lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
		lv.setAdapter(addressListviewAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).isChecked = false;
					if (!list.get(position).isChecked) {
						list.get(position).isChecked = true;
						addressListviewAdapter.notifyDataSetChanged();
					}
				}
			}
		});
		add.setOnClickListener(this);
		confirm.setOnClickListener(this);

	}

	private void requestData(Object entry) {
		gson = new Gson();
		String request = gson.toJson(entry);
		HttpConnectTool.update(request, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {

				ResultAddress resultAddress = gson.fromJson(result,
						ResultAddress.class);
				if (resultAddress.p.isTrue) {
					int point = getIntent().getIntExtra("point", 0);
					for (int i = 0; i < resultAddress.p.addrList.size(); i++) {
						if (resultAddress.p.addrList.get(i).id == point) {

							resultAddress.p.addrList.get(i).isChecked = true;

						} else if (point == 0) {
							resultAddress.p.addrList.get(0).isChecked = true;
						}

						name = resultAddress.p.addrList.get(i).name;
						detailedAddress = resultAddress.p.addrList.get(i).detailedAddress;
						telephone = resultAddress.p.addrList.get(i).telephone;
						province = resultAddress.p.addrList.get(i).province;
						city = resultAddress.p.addrList.get(i).city;
						area = resultAddress.p.addrList.get(i).area;
						id = resultAddress.p.addrList.get(i).id;
						postalCode = resultAddress.p.addrList.get(i).postalCode;
						type = resultAddress.p.addrList.get(i).type;

						list.add(resultAddress.p.addrList.get(i));

					}
					init();

				} else {
					Intent intent = new Intent(MoreAddressActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
			}

			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void contectFailed(String path, String request) {
				Toast.makeText(mContext, "访问网络失败", Toast.LENGTH_SHORT).show();

			}
		});

	}

	private void setdata() {
		String id = GetUserIdUtil.getUserId(this);
		if (id == null) {
			return;
		} else {
			requestAddress.p.userId = id;
		}
		tokenId = GetUserIdUtil.getTokenId(this);
		requestAddress.p.tokenId = tokenId;
		// requestAddress.p.userId = "1";
		requestData(requestAddress);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_address_add_ll:
			Intent intent = new Intent(this, AddAddressActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.more_address_confirm:
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).isChecked) {
					address = list.get(i);
				}
			}
			// startForActivity(this, FirmOrderActivity.class, address);
			LogUtil.e("address++++++++" + address);
			if (address == null) {
				ToastUtils.makeText(mContext, "请添加收货地址", Toast.LENGTH_LONG)
						.show();
				break;
			}
			Intent intent1 = new Intent(this, FirmOrderActivity.class);
			intent1.putExtra("Address", address);
			if (unit != null && unit.equals("exchange")) {

				intent1.putExtra("type", unit);
			}
			// startActivity(intent1);
			setResult(RESULT_OK, intent1);
			finish();
			break;

		default:
			break;
		}

	}

}
