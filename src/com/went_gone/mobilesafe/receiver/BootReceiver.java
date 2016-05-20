package com.went_gone.mobilesafe.receiver;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 接受手机重启完成后发出的广播
 * @author Went_Gone
 *
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//1.获取开机后sim卡的序列号
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String simSerialNumber = manager.getSimSerialNumber();
		//2.sp中存储的序列号
		String spSimNumber = SpUtil.getString(context, ConstantValue.SIM_NUMBER,"");
//		ToastUtil.show(BaseApplication.getInstance(),"重启了");
		//开机后的sim卡序列号与存储的序列号不一致
		if (!simSerialNumber.equals(spSimNumber)&&!TextUtils.isEmpty(simSerialNumber)) {
			//发送短信给报警号码
			SmsManager smsManager = SmsManager.getDefault();
			//目标地址，短信中心(在中国为null)，送达的意图，送达后返回的意图
			String sendAddress = SpUtil.getString(BaseApplication.getInstance(),ConstantValue.CONTACT_PHONE, "");
			String name = SpUtil.getString(BaseApplication.getInstance(), ConstantValue.CONTACT_CONTENT, "");
			String msg = "";
			if (!TextUtils.isEmpty(name)) {
				msg = "我是"+name+","+BaseApplication.getInstance().getResources().getString(R.string.changeCardContent);
			}else{
				msg = BaseApplication.getInstance().getResources().getString(R.string.changeCardContent);
			}
			smsManager.sendTextMessage(sendAddress,null,msg,null,null);
		}
	}
}
