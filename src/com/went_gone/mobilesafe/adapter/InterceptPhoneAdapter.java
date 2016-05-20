package com.went_gone.mobilesafe.adapter;

import java.util.List;

import android.content.Context;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.entity.PhoneEntity;

public class InterceptPhoneAdapter extends CommonAdapter<PhoneEntity> {

	public InterceptPhoneAdapter(Context context, List<PhoneEntity> mDatas,
			int layoutId) {
		super(context, mDatas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, PhoneEntity t) {
		holder.setTextViewText(R.id.item_listview_intercept_phone_TV_phone, t.getPhone())
		.setTextViewText(R.id.item_listview_intercept_phone_TV_Date, t.getDateString());
	}

}
