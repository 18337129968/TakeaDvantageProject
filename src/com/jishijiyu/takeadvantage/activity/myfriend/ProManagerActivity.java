package com.jishijiyu.takeadvantage.activity.myfriend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.ShareTool;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.ShareDirectUtil;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class ProManagerActivity extends HeadBaseActivity {
	@ViewInject(R.id.et_por)
	private EditText et_por;
	private ImageView im_por2;
	private ShareDirectUtil share;
	private ShareTool sharePop;
	private String shareNum = null;
	private String messages = null;
	private String publicity = null;
	private String url = null;
	private Bitmap bitmapCode;
	// spinner
	private Spinner spCity = null;
	private ArrayAdapter<String> adapterCity = null;
	private String[] cityInfo = { "屠龙宝刀点击就送", "大家快来玩啊" };

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		messages = publicity + "." + et_por.getText().toString().trim();
		share = new ShareDirectUtil(this, mContext, messages, url);
		sharePop = new ShareTool(this);
		switch (v.getId()) {
		case R.id.btn_top_left:
			finish();
			break;
		case R.id.btn_top_right:
			share.postShare();
			if (shareNum.equals("WEIXIN_CIRCLE")) {
				sharePop.performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			} else if (shareNum.equals("SINA")) {
				sharePop.performShare(SHARE_MEDIA.SINA);
			} else if (shareNum.equals("QZONE")) {
				sharePop.performShare(SHARE_MEDIA.QZONE);
			} else if (shareNum.equals("QQ")) {
				sharePop.performShare(SHARE_MEDIA.QQ);
			} else if (shareNum.equals("WEIXIN")) {
				sharePop.performShare(SHARE_MEDIA.WEIXIN);
			}
			// intent = new Intent();
			// intent.setClass(this, cls);
			// startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void appHead(View view) {
		// TODO Auto-generated method stub
		btn_left.setVisibility(View.VISIBLE);
		btn_right.setVisibility(View.VISIBLE);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		btn_left.setText("关闭");
		top_text.setText("推广管理");
		btn_right.setText("分享");

	}

	private class OnItemSelectedListenerImpl implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			publicity = parent.getItemAtPosition(position).toString();
			messages = publicity + "." + et_por.getText().toString().trim();
			et_por.setText(publicity);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}

	}

	@Override
	public void initReplaceView() {
		// TODO Auto-generated method stub
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(ProManagerActivity.this,
				R.layout.activity_pro_manager, null);

		base_centent.addView(view);
		AppManager.getAppManager().addActivity(this);

		// 初始化函数中代码如下
		this.spCity = (Spinner) super.findViewById(R.id.sp_speech);
		// 将数据cityInfo填充到适配器adapterCity中
		this.adapterCity = new ArrayAdapter<String>(this,
				R.layout.activity_item, cityInfo);
		// 设置下拉框的数据适配器adapterCity
		this.spCity.setAdapter(adapterCity);
		this.spCity.setOnItemSelectedListener(new OnItemSelectedListenerImpl());

		initdata();
	}

	private void initdata() {
		et_por = (EditText) findViewById(R.id.et_por);
		im_por2 = (ImageView) findViewById(R.id.im_por2);

		Intent intent = getIntent();
		if (intent != null) {
			bitmapCode = intent.getParcelableExtra("bitmapCode");
			im_por2.setImageBitmap(bitmapCode);
		}
		Bundle bundle = getIntent().getExtras();
		shareNum = bundle.getString("share");
		url = bundle.getString("url");
		// Bundle bundle = getIntent().getExtras();
		// String share = bundle.getString("share");
		// LogUtil.i("share", share);
	}

}
