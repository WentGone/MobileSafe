package com.went_gone.mobilesafe;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.MD5Util;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

public class EnterPsdActivity extends BaseActivity {
	private Intent intent;
	private String mPackagename;
	private TextView mTV_AppName;
	private ImageView mIV_Icon;
	private EditText mET_Psd;
	private Button mBtn_Sure;
	private String mPsdLocal;
	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_enterpsd);
		mTV_AppName = (TextView) findViewById(R.id.act_enterpsd_TV_name);
		mIV_Icon = (ImageView) findViewById(R.id.act_enterpsd_IV_icon);
		mET_Psd = (EditText) findViewById(R.id.act_enterpsd_ET_psd);
		mBtn_Sure = (Button) findViewById(R.id.act_enterpsd_Btn_sure);
		mPsdLocal = SpUtil.getString(EnterPsdActivity.this, ConstantValue.MOBLIE_SAFE_PASSWORD, "");
	}

	@Override
	protected void initVariables() {
		intent = getIntent();
		mPackagename = intent.getStringExtra("packagename");
		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(mPackagename, 0);
			Drawable icon = appInfo.loadIcon(pm);
			mIV_Icon.setBackgroundDrawable(icon);
			mTV_AppName.setText(appInfo.loadLabel(pm).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mBtn_Sure.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				String psd = mET_Psd.getText().toString();
				if (TextUtils.isEmpty(psd)) {
					ToastUtil.show(EnterPsdActivity.this, "请输入密码");
				}else {
					psd = MD5Util.encoder(psd);
					if (psd.equals(mPsdLocal)) {
						//解锁进入应用    进入应用   告知看门狗不要再去监听以及解锁的应用 发送广播
						Intent intent = new Intent("com.went_gone.action.SKIP");
						intent.putExtra("packagename", mPackagename);
						sendBroadcast(intent);
						finish();
					}else {
						ToastUtil.show(EnterPsdActivity.this, "密码输入错误");
					}
				}
			}
		});
	}

	
	@Override
	public void onBackPressed() {
		//通过隐式Intent 跳转到桌面  
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		super.onBackPressed();
	}
}
