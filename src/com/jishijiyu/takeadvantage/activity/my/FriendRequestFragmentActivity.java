package com.jishijiyu.takeadvantage.activity.my;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.myfriend.ProManagerActivity;
import com.jishijiyu.takeadvantage.activity.widget.CustomShareBoard;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.Reference;
import com.jishijiyu.takeadvantage.entity.request.InviteCodeAddressRequest;
import com.jishijiyu.takeadvantage.entity.result.InviteCodeAddressResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ShareUtil;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 好友分享
 * 
 * @author sunaoyang
 * 
 */
@SuppressLint("NewApi")
public class FriendRequestFragmentActivity extends Fragment implements
		OnClickListener {
	private LinearLayout data, nodata, weixin1, weixin, QQ, Qzone, sina;
	private ListView referencelist;
	private List<Reference> list = new ArrayList<Reference>();
	private Reference reference;
	private View view;
	private ShareUtil share;
	private MyAdapter<Reference> referenceAdapter = null;
	private CustomShareBoard board;

	private int QR_WIDTH = 200, QR_HEIGHT = 200;
	private Gson gson = null;
	private String invitationUrl;
	private ImageView iv_dimension;
	private Bitmap bitmapCode;

	@Override
	public void onClick(View v) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		bundle.putString("url", invitationUrl);
		switch (v.getId()) {
		case R.id.friend_request_weixin1:
			// share.postShare();
			// board.performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			bundle.putString("share", "WEIXIN_CIRCLE");
			intent = new Intent(getActivity(), ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			// ShowGoldAnimActivity activity = new ShowGoldAnimActivity(
			// getActivity());
			// activity.showPopWindows(getActivity(), view, "20", false);
			break;
		case R.id.friend_request_weixin:
			bundle.putString("share", "WEIXIN");
			intent = new Intent(getActivity(), ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			// board.performShare(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.friend_request_qq:
			bundle.putString("share", "WEIXIN");
			intent = new Intent(getActivity(), ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			// board.performShare(SHARE_MEDIA.QQ);
			break;
		case R.id.friend_request_qzone:
			bundle.putString("share", "QZONE");
			intent = new Intent(getActivity(), ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			// board.performShare(SHARE_MEDIA.QZONE);
			break;
		case R.id.friend_request_sina:
			bundle.putString("share", "SINA");
			intent = new Intent(getActivity(), ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			// board.performShare(SHARE_MEDIA.SINA);
			break;

		default:
			break;
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_friend_request, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initReplaceView();
	}

	// @Override
	// public void appHead(View view) {
	// top_text.setText(getResources().getString(R.string.friend_share));
	// btn_right.setVisibility(View.INVISIBLE);
	// btn_left.setVisibility(View.VISIBLE);
	// btn_left.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// finish();
	// }
	// });
	//
	// }

	public void initReplaceView() {
		// share = new ShareUtil(getActivity(), getActivity());
		// board = new CustomShareBoard(getActivity());
		getdata();
		FrameLayout base_centent = (FrameLayout) getActivity().findViewById(
				R.id.base_content);
		// view = View.inflate(getActivity(), R.layout.activity_friend_request,
		// null);

		// 二维码
		iv_dimension = (ImageView) getActivity()
				.findViewById(R.id.iv_dimension);
		initdata();

		View headitem = View.inflate(getActivity(),
				R.layout.item_list_friend_request_activity, null);
		data = (LinearLayout) getActivity().findViewById(
				R.id.friend_request_data);
		nodata = (LinearLayout) getActivity().findViewById(
				R.id.friend_request_nodata);
		if (list.size() != 0) {
			data.setVisibility(View.VISIBLE);
			nodata.setVisibility(View.GONE);
		} else {
			nodata.setVisibility(View.VISIBLE);
			data.setVisibility(View.GONE);
		}
		// base_centent.addView(view);

		init();
		ScrollView sv_friend = (ScrollView) getActivity().findViewById(
				R.id.sv_friend);
		sv_friend.smoothScrollTo(0, 0);
		referencelist = (ListView) getActivity().findViewById(
				R.id.friend_request_list);
		referenceAdapter = new MyAdapter<Reference>(getActivity(), list,
				R.layout.item_list_friend_request_activity) {

			@Override
			public void convert(ViewHolder helper, int posittion, Reference item) {
				helper.setText(R.id.friend_request_directReference,
						item.getDirectReference());
				helper.setText(R.id.friend_request_inderectReference,
						item.getInderectReference());
				helper.setText(R.id.friend_request_points, item.getPoints()
						+ "分");
				notifyDataSetChanged();
			}
		};
		referencelist.addHeaderView(headitem);
		referencelist.setAdapter(referenceAdapter);
	}

	private void getdata() {
		reference = new Reference();
		reference.setDirectReference("无间道");
		reference.setInderectReference("10人/50");
		reference.setPoints(60);
		list.add(reference);
		reference = new Reference();
		reference.setDirectReference("天涯");
		reference.setInderectReference("5人/20");
		reference.setPoints(35);
		list.add(reference);

	}

	private void init() {
		weixin = (LinearLayout) getActivity().findViewById(
				R.id.friend_request_weixin);
		weixin1 = (LinearLayout) getActivity().findViewById(
				R.id.friend_request_weixin1);
		QQ = (LinearLayout) getActivity().findViewById(R.id.friend_request_qq);
		Qzone = (LinearLayout) getActivity().findViewById(
				R.id.friend_request_qzone);
		sina = (LinearLayout) getActivity().findViewById(
				R.id.friend_request_sina);
		weixin1.setOnClickListener(this);
		weixin.setOnClickListener(this);
		QQ.setOnClickListener(this);
		Qzone.setOnClickListener(this);
		sina.setOnClickListener(this);
	}

	/**
	 * 弹出分享对话框
	 * 
	 * @param 当前显示的view
	 * @author 张翠玲
	 */
	@SuppressLint("InflateParams")
	public void showSharePop(View view) {
		LayoutInflater mLayoutInflater = (LayoutInflater) getActivity()
				.getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		View menuView = (View) mLayoutInflater.inflate(R.layout.pop_share,
				null, true);// 弹出窗口包含的视图
		ImageView close = (ImageView) menuView
				.findViewById(R.id.pop_share_close);
		@SuppressWarnings("deprecation")
		final PopupWindow popupWindow = new PopupWindow(menuView,
				LayoutParams.FILL_PARENT, 200, false);// 创建弹出窗口，指定大小
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);// 设置窗口显示的动画效果
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);// 设置窗口显示的位置
		final WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = 0.3f;
		getActivity().getWindow().setAttributes(lp);
		popupWindow.update();
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				lp.alpha = 1f;
				getActivity().getWindow().setAttributes(lp);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig()
				.getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	public void initdata() {
		gson = new Gson();
		InviteCodeAddressRequest inviteCodeAddressRequest = new InviteCodeAddressRequest();
		InviteCodeAddressRequest.Pramater pramater = inviteCodeAddressRequest.p;
		pramater.userId = GetUserIdUtil.getUserId(getActivity());
		pramater.tokenId = GetUserIdUtil.getTokenId(getActivity());
		String json = gson.toJson(inviteCodeAddressRequest);
		HttpConnectTool.update(json, getActivity(), new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				InviteCodeAddressResult inviteCodeAddressResult = gson
						.fromJson(result, InviteCodeAddressResult.class);
				invitationUrl = inviteCodeAddressResult.p.invitationUrl;
				try {
					// 判断URL合法性
					if (invitationUrl == null || "".equals(invitationUrl)
							|| invitationUrl.length() < 1) {
						return;
					}
					invitationUrl = invitationUrl + "?userId="
							+ GetUserIdUtil.getUserId(getActivity()) + "&code="
							+ GetUserIdUtil.invitationCode(getActivity());
					Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
					hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
					// 图像数据转换，使用了矩阵转换
					BitMatrix bitMatrix = new QRCodeWriter().encode(
							invitationUrl, BarcodeFormat.QR_CODE, QR_WIDTH,
							QR_HEIGHT, hints);
					int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
					// 下面这里按照二维码的算法，逐个生成二维码的图片，
					// 两个for循环是图片横列扫描的结果
					for (int y = 0; y < QR_HEIGHT; y++) {
						for (int x = 0; x < QR_WIDTH; x++) {
							if (bitMatrix.get(x, y)) {
								pixels[y * QR_WIDTH + x] = 0xff000000;
							} else {
								pixels[y * QR_WIDTH + x] = 0xffffffff;
							}
						}
					}
					// 生成二维码图片的格式，使用ARGB_8888
					Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
							Bitmap.Config.ARGB_8888);
					bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH,
							QR_HEIGHT);
					bitmapCode = bitmap;
					// 显示到一个ImageView上面
					iv_dimension.setImageBitmap(toRoundBitmap(bitmap));
				} catch (WriterException e) {
					e.printStackTrace();
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

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		// 圆形图片宽高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 正方形的边长
		int r = 0;
		// 取最短边做边长
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// 构建一个bitmap
		Bitmap backgroundBmp = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		// new一个Canvas，在backgroundBmp上画图
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// 设置边缘光滑，去掉锯齿
		paint.setAntiAlias(true);
		// 宽高相等，即正方形
		RectF rect = new RectF(0, 0, r, r);
		// 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
		// 且都等于r/2时，画出来的圆角矩形就是圆形
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas将bitmap画在backgroundBmp上
		canvas.drawBitmap(bitmap, null, rect, paint);
		// 返回已经绘画好的backgroundBmp
		return backgroundBmp;
	}
}
