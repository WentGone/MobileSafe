package com.went_gone.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.db.dao.AppLockDao;
import com.went_gone.mobilesafe.engine.AppInfoProvider;
import com.went_gone.mobilesafe.entity.AppInfoEntity;
import com.went_gone.mobilesafe.fragment.LockAppFragment;
import com.went_gone.mobilesafe.fragment.LockNoAppFragment;

public class LockAppActivity extends FragmentActivity {
	private RadioGroup mRadioGroup;
	private LockAppFragment mLockAppFrag;
	private LockNoAppFragment mLockNoAppFrag;
	private FragmentManager mFManager;
	private FragmentTransaction mFTransaction;
	private AppLockDao mDao;
	public List<AppInfoEntity> mAppInfoList,mLockList,mUnLockList;
	public static LockAppActivity instance;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	public TranslateAnimation mTranslateAddLockAnimation,mTranslateDeleteLockAnimation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		initVariables();
		initViews(savedInstanceState);
		initAnimation();
	}

	/**
	 * 初始化平移动画（平移自身宽度大小）
	 */
	private void initAnimation() {
		mTranslateAddLockAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 1, 
				Animation.RELATIVE_TO_SELF,0,
				Animation.RELATIVE_TO_SELF, 0);
		mTranslateAddLockAnimation.setDuration(500);
		mTranslateDeleteLockAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1, 
				Animation.RELATIVE_TO_SELF,0,
				Animation.RELATIVE_TO_SELF, 0);
		mTranslateDeleteLockAnimation.setDuration(500);
	}

	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_lockapp);
		 initRadioGroup();
	}

	private void initRadioGroup() {
		mRadioGroup = (RadioGroup) findViewById(R.id.act_lockapp_RadiopGroup);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mFTransaction = mFManager.beginTransaction();
				switch (checkedId) {
				case R.id.act_lockapp_RB_lock:
					mFTransaction.replace(R.id.act_lockapp_FrameLayout,
							mLockAppFrag);
					break;
				case R.id.act_lockapp_RB_nolock:
					mFTransaction.replace(R.id.act_lockapp_FrameLayout,
							mLockNoAppFrag);
					break;
				}
				mFTransaction.commit();
			}
		});
		mRadioGroup.check(R.id.act_lockapp_RB_nolock);
	}

	protected void initVariables() {
		initFragment();
		initDatas();
	}

	/**
	 * 区分已加锁和未加锁应用集合
	 */
	private void initDatas() {
		new Thread(){
			public void run() {
				//获取手机上所有的应用信息
				mAppInfoList = AppInfoProvider.getAppListInfo(LockAppActivity.this);
				//区分已加锁应用与未加锁应用
				mLockList = new ArrayList<AppInfoEntity>();
				mUnLockList = new ArrayList<AppInfoEntity>();
				//获取数据库中已加锁应用的包名
				mDao = AppLockDao.getInstance();
				List<String> lockPackageNameList = mDao.selectAll();
				for (AppInfoEntity appInfo : mAppInfoList) {
					//如果循环到的包名在数据库中  这说明是已加锁应用
					if (lockPackageNameList.contains(appInfo.getPackageName())) {
						mLockList.add(appInfo);
					}else {
						mUnLockList.add(appInfo);
					}
				}
			};
		}.start();
		
	}

	private void initFragment() {
		mFManager = getSupportFragmentManager();
		mLockAppFrag = new LockAppFragment();
		mLockNoAppFrag = new LockNoAppFragment();
	}
	public void comeBack(View view){
		this.finish();
	}
}
