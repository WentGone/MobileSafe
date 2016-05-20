package com.went_gone.mobilesafe.adapter;

import java.util.List;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.entity.AppInfoEntity;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppUnLockAdapter extends BaseAdapter {
	private List<AppInfoEntity> mAppInfoList;
	private Context context;

	public AppUnLockAdapter(List<AppInfoEntity> mAppInfoList, Context context) {
		super();
		this.mAppInfoList = mAppInfoList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mAppInfoList.size();
	}

	@Override
	public AppInfoEntity getItem(int position) {
		return mAppInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_lockapp, null);
			vh = new ViewHolder();
			vh.iv_Icon = (ImageView) convertView.findViewById(R.id.item_listview_lockapp_IV);
			vh.tv_Name = (TextView) convertView.findViewById(R.id.item_listview_lockapp_TV_name);
			vh.iv_State = (ImageView) convertView.findViewById(R.id.item_listview_lockapp_IV_State);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.iv_Icon.setBackgroundDrawable(getItem(position).getIcon());
		vh.tv_Name.setText(getItem(position).getName());
		vh.iv_State.setImageResource(R.drawable.lock_no);
		final View conView = convertView;
		vh.iv_State.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.animation(conView,getItem(position));
			}
		});
		return convertView;
	}
	private static class ViewHolder{
		ImageView iv_Icon;
		TextView tv_Name;
		ImageView iv_State;
	}
	public interface OnAnimationListener{
		void animation(View view,AppInfoEntity appInfo);
	}
	private OnAnimationListener listener;

	public void setListener(OnAnimationListener listener) {
		this.listener = listener;
	}
	
}
