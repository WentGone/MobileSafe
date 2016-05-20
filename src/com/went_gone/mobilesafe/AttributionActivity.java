package com.went_gone.mobilesafe;

import android.app.DownloadManager.Query;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.engine.AddressDao;

/**
 * 归属地查询的界面  用于归属地查询
 * @author Went_Gone
 *
 */
public class AttributionActivity extends BaseActivity {
	private EditText mET_PhoneNumber;
	private TextView mTV_Content;
	private String mAddress;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mTV_Content.setText(mAddress);
		};
	};
	
	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_attribution);
		TextView tv_ComeBack = (TextView) findViewById(R.id.ComeBack);
		tv_ComeBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AttributionActivity.this.finish();
			}
		});
		mET_PhoneNumber = (EditText) findViewById(R.id.Attribution_ET);
		Button btn_Query = (Button) findViewById(R.id.Attribution_Btn);
		mTV_Content = (TextView) findViewById(R.id.Attribution_TV);
		btn_Query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String phone = mET_PhoneNumber.getText().toString();
				if (!TextUtils.isEmpty(phone)) {
				     query(phone);
				}else {
					//为空抖动效果  Shake
					 Animation shake = AnimationUtils.loadAnimation(AttributionActivity.this, R.anim.shake);
				     
				     //interpolater  插补器，数学函数
				     //自定义插补器
				     /*shake.setInterpolator(new Interpolator() {
						//y = ax+b
						@Override
						public float getInterpolation(float arg0) {
							return 0;
						}
					});*/
				     
				     mET_PhoneNumber.startAnimation(shake);
				     
				     
				     //手机震动效果（Vibrator）
				     Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				     //震动的毫秒值
//				     vibrator.vibrate(2000);
				     //规律震动（震动的规则(间隔时间(不震动时间),震动时间.....)，重复的次数[-1不重复]）
				     vibrator.vibrate(new long[]{1000,2000,1000,2000},-1);
				}
			}
		});
		//监听输入框文本的变化的监听器
		mET_PhoneNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String phone = mET_PhoneNumber.getText().toString();
				query(phone);
			}
		});
	}

	@Override
	protected void initVariables() {
	}

	/**
	 *电话归属地查询的方法
	 * @param phone
	 */
	private void query(final String phone) {
		new Thread(){
			public void run() {
				mAddress = AddressDao.getAddress(phone);
				//告知UI主线程查询结束；
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

}
