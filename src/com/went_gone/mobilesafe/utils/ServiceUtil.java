package com.went_gone.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 服务的工具类
 * @author Went_Gone
 *
 */
public class ServiceUtil {
	private static ActivityManager mAm;

	/**
	 * @param context	上下文环境
	 * @param serviceName	判断是否正在运行的服务
	 * @return	true正在运行  false没有运行
	 */
	public static boolean isRunning(Context context,String serviceName) {
		//获取activitymanager对象，可以去获取当前手机正在运行的所有的服务
		mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//获取手机中正在运行的服务(多少个服务)(集合)
		List<RunningServiceInfo> runningServices = mAm.getRunningServices(100);
		//遍历集合所有元素，拿到每一个服务的类的名称，和传递过来的类的名称作比对，如果一致则正在运行
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			//获取每一个服务的名称
			if (serviceName.equals(runningServiceInfo.service.getClassName())) {
				return true;
			};
		}
		return false;
	}
}
