package com.jishijiyu.takeadvantage.activity.my;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.entity.request.FeedBack;
import com.jishijiyu.takeadvantage.entity.request.FeedBack.Paramet;
import com.jishijiyu.takeadvantage.entity.result.FeedBackResult;
import com.jishijiyu.takeadvantage.utils.AppManager;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.DialogUtils;
import com.jishijiyu.takeadvantage.utils.EmojiUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 意见反馈
 * 
 * @author baohan
 * 
 */
public class FeedBackActivity extends HeadBaseActivity {
	private EditText editComments;
	private TextView rechargeBtn;
	int num = 400;
	Dialog dialogs;
	String userId,tokenId;

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.feedback));
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
		View view = View.inflate(FeedBackActivity.this,
				R.layout.activity_feedback, null);
		base_centent.addView(view);
		editComments = (EditText) findViewById(R.id.edit_comments);
		rechargeBtn = (TextView) findViewById(R.id.recharge_btn);
		userId = GetUserIdUtil.getUserId(FeedBackActivity.this);
		tokenId = GetUserIdUtil.getTokenId(this);
		rechargeBtn.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_btn:
			String text = editComments.getText().toString().trim();
			String content =EmojiUtil.filterEmoji(text);
			if (TextUtils.isEmpty(content)) {
				ToastUtils.makeText(FeedBackActivity.this, "输入内容不正确", 0).show();
			} else {

				FeedBack feedBack = new FeedBack();
				feedBack.setC(Constant.FEEDBACK);
				Paramet paramet = new Paramet();
				paramet.setContents(content);
				paramet.setTokenId(tokenId);
				paramet.setUserId(userId);
				feedBack.setP(paramet);

				final Gson gson = new Gson();
				String json = gson.toJson(feedBack);
				HttpConnectTool.update(json, this, new ConnectListener() {

					@Override
					public void contectSuccess(String result) {
						if (!TextUtils.isEmpty(result)) {
							FeedBackResult feedBackResult = gson.fromJson(
									result, FeedBackResult.class);
							if (feedBackResult.getP().isSucce) {

								dialogs = DialogUtils.showDialog(
										FeedBackActivity.this, "提交成功",
										new int[] { R.string.end },
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												dialogs.cancel();
												AppManager
														.getAppManager()
														.finishActivity(
																FeedBackActivity.this);
											}
										}, false);
							} else {
								ToastUtils.makeText(FeedBackActivity.this,
										"抱歉提交失败!", 2000);
							}
						}
					}

					@Override
					public void contectStarted() {

					}

					@Override
					public void contectFailed(String path, String request) {

					}
				});

			}

			break;

		default:
			break;
		}

	}
}
