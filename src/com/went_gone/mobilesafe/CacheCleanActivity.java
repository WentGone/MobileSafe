package com.went_gone.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.entity.CacheInfoEntity;

/**
 * 清理缓存的界面
 * 
 * @author Went_Gone
 * 
 */
public class CacheCleanActivity extends BaseActivity {
	private ProgressBar pb;
	private PackageManager mPm;
	private LinearLayout mLinearLayout;
	private static final int UPDATE_CACHE_APP = 0x00;
	private static final int CACHE_APP_NAME = 0x01;
	private static final int FINISH_CACHE_APP = 0x02;
	private static final int CLEAN_ALL_CACHE = 0x03;
	private TextView mTV_AppName;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_CACHE_APP:
				View view = View.inflate(BaseApplication.getInstance(),
						R.layout.item_cache_info, null);
				ImageView iv_Icon = (ImageView) view
						.findViewById(R.id.item_cache_info_IV_icon);
				TextView tv_name = (TextView) view
						.findViewById(R.id.item_cache_info_TV_appname);
				TextView tv_cache = (TextView) view
						.findViewById(R.id.item_cache_info_TV_cacheSize);
				ImageView iv_delete = (ImageView) view
						.findViewById(R.id.item_cache_info_IV_Delete);

				final CacheInfoEntity cacheInfo = (CacheInfoEntity) msg.obj;
				iv_Icon.setBackgroundDrawable(cacheInfo.getIcon());
				tv_name.setText(cacheInfo.getAppName());
				tv_cache.setText(Formatter.formatFileSize(
						getApplicationContext(), cacheInfo.getCacheSize()));
				iv_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//清除指定应用的额缓存
						deleteCacheByPackageName(cacheInfo.getPackageame());
					}
				});
				
				mLinearLayout.addView(view, 0);
				break;
			case CACHE_APP_NAME:
				String appname = (String) msg.obj;
				mTV_AppName.setText(appname);
				break;
			case FINISH_CACHE_APP:
				mTV_AppName.setText("扫描完成");
				break;
			case CLEAN_ALL_CACHE:
				// 移除所有条目。
				mLinearLayout.removeAllViews();
				break;
			}
		};
	};

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_cache_clean);
		Button mBtnClean = (Button) findViewById(R.id.act_cache_clean_Btn_Clean);
		mBtnClean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cleanAllCache();
			}
		});
		pb = (ProgressBar) findViewById(R.id.act_cache_clean_ProgressBar);
		mTV_AppName = (TextView) findViewById(R.id.act_cache_clean_TV_appname);
		mLinearLayout = (LinearLayout) findViewById(R.id.act_cache_clean_Linearlayout_cache);
	}

	/**
	 * 清理所有缓存
	 */
	protected void cleanAllCache() {
		try {
			Class<?> clazz = Class.forName("android.content.pm.PackageManager");
			Method method = clazz.getMethod("freeStorageAndNotify", long.class,
					IPackageDataObserver.class);
			// 获取对象，调用方法
			method.invoke(mPm, Long.MAX_VALUE, new IPackageDataObserver.Stub() {

				@Override
				public void onRemoveCompleted(String packageName,
						boolean succeeded) throws RemoteException {
					// 清除缓存完成后调用的方法（权限）
					mHandler.sendEmptyMessage(CLEAN_ALL_CACHE);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过包名清理缓存
	 * @param packagename
	 */
	private void deleteCacheByPackageName(String packagename) {
		/*
		 * 以下代码需要系统应用才可成功
		 * <uses-permission android:name="android.permission.DELETE_CACHE_FILES" />
		 * try {
			Class<?> clazz = Class.forName("android.content.pm.PackageManager");
			Method method = clazz.getMethod("deleteApplicationCacheFiles", String.class,
					IPackageDataObserver.class);
			// 获取对象，调用方法
			method.invoke(mPm, packagename, new IPackageDataObserver.Stub() {

				@Override
				public void onRemoveCompleted(String packageName,
						boolean succeeded) throws RemoteException {
					// 清除缓存完成后调用的方法 子线程中
//					mHandler.sendEmptyMessage(CLEAN_ALL_CACHE);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
		Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:"+packagename));
		startActivity(intent);
	}

	
	@Override
	protected void initVariables() {
		mPm = getPackageManager();
		// 遍历手机所有的应用
		initAppDatas();
	}

	/**
	 * 遍历手机所有的应用。获取有缓存的应用
	 */
	private void initAppDatas() {
		new Thread() {

			public void run() {
				// 获取安装在手机上所有应用
				List<ApplicationInfo> appInfoList = mPm
						.getInstalledApplications(0);
				pb.setMax(appInfoList.size());// 进度条最大值
				// 遍历每一个应用，获取有缓存的应用(图标，名称，缓存大小，包名)
				int index = 0;
				for (ApplicationInfo appInfo : appInfoList) {
					// 获取缓存信息的条件
					String packageName = appInfo.packageName;
					getPackageCache(packageName);
					index++;
					pb.setProgress(index);
					try {
						Thread.sleep(50 + new Random().nextInt(100));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.what = CACHE_APP_NAME;
					String name = null;
					try {
						name = mPm.getApplicationInfo(packageName, 0)
								.loadLabel(mPm).toString();
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					msg.obj = name;
					mHandler.sendMessage(msg);
				}
				mHandler.sendEmptyMessage(FINISH_CACHE_APP);
			};
		}.start();

	}

	/**
	 * 指定包名应用的缓存信息
	 * 
	 * @param packageName
	 *            包名
	 */
	protected void getPackageCache(String packageName) {

		IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
			public void onGetStatsCompleted(PackageStats stats,
					boolean succeeded) {
				// 缓存大小的过程,子线程中的代码，不能更新UI
				long cacheSize = stats.cacheSize;
				// 判断缓存大小是否大于0
				CacheInfoEntity cacheInfo = null;
				if (cacheSize > 0) {
					// 有缓存,告知主线程更新UI
					Message msg = Message.obtain();
					msg.what = UPDATE_CACHE_APP;
					try {
						cacheInfo = new CacheInfoEntity();
						cacheInfo.setCacheSize(cacheSize);
						cacheInfo.setPackageame(stats.packageName);
						cacheInfo.setAppName(mPm
								.getApplicationInfo(stats.packageName, 0)
								.loadLabel(mPm).toString());
						cacheInfo.setIcon(mPm.getApplicationInfo(
								stats.packageName, 0).loadIcon(mPm));
					} catch (Exception e) {
						e.printStackTrace();
					}
					msg.obj = cacheInfo;
					mHandler.sendMessage(msg);
				}
			}
		};

		// PackageManager中getPackageSizeInfo方法是隐藏的不能被调用，需要反射
		// mPm.getPackageSizeInfo(getPackageName(), mStatsObserver);
		// 获取指定类字节码文件
		try {
			Class<?> clazz = Class.forName("android.content.pm.PackageManager");
			Method method = clazz.getMethod("getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			// 获取对象，调用方法
			method.invoke(mPm, packageName, mStatsObserver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
