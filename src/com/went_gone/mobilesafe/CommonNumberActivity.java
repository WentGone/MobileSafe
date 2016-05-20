package com.went_gone.mobilesafe;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.went_gone.mobilesafe.adapter.CommonNumberAdapter;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.engine.CommonNumberDao;
import com.went_gone.mobilesafe.engine.CommonNumberDao.Child;
import com.went_gone.mobilesafe.engine.CommonNumberDao.Group;
import com.went_gone.mobilesafe.utils.ToastUtil;

/**
 * 常用号码
 * @author Went_Gone
 *
 */
public class CommonNumberActivity extends BaseActivity {
	private ExpandableListView mELV_CommonNumber;
	private CommonNumberAdapter mAdapter;
	private List<Group> mGroupList;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (mAdapter == null) {
				mAdapter = new CommonNumberAdapter(mGroupList, CommonNumberActivity.this);
				mELV_CommonNumber.setAdapter(mAdapter);
			}else {
				mAdapter.notifyDataSetChanged();
			}
		};
	};
	
	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_common_number);
		mELV_CommonNumber = (ExpandableListView) findViewById(R.id.act_common_number_ELV);
		mELV_CommonNumber.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				startCall(mAdapter.getChild(groupPosition, childPosition).getPhone());
				return false;
			}
		});
	}

	/**
	 * 开始打电话
	 */
	protected void startCall(String phone) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:"+phone));
		startActivity(intent);
	}

	@Override
	protected void initVariables() {
		initListViewData();
	}

	/**
	 * 给可扩展的ListView添加数据
	 */
	private void initListViewData() {
		final CommonNumberDao dao = new CommonNumberDao();
		new Thread(){

			public void run() {
				mGroupList = dao.getGroup();
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

}
