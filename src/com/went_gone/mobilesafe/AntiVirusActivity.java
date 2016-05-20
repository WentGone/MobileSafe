package com.went_gone.mobilesafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.engine.VirusDao;
import com.went_gone.mobilesafe.entity.ScanInfoEntity;
import com.went_gone.mobilesafe.utils.MD5Util;

/**
 * 手机杀毒
 * @author Went_Gone
 *
 */
public class AntiVirusActivity extends BaseActivity {
	private int SCANNING = 0;
	private int SCAN_FINISH = 1;
	private TextView mTV_AppName;
	private ProgressBar mPB_Scann;
	private LinearLayout mLinLayout;
	private ImageView mIV_Animation;
	private List<ScanInfoEntity> mVirusScanInfoList;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == SCANNING) {
				//显示正在扫描应用的名称
				ScanInfoEntity scanInfo = (ScanInfoEntity) msg.obj;
				mTV_AppName.setText(scanInfo.getAppName());
				//在LinearLayout中添加一个正在扫描的TextView
				TextView textView = new TextView(BaseApplication.getInstance());
				if (scanInfo.isVirus()) {
					//病毒
					textView.setTextColor(Color.RED);
					textView.setText("发现病毒："+scanInfo.getAppName());
				}else {
					textView.setTextColor(Color.BLACK);
					textView.setText("扫描安全："+scanInfo.getAppName());
				}
				mLinLayout.addView(textView, 0);
			}
			if (msg.what == SCAN_FINISH) {
				mTV_AppName.setText("扫描完成");
				//停止扫描动画
				mIV_Animation.clearAnimation();
				unInstallVirusApp();
			}
		};
	};
	private RotateAnimation mRotateAnimation;

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_antivirus);
		mTV_AppName = (TextView) findViewById(R.id.act_antivirus_TV_AppName);
		mPB_Scann = (ProgressBar) findViewById(R.id.act_antivirus_ProgressBar);
		mLinLayout = (LinearLayout) findViewById(R.id.act_antivirus_LinearLayout);
		mIV_Animation = (ImageView) findViewById(R.id.act_antivirus_IV);
		initAnimation();
		checkVirus();
		
	}

	/**
	 * 卸载病毒软件
	 */
	protected void unInstallVirusApp() {
		if (mVirusScanInfoList!=null) {
			for (ScanInfoEntity scanInfo : mVirusScanInfoList) {
				String packagename = scanInfo.getPackagename();
				Intent intent = new Intent("android.intent.action.DELETE");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("package:" + packagename));
				startActivity(intent);
			}
		}
	}

	/**
	 * 检测手机应用病毒的方法
	 */
	private void checkVirus() {
		new Thread(){
			public void run() {
				List<String> virusList = VirusDao.getVirusList();
				//获取签名文件的数组
				// 获取手机上的所有手机应用的签名文件的md5码
				// 获取包的管理者对象
				PackageManager pm = getPackageManager();
				// 获取所有应用签名文件（PackageManager.GET_SIGNATURES
				// 已安装应用的签名文件+PackageManager.GET_UNINSTALLED_PACKAGES 卸载掉应用，剩余的文件）
				List<PackageInfo> packageInfoList = pm
						.getInstalledPackages(PackageManager.GET_SIGNATURES
								+ PackageManager.GET_UNINSTALLED_PACKAGES);
				
				//设置进度条的最大值
				mPB_Scann.setMax(packageInfoList.size());
				
				int index = 0;
				
				mVirusScanInfoList = new ArrayList<ScanInfoEntity>();
				//记录所有应用的集合
				List<ScanInfoEntity> scanInfoList = new ArrayList<ScanInfoEntity>();
				for (PackageInfo packageInfo : packageInfoList) {
					ScanInfoEntity scanInfo = new ScanInfoEntity();
					Signature[] signatures = packageInfo.signatures;
					//获取签名文件的第一位,然后进行md5，将此md5和数据库中的md5比对
					Signature signature = signatures[0];
					String charsString = signature.toCharsString();
					String encoder = MD5Util.encoder(charsString);
//					Log.e("TAG", packageInfo.applicationInfo.loadLabel(pm).toString()+"MD5："+encoder);
					//比对应用是否为病毒
					
					if (virusList.contains(encoder)) {
						//记录病毒
						scanInfo.setVirus(true);
						Log.e("TAG", "有相同  病毒");
						mVirusScanInfoList.add(scanInfo);
					}else {
						scanInfo.setVirus(false);
					}
					//维护对象的包名以及应用
					scanInfo.setPackagename(packageInfo.packageName);
					scanInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
					scanInfoList.add(scanInfo);
					
					//更新进度条
					index++;
					mPB_Scann.setProgress(index);
					
					try {
						Thread.sleep(50+new Random().nextInt(100));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					//更新UI
					Message msg = Message.obtain();
					msg.what = SCANNING;
					msg.obj = scanInfo;
					mHandler.sendMessage(msg);
				}
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	private void initAnimation() {
		mRotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setDuration(1000);
		// 旋转动画一直旋转
//		mRotateAnimation.setRepeatMode(Animation.INFINITE);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		// 保持动画结束后的位置
		mRotateAnimation.setFillAfter(true);
		mIV_Animation.startAnimation(mRotateAnimation);
	}

	@Override
	protected void initVariables() {

	}

}
