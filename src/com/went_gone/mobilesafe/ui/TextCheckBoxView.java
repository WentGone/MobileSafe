package com.went_gone.mobilesafe.ui;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TextCheckBoxView extends RelativeLayout {
	private String text;
	private CheckBox mCB;
	private boolean isCheckBox;

	public TextCheckBoxView(Context context) {
		this(context,null);
	}

	public TextCheckBoxView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public TextCheckBoxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View view = View.inflate(context, R.layout.text_checkbox_layout,this);
		TextView textView = (TextView) findViewById(R.id.Text_CheckBox_TextView);
		mCB = (CheckBox) findViewById(R.id.Text_CheckBox_CheckBox);
		TextView tv_ArrowRight = (TextView) findViewById(R.id.Text_CheckBox_ArrowRight);
		
		initAttrs(attrs);
		textView.setText(text);
		if (isCheckBox) {
			mCB.setVisibility(VISIBLE);
			tv_ArrowRight.setVisibility(INVISIBLE);
		}else {
			tv_ArrowRight.setVisibility(VISIBLE);
			mCB.setVisibility(INVISIBLE);
		}
		
	}

	/**
	 * 初始化设置属性
	 * @param attrs
	 */
	private void initAttrs(AttributeSet attrs) {
		text = attrs.getAttributeValue(ConstantValue.NAME_SPACE, "text");
		isCheckBox = attrs.getAttributeBooleanValue(ConstantValue.NAME_SPACE, "ischeckbox",true);
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
	}
}
