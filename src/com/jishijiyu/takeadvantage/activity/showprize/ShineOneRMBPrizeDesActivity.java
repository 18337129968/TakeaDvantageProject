package com.jishijiyu.takeadvantage.activity.showprize;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.OnePrizeDetailsRequest;
import com.jishijiyu.takeadvantage.entity.result.OnePrizeDetailsResult;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ShineOneRMBPrizeDesActivity extends Activity {
	private Context mContext;
	@ViewInject(R.id.img_btn_top_left)
	private Button img_btn_top_left;
	@ViewInject(R.id.top_text)
	private TextView top_text;
	@ViewInject(R.id.iv_show_prize_des_img)
	private ImageView iv_show_prize_des_img;
	@ViewInject(R.id.tv_show_prize_des_level)
	private TextView tv_show_prize_des_level;
	@ViewInject(R.id.tv_show_prize_des_name)
	private TextView tv_show_prize_des_name;
	@ViewInject(R.id.tv_show_prize_des_number)
	private TextView tv_show_prize_des_number;
	@ViewInject(R.id.tv_show_prize_des_speak)
	private TextView tv_show_prize_des_speak;
	private String imgUrl;
	private String nikName;
	private String mobile;
	private String awardContent;
	private String awardName;
	private String periods;
	private String mealType;
	private String roomType;
	private int awardGrade;;

	OnePrizeDetailsRequest opdq = new OnePrizeDetailsRequest();
	OnePrizeDetailsResult opds = new OnePrizeDetailsResult();
	private Gson gson = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_prize_des);
		mContext = ShineOneRMBPrizeDesActivity.this;
		ViewUtils.inject(this);
		initHead();
		initData();
	}

	private void initHead() {
		top_text.setText("我要晒奖");
		img_btn_top_left.setVisibility(View.VISIBLE);
		img_btn_top_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initView() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < mobile.length(); i++) {
			if (i > 2 && i < 7) {
				builder.append("*");
			} else {
				builder.append(mobile.charAt(i));
			}
		}
		String award = "";
		if (awardGrade == 1) {
			award = "一等奖";
		} else if (awardGrade == 2) {
			award = "二等奖";
		} else if (awardGrade == 3) {
			award = "三等奖";
		} else if (awardGrade == 4) {
			award = "四等奖";
		} else if (awardGrade == 5) {
			award = "五等奖";
		}
		if (imgUrl.equals("prizePicture.jpg")) {
			BitmapFactory factory = new BitmapFactory();
			Bitmap bitmap = factory.decodeFile(getFilesDir()
					+ "prizePicture.jpg");
			iv_show_prize_des_img.setImageBitmap(bitmap);

			tv_show_prize_des_level.setText(award);
			tv_show_prize_des_name.setText(awardName);
			tv_show_prize_des_number.setText(builder.toString());
			tv_show_prize_des_speak.setText(awardContent);
		} else {
			ImageLoaderUtil.loadImage(imgUrl, iv_show_prize_des_img);
			tv_show_prize_des_level.setText(award);
			tv_show_prize_des_name.setText(awardName);
			tv_show_prize_des_number.setText(builder.toString());
			tv_show_prize_des_speak.setText(awardContent);
		}
	}

	private void initData() {
		gson = new Gson();
		opdq = new OnePrizeDetailsRequest();
		opdq.p.userId = GetUserIdUtil.getUserId(this);
		opdq.p.tokenId = GetUserIdUtil.getTokenId(this);
		Bundle bundle = getIntent().getExtras();
		opdq.p.id = bundle.getString("id");

		String request = gson.toJson(opdq);
		HttpConnectTool.update(request, false, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					opds = gson.fromJson(result, OnePrizeDetailsResult.class);
					if (opds.p.isTrue) {
						imgUrl = opds.p.showAward.imgUrl;
						nikName = opds.p.showAward.nikName;
						mobile = opds.p.showAward.mobile;
						awardContent = opds.p.showAward.awardContent;
						awardName = opds.p.showAward.awardName;
						periods = opds.p.showAward.periods;
						awardGrade = opds.p.showAward.awardGrade;
						mealType = opds.p.showAward.mealType;
						roomType = opds.p.showAward.roomType;
						initView();
					} else {
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivityForResult(intent,
								Constant.AGAIN_LOGIN_CODE);
					}
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				LogUtil.i(path + "---" + request);
			}
		});
	};
}
