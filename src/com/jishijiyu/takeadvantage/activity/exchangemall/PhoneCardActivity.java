//package com.jishijiyu.takeadvantage.activity.exchangemall;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Intent;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.FrameLayout;
//import android.widget.ListView;
//
//import com.jishijiyu.takeadvantage.R;
//import com.jishijiyu.takeadvantage.activity.HeadBaseActivity;
//import com.jishijiyu.takeadvantage.adapter.MyAdapter;
//import com.jishijiyu.takeadvantage.adapter.ViewHolder;
//import com.jishijiyu.takeadvantage.utils.AppManager;
///**
// * 电话卡
// * @author baohan
// *
// */
//public class PhoneCardActivity extends HeadBaseActivity {
//   private ListView listview;
//   private List<Content> content = null;
//	private MyAdapter<Content> listAdapter = null;
//
//	@Override
//	public void appHead(View view) {
//		// TODO Auto-generated method stub
//		top_text.setText(getResources().getString(R.string.phonecard));
//		btn_right.setVisibility(View.INVISIBLE);
//	}
//
//	@Override
//	public void initReplaceView() {
//		// TODO Auto-generated method stub
//		FrameLayout base_centent = (FrameLayout) findViewById(R.id.base_content);
//		View view = View.inflate(PhoneCardActivity.this,
//				R.layout.phone_card_list, null);
//		base_centent.addView(view);
//		listview = (ListView) view.findViewById(R.id.phone_card_listview);
//		content = new ArrayList<Content>();
//	
////		int imageId[]= { R.drawable.china_telecom, R.drawable.china_unicom};
//		int rechargeAmountId[]={R.string.one_hundred_yuan, R.string.fifty_yuan};
//		
//		int exchangeNumber[]={R.string.exchange_number,R.string.exchange_number1};
//		int needIntegralNumber[]={R.string.need_integral_number,R.string.need_integral_number1};
//		for (int i = 0; i < imageId.length; i++) {
//			Content image = new Content();
//			image.imgId = imageId[i];
//			image.rechargeAmount = getResources().getString(rechargeAmountId[i]);
//			image.exchangeNumber=getResources().getString(exchangeNumber[i]);
//			image.needIntegralNumber=getResources().getString(needIntegralNumber[i]);
//			content.add(image);
//			
//			
//			
//			
//		}
//	
//		
//		
//		listAdapter = new MyAdapter<PhoneCardActivity.Content>(
//				PhoneCardActivity.this, content, R.layout.phone_card_item) {
//			
//
//			@Override
//			public void convert(ViewHolder helper, int posittion, Content item) {
//				helper.setImageResource(R.id.photo, item.imgId);
//				helper.setText(R.id.recharge_amount, item.rechargeAmount);
//				helper.setText(R.id.exchange_number, item.exchangeNumber);
//				helper.setText(R.id.need_integral_number, item.needIntegralNumber);
//				
//				helper.setOnclick(R.id.phonecard_item_layout, new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						Log.e("ce", "测试数据-----------------");
//						Intent intent=new Intent(PhoneCardActivity.this, PrepaidPhoneDetailsActivity.class);
//						startActivity(intent);
//					}
//				});
//				
//			}
//		};
//		listview.setAdapter(listAdapter);
//		
//		 AppManager.getAppManager().addActivity(this);
//
//		 AppManager.getAppManager()
//			.finishActivity(PhoneCardActivity.this);
//	}
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//	private class  Content{
//		public int imgId;
//		public String   rechargeAmount;
//		public String  exchangeNumber;
//		public String needIntegralNumber;
//	}
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		
//	}
//}
