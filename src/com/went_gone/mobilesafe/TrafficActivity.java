package com.went_gone.mobilesafe;

import android.net.TrafficStats;
import android.os.Bundle;

import com.went_gone.mobilesafe.base.BaseActivity;

public class TrafficActivity extends BaseActivity {

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_traffic);
		
		//获取流量(R  接受   手机下载流量(2/3/4G网络)  bytes)
		long mobileRxBytes = TrafficStats.getMobileRxBytes();
		
		//所有流量(2/3/4G网络)(T 上传+下载)
		long mobileTxBytes = TrafficStats.getMobileTxBytes();
		
		//获取下载流量的总和   (手机+wifi)
		long totalRxBytes = TrafficStats.getTotalRxBytes();
		
		//获取流量的总和   (手机+wifi)(上传+下载)
		long totalTxBytes = TrafficStats.getTotalTxBytes();
	}

	@Override
	protected void initVariables() {

	}

}
