package com.jishijiyu.takeadvantage.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xkeuops.diy.AdExtraTaskStatus;
import com.android.xkeuops.diy.AdTaskStatus;
import com.android.xkeuops.diy.AppDetailDataInterface;
import com.android.xkeuops.diy.AppDetailObject;
import com.android.xkeuops.diy.AppExtraTaskObject;
import com.android.xkeuops.diy.AppExtraTaskObjectList;
import com.android.xkeuops.diy.AppSummaryObject;
import com.android.xkeuops.diy.DiyAppNotify;
import com.android.xkeuops.diy.DiyOfferWallManager;
import com.android.xkeuops.diy.SignInInterface;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.adapter.GridViewAdapter;
import com.jishijiyu.takeadvantage.adapter.ListViewAdapter_TaskDesc;
import com.jishijiyu.takeadvantage.entity.TaskDescObject;
import com.jishijiyu.takeadvantage.utils.BitmapLoaderManager;
import com.jishijiyu.takeadvantage.utils.Util;

public class DiyOfferAdDetailActivity extends Activity implements OnClickListener, BitmapDownloadListener,
		DiyAppNotify, AppDetailDataInterface {

	public AppSummaryObject appSumObject;

	private Button sigin;

	private ImageView appIcon;

	private TextView appName;

	private TextView appVersion;

	private TextView appSize;

	private TextView appStyle;

	private TextView appDesc;

	private Button openOrDownloadBtn;

	private ProgressBar downlaodProgressBar;

	private SwipeRefreshLayout mSwipeRefreshLayout; // 下拉刷新组件

	private GridView gridView;

	private ListView listView;

	private GridViewAdapter gvAdapter;

	private ListViewAdapter_TaskDesc lvAdapter;

	private AppDetailObject appDetailObject;

	private ArrayList<Bitmap> bmLists;

	private boolean isPackageExist = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diyoffer_detail);

		// 检查传入的Intent是否合法，不合法就直接finish
		checkIntentIsLegal();

		// 检查这个应用是否已经存在于手机中
		isPackageExist = Util.checkLocalAppExistOrNot(this, appSumObject.getPackageName());

		// 初始化View
		initView();

		// （可选）注册广告下载安装监听-随时随地获得应用下载安装状态的变动情况
		DiyOfferWallManager.getInstance(this).registerDiyAppNotifyListener(this);

		// 获取广告的详细数据
		requestDetailData();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// （可选）注销下载安装监听-如果在onCreate中注册了，那这里必须得注销
		DiyOfferWallManager.getInstance(this).removeDiyAppNotifyListener(this);

		// （重要）如果使用SDK提供的异步方法请求详情页的数据，需要在请求之前先注册回调接口，并且记得在退出的时候（如activity的onDestory方法中）注销接口
		DiyOfferWallManager.getInstance(this).removeAppDetailDataInterface(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		checkIntentIsLegal();
		isPackageExist = Util.checkLocalAppExistOrNot(this, appSumObject.getPackageName());
		requestDetailData();
	}

	/**
	 * 检查传入的Intent是否合法
	 */
	private void checkIntentIsLegal() {

		Object obj = getIntent().getSerializableExtra("ad");

		if (obj == null || !(obj instanceof AppSummaryObject)) {
			this.finish();
			return;
		}

		appSumObject = (AppSummaryObject) obj;
		if (appSumObject == null) {
			this.finish();
			return;
		}
	}

	/**
	 * 获取广告的详细数据,下面展示两种加载方式，开发者可选择适合自己的方式
	 */
	private void requestDetailData() {

		mSwipeRefreshLayout.setRefreshing(true);

		// ------------------------------------------------------------------------
		// 异步加载方式

		// （重要）如果使用SDK提供的异步方法请求详情页的数据，需要在请求之前先注册回调接口，并且记得在退出的时候（如activity的onDestory方法中）注销接口
		DiyOfferWallManager.getInstance(this).registerAppDetailDataInterface(this);

		// 发起异步请求, 有结果之后会回调上述注册的接口
		DiyOfferWallManager.getInstance(this).loadAppDetailData(appSumObject);

		// ------------------------------------------------------------------------
		// 同步加载方式

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// final AppDetailObject data = DiyOfferWallManager.getInstance(
		// DiyOfferAdDetailActivity.this).getAppDetailData(appSumObject);
		// Util_System_Runtime.getInstance().runInUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// onLoadAppDetailDataSuccess(DiyOfferAdDetailActivity.this, data);
		// }
		// });
		// } catch (NetworkException e) {
		// Log.e("YoumiSdk", "", e);
		// Util_System_Runtime.getInstance().runInUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// onLoadAppDetailDataFailed();
		// }
		// });
		//
		// } catch (final ErrorCodeException e) {
		// Log.e("YoumiSdk", "", e);
		// Util_System_Runtime.getInstance().runInUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// onLoadAppDetailDataFailedWithErrorCode(e.getErrCode());
		// }
		// });
		// }
		// }
		// }).start();
	}

	private void initView() {

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr_ad_detail);
		mSwipeRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
				R.color.holo_orange_light, R.color.holo_red_light);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				// 重新刷新页面
				requestDetailData();
			}
		});

		appIcon = (ImageView) findViewById(R.id.iv_detailpage_appicon);
		appName = (TextView) findViewById(R.id.tv_detailpage_appname);
		appVersion = (TextView) findViewById(R.id.tv_detailpage_apppvn);
		appSize = (TextView) findViewById(R.id.tv_detailpage_appsize);
		appStyle = (TextView) findViewById(R.id.tv_detailpage_appstyle);
		appDesc = (TextView) findViewById(R.id.tv_detailpage_appdesc);
		sigin = (Button) findViewById(R.id.btn_detailpage_sign_in);
		sigin.setVisibility(View.GONE);
		sigin.setOnClickListener(this);

		downlaodProgressBar = (ProgressBar) findViewById(R.id.pb_download);
		downlaodProgressBar.setVisibility(View.GONE);

		openOrDownloadBtn = (Button) findViewById(R.id.btn_detailpage_open_or_install);
		openOrDownloadBtn.setVisibility(View.INVISIBLE);
		openOrDownloadBtn.setOnClickListener(this);
		updateOpenOrDownloadButtonStatus(appSumObject.getAdTaskStatus());

		gridView = (GridView) findViewById(R.id.detailpage_gridView);
		gvAdapter = new GridViewAdapter(DiyOfferAdDetailActivity.this, null);
		gridView.setAdapter(gvAdapter);

		listView = (ListView) findViewById(R.id.detailpage_listview);
		listView.setEnabled(false);
		lvAdapter = new ListViewAdapter_TaskDesc(DiyOfferAdDetailActivity.this, null);
		listView.setAdapter(lvAdapter);

	}

	/**
	 * 更新按钮状态
	 */
	private void updateOpenOrDownloadButtonStatus(int status) {
		switch (status) {
		case AdTaskStatus.NOT_COMPLETE: // 未完成
			openOrDownloadBtn.setEnabled(true);
			openOrDownloadBtn.setText(isPackageExist ? "任务未完成，打开体验" : "下载安装");
			break;
		case AdTaskStatus.HAS_EXTRA_TASK: // 有追加任务
			openOrDownloadBtn.setEnabled(true);
			boolean isExtraTaskCanDo = false; // 标记追加任务现在是否可以进行
			for (int i = 0; i < appSumObject.getExtraTaskList().size(); ++i) {
				if (AdExtraTaskStatus.IN_PROGRESS == appSumObject.getExtraTaskList().get(i)
						.getStatus()) {
					isExtraTaskCanDo = true;
					break;
				}
			}
			openOrDownloadBtn.setText(isPackageExist ? (isExtraTaskCanDo ? "任务未完成，打开体验" : "任务等待中") : "下载安装");
			break;
		case AdTaskStatus.ALREADY_COMPLETE: // 已完成
			openOrDownloadBtn.setEnabled(true);
			openOrDownloadBtn.setText(isPackageExist ? "任务已完成，打开" : "重新安装");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_detailpage_open_or_install:
			if (appDetailObject != null) {

				// 如果应用已经安装了，就统一使用有米sdk的打开api
				// 如果应用还没有安装的话,需要判断选用哪种下载方式，
				// 1、调用sdk的下载api进行下载（适用于国内app）
				// 2、跳转到googleplay进行下载（使用与国外app）
				// if (!isPackageExist) {
				//
				// // 如果googleplay的跳转url不为空，就用用外部浏览器打开googleplay的url进行跳转
				// String googlePlayUrl = appDetailObject.getGoogleAdDownloadUrl();
				// if (googlePlayUrl != null && googlePlayUrl.trim().length() > 0) {
				// Util.tryToOpenGooglePlayToDownload(this, googlePlayUrl);
				// break;
				// }
				// }
				DiyOfferWallManager.getInstance(this).openOrDownloadApp(appDetailObject);
			}
			break;

		case R.id.btn_detailpage_sign_in:
			// demo演示的签到规则：（要点：需要用户真实地打开过应用并且体验一段时间之后才发送签到效果记录，这样的签到效果记录才是有效的，不然只发签到效果记录而不打开应用可能会被视为无效签到）
			// 先打开应用，然后一段时间（10s）之后，发送签到效果记录，由于篇幅限制，demo演示的签到任务并没有实现签到之后返回用户拍币
			// 如果打开失败的话，就提示用户需要重新安装才可以

			if (Util.startActivityByPackageName(this, appDetailObject.getPackageName())) {

				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						DiyOfferWallManager.getInstance(DiyOfferAdDetailActivity.this)
								.sendSignInActionType(appDetailObject, new SignInInterface() {

									/**
									 * 签到成功时会回调这个接口，本回调接口执行在UI线程中
									 */
									@Override
									public void onSignInSuccessed(Context context) {
										String tips = "签到成功";
										Toast.makeText(context, tips, Toast.LENGTH_SHORT).show();
										Log.d("YoumiSdk", tips);
									}

									/**
									 * 签到失败时会回调这个接口，本回调接口执行在UI线程中
									 * 
									 * @param adId
									 *            广告ID
									 * @param errorCode
									 *            错误代码
									 */
									@Override
									public void onSignInFailed(Context context, int adId, int errorCode) {
										String tips = String.format("广告id = %d, 签到失败,错误代码：%d", adId, errorCode);
										Toast.makeText(context, tips, Toast.LENGTH_LONG).show();
										Log.d("YoumiSdk", tips);
									}
								});
					}
				}, 10000);
			} else {
				Toast.makeText(DiyOfferAdDetailActivity.this, "应用不存在，要重新安装才能完成签到", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 构造任务描述列表
	 * 
	 * @return
	 */
	private List<TaskDescObject> generateDestList() {
		List<TaskDescObject> list = new ArrayList<TaskDescObject>();

		// 1、将正常的任务加入到描述列表中
		int status = 0;
		if (appDetailObject.getAdTaskStatus() == AdTaskStatus.ALREADY_COMPLETE
				|| appDetailObject.getAdTaskStatus() == AdTaskStatus.HAS_EXTRA_TASK) {
			status = AdExtraTaskStatus.COMPLETE; // 标记任务已经完成
		} else if (appDetailObject.getAdTaskStatus() == AdTaskStatus.NOT_COMPLETE) {
			status = AdExtraTaskStatus.IN_PROGRESS; // 标记任务可以进行
		}
		TaskDescObject normalTask = new TaskDescObject(status, appDetailObject.getTaskSteps(),
				appDetailObject.getPoints());
		list.add(normalTask);

		// 2、将追加任务加入到描述列表中
		AppExtraTaskObjectList extraTastkList = appDetailObject.getExtraTaskList();
		if (extraTastkList != null && extraTastkList.size() > 0) {
			for (int i = 0; i < extraTastkList.size(); ++i) {
				AppExtraTaskObject extraTaskObject = extraTastkList.get(i);
				TaskDescObject temp = new TaskDescObject(extraTaskObject.getStatus(),
						extraTaskObject.getAdText(), extraTaskObject.getPoints());
				list.add(temp);
			}
		}
		return list;
	}

	/**
	 * 更新app截图的Gridview显示
	 * 
	 * @param bmLists
	 */
	private void updateGridView(ArrayList<Bitmap> bmLists) {
		if (bmLists != null && !bmLists.isEmpty()) {
			int colWidth = getResources().getDisplayMetrics().widthPixels / 2;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bmLists.size() * colWidth,
					LayoutParams.WRAP_CONTENT);
			gridView.setLayoutParams(params);
			gridView.setColumnWidth(colWidth);
			gridView.setHorizontalSpacing(6);
			gridView.setStretchMode(GridView.NO_STRETCH);
			gridView.setNumColumns(bmLists.size());
			gridView.setVisibility(View.VISIBLE);
			gvAdapter.setData(bmLists);
			gvAdapter.notifyDataSetChanged();
		} else {
			gridView.setVisibility(View.GONE);
		}
	}

	/**
	 * 更新任务描述的Listview显示
	 * 
	 * @param list
	 */
	private void updateListView(List<TaskDescObject> list) {
		if (list != null && !list.isEmpty()) {
			listView.setVisibility(View.VISIBLE);
			lvAdapter.setData(list);
			lvAdapter.notifyDataSetChanged();
		} else {
			listView.setVisibility(View.GONE);
		}
	}

	/**
	 * 当成功请求到数据的时候，会回调本方法（注意:本回调接口执行在UI线程中）
	 * <p>
	 * 注意：广告详细数据有可能为空，开发者处理之前，请先判断是否为空
	 */
	@Override
	public void onLoadAppDetailDataSuccess(Context context, final AppDetailObject appDetailObject) {
		Toast.makeText(context, String.format("请求广告详情数据成功 返回对象 %s", appDetailObject == null ? "为空" : "不为空"),
				Toast.LENGTH_SHORT).show();

		// 先停止刷新状态
		mSwipeRefreshLayout.setRefreshing(false);

		if (appDetailObject != null) {

			this.appDetailObject = appDetailObject;

			// 这里生成一下描述列表并更新Listview显示任务描述
			final List<TaskDescObject> descList = generateDestList();
			updateListView(descList);

			// 当获取到数据的时候，先设置默认图片，等后续图片下载完毕之后在更新
			bmLists = new ArrayList<Bitmap>();
			for (int i = 0; i < appDetailObject.getScreenShotUrls().length; i++) {
				bmLists.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
			}

			openOrDownloadBtn.setVisibility(View.VISIBLE);
			appName.setText(appDetailObject.getAppName());
			appVersion.setText("版本号：" + appDetailObject.getVersionName());
			appSize.setText("大小：" + appDetailObject.getAppSize());
			appStyle.setText(appDetailObject.getAppCategory());
			appDesc.setText(appDetailObject.getDescription());
			if (appDetailObject.getAdTaskStatus() == AdTaskStatus.ALREADY_COMPLETE) {
				sigin.setVisibility(View.VISIBLE);
			}
			updateOpenOrDownloadButtonStatus(appDetailObject.getAdTaskStatus());

			// 更新任务截图Gradview
			updateGridView(bmLists);

			// 构造需要加载的图片url数组，当图片加载完毕的时候更新显示
			// 1、传入图标url
			int ssUrlsLength = appDetailObject.getScreenShotUrls().length;
			String[] imageUrlArray = new String[ssUrlsLength + 1];
			imageUrlArray[0] = appDetailObject.getIconUrl();

			// 2、传入截图url
			if (appDetailObject.getScreenShotUrls() != null) {
				System.arraycopy(appDetailObject.getScreenShotUrls(), 0, imageUrlArray, 1, ssUrlsLength); // 传入截图地址
			}

			// 线程池异步加载图片
			BitmapLoaderManager.loadBitmap(DiyOfferAdDetailActivity.this, DiyOfferAdDetailActivity.this, imageUrlArray);
		}
	}

	/**
	 * 请求成功，但是返回有米错误代码时候，会回调这个接口（注意:本回调接口执行在UI线程中）
	 */
	@Override
	public void onLoadAppDetailDataFailedWithErrorCode(int code) {

		// 先停止刷新状态
		mSwipeRefreshLayout.setRefreshing(false);

		new AlertDialog.Builder(DiyOfferAdDetailActivity.this).setTitle("请求失败")
				.setMessage(String.format("请求错误，错误代码 ： %d， 请联系客服")).create().show();
	}

	/**
	 * 因为网络问题而导致请求失败时，会回调这个接口（注意:本回调接口执行在UI线程中）
	 */
	@Override
	public void onLoadAppDetailDataFailed() {

		// 先停止刷新状态
		mSwipeRefreshLayout.setRefreshing(false);

		new AlertDialog.Builder(DiyOfferAdDetailActivity.this).setTitle("请求失败").setMessage("请求失败，请检查网络").create()
				.show();
	}

	/**
	 * 加载图片有结果时回调（注意:本回调接口执行在UI线程中）
	 */
	@Override
	public void onLoadBitmap(String url, final Bitmap bm) {
		try {
			if (url == appDetailObject.getIconUrl()) { // 显示app图标
				appIcon.setImageBitmap(bm);
			}
			for (int i = 0; i < appDetailObject.getScreenShotUrls().length; i++) { // 显示app截图
				if (url == appDetailObject.getScreenShotUrls()[i]) {
					bmLists.set(i, bm);
					gvAdapter.setData(bmLists);
					gvAdapter.notifyDataSetChanged();
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDownloadStart(int id) {

	}

	@Override
	public void onDownloadFailed(int id) {
		try {
			if (appDetailObject == null) {
				return;
			}

			if (appDetailObject.getAdId() != id) {
				return;
			}
			this.downlaodProgressBar.setProgress(0);
			this.downlaodProgressBar.setVisibility(View.GONE);
			this.openOrDownloadBtn.setEnabled(true);
			this.openOrDownloadBtn.setText("下载失败,请稍候重试!");
		} catch (Throwable e) {
			Log.d("YoumiSdk", "", e);
		}
	}

	@Override
	public void onDownloadSuccess(int id) {
		try {
			if (appDetailObject == null) {
				return;
			}

			if (appDetailObject.getAdId() != id) {
				return;
			}
			this.downlaodProgressBar.setProgress(0);
			this.downlaodProgressBar.setVisibility(View.GONE);
			this.openOrDownloadBtn.setEnabled(true);
			this.openOrDownloadBtn.setText("下载成功,请安装!");
		} catch (Throwable e) {
			Log.d("YoumiSdk", "", e);
		}
	}

	@Override
	public void onDownloadProgressUpdate(int id, long contentLength, long completeLength, int percent,
			long speedBytesPerS) {
		try {
			if (appDetailObject == null) {
				return;
			}

			if (appDetailObject.getAdId() != id) {
				return;
			}
			this.downlaodProgressBar.setVisibility(View.VISIBLE);
			this.downlaodProgressBar.setProgress(percent);
			this.openOrDownloadBtn.setEnabled(false);
			this.openOrDownloadBtn.setText(String
					.format("正在下载,已完成%d%% ,下载速度: %dKB/s", percent, (speedBytesPerS / 1024)));
		} catch (Throwable e) {
			Log.d("YoumiSdk", "", e);
		}

	}

	@Override
	public void onInstallSuccess(int id) {
		try {
			if (appDetailObject == null) {
				return;
			}

			if (appDetailObject.getAdId() != id) {
				return;
			}
			this.downlaodProgressBar.setProgress(0);
			this.downlaodProgressBar.setVisibility(View.GONE);
			this.openOrDownloadBtn.setEnabled(true);
			this.openOrDownloadBtn.setText("已安装成功,打开");
		} catch (Throwable e) {
			Log.d("YoumiSdk", "", e);
		}
	}

}
