package com.went_gone.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.db.dao.BlackNumberDao;
import com.went_gone.mobilesafe.service.BlackNumberService;
import com.went_gone.mobilesafe.ui.SettingItemView;
import com.went_gone.mobilesafe.utils.ServiceUtil;

/**
 * 拦截设置
 * @author Went_Gone
 *
 */
public class CallMsgSafeSetting extends BaseActivity {
	private SettingItemView mSIV_InterceptSetting;
	private SettingItemView mSIV_BlackList;
	private BlackNumberDao mDao;
	private int mCount;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mSIV_BlackList.setArrowRightText(mCount+"个");
		};
	};

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_callmsgsafe_setting);
		initBlackList();
		initBlackNumberService();
	}

	/**
	 * 初始化骚扰拦截服务
	 */
	private void initBlackNumberService() {
		mSIV_InterceptSetting = (SettingItemView) findViewById(R.id.siv_CMS_Setting_Intercept);
		boolean isRunning = ServiceUtil.isRunning(CallMsgSafeSetting.this, "com.went_gone.mobilesafe.service.BlackNumberService");
		mSIV_InterceptSetting.setChecked(isRunning);
		mSIV_InterceptSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//切换选择/未选中状态
				boolean checked = mSIV_InterceptSetting.isChecked();
				mSIV_InterceptSetting.setChecked(!checked);
				if (!checked) {
					//开启服务
					startService(new Intent(CallMsgSafeSetting.this, BlackNumberService.class));
				}else {
					//关闭服务
					stopService(new Intent(CallMsgSafeSetting.this, BlackNumberService.class));
				}
			}
		});
	}

	/**
	 * 初始化黑名单
	 */
	private void initBlackList() {
		mSIV_BlackList = (SettingItemView) findViewById(R.id.siv_CMS_Setting_BlackList);
//		siv_BlackList.setArrowRightText("0个");
		mSIV_BlackList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//进入黑名单
				enterBlackList();
			}
		});
	}

	/**
	 * 进入黑名单
	 */
	protected void enterBlackList() {
		startActivity(new Intent(CallMsgSafeSetting.this,BlackListActivity.class));
	}

	@Override
	protected void initVariables() {
		mDao = BlackNumberDao.getInstance();
		getBlackNumberCount();
	}

	/**
	 * 获得黑名单的个数
	 */
	private void getBlackNumberCount() {
		new Thread(){
			public void run() {
				mCount = mDao.getCount();
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

}
