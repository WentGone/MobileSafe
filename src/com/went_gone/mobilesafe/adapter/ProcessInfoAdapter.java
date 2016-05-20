package com.went_gone.mobilesafe.adapter;

import java.util.List;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.entity.ProcessInfoEntity;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ProcessInfoAdapter extends BaseAdapter {
	private List<ProcessInfoEntity> mProcessInfoList;
	private Context context;
	
	
	public ProcessInfoAdapter(List<ProcessInfoEntity> mProcessInfoList,
			Context context) {
		super();
		this.mProcessInfoList = mProcessInfoList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mProcessInfoList.size();
	}

	@Override
	public ProcessInfoEntity getItem(int position) {
		return mProcessInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHodler vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_process_management, null);
			vh = new ViewHodler();
			vh.iv_Icon = (ImageView) convertView.findViewById(R.id.item_listview_process_IV);
			vh.tv_Name = (TextView) convertView.findViewById(R.id.item_listview_process_TV_Name);
			vh.tv_Memory = (TextView) convertView.findViewById(R.id.item_listview_process_TV_Memory);
			vh.cb_seclect = (CheckBox) convertView.findViewById(R.id.item_listview_process_CB);
			convertView.setTag(vh);
		}else {
			vh = (ViewHodler) convertView.getTag();
		}
		vh.iv_Icon.setBackgroundDrawable(getItem(position).getIcon());
		vh.tv_Name.setText(getItem(position).getName());
		vh.tv_Memory.setText(Formatter.formatFileSize(context, getItem(position).getMemory()));
		if (getItem(position).isCheck()) {
			vh.cb_seclect.setSelected(true);
		}else {
			vh.cb_seclect.setSelected(false);
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getItem(position).setCheck(!getItem(position).isCheck());
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	private static class ViewHodler{
		ImageView  iv_Icon;
		TextView tv_Name;
		TextView tv_Memory;
		CheckBox cb_seclect;
	}
}
