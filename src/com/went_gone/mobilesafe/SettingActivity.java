package com.went_gone.mobilesafe;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.service.AddressService;
import com.went_gone.mobilesafe.ui.SettingItemView;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.ServiceUtil;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

public class SettingActivity extends BaseActivity {
	private TextView mTV_ComeBack;
	private String[] mStyleString = new String[]{"白色","蓝色","红色","黑色","深绿色"};
	private SettingItemView mSIV_AddressStyle;

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_setting);
		mTV_ComeBack = (TextView) findViewById(R.id.ComeBack);
		mTV_ComeBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// enterLastActivity();
				finish();
			}
		});
	}

	@Override
	protected void initVariables() {
		initUpdate();
		initPhoneAddress();
		initPhoneAddressStyle();
		initPhoneAddressStyleAddress();
	}

	/**
	 * 初始化归属地提示框位置条目
	 */
	private void initPhoneAddressStyleAddress() {
		SettingItemView siv_AddressStyleAddress = (SettingItemView) findViewById(R.id.siv_addressStyleAddress);
		siv_AddressStyleAddress.setDesText("设置提示框位置");
		siv_AddressStyleAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,ToastLocationActivity.class));
			}
		});
	}

	/**
	 * 初始化选择归属地弹出框的样式
	 */
	private void initPhoneAddressStyle() {
		mSIV_AddressStyle = (SettingItemView) findViewById(R.id.siv_addressStyle);
		int styleId = SpUtil.getInt(this,ConstantValue.TOAST_STYLE,0);
		mSIV_AddressStyle.setDesText(mStyleString[styleId]);
		mSIV_AddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//弹出选择归属地显示风格的弹出框
				showChooseStyleDialog();
			}
		});
	}

	/**
	 * 弹出选择归属地显示风格的弹出框
	 */
	protected void showChooseStyleDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请选择归属地样式");
		builder.setIcon(R.drawable.ic_launcher);
		int styleId = SpUtil.getInt(this,ConstantValue.TOAST_STYLE,0);
		builder.setSingleChoiceItems(mStyleString, styleId,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// sex = mSexs[which];
//						ToastUtil.show(SettingActivity.this,mStyleString[which]);
						mSIV_AddressStyle.setDesText(mStyleString[which]);//设置文本
						SpUtil.putInt(SettingActivity.this,ConstantValue.TOAST_STYLE,which);
						dialog.dismiss();
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}

	/**
	 * 是否显示电话号码归属地的设置
	 */
	private void initPhoneAddress() {
		final SettingItemView siv_PhoneAddress = (SettingItemView) findViewById(R.id.siv_address);
		siv_PhoneAddress.setChecked(ServiceUtil.isRunning(SettingActivity.this,
				"com.went_gone.mobilesafe.service.AddressService"));
		// 是否开启电话号码归属地的切换过程
		siv_PhoneAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isChecked = siv_PhoneAddress.isChecked();
				siv_PhoneAddress.setChecked(!isChecked);
				if (!isChecked) {
					// 开启服务
					startService(new Intent(BaseApplication.getInstance(),
							AddressService.class));
				} else {
					// 关闭服务
					stopService(new Intent(BaseApplication.getInstance(),
							AddressService.class));
				}
			}
		});
	}

	/**
	 * 初始化更新的方法
	 */
	private void initUpdate() {
		final SettingItemView siv_Update = (SettingItemView) findViewById(R.id.siv_update);

		// 获取已有的开关的状态，用作显示
		boolean open_update = SpUtil.getBoolean(this,
				ConstantValue.OPEN_UPDATE, false);
		siv_Update.setChecked(open_update);

		siv_Update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 之前选中的变成未选中的
				// 之前未选中的变成选中的
				/*
				 * if (siv_Update.isChecked()) { siv_Update.setChecked(false);
				 * }else { siv_Update.setChecked(true); }
				 */
				boolean isChecked = siv_Update.isChecked();
				siv_Update.setChecked(!isChecked);
				// 将取反后的状态存储到sp中
				SpUtil.putBoolean(BaseApplication.getInstance(),
						ConstantValue.OPEN_UPDATE, !isChecked);
			}
		});
	}

}
