package com.went_gone.mobilesafe.adapter;

import java.util.List;

import android.content.Context;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.entity.SmsEntity;

public class InterceptSmsAdapter extends CommonAdapter<SmsEntity> {

	public InterceptSmsAdapter(Context context, List<SmsEntity> mDatas,
			int layoutId) {
		super(context, mDatas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, SmsEntity t) {
		holder.setTextViewText(R.id.item_listview_intercept_sms_TV_phone, t.getPhone())
		.setTextViewText(R.id.item_listview_intercept_sms_TV_Date, t.getDateString())
		.setTextViewText(R.id.item_listview_intercept_sms_TV_Content, t.getContent());
	}

}
