package com.went_gone.mobilesafe.adapter;

import java.util.List;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.entity.AppInfoEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AppInfoAdapter extends BaseAdapter {
	private List<AppInfoEntity> mAppInfoList;
	private List<AppInfoEntity> mCustomerAppInfoList;
	private List<AppInfoEntity> mSystemAppInfoList;
	private Context context;

	public AppInfoAdapter(List<AppInfoEntity> mAppInfoList,
			List<AppInfoEntity> mCustomerAppInfoList,
			List<AppInfoEntity> mSystemAppInfoList, Context context) {
		super();
		this.mAppInfoList = mAppInfoList;
		this.mCustomerAppInfoList = mCustomerAppInfoList;
		this.mSystemAppInfoList = mSystemAppInfoList;
		this.context = context;
	}

	public AppInfoAdapter(List<AppInfoEntity> mAppInfoList, Context context) {
		super();
		this.mAppInfoList = mAppInfoList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mAppInfoList.size() + 2;
	}

	// 告知ListView现在有2中类型的条目
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	// 告知每一个索引当前条目类型
	@Override
	public int getItemViewType(int position) {
		if (position == 0 || position == mCustomerAppInfoList.size() + 1) {
			// 标题的状态码
			return 0;
		} else {
			// 应用的状态码
			return 1;
		}
	}

	@Override
	public AppInfoEntity getItem(int position) {
		if (position == 0 || position == mCustomerAppInfoList.size() + 1) {
			// 标题时只显示文字 不能使用List集合中的数据
			return null;
		} else {
			if (position < mCustomerAppInfoList.size() + 1) {
				// 使用用户应用的集合
				return mCustomerAppInfoList.get(position - 1);
			} else {
				// 使用系统应用的集合
				return mSystemAppInfoList.get(position - 2
						- mCustomerAppInfoList.size());
			}
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int mode = getItemViewType(position);
		switch (mode) {
		case 0:
			// 标题
			ViewHodlerTitle vht = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_listview_appinfo_title, null);
				vht = new ViewHodlerTitle();
				vht.tv_Title = (TextView) convertView
						.findViewById(R.id.item_listview_appinfo_TV_Title);
				convertView.setTag(vht);
			} else {
				vht = (ViewHodlerTitle) convertView.getTag();
			}
			if (position == 0) {
				vht.tv_Title.setText("用户应用("+mCustomerAppInfoList.size()+")");
			} else {
				vht.tv_Title.setText("系统应用("+mSystemAppInfoList.size()+")");
			}
			break;
		case 1:
			// 应用
			ViewHolder vh = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_listview_appinfo_des, null);
				vh = new ViewHolder();
				vh.iv_icon = (ImageView) convertView
						.findViewById(R.id.item_listview_appinfo_IV);
				vh.tv_Name = (TextView) convertView
						.findViewById(R.id.item_listview_appinfo_TV_Name);
				vh.tv_Location = (TextView) convertView
						.findViewById(R.id.item_listview_appinfo_TV_Location);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.iv_icon.setBackgroundDrawable(getItem(position).getIcon());
			vh.tv_Name.setText(getItem(position).getName());
			if (getItem(position).isSDCard()) {
				vh.tv_Location.setText("sd卡应用");
			} else {
				vh.tv_Location.setText("手机应用");
			}
			break;
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageView iv_icon;
		TextView tv_Name;
		TextView tv_Location;
	}

	private static class ViewHodlerTitle {
		TextView tv_Title;
	}
}
