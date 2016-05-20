package com.went_gone.mobilesafe.receiver;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.service.LocationService;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * 监听短信内容
 * 
 * @author Went_Gone
 * 
 */
public class SmsReceiver extends BroadcastReceiver {
	DevicePolicyManager mDPM = (DevicePolicyManager) BaseApplication.getInstance().getSystemService(Context.DEVICE_POLICY_SERVICE);

	@Override
	public void onReceive(Context context, Intent intent) {
		ToastUtil.show(context, "你好吗？");
		// 判断是否开启了防盗保护
		boolean setup_over = SpUtil.getBoolean(context,
				ConstantValue.SETUP_OVER, false);
		if (setup_over) {
			ComponentName componentName = new ComponentName(context,DeviceAdmin.class);
			// 获取短信内容
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			// 循环遍历短信数组
			for (Object object : objects) {
				// 获取短信对象
				SmsMessage pdu = SmsMessage.createFromPdu((byte[]) object);
				// 获取短信对象的基本信息
				String originatingAddress = pdu.getOriginatingAddress();
				String messageBody = pdu.getMessageBody();

				// 判断是否包含播放音乐的关键字
				if (messageBody.contains(BaseApplication.getInstance()
						.getResources().getString(R.string.alarm_password))) {
					// 播放音乐(准备音乐，MediaPlayer)
					MediaPlayer player = MediaPlayer
							.create(context, R.raw.ylzs);
					player.setLooping(true);
					player.start();
				}
				if (messageBody.contains(BaseApplication.getInstance()
						.getResources().getString(R.string.location_password))) {
					// 开启获取位置的服务
					context.startService(new Intent(context,
							LocationService.class));
				}
				if (messageBody.contains(BaseApplication.getInstance()
						.getResources().getString(R.string.lock_password))) {
					if (mDPM.isAdminActive(componentName)) {
						//锁屏
						mDPM.lockNow();
						mDPM.resetPassword("962464", 0);//重置密码
					}
				}
				if (messageBody.contains(BaseApplication.getInstance()
						.getResources().getString(R.string.destory_password))) {
					if (mDPM.isAdminActive(componentName)) {
						//清除数据
//						mDPM.wipeData(0);
						//mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//清除手机sd卡上的数据
					}
				}
			}
		}
	}

}
