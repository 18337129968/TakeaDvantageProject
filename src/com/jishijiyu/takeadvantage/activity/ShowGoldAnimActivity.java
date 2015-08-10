package com.jishijiyu.takeadvantage.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.utils.VoiceUtil;
import com.jishijiyu.takeadvantage.view.FlakeView;

/**
 * @category 金币掉落动画
 * @author zcl
 * 
 */
public class ShowGoldAnimActivity extends Activity {
	private PopupWindow pop;
	// 金币掉落动画的主体动画
	private FlakeView flakeView;
	private Button btnAll, btnthree;
	TextView textView1;
	AinmationEndListener listener;

	public ShowGoldAnimActivity(Context context) {
		flakeView = new FlakeView(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		flakeView = new FlakeView(this);
		textView1 = (TextView) findViewById(R.id.tv_1);
		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		flakeView.resume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		flakeView.pause();
	}

	@SuppressLint("NewApi")
	public PopupWindow showPopWindows(Activity context, View v,
			String moneyStr, boolean show) {
		View view = View.inflate(context, R.layout.pop_gold, null);
		final TextView addGolds = (TextView) view
				.findViewById(R.id.pop_gold_tv);
		addGolds.setText("+" + moneyStr + "拍币");
		addGolds.setVisibility(View.GONE);
		// TextView money = (TextView) view.findViewById(R.id.tv_money);
		// tvTips.setText("连续登陆3天，送您" + moneyStr + "个爱心币");
		// money.setText(moneyStr);
		final LinearLayout container = (LinearLayout) view
				.findViewById(R.id.pop_container);
		// 将flakeView 添加到布局中
		container.addView(flakeView);
		// 设置背景
		context.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.BLACK));
		// 设置同时出现在屏幕上的金币数量 建议64以内 过多会引起卡顿
		flakeView.addFlakes(8);
		/**
		 * 绘制的类型
		 * 
		 * @see View.LAYER_TYPE_HARDWARE
		 * @see View.LAYER_TYPE_SOFTWARE
		 * @see View.LAYER_TYPE_NONE
		 */
		flakeView.setLayerType(View.LAYER_TYPE_NONE, null);
		// view.findViewById(R.id.btn_ikow).setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (container!=null){
		// container.removeAllViews();
		// }
		// pop.dismiss();
		// }
		// });
		pop = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
				R.color.half_color));
		pop.setBackgroundDrawable(dw);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.showAtLocation(v, Gravity.CENTER, 0, 0);

		/**
		 * 移除动画
		 */
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 设置2秒后
					Thread.sleep(1500);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							container.removeAllViews();
							// pop.dismiss();
							addGolds.setVisibility(View.VISIBLE);
							AlphaAnimation alphaAnimation = new AlphaAnimation(
									0, 1);
							alphaAnimation.setDuration(1000);
							alphaAnimation.setRepeatCount(1);
							alphaAnimation.setFillAfter(true);
							alphaAnimation.setRepeatMode(Animation.REVERSE);
							alphaAnimation
									.setAnimationListener(new AnimationListener() {

										@Override
										public void onAnimationStart(
												Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationRepeat(
												Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationEnd(
												Animation animation) {
											pop.dismiss();
											if (listener != null) {
												listener.end();
											}
										}
									});
							addGolds.startAnimation(alphaAnimation);

						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		if (!show)
			thread.start();
		MediaPlayer player = MediaPlayer.create(context, R.raw.shake);
		boolean isvoice = VoiceUtil.isVoice(context);
		if (true == isvoice) {
			player.start();
		}
		return pop;
	}

	public void setAnimListener(AinmationEndListener listener) {
		this.listener = listener;

	}

	public interface AinmationEndListener {
		public void end();
	}

}
