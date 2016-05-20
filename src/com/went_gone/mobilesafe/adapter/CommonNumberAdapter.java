package com.went_gone.mobilesafe.adapter;

import java.util.List;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.engine.CommonNumberDao.Child;
import com.went_gone.mobilesafe.engine.CommonNumberDao.Group;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CommonNumberAdapter extends BaseExpandableListAdapter {
	private List<Group> mGroup;
	private Context context;
	

	public CommonNumberAdapter(List<Group> mGroup, Context context) {
		super();
		this.mGroup = mGroup;
		this.context = context;
	}

	@Override
	public int getGroupCount() {
		return mGroup.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mGroup.get(groupPosition).getChildList().size();
	}

	@Override
	public Group getGroup(int groupPosition) {
		return mGroup.get(groupPosition);
	}

	@Override
	public Child getChild(int groupPosition, int childPosition) {
		return mGroup.get(groupPosition).getChildList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView textView = new TextView(context);
		textView.setText("        "+getGroup(groupPosition).getName());
		textView.setTextColor(Color.RED);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
		textView.setPadding(0, 15, 0, 15);
		return textView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_common_number_child, null);
		TextView tv_Phone = (TextView) view.findViewById(R.id.item_common_number_TV_Phone);
		TextView tv_Name = (TextView) view.findViewById(R.id.item_common_number_TV_Name);
		tv_Name.setText(getChild(groupPosition, childPosition).getName());
		tv_Phone.setText(getChild(groupPosition, childPosition).getPhone());
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
