package kankan.wheel.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;

/**
 * 类描述： 自定义对话框 类名称：MyDialog
 */
public class MyProgressDialog extends Dialog {

	private Animation mAnimation;
	private Animation mReverseAnimation;

	public MyProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initDialog(context);
	}

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
		initDialog(context);
	}

	public MyProgressDialog(Context context) {
		super(context, R.style.MyProgressDialogStyle);
		initDialog(context);
	}

	private void initDialog(Context context) {
		setContentView(R.layout.dialog_progress);
		setCanceledOnTouchOutside(false);
		Window window = getWindow();
		window.setWindowAnimations(R.style.pop_menu_animation);
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(params);
	}

	@Override
	public void show() {
		if (mAnimation == null) {
			mAnimation = createForeverRotationAnimation();
			mAnimation.setDuration(3000);
		}
		findViewById(R.id.progress_layout_imageView).startAnimation(mAnimation);

		if (mReverseAnimation == null) {
			mReverseAnimation = createForeverReverseRotationAnimation();
			mReverseAnimation.setDuration(3000);
		}
		findViewById(R.id.progress_reverse_layout_imageView).startAnimation(
				mReverseAnimation);
		
	}

	public void setMessage(CharSequence message) {
		((TextView) findViewById(R.id.tv_message)).setText(message);
	}

	/**
	 * 创建一个不停旋转的动画
	 * 
	 * @return 动画实例
	 */
	public static Animation createForeverReverseRotationAnimation() {
		Animation mRotateAnimation = new RotateAnimation(720, 0,
				Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
		mRotateAnimation.setInterpolator(new LinearInterpolator());
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
		mRotateAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
		return mRotateAnimation;
	}

	/**
	 * 创建一个不停旋转的动画
	 * 
	 * @return 动画实例
	 */
	public static Animation createForeverRotationAnimation() {
		Animation mRotateAnimation = new RotateAnimation(0, 1080,
				Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
		mRotateAnimation.setInterpolator(new LinearInterpolator());
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
		mRotateAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
		return mRotateAnimation;
	}
}
