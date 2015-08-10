package com.jishijiyu.takeadvantage.activity.myinformation;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.activity.widget.CustomShareBoard;
import com.jishijiyu.takeadvantage.entity.request.WinningRecordGoodsRequest;
import com.jishijiyu.takeadvantage.entity.result.WinningRecordGoodsResult;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 
 * @author 张翠玲 中奖纪录商品详情分享页
 * 
 */
public class WinningRecordGoodsDetailActivty extends HeadBaseActivity {
	/** 获取数据成功 */
	private static final int LOADING_DATA_SUCCESS = 2;
	/** 获取数据失败 */
	private static final int LOADING_DATA_FAILED = 3;
	private Gson gson;
	private String result;
	private Message message;
	LinearLayout wechat, friends, QQ, Qzone, sina;
	ImageView icon;
	TextView goodsName, levex, goodsPoint, details, lottery_data,
			goods_receive, goodsState;
	CustomShareBoard board;
	WinningRecordGoodsRequest goodsRequest = new WinningRecordGoodsRequest();
	WinningRecordGoodsResult goodsResult = new WinningRecordGoodsResult();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.winning_record_goods_weixinfriends_btn:
			board.performShare(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.winning_record_goods_friendsshare_btn:
			board.performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.winning_record_goods_QQ_btn:
			board.performShare(SHARE_MEDIA.QQ);
			break;
		case R.id.winning_record_goods_Qzone_btn:
			board.performShare(SHARE_MEDIA.QZONE);
			break;
		case R.id.winning_record_goods_sina_btn:
			board.performShare(SHARE_MEDIA.SINA);
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
		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
		View view = View.inflate(this,
				R.layout.activity_winning_record_goods_details, null);
		base_centent.addView(view);
		board = new CustomShareBoard(this);
		init();

	}


	private void requestData(Object entry) {
		gson = new Gson();
		String request = gson.toJson(entry);
		LogUtil.i("request=====" + request);
		
		LogUtil.i("result=====" + result);
		HttpConnectTool.update(request, mContext, new ConnectListener() {
			
			@Override
			public void contectSuccess(String result) {

				WinningRecordGoodsResult goodsResult = gson.fromJson(result,
						WinningRecordGoodsResult.class);
				if(goodsResult.p.isTrue){
				if (goodsResult.p.WinningDetails != null) {
					goodsName.setText(goodsName.getText()
							+ goodsResult.p.WinningDetails.name);
					levex.setText("" + levex.getText()
							+ goodsResult.p.WinningDetails.prizeGrade + "等奖");
					goodsPoint.setText(goodsPoint.getText()
							+ goodsResult.p.WinningDetails.score);
					details.setText(goodsResult.p.WinningDetails.prizeExplain);
					switch (goodsResult.p.WinningDetails.state) {
					case 0:
						goodsState.setText("奖品未领取");
						break;
					case 1:
						goodsState.setText("待发货");
						break;
					case 2:
						goodsState.setText("派送中");
						break;
					case 3:
						goodsState.setText("已完成收货");
						break;
					case 4:
						goodsState.setText("取消订单");
						break;

					default:
						goodsState.setText("未获取到状态");
						break;
					}

					if (goodsResult.p.WinningDetails.prizeImg != null) {

						ImageLoaderUtil.loadImage(
								goodsResult.p.WinningDetails.prizeImg, icon);
					}
					long lotteryTime =goodsResult.p.WinningDetails.lotteryTime ;
				    long receiveTime =goodsResult.p.WinningDetails.receiveTime;
				    Date time = new Date(lotteryTime);
				    Date time2 = new Date(receiveTime);
				    SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
					lottery_data.setText(lottery_data.getText()+formatter.format(time));
					goods_receive.setText(goods_receive.getText()+formatter.format(time2));
				}
				}else{
					startForActivity(mContext, LoginActivity.class, null);
				}
			}
			
			@Override
			public void contectStarted() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void contectFailed(String path, String request) {
				ToastUtils.makeText(mContext, "访问网络失败", 0).show();
				
			}
		});
//		message = Message.obtain();
//		if (!TextUtils.isEmpty(result)) {
//			message.what = LOADING_DATA_SUCCESS;
//		} else {
//			message.what = LOADING_DATA_FAILED;
//		}
//		handler.sendMessage(message);
	}

	private void setdata() {
		String id = GetUserIdUtil.getUserId(this);
		if (id == null) {
			return;
		} else {
			goodsRequest.p.userId = id;
		}
		String tokenId = GetUserIdUtil.getTokenId(this);
		goodsRequest.p.tokenId = tokenId;
		goodsRequest.p.winningId = getIntent().getStringExtra(
				HeadBaseActivity.intentKey);
		requestData(goodsRequest);

	}

	private void init() {
		icon = (ImageView) findViewById(R.id.firm_order_img);
		goodsName = (TextView) findViewById(R.id.firm_order_detail);
		levex = (TextView) findViewById(R.id.winning_record_goods_prize_levex);
		goodsPoint = (TextView) findViewById(R.id.winning_record_goods_point);
		details = (TextView) findViewById(R.id.winning_record_goods_details_product_tv);
		lottery_data = (TextView) findViewById(R.id.winning_recordz_goods_lottery_data);
		goods_receive = (TextView) findViewById(R.id.winning_recordz_goods_receive);
		goodsState = (TextView) findViewById(R.id.winning_record_goods_isfinish);
		wechat = (LinearLayout) findViewById(R.id.winning_record_goods_weixinfriends_btn);
		friends = (LinearLayout) findViewById(R.id.winning_record_goods_friendsshare_btn);
		QQ = (LinearLayout) findViewById(R.id.winning_record_goods_QQ_btn);
		Qzone = (LinearLayout) findViewById(R.id.winning_record_goods_Qzone_btn);
		sina = (LinearLayout) findViewById(R.id.winning_record_goods_sina_btn);
		wechat.setOnClickListener(this);
		friends.setOnClickListener(this);
		QQ.setOnClickListener(this);
		Qzone.setOnClickListener(this);
		sina.setOnClickListener(this);
		setdata();
	}
}
