package com.went_gone.mobilesafe.fragment;

import java.util.List;

import com.went_gone.mobilesafe.LockAppActivity;
import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.adapter.AppLockAdapter;
import com.went_gone.mobilesafe.adapter.AppLockAdapter.OnAnimationListener;
import com.went_gone.mobilesafe.adapter.AppUnLockAdapter;
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
 * 已加锁的App的Fragment
 * 
 * @author Went_Gone
 * 
 */
public class LockAppFragment extends Fragment {
	private List<AppInfoEntity> mLockAppList;
	private AppLockAdapter mAdapter;
	private ListView mLV_LockApp;
	private TextView mTV_Title;
	private int count;
	private AppLockDao mDao;

	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x00) {
				if (LockAppActivity.instance.mLockList != null) {
					mLockAppList = LockAppActivity.instance.mLockList;
					count = mLockAppList.size();
					mTV_Title.setText("已加锁的应用(" + count + ")");
					mAdapter = new AppLockAdapter(mLockAppList,
							getActivity());
					mLV_LockApp.setAdapter(mAdapter);
					mAdapter.setListener(new OnAnimationListener() {
						
						@Override
						public void animation(View view, final AppInfoEntity appInfo) {
							view.startAnimation(LockAppActivity.instance.mTranslateDeleteLockAnimation);
							//对动画执行过程做事件监听
							LockAppActivity.instance.mTranslateDeleteLockAnimation.setAnimationListener(new AnimationListener() {
								
								@Override
								public void onAnimationStart(Animation animation) {
									
								}
								
								@Override
								public void onAnimationRepeat(Animation animation) {
									
								}
								
								@Override
								public void onAnimationEnd(Animation animation) {
									LockAppActivity.instance.mLockList.remove(appInfo);
									LockAppActivity.instance.mUnLockList.add(appInfo);
									//从已加锁的数据库中删除一条数据
									mDao.delete(appInfo.getPackageName());
									mAdapter.notifyDataSetChanged();
									count = mLockAppList.size();
									mTV_Title.setText("已加锁的应用(" + count + ")");
								}
							});
						}
					});
				} else {
					mHandler.sendEmptyMessageDelayed(0x00, 1000);
				}
			}
		};
	};
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDao = AppLockDao.getInstance();
		View view = inflater.inflate(R.layout.frag_lock_app, null);
		initViews(view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void initViews(View view) {
		mTV_Title = (TextView) view
				.findViewById(R.id.frag_lock_app_TV_title);
		mLV_LockApp = (ListView) view.findViewById(R.id.frag_lock_app_ListView);
		
		mHandler.sendEmptyMessage(0x00);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
