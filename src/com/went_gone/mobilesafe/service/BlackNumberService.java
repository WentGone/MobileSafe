package com.went_gone.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.db.dao.BlackNumberDao;
import com.went_gone.mobilesafe.db.dao.PhoneDao;
import com.went_gone.mobilesafe.db.dao.SmsDao;
import com.went_gone.mobilesafe.receiver.DeviceAdmin;
import com.went_gone.mobilesafe.service.AddressService.MyPhoneStateListener;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

/**
 * 黑名单拦截的服务
 * 
 * @author Went_Gone
 * 
 */
public class BlackNumberService extends Service {
	private BlackNumberDao mDao;
	private InnerSmsReceiver mInnerSmsReceiver;
	DevicePolicyManager mDPM = (DevicePolicyManager) BaseApplication
			.getInstance().getSystemService(Context.DEVICE_POLICY_SERVICE);
	private TelephonyManager mTm;
	private MyPhoneStateListener mPhoneStateListener;
	private MyContentObserver mObserver;

	@Override
	public void onCreate() {
		mDao = BlackNumberDao.getInstance();
		
		// 拦截短信
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		// intentFilter.setPriority(Integer.MAX_VALUE);
		intentFilter.setPriority(1000);

		mInnerSmsReceiver = new InnerSmsReceiver();
		registerReceiver(mInnerSmsReceiver, intentFilter);

		// 监听电话的状态
		// 服务开启的时候监听 关闭的时候不需要监听
		mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 监听电话状态
		mPhoneStateListener = new MyPhoneStateListener();
		mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	/**
	 * 接收电话状态时的监听
	 * 
	 * @author Went_Gone
	 * 
	 */
	class MyPhoneStateListener extends PhoneStateListener {
		// 手动重写电话状态发生改变会触发的方法
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				// 空闲状态(移除展示)
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// 摘机状态，拨打或通话中
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				// 来电状态(展示) 响铃
				// 挂断电话， mTm.endCall();aidl文件中
				endCall(incomingNumber);
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	/**
	 * 接收到短信时干的事情
	 * 
	 * @author Went_Gone
	 * 
	 */
	class InnerSmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			SmsDao dao = SmsDao.getInstance();
			// 获取短信内容，获取发送短信的号码，此电话号码在黑名单中，且拦截模式为1或者3
			// 拦截短信。
			ComponentName componentName = new ComponentName(context,
					DeviceAdmin.class);
			// 获取短信内容
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			// 循环遍历短信数组
			for (Object object : objects) {
				// 获取短信对象
				SmsMessage pdu = SmsMessage.createFromPdu((byte[]) object);
				// 获取短信对象的基本信息
				String originatingAddress = pdu.getOriginatingAddress();// 发送短信的号码
				String messageBody = pdu.getMessageBody();// 短信内容
				int mode = mDao.getMode(originatingAddress);
				if (mode == 1 || mode == 3) {
					if (originatingAddress.startsWith("+")) {
						originatingAddress = originatingAddress.substring(3);//去掉前三个字符
					}
					dao.insert(originatingAddress, mDao.getName(originatingAddress), messageBody);
					// 拦截短信
					abortBroadcast();// 中断一个广播接受者
				}
			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 挂断电话
	 */
	public void endCall(String phone) {
		int mode = mDao.getMode(phone);
		PhoneDao phoneDao = PhoneDao.getInstance();
		if (mode == 2 || mode == 3) {
			phoneDao.insert(phone, mDao.getName(phone));
			
			// ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
			// ServiceManager此类android对开发者隐藏，所以不能去直接调用其方法，所以需要用反射调用
			// 1.获取ServiceManager的字节码文件
			try {
				Class<?> clazz = Class.forName("android.os.ServiceManager");
				// 2.获取方法
				Method method = clazz.getMethod("getService", String.class);
				// 3.反射调用此方法 静态方法传null
				IBinder iBinder = (IBinder) method.invoke(null,
						Context.TELEPHONY_SERVICE);
				// 调用会获取aidl文件对象的方法
				ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
				// 调用在aidl隐藏的endCall方法
				iTelephony.endCall();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mObserver = new MyContentObserver(new Handler(),phone);
			getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, mObserver);
			
//			
		}
	}
/**
 * 内容观察者，用于观察数据库calls是否发生改变
 * @author Went_Gone
 *
 */
class MyContentObserver extends ContentObserver{
	private String phone;
	public MyContentObserver(Handler handler,String phone) {
		super(handler);
		this.phone = phone;
		
	}
	//数据库指定calls表发生改变的调用
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		//插入一条数据后，消除此拦截号码的通话记录（权限）
		getContentResolver().delete(Uri.parse("content://call_log/calls"), "number = ?", new String[]{phone});
	}
}
	@Override
	public void onDestroy() {
		if (mInnerSmsReceiver != null) {
			unregisterReceiver(mInnerSmsReceiver);
		}
		//取消对电话状态的监听
		if (mTm != null && mPhoneStateListener != null) {
			mTm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		if (mObserver != null) {
			//注销内容观察者
			getContentResolver().unregisterContentObserver(mObserver);
		}
		super.onDestroy();
	}
}
