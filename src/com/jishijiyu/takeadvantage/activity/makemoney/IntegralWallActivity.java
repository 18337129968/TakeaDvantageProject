package com.jishijiyu.takeadvantage.activity.makemoney;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshBase;
import com.jishijiyu.takeadvantage.activity.widget.PullToRefreshListView;
import com.jishijiyu.takeadvantage.adapter.MyAdapter;
import com.jishijiyu.takeadvantage.adapter.ViewHolder;
import com.jishijiyu.takeadvantage.entity.request.DownloadPriceRequest;
import com.jishijiyu.takeadvantage.entity.request.IntegralWallRequest;
import com.jishijiyu.takeadvantage.entity.request.QueryGoldNumRequest;
import com.jishijiyu.takeadvantage.entity.result.DowloadPriceResult;
import com.jishijiyu.takeadvantage.entity.result.IntegralWallResult;
import com.jishijiyu.takeadvantage.entity.result.IntegralWallResult.AppList;
import com.jishijiyu.takeadvantage.entity.result.LoginUserResult;
import com.jishijiyu.takeadvantage.entity.result.QueryGoldNumResult;
import com.jishijiyu.takeadvantage.utils.DownLoadApps;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.ImageLoaderUtil;
import com.jishijiyu.takeadvantage.utils.IntentActivity;
import com.jishijiyu.takeadvantage.utils.LogUtil;
import com.jishijiyu.takeadvantage.utils.ToastUtils;

/**
 * 拍币榜
 * 
 * @author baohan
 * 
 */
@SuppressLint("NewApi")
public class IntegralWallActivity extends Fragment implements IEarnPointsRequest{
	private PullToRefreshListView listView;
	private Context context;
	String type;
	private int page = 0;
	private int pageSize = 9;
	private Gson gson = null;
	private IntegralWallResult integralWallResult = null;
	private MyAdapter<IntegralWallResult.AppList> adapter = null;
	private List<IntegralWallResult.AppList> appLists = null;
	private List<IntegralWallResult.AppList> appLists2 = null;
	private boolean flag = true;
	private static int thread_count=0;
	private static final int thread_total_count=3;

	public IntegralWallActivity(Context mContext, String type) {
		this.context = mContext;
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list, container, false);
		init(view);
		return view;
	}

	@SuppressLint("NewApi")
	public void init(View view) {

		listView = (PullToRefreshListView) view.findViewById(R.id.ListView1);
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 0;
				appLists2 = null;
				getResult();
				listView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page++;
				getResult();
				listView.onRefreshComplete();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				IntegralWallResult.AppList appList = appLists2.get(arg2);
				Intent intent = new Intent();
				intent.setClass(context, SoftwareActivity.class);
				intent.putExtra(HeadBaseActivity.intentKey, appList);
				context.startActivity(intent);
			}
		});
		
	}

	@Override
	public void request() {
		// TODO Auto-generated method stub
		getResult();
		flag = false;
	}

	@Override
	public boolean isFirst() {
		// TODO Auto-generated method stub
		return flag;
	}

	private void QueryUser() {
		QueryGoldNumRequest queryGoldNumRequest = new QueryGoldNumRequest();
		QueryGoldNumRequest.User user = queryGoldNumRequest.p;
		user.userId = GetUserIdUtil.getUserId(getActivity());
		user.tokenId = GetUserIdUtil.getTokenId(getActivity());
		final Gson gson = new Gson();
		String json = gson.toJson(queryGoldNumRequest);
		HttpConnectTool.update(json, getActivity(), new ConnectListener() {

			@Override
			public void contectSuccess(String result) {

				if (!TextUtils.isEmpty(result)) {
					QueryGoldNumResult queryGoldNumResult = gson.fromJson(
							result, QueryGoldNumResult.class);
					if (queryGoldNumResult.p.isTrue) {
						if (queryGoldNumResult.p.user != null) {
							LoginUserResult login = GetUserIdUtil
									.getLogin(getActivity());
							login.p.user.score = queryGoldNumResult.p.user.score != null ? Integer
									.parseInt(queryGoldNumResult.p.user.score)
									: 0;
							GetUserIdUtil.saveLoginUserResult(getActivity(),
									login);
						}
					} else {
						IntentActivity.mIntent(getActivity());
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

	};

	public void getPrice(String appScore) {
		gson = new Gson();
		DownloadPriceRequest request = new DownloadPriceRequest();
		DownloadPriceRequest.Parameter pramater = request.p;
		pramater.appScore = appScore;
		pramater.userId = GetUserIdUtil.getUserId(getActivity());
		pramater.tokenId = GetUserIdUtil.getTokenId(getActivity());
		String json = gson.toJson(request);
		HttpConnectTool.update(json, getActivity(), new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					LogUtil.i("result", result);
					DowloadPriceResult dowloadPriceResult = gson.fromJson(
							result, DowloadPriceResult.class);
					if (dowloadPriceResult != null) {
						if (dowloadPriceResult.p.isTrue) {
							if (dowloadPriceResult.p.isSucce) {
								QueryUser();
							} else {
								ToastUtils.makeText(getActivity(), "拍币获取失败！",
										ToastUtils.LENGTH_SHORT).show();
							}
						} else {
							IntentActivity.mIntent(getActivity());
						}
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

	private void getResult() {
		gson = new Gson();
		IntegralWallRequest integralWallRequest = new IntegralWallRequest();
		IntegralWallRequest.Pramater pramater = integralWallRequest.p;
		pramater.page = page + "";
		pramater.pageSize = pageSize + "";
		pramater.userId = GetUserIdUtil.getUserId(getActivity());
		pramater.tokenId = GetUserIdUtil.getTokenId(getActivity());
		String json = gson.toJson(integralWallRequest);
		HttpConnectTool.update(json, getActivity(), new ConnectListener() {
			@Override
			public void contectSuccess(String result) {
				if (!TextUtils.isEmpty(result)) {
					LogUtil.i("result", result);
					integralWallResult = gson.fromJson(result,
							IntegralWallResult.class);
					if (integralWallResult != null) {
						if (integralWallResult.p.isTrue) {
							appLists = integralWallResult.p.appList;
							if (appLists != null && appLists.size() > 0) {
								if (appLists2 == null) {
									appLists2 = appLists;
								} else {
									for (int i = 0; i < appLists.size(); i++) {
										appLists2.add(appLists.get(i));
									}
								}
								if (adapter == null) {
									IntegralWallActivity.this.adapter = new MyAdapter<IntegralWallResult.AppList>(
											getActivity(), appLists2,
											R.layout.integral_wall_item) {

										@Override
										public void convert(ViewHolder helper,
												final int position, final AppList item) {
											ImageLoaderUtil
													.loadImage(
															item.appIco,
															(ImageView) helper
																	.getView(R.id.icon_iv));
											helper.setText(
													R.id.application_name,
													item.appTitle);
											helper.setText(
													R.id.application_type,
													item.appType);
											helper.setText(R.id.integral, "+"
													+ item.appScore + "拍币");
											helper.setOnclick(
													R.id.download_btn,
													new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															getPrice(item.appScore);
															thread_count++;
															if(thread_count<=thread_total_count){
																ToastUtils
																.makeText(
																		getActivity(),
																		"已开始下载，请稍后...",
																		ToastUtils.LENGTH_SHORT)
																.show();
														
																implementsDownLoad(appLists2.get(position).appApk,appLists2.get(position).appTitle,Long.parseLong(appLists2.get(position).appSize),item.appScore);
															}else{
																Toast.makeText(context, "最多可同时下载3个，请稍再继续...", 1).show();
															}
														}
													});
										}
									};
									listView.setAdapter(adapter);
								} else {
									adapter.refresh(appLists2);
								}
							}
						} else {
							IntentActivity.mIntent(getActivity());
						}
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
	
	//实现下载
	
			public void implementsDownLoad(final String url,final String fileName,final Long Appsize,final String appScore){
					new Thread(){
						public void run() {
							DownLoadApps downLoadApps=new DownLoadApps();
							try {
								File file = downLoadApps.getData(url, fileName);
								if(file.length()>=Appsize){
									thread_count=thread_count-1;
									System.out.println("恭喜你下载完成...");
									//runOnUiThread
									ToastUtils.makeText(context, "恭喜你下载完成...", 1).show();
									//实现自动安装
									openFile(file);
									//实现获取拍币
									getPrice(appScore);
								}
								
								
							} catch (Exception e) {
								
								e.printStackTrace();
							}
						};
					}.start();
				
			}
			
			//在手机上打开，并安装
			public void openFile(File file){
				String fileName=file.getName();
				Intent intent = new Intent();
		        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        intent.setAction(android.content.Intent.ACTION_VIEW);
		        String type ="application/vnd.android.package-archive";
		        intent.setDataAndType(Uri.fromFile(file), type);
		        startActivity(intent);
		       
			}
}
