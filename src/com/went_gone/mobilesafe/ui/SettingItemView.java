package com.went_gone.mobilesafe.ui;

import com.went_gone.mobilesafe.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {
	private static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.went_gone.mobilesafe";
	private TextView mTvTitle;
	private TextView mTvDes;
	private CheckBox mCB;
	private String mDestitle;
	private String mDesoff;
	private String mDeson;
	private boolean isCheckBox;

	public SettingItemView(Context context) {
		this(context,null);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// xml ---> view   将设置界面的一个条目转换成一个view对象
		/*View view = View.inflate(context, R.layout.setting_item_layout, null);
		this.addView(view);*/
		View.inflate(context,R.layout.setting_item_layout,this);
		mTvTitle = (TextView) findViewById(R.id.Setting_TV_title);
		mTvDes = (TextView) findViewById(R.id.Setting_TV_des);
		mCB = (CheckBox) findViewById(R.id.Setting_CheckBox);
		TextView tv_ArrowRight = (TextView) findViewById(R.id.Setting_TV_arrowRight);
		//获取自定义以及原生属性的操作，AttributeSet attrs对象中获取
		initAttrs(attrs);
		
		mTvTitle.setText(mDestitle);
		if (isCheckBox) {
			mCB.setVisibility(VISIBLE);
			tv_ArrowRight.setVisibility(INVISIBLE);
		}else {
			tv_ArrowRight.setVisibility(VISIBLE);
			mCB.setVisibility(INVISIBLE);
		}
		if (!isCheckBox) {
			//如果是arrowRight按钮
			mTvDes.setText(mDesoff);
		}
	}
	
	/**
	 * 
	 * @param attrs   构造方法中维护好的属性集合
	 * 返回属性集合中的自定义属性值
	 */
	private void initAttrs(AttributeSet attrs) {
		/*//获取属性个数
		int count = attrs.getAttributeCount();
		//获取属性名称以及属性值
		for (int i = 0; i < count; i++) {
			String attributeName = attrs.getAttributeName(i);
		}*/
		
		//通过属性索引找到对应的属性名称&名空间
		mDestitle = attrs.getAttributeValue(NAME_SPACE, "destitle");
		mDesoff = attrs.getAttributeValue(NAME_SPACE, "desoff");
		mDeson = attrs.getAttributeValue(NAME_SPACE, "deson");
		isCheckBox = attrs.getAttributeBooleanValue(NAME_SPACE,"ischeckbox", true);
	}

	/**
	 * 判断是否开启的方法
	 * @return  返回当前SettingItem是否选中，true开启（checkBox），false关闭
	 */
	public boolean isChecked(){
		//由当前CheckBox的选中结果决定当前条目是否开启
		return mCB.isChecked();
	}
	
	/**
	 * @param isChecked  是否作为开启的变量   由点击过程中做传递
	 */
	public void setChecked(boolean isChecked){
		mCB.setChecked(isChecked);
		if (isChecked) {
			//开启
			mTvDes.setText(mDeson);
		}else {
			mTvDes.setText(mDesoff);
		}
	}
	
	/**
	 * 如果是向右的箭头   设置他的des的内容
	 * @param msg  内容
	 */
	public void setArrowRightText(String msg){
		if (TextUtils.isEmpty(msg)) {
			mTvDes.setText(mDesoff);
		}else {			
			mTvDes.setText(msg);
		}
		mTvDes.setTextSize(13);
	}
	
	/**
	 * 如果没有checkbox，那么此方法给DesTextView设置文本
	 * @param des 文本内容
	 */
	public void setDesText(String des){
		mTvDes.setText(des);
	}
}
