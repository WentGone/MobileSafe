package com.went_gone.mobilesafe.fragment;

import java.util.List;

import com.went_gone.mobilesafe.LockAppActivity;
import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.adapter.AppUnLockAdapter;
import com.went_gone.mobilesafe.adapter.AppUnLockAdapter.OnAnimationListener;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.db.dao.AppLockDao;
import com.went_gone.mobilesafe.entity.AppInfoEntity;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 未加锁App的Fragment
 * @author Went_Gone
 *
 */
public class LockNoAppFragment extends Fragment {
	private AppUnLockAdapter mAdapter;
	private ListView mLV_LockNoApp;
	private TextView mTV_Title;
	private List<AppInfoEntity> mLockAppList;
	private int count;
	private AppLockDao mDao;
	
	public Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x00) {
				if (LockAppActivity.instance.mUnLockList!=null) {
					mLockAppList = LockAppActivity.instance.mUnLockList;
					count = mLockAppList.size();
					mTV_Title.setText("未加锁的应用("+count+")");
					mAdapter = new AppUnLockAdapter(mLockAppList, getActivity());
					mLV_LockNoApp.setAdapter(mAdapter);
					mAdapter.setListener(new OnAnimationListener(){
						@Override
						public void animation(View view,
								final AppInfoEntity appInfo) {
							view.startAnimation(LockAppActivity.instance.mTranslateAddLockAnimation);
							//对动画执行过程做事件监听
							LockAppActivity.instance.mTranslateAddLockAnimation.setAnimationListener(new AnimationListener() {
								
								@Override
								public void onAnimationStart(Animation animation) {
									
								}
								
								@Override
								public void onAnimationRepeat(Animation animation) {
									
								}
								
								@Override
								public void onAnimationEnd(Animation animation) {
									LockAppActivity.instance.mUnLockList.remove(appInfo);
									LockAppActivity.instance.mLockList.add(appInfo);
									//从已加锁的数据库中添加一条数据
									mDao.insert(appInfo.getPackageName());
									mAdapter.notifyDataSetChanged();
									count = mLockAppList.size();
									mTV_Title.setText("未加锁的应用(" + count + ")");
								}
							});
						}
						
					});
				}else {
					mHandler.sendEmptyMessageDelayed(0x00, 1000);
				}
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDao = AppLockDao.getInstance();
		View view = inflater.inflate(R.layout.frag_lock_no_app, null);
		initViews(view);
		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
	}

	private void initViews(View view) {
		mTV_Title = (TextView) view.findViewById(R.id.frag_lock_no_app_TV_title);
		mLV_LockNoApp = (ListView) view.findViewById(R.id.frag_lock_no_app_ListView);
		
		mHandler.sendEmptyMessage(0x00);
	}
}
