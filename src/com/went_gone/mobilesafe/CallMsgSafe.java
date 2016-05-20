package com.went_gone.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.went_gone.mobilesafe.adapter.InterceptPhoneAdapter;
import com.went_gone.mobilesafe.adapter.InterceptSmsAdapter;
import com.went_gone.mobilesafe.adapter.InterceptViewPagerAdapter;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.db.dao.BlackNumberDao;
import com.went_gone.mobilesafe.db.dao.PhoneDao;
import com.went_gone.mobilesafe.db.dao.SmsDao;
import com.went_gone.mobilesafe.entity.BlackNumberEntity;
import com.went_gone.mobilesafe.entity.PhoneEntity;
import com.went_gone.mobilesafe.entity.SmsEntity;

public class CallMsgSafe extends BaseActivity {
	private List<View> mViews;
	private ViewPager mVP_Intercept;
	private InterceptViewPagerAdapter mIVP_Adapter;
	private RadioGroup mRG_Intercept;
	private ListView mLV_Msg;
	private ListView mLV_Phone;
	private List<SmsEntity> mSmsList;
	private List<PhoneEntity> mPhoneList;
	private InterceptSmsAdapter mSmsAdapter;
	private InterceptPhoneAdapter mPhoneAdapter;
	private SmsDao mSmsDao;
	private PhoneDao mPhoneDao;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				if (mSmsAdapter == null) {
					mSmsAdapter = new InterceptSmsAdapter(CallMsgSafe.this, mSmsList, R.layout.item_listview_intercept_sms);
					mLV_Msg.setAdapter(mSmsAdapter);
				}
			}
			if (msg.what == 1) {
				if (mPhoneAdapter == null) {
					mPhoneAdapter = new InterceptPhoneAdapter(CallMsgSafe.this, mPhoneList, R.layout.item_listview_incepter_phone);
					mLV_Phone.setAdapter(mPhoneAdapter);
				}
			}
		};
	};
	
	
	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_callmsgsafe);
		initViewPager();
		initComeBack();
		initRadioGroup();
		ImageView iv_Setting = (ImageView) findViewById(R.id.Intercept_Setting);
		iv_Setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setIntercept();
			}
		});
		
	}


	/**
	 * 初始化上方的选择条
	 */
	private void initRadioGroup() {
		mRG_Intercept = (RadioGroup) findViewById(R.id.Intercept_RadiopGroup);
		mRG_Intercept.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.Intercept_SMS:
					//拦截短信
					mVP_Intercept.setCurrentItem(0);
					break;
				case R.id.Intercept_Phone:
					//拦截电话
					mVP_Intercept.setCurrentItem(1);
					break;
				}
			}
		});
		
		//初始化时RadioButton选择短信
		mRG_Intercept.check(R.id.Intercept_SMS);
	}


	private void initComeBack() {
		TextView tv_ComeBack = (TextView) findViewById(R.id.ComeBack);
		tv_ComeBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CallMsgSafe.this.finish();
			}
		});
	}
	
	
	/**
	 * 拦截设置
	 */
	protected void setIntercept() {
		startActivity(new Intent(CallMsgSafe.this,CallMsgSafeSetting.class));
	}

	@Override
	protected void initVariables() {
		mSmsDao = SmsDao.getInstance();
		mPhoneDao = PhoneDao.getInstance();
	}


	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mVP_Intercept = (ViewPager) findViewById(R.id.CallMsgSafe_ViewPager);
		mViews = new ArrayList<View>();
		View viewMsg = View.inflate(CallMsgSafe.this,R.layout.viewpager_intercept_msg,null);
		mViews.add(viewMsg);
		View viewPhone = View.inflate(CallMsgSafe.this,R.layout.viewpager_intercept_phone,null);
		mViews.add(viewPhone);
		mIVP_Adapter = new InterceptViewPagerAdapter(mViews);
		mVP_Intercept.setAdapter(mIVP_Adapter);
		mVP_Intercept.setCurrentItem(0);
		
		mLV_Msg = (ListView) viewMsg.findViewById(R.id.InterceptMsg_ListView);
		mLV_Phone = (ListView) viewPhone.findViewById(R.id.InterceptPhone_ListView);
		
		
		//给ViewPager添加滑动监听
		mVP_Intercept.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if (arg0==0) {
					mRG_Intercept.check(R.id.Intercept_SMS);
				}else {
					mRG_Intercept.check(R.id.Intercept_Phone);
					
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		initMsgListView();
		initPhoneListView();
		super.onResume();
	}

	


	/**
	 * 初始化拦截短信的展示列表
	 */
	private void initMsgListView() {
		if (mSmsList == null) {
			mSmsList = new ArrayList<SmsEntity>();
		}
		new Thread(){
			public void run() {
				mSmsList = mSmsDao.findAll();
				mHandler.sendEmptyMessage(0);
			};
		}.start();
		
	}

	/**
	 * 初始化拦截电话的展示列表
	 */
	private void initPhoneListView() {
		if (mPhoneList == null) {
			mPhoneList = new ArrayList<PhoneEntity>();
		}
		new Thread(){
			public void run() {
				mPhoneList = mPhoneDao.findAll();
				mHandler.sendEmptyMessage(1);
			};
		}.start();
	}
	
}
