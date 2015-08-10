package com.jishijiyu.takeadvantage.activity;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.view.CityPicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

/**
 * 城市选择器
 * 
 * @author zcl
 * 
 */
public class LocationActivity extends Activity {
	private CityPicker city_picker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_location);
		city_picker = (CityPicker) findViewById(R.id.citypicker);
		Button cancel = (Button) findViewById(R.id.city_cancel);
		Button ok = (Button) findViewById(R.id.city_ok);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// PersonalMoreActivity per = new PersonalMoreActivity();
				String province = city_picker.getProvince_string();
				String cityName = city_picker.getCity_string();
				String areaName = city_picker.getArea_string();
				LogUtil.i("province", city_picker.getProvinceCode());
				LogUtil.i("cityName", city_picker.getCityCode());
				LogUtil.i("areaName", city_picker.getAreaCode());
				String all = province + " " + cityName + " " + areaName;
				Intent intent = new Intent();
				intent.putExtra("provinceCode", city_picker.getProvinceCode());
				intent.putExtra("cityCode", city_picker.getCityCode());
				intent.putExtra("areaCode", city_picker.getAreaCode());
				intent.putExtra("province", province);
				intent.putExtra("city", cityName);
				intent.putExtra("area", areaName);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
