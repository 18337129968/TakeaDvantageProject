package com.jishijiyu.takeadvantage.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jishijiyu.takeadvantage.R;
import com.jishijiyu.takeadvantage.entity.ReceivingAddress;

public class MoreAddressAdapter extends MyBaseAdapter {
	List<ReceivingAddress> list;
	public MoreAddressAdapter(Context context, List<ReceivingAddress> list) {
		super(context, list);
		  this.list = list;
;
	}

	@Override
	public View initView(int position, View convertView) {
		  ViewHolder viewHolder;
          if (convertView == null) {
                  convertView = inflate.inflate(
                                  R.layout.more_address_lv_item, null);
                  viewHolder = new ViewHolder();
                  viewHolder.nameText = (TextView) convertView
                                  .findViewById(R.id.more_address_lv_item_name_tv);
                  viewHolder.phoneNumText = (TextView) convertView
                		  .findViewById(R.id.more_address_lv_item_phonenum_tv);
                  viewHolder.addressText = (TextView) convertView
                		  .findViewById(R.id.more_address_lv_item_address);
             
                  viewHolder.defltText = (TextView) convertView
                		  .findViewById(R.id.more_address_lv_item_tv_defult);
                  
                  viewHolder.defltimage = (ImageView) convertView
                                  .findViewById(R.id.more_address_lv_item_deflt_img);

                 convertView.setTag(viewHolder);
          } else {
                  viewHolder = (ViewHolder) convertView.getTag();
          }
          
         if(convertView != null){
        	 
                  viewHolder.nameText.setText(list.get(position).getName());
                  viewHolder.phoneNumText.setText(list.get(position).getTelephone());
                  viewHolder.addressText.setText(list.get(position).getDetailedAddress());
                  if(list.get(position).isIsdefult()){
                	  viewHolder.defltText.setVisibility(View.VISIBLE);
                  }else{
                	  viewHolder.defltText.setVisibility(View.GONE);
                  }
//                  viewHolder.defltText.setText(list.get(position).getDetailsAddress());
//                  viewHolder.icon.setImageResource(list.get(position).Icon());
          }
          
         return convertView;
  }
  
 public class ViewHolder{ 
          TextView nameText;
          TextView phoneNumText;
          TextView defltText;
          TextView addressText;
          ImageView defltimage;
          ImageView editIcon;
  }
}
