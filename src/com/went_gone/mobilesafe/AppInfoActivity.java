package com.went_gone.mobilesafe;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.entity.AppInfoEntity;
import com.went_gone.mobilesafe.utils.BitmapUtil;
import com.went_gone.mobilesafe.utils.ObjectStoreUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

/**
 * 应用信息界面 卸载 启动 分享
 * 
 * @author Went_Gone
 * 
 */
public class AppInfoActivity extends BaseActivity {
	private Intent mIntent;
	private AppInfoEntity mAppInfo;
	private ImageView mIV_app;
	private LinearLayout mLayout_Permissions;
	private List<String> mPremissions;
	private String[] permissions;
	private TextView mTV_Name;
	private String packageName;

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_appinfo);
		mIV_app = (ImageView) findViewById(R.id.act_appinfo_IV);
		mLayout_Permissions = (LinearLayout) findViewById(R.id.act_appinfo_Linlayout_permission);
		mTV_Name = (TextView) findViewById(R.id.act_appinfo_TV_Name);
		initButtons();
		ImageView iv_Share = (ImageView) findViewById(R.id.act_appinfo_share);
		iv_Share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareApp();
			}
		});
	}

	/**
	 * 分享App
	 */
	protected void shareApp() {
		//此方法会呼起系统中所有支持文本分享的app列表
		//通过短信应用,向外发送短信
		if (packageName!=null) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT,"分享一个应用,应用名称为"+packageName);
			intent.setType("text/plain");
			startActivity(intent);
		}
	}

	/**
	 * 初始化卸载、启动
	 */
	private void initButtons() {
		Button btn_unInstall = (Button) findViewById(R.id.act_appinfo_Btn_Delete);
		Button btn_Start = (Button) findViewById(R.id.act_appinfo_Btn_Start);
		btn_unInstall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				unInstallApp();
			}
		});
		btn_Start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startApp();
			}
		});
	}

	/**
	 * 启动App
	 */
	protected void startApp() {
		// 通过桌面启动指定包名的应用
		if (packageName != null) {
			PackageManager pm = getPackageManager();
			// 通过Launch开启指定包名的意图，去开启应用
			Intent intent = pm.getLaunchIntentForPackage(packageName);
			if (intent != null) {
				startActivity(intent);
				this.finish();
			} else {
				ToastUtil.show(this, "此应用不能被开启");
			}
		}
	}

	/**
	 * 卸载App
	 */
	protected void unInstallApp() {
		if (packageName != null) {
			Intent intent = new Intent("android.intent.action.DELETE");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(Uri.parse("package:" + packageName));
			startActivityForResult(intent, 0x00);
		}
	}

	@Override
	protected void initVariables() {
		mIntent = getIntent();
		packageName = mIntent.getStringExtra("packageName");
		permissions = mIntent.getStringArrayExtra("appPermissions");
		byte[] bitmapByte = mIntent.getByteArrayExtra("bitmap");

		String name = mIntent.getStringExtra("appName");
		mTV_Name.setText(name);

		mIV_app.setImageBitmap(BitmapUtil.Bytes2Bimap(bitmapByte));
		mIV_app.setVisibility(View.VISIBLE);
		initPermissions();
	}

	/**
	 * 初始化权限
	 */
	private void initPermissions() {
		if (permissions != null) {
			for (String str : permissions) {
				TextView tv_item = new TextView(this);
				tv_item.setText(str);
				tv_item.setPadding(10, 10, 10, 10);
				mLayout_Permissions.addView(tv_item);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x00) {
			this.finish();
		}
	}
}
