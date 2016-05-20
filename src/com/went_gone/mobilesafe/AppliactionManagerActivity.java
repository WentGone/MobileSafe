package com.went_gone.mobilesafe;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.went_gone.mobilesafe.adapter.AppInfoAdapter;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.engine.AppInfoProvider;
import com.went_gone.mobilesafe.entity.AppInfoEntity;
import com.went_gone.mobilesafe.utils.ObjectStoreUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

/**
 * 应用管理
 * 
 * @author Went_Gone
 * 
 */
public class AppliactionManagerActivity extends BaseActivity {
	private String mSDPath, mMobilePath;
	private String mMobileMemorySize,mSDMemorySize;
	private TextView mTV_SDMemory,mTV_MobileMemory;
	private List<AppInfoEntity> mAppInfoList;
	private ListView mLV_AppInfo;
	private AppInfoAdapter mAdapter;
	private List<AppInfoEntity> mCustomerAppInfoList;
	private List<AppInfoEntity> mSystemAppInfoList;
	private AppInfoEntity mAppInfo;
	private TextView mTV_Title;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mAdapter = new AppInfoAdapter(mAppInfoList, mCustomerAppInfoList, mSystemAppInfoList, AppliactionManagerActivity.this);
			mLV_AppInfo.setAdapter(mAdapter);
		}
	};
	
	
	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_application_manager);
		initMemoryViews();
		initListView();
		initLockApp();
	}


	/**
	 * 应用加锁
	 */
	private void initLockApp() {
		ImageView iv_Setting = (ImageView) findViewById(R.id.act_application_manager_IV_Setting);
		iv_Setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AppliactionManagerActivity.this, AppManagerSetActivity.class));
			}
		});
	}


	/**
	 * 初始化展示应用的列表
	 */
	private void initListView() {
		//初始化常驻悬浮标题栏
		mTV_Title = (TextView) findViewById(R.id.act_application_manager_TV_Title);
		
		mLV_AppInfo = (ListView) findViewById(R.id.act_application_manager_LV);
		mLV_AppInfo.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mCustomerAppInfoList!=null&&mSystemAppInfoList!=null) {
					if (mTV_Title.getVisibility() == view.INVISIBLE) {
						mTV_Title.setVisibility(View.VISIBLE);
					}
					if (firstVisibleItem>=mCustomerAppInfoList.size()+1) {
						//顶部描述显示系统应用
						mTV_Title.setText("系统应用("+mSystemAppInfoList.size()+")");
					}else {
						//顶部描述显示用户应用
						mTV_Title.setText("用户应用("+mCustomerAppInfoList.size()+")");
					}
				}else {
					if (mTV_Title.getVisibility() == view.VISIBLE) {
						mTV_Title.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
		
		mLV_AppInfo.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || position == mCustomerAppInfoList.size() + 1) {
					// 标题条目
					return;
				} else {
					// 用户应用信息条目
					if (position < mCustomerAppInfoList.size() + 1) {
						mAppInfo = mCustomerAppInfoList.get(position - 1);
						Intent intent = new Intent(AppliactionManagerActivity.this, AppInfoActivity.class);
//						intent.putExtra("appInfo", mAppInfo);
						intent.putExtra("appName", mAppInfo.getName());
						String[] perStrings = new String[mAppInfo.getPermission().size()];
						for (int i = 0; i <  mAppInfo.getPermission().size(); i++) {
							perStrings[i] = mAppInfo.getPermission().get(i);
						}
						intent.putExtra("appPermissions", perStrings);
						intent.putExtra("packageName", mAppInfo.getPackageName());
						Bitmap bmp = mAppInfo.getBitmap();
						ByteArrayOutputStream baos=new ByteArrayOutputStream();  
						bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);  
						byte [] bitmapByte =baos.toByteArray();  
						intent.putExtra("bitmap", bitmapByte);  
//						ObjectStoreUtil.saveObject("appInfo.dtt", mAppInfo);
						startActivityForResult(intent, 0x00);
					} else {
						// 使用系统应用的集合
						mAppInfo = mSystemAppInfoList.get(position - 2
								- mCustomerAppInfoList.size());
						ToastUtil.show(AppliactionManagerActivity.this,"系统应用无法删除");
					}
					
				}				
			}
		});
	}

	/**
	 * 初始化显示内存的View
	 */
	private void initMemoryViews() {
		// 获取手机内存大小
		initMobileMemory();
		// 获取内存卡内存大小
		initSDMemory();
	}

	/**
	 * 初始化内存卡内存
	 */
	private void initSDMemory() {
		mTV_SDMemory = (TextView) findViewById(R.id.act_application_manager_TV_sd_memory);
		mSDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 初始化手机内存
	 */
	private void initMobileMemory() {
		mTV_MobileMemory = (TextView) findViewById(R.id.act_application_manager_TV_memory);
		mMobilePath = Environment.getDataDirectory().getAbsolutePath();// 获取手机内存的路径
	}

	@Override
	protected void initVariables() {
		initAppsData();
		
		mMobileMemorySize = Formatter.formatFileSize(this,getAvailSpace(mMobilePath));
		mSDMemorySize = Formatter.formatFileSize(this,getAvailSpace(mSDPath));
		
		mTV_MobileMemory.setText("磁盘可用空间："+mMobileMemorySize);
		mTV_SDMemory.setText("sd卡可用空间："+mSDMemorySize);
	}


	private void initAppsData() {
		new Thread(){
			public void run() {
				mAppInfoList = AppInfoProvider.getAppListInfo(BaseApplication.getInstance());
				//用户应用集合
				mCustomerAppInfoList = new ArrayList<AppInfoEntity>();
				//系统应用集合
				mSystemAppInfoList = new ArrayList<AppInfoEntity>();
				for (AppInfoEntity appInfo : mAppInfoList) {
					if (appInfo.isSystem()) {
						//系统应用
						mSystemAppInfoList.add(appInfo);
					}else {
						//用户应用
						mCustomerAppInfoList.add(appInfo);
					}
				}
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

	@Override
	protected void onResume() {
		initAppsData();
		super.onResume();
	}
	/**
	 * 获取可用空间
	 * 返回结果为byte = 8bit，int类型的最大结果为2147483647 bytes 相当于2G
	 * @param path
	 */
	private long getAvailSpace(String path) {
		//此类可以获取磁盘可用大小
		StatFs statFs = new StatFs(path);
		//获取可用区块的个数
		long count = statFs.getAvailableBlocks();
		//获取区块的大小
		long size = statFs.getBlockSize();
		//可用空间= 可用区块的个数*获取区块的个数
		return count*size;
	}
}
