package com.jishijiyu.takeadvantage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.view.ProfessionPicker;

/**
 * 城市选择器
 * 
 * @author zcl     industry profession
 * 
 */
public class ProfessionActivity extends Activity {
	private ProfessionPicker profession_picker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_profession);
		profession_picker = (ProfessionPicker) findViewById(R.id.professionpicker);
		Button cancel = (Button) findViewById(R.id.profession_cancel);
		Button ok = (Button) findViewById(R.id.profession_ok);

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
				String industry = profession_picker.getIndustry_string();
				String professionName = profession_picker.getProfession_string();
//				String areaName = profession_picker.getArea_string();
//				LogUtil.i("industry", profession_picker.getIndustryCode());
//				LogUtil.i("professionName", profession_picker.getProfessionCode());
//				LogUtil.i("areaName", profession_picker.getAreaCode());
				String all = industry + " " + professionName;
				Intent intent = new Intent();
				intent.putExtra("industryCode", profession_picker.getIndustryCode());
				intent.putExtra("professionCode", profession_picker.getProfessionCode());
//				intent.putExtra("areaCode", profession_picker.getAreaCode());
				intent.putExtra("industry", industry);
				intent.putExtra("profession", professionName);
//				intent.putExtra("area", areaName);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
