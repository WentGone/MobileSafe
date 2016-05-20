package com.went_gone.mobilesafe;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.ui.AntithiefIntroduceItemView;

public class AntithiefIntroduceActivity extends BaseActivity {
	private AntithiefIntroduceItemView mAII_Lock,mAII_Location,mAII_Alarm,mAII_BackUp,mAII_Destory;

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_antithief_introduce);
		TextView tv_ComBack = (TextView) findViewById(R.id.ComeBack);
		tv_ComBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AntithiefIntroduceActivity.this.finish();
			}
		});
		mAII_Lock = (AntithiefIntroduceItemView) findViewById(R.id.AII_Lock);
		mAII_Location = (AntithiefIntroduceItemView) findViewById(R.id.AII_Loction);
		mAII_Alarm = (AntithiefIntroduceItemView) findViewById(R.id.AII_Alarm);
		mAII_BackUp = (AntithiefIntroduceItemView) findViewById(R.id.AII_BackUp);
		mAII_Destory = (AntithiefIntroduceItemView) findViewById(R.id.AII_Destory);
		String source = "发送<font color='#ff0000'>"+getResources().getString(R.string.lock_password)+"</font>到被盗手机，可立即锁定被盗手机，通过手机安全卫士密码解锁才可进入";
		mAII_Lock.setSpandForDes(source);
		source = "发送<font color='#ff0000'>"+getResources().getString(R.string.location_password)+"</font>到被盗手机，可通过远程定位被盗手机的位置";
		mAII_Location.setSpandForDes(source);
		source = "发送<font color='#ff0000'>"+getResources().getString(R.string.alarm_password)+"</font>到被盗手机，被盗手机立即发出报警音";
		mAII_Alarm.setSpandForDes(source);
		source = "请先设置备份邮箱，发送<font color='#ff0000'>"+getResources().getString(R.string.backup_password)+"</font>到被盗手机，备份被盗手机的短信，联系人等信息";
		mAII_BackUp.setSpandForDes(source);
		source = "发送<font color='#ff0000'>"+getResources().getString(R.string.destory_password)+"</font>到被盗手机，立即销毁被盗手机的信息，及时保护隐私";
		mAII_Destory.setSpandForDes(source);
	}

	@Override
	protected void initVariables() {

	}

}
