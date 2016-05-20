package com.went_gone.mobilesafe.service;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.SpUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;

public class LocationService extends Service {
	@Override
	public void onCreate() {
		super.onCreate();
		// 获取经纬度坐标（LocationManager）
		// 获取位置的管理者对象
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//以最优的方式获取经纬度坐标()
		Criteria criteria = new Criteria();
		//允许花费
		criteria.setCostAllowed(true);
		//指定获取经纬度的精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lm.getBestProvider(criteria,true);
		//在一定时间间隔或者移动一段距离后获取经纬度的坐标
		// 通过对象获取经纬度坐标(定位方式;minTime 获取经纬度坐标的最小间隔时间，0时时刻刻；minDistance
		// 移动的最小间距，0时时刻刻；位置的事件监听)
		lm.requestLocationUpdates(bestProvider, 0, 0,
				new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// gps在状态发生切换的时候的监听
					}

					@Override
					public void onProviderEnabled(String provider) {
						// 当gps开启的时候的事件监听
					}

					@Override
					public void onProviderDisabled(String provider) {
						// 当gps关闭的时候的事件监听
					}

					@Override
					public void onLocationChanged(Location location) {
						// 当位置发生改变的监听
						// 经度
						double longitude = location.getLongitude();
						// 纬度
						double latitude = location.getLatitude();
						
						//发送短信
						SmsManager smsManager = SmsManager.getDefault();
						//目标地址，短信中心(在中国为null)，送达的意图，送达后返回的意图
						String sendAddress = SpUtil.getString(BaseApplication.getInstance(),ConstantValue.CONTACT_PHONE, "");
						String msg = "经度："+longitude+"     纬度："+latitude;
						if (!TextUtils.isEmpty(sendAddress)) {
							smsManager.sendTextMessage(sendAddress,null,msg,null,null);
						}
						
						//最好有地图，可查看
					}
				});
	}

	/*
	 * @Override public int onStartCommand(Intent intent, int flags, int
	 * startId) { // TODO Auto-generated method stub return
	 * super.onStartCommand(intent, flags, startId); }
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
