package com.went_gone.mobilesafe.service;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.ToastLocationActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.engine.AddressDao;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.R.xml;
import android.app.DownloadManager.Query;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {
	private TelephonyManager mTm;
	private MyPhoneStateListener mPhoneStateListener;
	private WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private View mViewToast;
	private WindowManager mWm;
	private TextView mTV_Toast_Address;
	private String mAddress;
	private int[] mStyleColors;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mTV_Toast_Address.setText(mAddress);
		};
	};
	private int mStyleId;
	private InnerOutCallReceiver mCallReceiver;

	@Override
	public void onCreate() {
		mStyleColors = new int[] { R.color.transparent_white,
				R.color.transparent_blue, R.color.transparent_red,
				R.color.transparent_black, R.color.transparent_green };

		mWm = (WindowManager) getSystemService(WINDOW_SERVICE);

		// 第一次开启服务以后，管理显示
		// 电话状态的接听

		// 服务开启的时候监听 关闭的时候不需要监听
		mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 监听电话状态
		mPhoneStateListener = new MyPhoneStateListener();
		mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		//监听播出电话的广播的过滤条件(权限)
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		mCallReceiver = new InnerOutCallReceiver();
		registerReceiver(mCallReceiver, intentFilter);
		
		super.onCreate();
	}
	
	class InnerOutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//获取播出电话号码的字符串
			String phone = getResultData();
			showToast(phone);
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		// 销毁显示 取消对电话的监听
		if (mTm != null && mPhoneStateListener != null) {
			mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		if (mCallReceiver!=null) {
			//去电广播接受者的注销
			unregisterReceiver(mCallReceiver);
		}
		super.onDestroy();
	}

	class MyPhoneStateListener extends PhoneStateListener {
		// 手动重写电话状态发生改变会触发的方法
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (mViewToast != null && mWm != null) {
					mWm.removeView(mViewToast);
				}
				// 空闲状态(移除展示)
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// 摘机状态，拨打或通话中
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				// 来电状态(展示)
				showToast(incomingNumber);

				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	/**
	 * 展示一个Toast，显示来电号码
	 * 
	 * @param incomingNumber
	 *            来电号码
	 */
	public void showToast(String incomingNumber) {
		mStyleId = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);

		// 读取Sp中Toast的Left与Top的值

		final WindowManager.LayoutParams params = mParams;

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		// WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 不可点击
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// params.windowAnimations =
		// com.android.internal.R.style.Animation_Toast;//维护窗体显示的一个动画，不需要动画可删掉

		// 在响铃的时候显示Toast,和电话类型一致
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("Toast");

		// 指定Toast的所在位置(将Toast指定在左上角)
		params.gravity = Gravity.LEFT + Gravity.TOP;
		// params.gravity = Gravity.CENTER;

		// 获取SpUtil的left,top
		int left = SpUtil.getInt(this, ConstantValue.LEFT, 0);
		int top = SpUtil.getInt(this, ConstantValue.TOP, 0);
		// params.x为吐司左上角的x的坐标
		params.x = left;
		params.y = top;

		// Toast显示效果(Toast的布局文件),将Toast挂载到windowsManager窗体上
		mViewToast = View.inflate(this, R.layout.toast_layout, null);
		mViewToast.setBackgroundColor(getResources().getColor(
				mStyleColors[mStyleId]));
		mTV_Toast_Address = (TextView) mViewToast
				.findViewById(R.id.toast_layout_TV);

		// 添加拖拽
		mViewToast.setOnTouchListener(new OnTouchListener() {

			private int start_RawX;
			private int start_RawY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					start_RawX = (int) event.getRawX();
					start_RawY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					int move_RawX = (int) event.getRawX();
					int move_RawY = (int) event.getRawY();

					int disRawX = move_RawX - start_RawX;
					int disRawY = move_RawY - start_RawY;

					params.x = params.x+disRawX;
					params.y = params.y+disRawY;
					// 容错处理
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > mWm.getDefaultDisplay().getWidth()
							- mViewToast.getWidth()) {
						params.x = mWm.getDefaultDisplay().getWidth()
								- mViewToast.getWidth();
					}
					if (params.y > mWm.getDefaultDisplay().getHeight()
							- mViewToast.getHeight()) {
						params.y = mWm.getDefaultDisplay().getHeight()
								- mViewToast.getHeight();
					}

					// 告知窗体需要安装手势的移动，做位置的更新
					mWm.updateViewLayout(mViewToast, params);

					// 重置起始坐标
					start_RawX = (int) event.getRawX();
					start_RawY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_UP:
					SpUtil.putInt(AddressService.this, ConstantValue.LEFT,
							params.x);
					SpUtil.putInt(AddressService.this, ConstantValue.TOP,
							params.y);
					break;
				}
				return true;
			}
		});

		// 在窗体上挂载一个View的api(权限)
		mWm.addView(mViewToast, params);

		query(incomingNumber);
	}

	/**
	 * 用于查询来电归属地,告知应用显示归属地
	 * 
	 * @param incomingNumber
	 */
	private void query(final String incomingNumber) {
		new Thread() {
			public void run() {
				mAddress = AddressDao.getAddress(incomingNumber);
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}
}
