package com.jishijiyu.takeadvantage.activity.showprize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.activity.ShowGoldAnimActivity;
import com.jishijiyu.takeadvantage.activity.ShowGoldAnimActivity.AinmationEndListener;
import com.jishijiyu.takeadvantage.activity.login.LoginActivity;
import com.jishijiyu.takeadvantage.entity.request.OnePrizeNoteRequest;
import com.jishijiyu.takeadvantage.entity.request.OnePrizeShowRequest;
import com.jishijiyu.takeadvantage.entity.result.IntegerPrizeNoteResult.WrsList;
import com.jishijiyu.takeadvantage.entity.result.OnePrizeNoteResult.OneWrslist;
import com.jishijiyu.takeadvantage.entity.result.OnePrizeNoteResult;
import com.jishijiyu.takeadvantage.entity.result.OnePrizeShowResult;
import com.jishijiyu.takeadvantage.utils.Constant;
import com.jishijiyu.takeadvantage.utils.EmojiUtil;
import com.jishijiyu.takeadvantage.utils.GetUserIdUtil;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool;
import com.jishijiyu.takeadvantage.utils.HttpConnectTool.ConnectListener;
import com.jishijiyu.takeadvantage.utils.IntegralOperationfUtil;
import com.jishijiyu.takeadvantage.view.SelectPicPopupWindow;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MineOneShinePrizeDesActivity extends Activity {
	private Context mContext;
	@ViewInject(R.id.rl_show_prize_picture)
	private RelativeLayout rl_show_prize_picture;
	@ViewInject(R.id.img_btn_top_left)
	private Button img_btn_top_left;
	@ViewInject(R.id.top_text)
	private TextView top_text;
	@ViewInject(R.id.iv_show_prize_des_ming_img)
	private ImageView iv_show_prize_des_ming_img;
	@ViewInject(R.id.btn_show_prize_des_mine_updata)
	private Button btn_show_prize_des_mine_updata;
	// @ViewInject(R.id.et_show_prize_des_mine_select)
	// private EditText et_show_prize_des_mine_select;
	@ViewInject(R.id.sp_show_prize_des_mine_select)
	private Spinner sp_show_prize_des_mine_select;
	@ViewInject(R.id.et_show_prize_des_mine_input)
	private EditText et_show_prize_des_mine_input;
	@ViewInject(R.id.btn_show_prize_des_mine_ok)
	private Button btn_show_prize_des_mine_ok;
	private SelectPicPopupWindow menuWindow;
	private final String IMAGE_TYPE = "image/*";
	private String imgUrl;
	private Gson gson;
	private static final String IMAGE_FILE_NAME = "prizePicture.jpg";
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final int SET_RESULT_CODE = 3;
	private int awardGrade = 0;
	private int awardId = 0;
	private String awardName = null;
	private String periods = null;
	private String roomType = null;
	private String mealName = null;
	// spinner
	private Spinner spCity = null;
	private ArrayAdapter<String> adapterCity = null;

	private List<String> list = new ArrayList<String>();
	private String[] cityInfo = {};
	private OnePrizeNoteResult opns;

	// 数据list长度
	int size = 0;
	// 有数据按钮可见
	Boolean type = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = MineOneShinePrizeDesActivity.this;
		view = View.inflate(mContext, R.layout.activity_show_prize_des_mine,
				null);
		setContentView(view);
		ViewUtils.inject(this);
		getPrize();
		initHead();
		initView();
	}

	private class OnItemSelectedListenerImpl implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			String city = parent.getItemAtPosition(position).toString();
			awardGrade = opns.p.oneWrslist.get(position).awardGrade;
			awardId = opns.p.oneWrslist.get(position).awardId;
			awardName = opns.p.oneWrslist.get(position).awardName;
			periods = opns.p.oneWrslist.get(position).periods;
			roomType = opns.p.oneWrslist.get(position).roomType;
			mealName = opns.p.oneWrslist.get(position).mealName;

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}

	}

	private void getPrize() {
		gson = new Gson();
		OnePrizeNoteRequest opnq = new OnePrizeNoteRequest();
		opnq.p.tokenId = GetUserIdUtil.getTokenId(mContext);
		opnq.p.userId = GetUserIdUtil.getUserId(mContext);
		final String updataDes = gson.toJson(opnq);
		HttpConnectTool.update(updataDes, mContext, new ConnectListener() {

			@Override
			public void contectSuccess(String result) {
				opns = gson.fromJson(result, OnePrizeNoteResult.class);
				if (opns.p.isTrue) {
					size = opns.p.oneWrslist.size();
					for (int i = 0; i < size; i++) {
						list.add(opns.p.oneWrslist.get(i).awardName);
						// listcity.add(ipns.p.wrsList.get(i).awardName);
					}
					if (size == 0) {
						list.add("请选择奖品");
						cityInfo = (String[]) list.toArray(new String[size]);
					} else {
						type = true;
						cityInfo = (String[]) list.toArray(new String[size]);
					}
					initHead();
					// awardGrade = ipns.p.wrsList.awardGrade;
					// awardId = ipns.p.wrsList.awardId;
					// awardName = ipns.p.wrsList.awardName;
					// periods = ipns.p.wrsList.periods;
				}
			}

			@Override
			public void contectStarted() {

			}

			@Override
			public void contectFailed(String path, String request) {
				btn_show_prize_des_mine_ok.setClickable(true);
			}
		});
	}

	private void initHead() {
		top_text.setText("我要晒奖");
		// 初始化函数中代码如下
		this.spCity = (Spinner) super
				.findViewById(R.id.sp_show_prize_des_mine_select);
		// 将数据cityInfo填充到适配器adapterCity中
		this.adapterCity = new ArrayAdapter<String>(this,
				R.layout.activity_item, cityInfo);
		// 设置下拉框的数据适配器adapterCity
		this.spCity.setAdapter(adapterCity);
		if (size != 0) {
			this.spCity
					.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
		}
		
		img_btn_top_left.setVisibility(View.VISIBLE);
		img_btn_top_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn_show_prize_des_mine_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type) {
					updata();
				} else {
					System.out.println("没奖品");
				}
			}
		});
	}

	private void initView() {
		btn_show_prize_des_mine_updata
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow = new SelectPicPopupWindow(
								MineOneShinePrizeDesActivity.this, itemsOnClick);
						menuWindow.showAtLocation(
								MineOneShinePrizeDesActivity.this
										.findViewById(R.id.rl_show_prize_picture),
								Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
								0);
					}
				});

	}

	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				takePicture();
				break;
			case R.id.btn_pick_photo:
				getPicture();
				break;
			default:
				break;
			}
		}
	};
	private View view;

	/**
	 * 相机拍摄
	 */
	private void takePicture() {
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File picturePath = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			File pictureFile = new File(picturePath, IMAGE_FILE_NAME);
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(pictureFile));
		}
		startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
	}

	/**
	 * 相册拍摄
	 */
	private void getPicture() {
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);

		getAlbum.setType(IMAGE_TYPE);

		startActivityForResult(getAlbum, IMAGE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		Bitmap bm = null;

		ContentResolver resolver = getContentResolver();

		switch (requestCode) {
		case IMAGE_REQUEST_CODE:

			try {

				Uri originalUri = data.getData();

				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);

				String[] proj = { MediaStore.Images.Media.DATA };

				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);

				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				cursor.moveToFirst();
				startPhotoZoom(originalUri);
			} catch (IOException e) {

			}

			break;
		case CAMERA_REQUEST_CODE:
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				File path = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
				File tempFile = new File(path, IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case RESULT_REQUEST_CODE:
			if (data != null) {
				getImageToView(data);
			}
			break;
		default:
			break;
		}
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 340);
		intent.putExtra("outputY", 340);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(this.getResources(), photo);
			iv_show_prize_des_ming_img.setImageDrawable(drawable);
			saveUserLogo(photo);
		}
	}

	private void saveUserLogo(Bitmap bitmap) {
		try {
			File file = mContext.getFilesDir();
			FileOutputStream fos = new FileOutputStream(file + IMAGE_FILE_NAME);
			boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100,
					fos);
			if (success) {
				// 上传
				// updata();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getImage() {
		BitmapDrawable drawable = (BitmapDrawable) iv_show_prize_des_ming_img
				.getBackground();
		Bitmap cacheLogo = BitmapFactory.decodeFile(mContext.getFilesDir()
				+ IMAGE_FILE_NAME);
		if (cacheLogo == null) {
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		cacheLogo.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] byteArray = baos.toByteArray();
		imgUrl = Base64.encodeToString(byteArray, Base64.DEFAULT);
	}

	private void updata() {
		getImage();
		gson = new Gson();
		OnePrizeShowRequest opsq = new OnePrizeShowRequest();

		String awardContent = EmojiUtil
				.filterEmoji(et_show_prize_des_mine_input.getText().toString());

		if (TextUtils.isEmpty(imgUrl)) {
			Toast.makeText(mContext, "照片不能为空", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(awardContent)) {
			Toast.makeText(mContext, "获奖感言内容不正确", Toast.LENGTH_SHORT).show();

		} else {
			opsq.p.tokenId = GetUserIdUtil.getTokenId(mContext);
			opsq.p.userId = GetUserIdUtil.getUserId(mContext);
			opsq.p.awardContent = awardContent;
			opsq.p.awardId = awardId + "";
			opsq.p.imgUrl = imgUrl;
			opsq.p.periods = periods;
			opsq.p.awardGrade = awardGrade + "";
			opsq.p.mealType = mealName;
			opsq.p.roomType = roomType;
			opsq.p.awardName = awardName;
			final String updataDes = gson.toJson(opsq);
			btn_show_prize_des_mine_ok.setClickable(false);
			HttpConnectTool.update(updataDes, mContext, new ConnectListener() {

				@Override
				public void contectSuccess(String result) {
					OnePrizeShowResult opsr = gson.fromJson(result,
							OnePrizeShowResult.class);
					// LogUtil.i("myPrize2.p.isSucce:" + myPrize2.p.isSucce);
					if (opsr.p.isTrue) {
						if (opsr.p.isSucce) {
							setResult(SET_RESULT_CODE);
							btn_show_prize_des_mine_ok.setClickable(true);
							ShowGoldAnimActivity activity = new ShowGoldAnimActivity(
									mContext);
							activity.showPopWindows(
									MineOneShinePrizeDesActivity.this, view,
									"15", false);
							IntegralOperationfUtil.addIntegral(mContext, 15);
							activity.setAnimListener(new AinmationEndListener() {

								@Override
								public void end() {
									finish();
								}
							});
						}
					} else {
						Intent intent = new Intent(mContext,
								LoginActivity.class);
						startActivityForResult(intent,
								Constant.AGAIN_LOGIN_CODE);
					}
				}

				@Override
				public void contectStarted() {

				}

				@Override
				public void contectFailed(String path, String request) {
					btn_show_prize_des_mine_ok.setClickable(true);
				}
			});

		}
	}

}
