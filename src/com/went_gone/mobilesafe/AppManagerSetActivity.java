package com.went_gone.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.service.WatchDogService;
import com.went_gone.mobilesafe.ui.SettingItemView;
import com.went_gone.mobilesafe.ui.TextCheckBoxView;
import com.went_gone.mobilesafe.utils.ServiceUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

public class AppManagerSetActivity extends BaseActivity {

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_appmanager_set);
		final boolean running = ServiceUtil.isRunning(this, "com.went_gone.mobilesafe.service.WatchDogService");
		TextCheckBoxView tcbv_LockApp = (TextCheckBoxView) findViewById(R.id.act_appmanager_set_Lock);
		tcbv_LockApp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (running) {
					startActivity(new Intent(AppManagerSetActivity.this, LockAppActivity.class));
				}else {
					ToastUtil.show(getApplicationContext(), "请开启程序锁服务");
				}
			}
		});
		
		final SettingItemView siv_LockServer = (SettingItemView) findViewById(R.id.act_appmanager_set_siv_Lock_Server);
		siv_LockServer.setChecked(running);
		siv_LockServer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isChecked = siv_LockServer.isChecked();
				siv_LockServer.setChecked(!isChecked);
				if (!isChecked) {
					startService(new Intent(AppManagerSetActivity.this, WatchDogService.class));
				}else {
					stopService(new Intent(AppManagerSetActivity.this, WatchDogService.class));
				}
			}
		});
	}

	@Override
	protected void initVariables() {

	}

}
