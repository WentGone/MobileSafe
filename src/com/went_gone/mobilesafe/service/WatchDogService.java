package com.went_gone.mobilesafe.service;

import java.util.List;

import com.went_gone.mobilesafe.EnterPsdActivity;
import com.went_gone.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

/**
 * 看门狗的服务
 * @author Went_Gone
 *
 */
public class WatchDogService extends Service {
	private boolean isWacth;
	private AppLockDao mDao;
	private List<String> mLockAppPackageNames;
	private MyBroadCast myBroadCast;
	private String mSkipPackagename = "";
	private MyContentObserver mObserver;

	@Override
	public void onCreate() {
		mDao = AppLockDao.getInstance();
		//维护一个看门狗的死循环，让其时刻循环监测现在开启的应用是否为程序锁中要去拦截的应用
		isWacth = true;
		watch();
		myBroadCast = new MyBroadCast();
		IntentFilter filter = new IntentFilter("com.went_gone.action.SKIP");
		registerReceiver(myBroadCast, filter);
		
		//注册一个内容观察者，观察一个数据库的变化，一旦数据库有删除或者添加，则需要让集合重新获取一次数据
		mObserver = new MyContentObserver(new Handler());
		getContentResolver().registerContentObserver(Uri.parse("content://applock/change"), true,mObserver);
		super.onCreate();
	}
	class MyContentObserver extends ContentObserver{

		public MyContentObserver(Handler handler) {
			super(handler);
		}
		//一旦数据库发生改变调用的方法，重新获取包名所在集合的数据
		@Override
		public void onChange(boolean selfChange) {
			new Thread(){
				public void run() {
					mLockAppPackageNames = mDao.selectAll();
				};
			}.start();
			super.onChange(selfChange);
		}
		
	}
	private void watch() {
		//在子线程中开启一个可控的死循环
		new Thread(){
			public void run() {
				mLockAppPackageNames = mDao.selectAll();
				while (isWacth) {
					//监测正在开启的应用，任务栈
					//获取ActivityManager对象
					ActivityManager mAm = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					//获取正在运行任务栈的方法
					List<RunningTaskInfo> runningTasks = mAm.getRunningTasks(1);
					RunningTaskInfo runningTaskInfo = runningTasks.get(0);//获取正在开启的应用的任务栈
					//获取栈顶的Activity  拿到此Activity的所在应用的包名
					String packageName = runningTaskInfo.topActivity.getPackageName();
					//拿此包名在数据库中作比对
					if (mLockAppPackageNames.contains(packageName)) {
						//如果监测的应用已经解锁不需要拦截界面
						if (!packageName.equals(mSkipPackagename)) {
							//弹出拦截界面    
							Intent intent = new Intent(getApplicationContext(),EnterPsdActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//新建一个任务栈
							intent.putExtra("packagename", packageName);
							startActivity(intent);
						}
					}
					//睡眠一下  200毫秒  时间片轮转
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		//注销广播接受者
		if (myBroadCast!=null) {
 			unregisterReceiver(myBroadCast);
		}
		isWacth = false;//停止看门狗的循环
		//注销内容观察者
		if (mObserver!=null) {
			getContentResolver().unregisterContentObserver(mObserver);
		}
		super.onDestroy();
	}
	
	public boolean isWacth() {
		return isWacth;
	}

	public void setWacth(boolean isWacth) {
		this.isWacth = isWacth;
	}
	
	class MyBroadCast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			mSkipPackagename = intent.getStringExtra("packagename");
		}
		
	}
}
