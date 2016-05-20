package com.went_gone.mobilesafe.engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.entity.ProcessInfoEntity;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 进程信息的引擎类
 * 
 * @author Went_Gone
 * 
 */
public class ProcessInfoProvider {
	private static ActivityManager mAm;

	// 获取进程总数的方法
	public static int getProcessCount(Context context) {
		mAm = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取正在运行的进程的集合
		List<RunningAppProcessInfo> runningAppProcessList = mAm
				.getRunningAppProcesses();
		// 返回进程总数
		return runningAppProcessList.size();
	}

	/**
	 * @param context
	 * @return 返回可用的内存数 bytes
	 */
	public static long getAvailMemory(Context context) {
		mAm = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 构建存储可用内存的对象
		MemoryInfo memoryInfo = new MemoryInfo();
		// 给memoryInfo对象赋值（可用内存）值
		mAm.getMemoryInfo(memoryInfo);
		// 获取memoryInfo中可用内存的大小
		return memoryInfo.availMem;
	}

	/**
	 * @param context
	 * @return 返回总共的内存数 单位为bytes 0为异常
	 */
	public static long getTotilMemory(Context context) {
		// 最低16
		/*
		 * mAm = (ActivityManager)
		 * context.getSystemService(Context.ACTIVITY_SERVICE); //构建存储可用内存的对象
		 * MemoryInfo memoryInfo = new MemoryInfo(); //给memoryInfo对象赋值（可用内存）值
		 * mAm.getMemoryInfo(memoryInfo); //获取memoryInfo中可用内存的大小 return
		 * memoryInfo.totalMem;
		 */

		// 内存大小 会写入文件中去 读取proc/meminfo文件 读取第一行 获取数字字符，转换成bytes返回
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader("proc/meminfo");
			br = new BufferedReader(fr);
			String readLine = br.readLine();
			// 截取数字 将字符串转化成字符的数组
			char[] charArray = readLine.toCharArray();
			// 循环遍历每一个字符 如果字符的ASCII码0~9的区域内，说明此字符有效
			StringBuffer buffer = new StringBuffer();
			for (char c : charArray) {
				if (c >= '0' && c <= '9') {
					buffer.append(c);
				}
			}
			return Long.parseLong(buffer.toString()) * 1024;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null && fr != null) {
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

	/**
	 * @param context
	 * @return  当前手机正在运行的进程的相关信息
	 */
	public static List<ProcessInfoEntity> getProcessInfo(Context context) {
		List<ProcessInfoEntity> processInfoList = new ArrayList<ProcessInfoEntity>();
		// 获取进程相关信息
		// 得到activityManager对象和packageManager对象
		mAm = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		// 获取正在运行进程的集合
		List<RunningAppProcessInfo> runningAppProcesses = mAm
				.getRunningAppProcesses();
		// 循环遍历上述集合 获取进程相关信息 进程的名称 所在应用的包名 图标 已使用的内存大小 是否为系统进程
		for (RunningAppProcessInfo info : runningAppProcesses) {
			ProcessInfoEntity processInfo = new ProcessInfoEntity();
			// 获取进程的名称 即应用的包名
			processInfo.setPackageName(info.processName);
			// 获取进程占用的内存大小(传递一个进程对应的pid数组)
			android.os.Debug.MemoryInfo[] memoryInfos = mAm
					.getProcessMemoryInfo(new int[] { info.pid });
			// 返回数组中索引位置为0的对象为我们当前进程的内存信息
			android.os.Debug.MemoryInfo memoryInfo = memoryInfos[0];
			// 获取已使用的大小
			long totalPrivateDirty = memoryInfo.getTotalPrivateDirty() * 1024;
			processInfo.setMemory(totalPrivateDirty);
			// 获取应用的名称
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.getPackageName(), 0);
				//获取应用的名称
				processInfo.setName(applicationInfo.loadLabel(pm).toString());
				//获取应用的图标
				processInfo.setIcon(applicationInfo.loadIcon(pm));
				//判断是否为系统进程
				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
					processInfo.setSystem(true);
				}else {
					processInfo.setSystem(false);
				}
			} catch (Exception e) {
				//需要处理
				processInfo.setName(processInfo.getPackageName());
				processInfo.setIcon(context.getResources().getDrawable(R.drawable.splash));
				processInfo.setSystem(true);
				e.printStackTrace();
			}
			processInfoList.add(processInfo);
		}
		return processInfoList;
	}

	/**
	 * 杀死相应进程的方法
	 * @param context
	 * @param processInfo   进程对象（实体类）
	 */
	public static void killProcess(Context context,ProcessInfoEntity processInfo) {
		//1 获取ActivityManager管理者对象
		mAm = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		//杀死指定包名的进程（权限）
		mAm.killBackgroundProcesses(processInfo.getPackageName());
	}

}
