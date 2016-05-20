package com.went_gone.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.net.HttpConnection;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.StreamUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {
	private static final String TAG = "Splash";
	/**
	 * 更新版本的状态码
	 */
	private static final int UPDATE_VERSION = 100;
	/**
	 * 进入主界面的状态码
	 */
	private static final int ENTER_HOME = 101;
	protected static final int URL_EXCEPTION = 102;
	protected static final int IO_EXCEPTION = 103;
	protected static final int JSON_EXCEPTION = 104;
	private static int mProgressCount = 100;
	private TextView mTV_Version,mTv_Progress;
	private int mLocalVersionCode;// 本地版本号
	private int mHttpVersionCode;
	private String mHttpVersionName;
	private ProgressBar mPBarProgress;
	private String apkUrl;
	private String des;
	private LinearLayout mLinearProgressLayout;
	private RelativeLayout splash_root;


	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_VERSION:
				// 弹出对话框，提示用户更新
				showUpdateDialog();
				break;
			case ENTER_HOME:
				// 进入应用主界面
				enterHome();
				break;
			case URL_EXCEPTION:
				// URL异常
				ToastUtil.show(getApplicationContext(), "Url异常");
				enterHome();
				break;
			case IO_EXCEPTION:
				//读取数据异常  即网络
				ToastUtil.show(getApplicationContext(), "io异常");				
				enterHome();
				break;
			case JSON_EXCEPTION:
				// json异常
				ToastUtil.show(getApplicationContext(), "json异常");				
				enterHome();
				break;
			default:
				break;
			}
		};
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAnimation();
	}

	/**
	 * 创建动画    
	 */
	private void initAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
		alphaAnimation.setDuration(1500);
		splash_root.setAnimation(alphaAnimation);
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_splash);
		mTV_Version = (TextView) findViewById(R.id.Splash_Tv_version);
		mTv_Progress = (TextView) findViewById(R.id.Splash_Tv_Progress);
		mPBarProgress = (ProgressBar) findViewById(R.id.Splash_progressBar_Progress);
		mLinearProgressLayout = (LinearLayout) findViewById(R.id.Splash_Linear_Progress);
		splash_root = (RelativeLayout) findViewById(R.id.Splash_root);
	}
	@Override
	protected void initVariables() {
		mTV_Version.setText("版本：" + getVersionName());
		// 从本地获取版本号
		mLocalVersionCode = getVersionCode();
		// 从服务器获取版本号
		if (SpUtil.getBoolean(BaseApplication.getInstance(),ConstantValue.OPEN_UPDATE,false)) {			
			checkVersion();
		}else {
			//直接进入应用程序主界面
			mHandler.sendEmptyMessageDelayed(ENTER_HOME,3000);
		}
		initDataBase();
	}
	/**
	 * 初始化数据库
	 */
	private void initDataBase() {
		//1.归属地查询的数据库的拷贝过程
		initDBFromAssets("address.db");
		//常用号码的数据库的拷贝过程
		initDBFromAssets("commonnum.db");
		//拷贝反病毒数据库
		initDBFromAssets("antivirus.db");
	}

	/**
	 * 拷贝数据库到Files文件夹下
	 * @param dbName  数据库名称
	 */
	private void initDBFromAssets(String dbName) {
		//在files文件夹下创建同名数据库文件的过程
		File files = getFilesDir();
		File file = new File(files,dbName);
		//如果文件夹存在  那么不执行下边代码
		if (file.exists()) {
			return;
		}
		InputStream stream = null;
		FileOutputStream fos = null;
		//读取第三方资产目录下的文件
		try {
			//输入流
			stream = getAssets().open(dbName);
			//将读取的内容写入到指定文件夹的文件中去
			fos = new FileOutputStream(file);
			//每次读取内容大小
			byte[] bs = new byte[1024];
			int temp = -1;
			while ((temp = stream.read(bs))!=-1) {
				fos.write(bs,0,temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (stream!=null&&fos!=null) {
				try {
					stream.close();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 *弹出对话框，提示用户更新 
	 */
	protected void showUpdateDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("版本更新");
		builder.setMessage(des);//设置描述内容
		//积极按钮
		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//下载apk，apkUrl
				downLoadApk();
			}
		});
		
		//消极按钮
		builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				enterHome();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				enterHome();
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 *下载新版本apk 
	 */
	protected void downLoadApk() {
		//使用xUtils
		//apk的下载地址   apk的存在路径
		
		//判断sd卡是否可用，是否挂载上
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			//如果挂载上  获取sd卡的路径
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mobileSafe.apk";
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(apkUrl, path, new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					//下载后的放置在sd卡的apk文件
					File file = responseInfo.result;
					//提示用户安装应用
					installApk(file);
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onStart() {
					super.onStart();
					//开始下载
				}
				@Override
				public void onLoading(long total, long current,boolean isUploading) {
					mLinearProgressLayout.setVisibility(View.VISIBLE);
					//下载过程中   总大小   当前位置   是否正在下载
					super.onLoading(total, current, isUploading);
					mTv_Progress.setText((int)(current*1.0f/total*100)+"%");
					mPBarProgress.setProgress((int)(current*1.0f/total*100));
				}
			});
		}
	}

	/**
	 * 安装应用
	 * @param file
	 */
	protected void installApk(File file) {
		//调用系统应用安装apk    packageInstaller.apk
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		//文件作为数据源  intent.setData(Uri.fromFile(file));
		//设置安装类型 intent.setType("application/vnd.android.package-archive");
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		startActivityForResult(intent, 123);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		enterHome();
	}
	

	/**
	 * 进入应用程序主界面
	 */
	protected void enterHome() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 检验服务器版本
	 */
	private void checkVersion() {
		final Message msg = Message.obtain();
		new Thread() {
			public void run() {
				final long startTime = System.currentTimeMillis();
				try {
					URL url = new URL("http://192.168.1.101:8080/html/versionInfo.html");
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					con.setConnectTimeout(2000);
					con.setRequestMethod("GET");
					int requestCode = con.getResponseCode();
					if (requestCode == 200) {
						// 连接成功
						InputStream is = con.getInputStream();
						String json = StreamUtil.stream2String(is);
						JSONObject object = new JSONObject(json);
						mHttpVersionCode = object.getInt("versionCode");
						mHttpVersionName = object.getString("versionName");
						apkUrl = object.getString("apkurl");
						des = object.getString("des");

						if (mLocalVersionCode < mHttpVersionCode) {
							// 提示用户更新版本，弹出对话框（更新UI）
							msg.what = UPDATE_VERSION;
						} else {
							// 进入应用程序主界面
							msg.what = ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = URL_EXCEPTION;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					msg.what = IO_EXCEPTION;
					e.printStackTrace();
				} catch (JSONException e1) {
					msg.what = JSON_EXCEPTION;
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					//确保可以睡够4s
					long endTime = System.currentTimeMillis();
					if ((endTime - startTime) < 3000) {
						/*try {
							Thread.sleep(4000 - (endTime - startTime));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						SystemClock.sleep(3000 - (endTime - startTime));
					}
					 mHandler.sendMessage(msg);
				}
			}
		}.start();
	}

	/**
	 * 检验版本HttpUtils
	 */
	private void checkVersionUtils() {
		final Message msg = Message.obtain();
		final long startTime = System.currentTimeMillis();

		HttpConnection.getInstance().httpGet(
				"http://192.168.1.101:8080/html/versionInfo.html",
				new com.went_gone.mobilesafe.net.HttpCallBack() {

					@Override
					public void onSuccess(ResponseInfo<String> response) {
						Log.e(TAG, response.result);
						try {
							JSONObject object = new JSONObject(response.result);
							mHttpVersionCode = object.getInt("versionCode");
							mHttpVersionName = object.getString("versionName");
							apkUrl = object.getString("apkurl");
							des = object.getString("des");

							if (mLocalVersionCode < mHttpVersionCode) {
								// 提示用户更新版本，弹出对话框（更新UI）
								msg.what = UPDATE_VERSION;
							} else {
								// 进入应用程序主界面
								msg.what = ENTER_HOME;
							}
						} catch (JSONException e) { 
						} finally {
							long endTime = System.currentTimeMillis();
							if ((endTime - startTime) < 4000) {
								try {
									Thread.sleep(4000 - (endTime - startTime));
								} catch (InterruptedException e) { 
									e.printStackTrace();
								}
							} //
							mHandler.sendMessage(msg);
						}
					}
				});

		/*
		 * new Thread() { public void run() {
		 * 
		 * } }.start();
		 */
	}

	/**
	 * 返回本地版本号的方法
	 * 
	 * @return 返回0 有异常
	 */
	private int getVersionCode() {
		PackageManager manager = getPackageManager();
		// 从包管理者对象中，获取指定包名的基本信息（版本名称，版本号）。传0代表基本信息
		try {
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(),
					0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取版本名称：清单文件中
	 * 
	 * @return
	 */
	private String getVersionName() {
		PackageManager manager = getPackageManager();
		// 从包管理者对象中，获取指定包名的基本信息（版本名称，版本号）。传0代表基本信息
		try {
			PackageInfo packageInfo = manager.getPackageInfo(getPackageName(),
					0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
