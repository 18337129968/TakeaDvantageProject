package com.jishijiyu.takeadvantage.activity.my;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.myfriend.ProManagerActivity;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.Reference;
import com.jishijiyu.takeadvantage.entity.request.InviteCodeAddressRequest;
import com.jishijiyu.takeadvantage.entity.result.InviteCodeAddressResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 我的任务好友分享
 * 
 * @author sunaoyang
 * 
 */
public class FriendRequestActivity extends HeadBaseActivity {
	private LinearLayout data, nodata, weixin1, weixin, QQ, Qzone, sina;
	private ListView referencelist;
	private List<Reference> list = new ArrayList<Reference>();
	private Reference reference;
	private View view;
	private MyAdapter<Reference> referenceAdapter = null;
	private int QR_WIDTH = 200, QR_HEIGHT = 200;
	private Gson gson = null;
	private String invitationUrl;
	private ImageView iv_dimension;
	private Bitmap bitmapCode;

	// ShareUtil share;
	// CustomShareBoard board;

	@Override
	public void onClick(View v) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		bundle.putString("url", invitationUrl);

		switch (v.getId()) {
		case R.id.friend_request_weixin1:
			// share.postShare();
			// board.performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			// // 金币动画
			// ShowGoldAnimActivity activity = new
			// ShowGoldAnimActivity(mContext);
			// activity.showPopWindows(this, view, "20", false);
			bundle.putString("share", "WEIXIN_CIRCLE");
			intent = new Intent(this, ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.friend_request_weixin:
			// board.performShare(SHARE_MEDIA.WEIXIN);
			bundle.putString("share", "WEIXIN");
			intent = new Intent(this, ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.friend_request_qq:
			// board.performShare(SHARE_MEDIA.QQ);
			bundle.putString("share", "QQ");
			intent = new Intent(this, ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.friend_request_qzone:
			// board.performShare(SHARE_MEDIA.QZONE);
			bundle.putString("share", "QZONE");
			intent = new Intent(this, ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.friend_request_sina:
			// board.performShare(SHARE_MEDIA.SINA);
			bundle.putString("share", "SINA");
			intent = new Intent(this, ProManagerActivity.class);
			intent.putExtra("bitmapCode", bitmapCode);
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	@Override
	public void appHead(View view) {
		top_text.setText(getResources().getString(R.string.friend_share));
		btn_right.setVisibility(View.INVISIBLE);
		btn_left.setVisibility(View.VISIBLE);
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void initReplaceView() {
		// share = new ShareUtil(this, mContext);
		// board = new CustomShareBoard(this);
		getdata();
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		view = View.inflate(this, R.layout.activity_friend_request, null);

		// 二维码
		iv_dimension = (ImageView) view.findViewById(R.id.iv_dimension);
		initdata();

		View headitem = View.inflate(this,
				R.layout.item_list_friend_request_activity, null);
		data = (LinearLayout) view.findViewById(R.id.friend_request_data);
		nodata = (LinearLayout) view.findViewById(R.id.friend_request_nodata);
		if (list.size() != 0) {
			data.setVisibility(View.VISIBLE);
			nodata.setVisibility(View.GONE);
		} else {
			nodata.setVisibility(View.VISIBLE);
			data.setVisibility(View.GONE);
		}
		base_centent.addView(view);

		init();
		ScrollView sv_friend = (ScrollView) findViewById(R.id.sv_friend);
		sv_friend.smoothScrollTo(0, 0);
		referencelist = (ListView) findViewById(R.id.friend_request_list);
		referenceAdapter = new MyAdapter<Reference>(mContext, list,
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
		weixin = (LinearLayout) findViewById(R.id.friend_request_weixin);
		weixin1 = (LinearLayout) findViewById(R.id.friend_request_weixin1);
		QQ = (LinearLayout) findViewById(R.id.friend_request_qq);
		Qzone = (LinearLayout) findViewById(R.id.friend_request_qzone);
		sina = (LinearLayout) findViewById(R.id.friend_request_sina);
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
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
		final WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		popupWindow.update();
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initdata() {
		gson = new Gson();
		InviteCodeAddressRequest inviteCodeAddressRequest = new InviteCodeAddressRequest();
		InviteCodeAddressRequest.Pramater pramater = inviteCodeAddressRequest.p;
		pramater.userId = GetUserIdUtil.getUserId(this);
		pramater.tokenId = GetUserIdUtil.getTokenId(this);
		String json = gson.toJson(inviteCodeAddressRequest);
		HttpConnectTool.update(json, this, new ConnectListener() {

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
							+ GetUserIdUtil.getUserId(mContext) + "&code="
							+ GetUserIdUtil.invitationCode(mContext);
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
					bitmapCode = centerSquareScaleBitmap(bitmap);
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
	 /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     * @param bitmap      原图
     * @param edgeLength  希望得到的正方形部分的边长
     * @return  缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap)
    {
     if(null == bitmap )
     {
      return  null;
     }
     int edgeLength;
     Bitmap result = bitmap;
     int widthOrg = bitmap.getWidth();
     int heightOrg = bitmap.getHeight();
     edgeLength=widthOrg/2;
     
     if(widthOrg > edgeLength && heightOrg > edgeLength)
     {
      //压缩到一个最小长度是edgeLength的bitmap
      int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
      int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
      int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
      Bitmap scaledBitmap;
      
            try{
             scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } 
            catch(Exception e){
             return null;
            }
            
         //从图中截取正中间的正方形部分。
         int xTopLeft = (scaledWidth - edgeLength) / 2;
         int yTopLeft = (scaledHeight - edgeLength) / 2;
         
         try{
          result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
          scaledBitmap.recycle();
         }
         catch(Exception e){
          return null;
         }         
     }
          
     return result;
    }
}
