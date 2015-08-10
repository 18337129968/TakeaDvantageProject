package com.jishijiyu.takeadvantage.activity.showprize;

import android.R.integer;
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
import com.jishijiyu.takeadvantage.activity.ernie.LocksActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.showprize.ShinePrizeActivity.MyPLAdapter;
import com.jishijiyu.takeadvantage.entity.request.IntegerPrizeDetailsRequest;
import com.jishijiyu.takeadvantage.entity.result.ApplyMerchantResult;
import com.jishijiyu.takeadvantage.entity.result.IntegerPrizeDetailsResult;
import com.jishijiyu.takeadvantage.entity.result.InvitationLocksResult;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.ResultPrizeList;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ShinePrizeDesActivity extends Activity {
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
	private int awardGrade;;
	IntegerPrizeDetailsRequest ipdq = new IntegerPrizeDetailsRequest();
	IntegerPrizeDetailsResult ipds = new IntegerPrizeDetailsResult();
	private Gson gson = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_prize_des);
		mContext = ShinePrizeDesActivity.this;
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
		ipdq = new IntegerPrizeDetailsRequest();
		ipdq.p.userId = GetUserIdUtil.getUserId(this);
		ipdq.p.tokenId = GetUserIdUtil.getTokenId(this);
		Bundle bundle = getIntent().getExtras();
		ipdq.p.id = bundle.getString("id");

		String request = gson.toJson(ipdq);
		HttpConnectTool.update(request, false, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				ipds = gson.fromJson(result, IntegerPrizeDetailsResult.class);
				if (ipds.p.isTrue) {
					imgUrl = ipds.p.showAward.imgUrl;
					nikName = ipds.p.showAward.nikName;
					mobile = ipds.p.showAward.mobile;
					awardContent = ipds.p.showAward.awardContent;
					awardName = ipds.p.showAward.awardName;
					periods = ipds.p.showAward.periods;
					awardGrade = ipds.p.showAward.awardGrade;
					initView();
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
