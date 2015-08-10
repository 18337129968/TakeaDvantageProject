package com.jishijiyu.takeadvantage.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;

public class InputPopup implements OnClickListener {
	Context context;
	View vPopupWindow;
	TextView tv_cancel, tv_ensure;
	EditText et_edittext;
	private PopupWindow editDishPop;
	View view;
	PopupWindow popUp;
	Dialog dialog;

	public InputPopup(Context context, View view) {
		this.context = context;
		this.view = view;
		addData();
	}

	public void addData() {
		if (editDishPop != null) {
			editDishPop.showAtLocation(view, Gravity.CENTER, 0, 0);
			return;
		} else {
			editDishPop = initPopWindow();
		}
		tv_cancel = (TextView) vPopupWindow.findViewById(R.id.tv_cancel);
		tv_ensure = (TextView) vPopupWindow.findViewById(R.id.tv_ensure);
		et_edittext = (EditText) vPopupWindow.findViewById(R.id.et_edittext);
		tv_cancel.setOnClickListener(this);
		tv_ensure.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 取消
		case R.id.tv_cancel:
			popUp.dismiss();
			break;
		// 确定
		case R.id.tv_ensure:
			sss(et_edittext.getText().toString().trim());
			popUp.dismiss();
			break;

		default:
			break;
		}

	}

	public void sss(String s) {
		dialog = DialogUtils.showDialog(context, "拍币", s, new int[] {
				R.string.ext, R.string.end },
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.left:
							dialog.cancel();
							break;
						case R.id.right:

							break;
						default:
							break;
						}
					}
				});
	}

	public PopupWindow initPopWindow() {

		vPopupWindow = LayoutInflater.from(context).inflate(
				R.layout.inputdialog_pop, null, false); // 加载popupWindow的布局文件
		popUp = new PopupWindow(vPopupWindow, 450, 550, true);// 声明一个弹出框
		popUp.setAnimationStyle(R.style.pop_menu_animation);
		popUp.setFocusable(true); // 默认为false,如果不设置为true，PopupWindow里面是获取不到焦点的，那么如果PopupWindow里面有输入框等的话就无法输入。
		popUp.setOutsideTouchable(true);
		popUp.setClippingEnabled(true);
		popUp.setBackgroundDrawable(new ColorDrawable());
		// LinearLayout popupwindow = (LinearLayout) vPopupWindow
		// .findViewById(R.id.popupwindow);
		// popupwindow.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View arg0, MotionEvent arg1) {
		// popUp.dismiss();
		// return false;
		// }
		// });
		popUp.showAtLocation(view, Gravity.CENTER, 0, 0);
		return popUp;

	}

}
