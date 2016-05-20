package com.went_gone.mobilesafe.ui;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.base.BaseApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AntithiefItemView extends LinearLayout {
	private static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.went_gone.mobilesafe";
	private String mTitleOn;
	private String mMsgOn;
	private int mImIdOn;
	private String mTitleOff;
	private String mMsgOff;
	private int mImIdOff;
	private int mImId;
	private TextView mTV_Title;
	private ImageView mIV_Sate;
	private TextView mTV_Msg;
	private OnProtectedListener protectedListener;

	public AntithiefItemView(Context context) {
		this(context,null);
	}

	public AntithiefItemView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	@SuppressLint("NewApi")
	public AntithiefItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View.inflate(context, R.layout.mobilesafe_item_layout,this);
		mTV_Title = (TextView) findViewById(R.id.MobileSafe_item_title);
		mIV_Sate = (ImageView) findViewById(R.id.MobileSafe_item_IM);
		mTV_Msg = (TextView) findViewById(R.id.MobileSafe_item_msg);
		
		initAttriBute(attrs);
		
		mTV_Title.setText(mTitleOff);
		mIV_Sate.setVisibility(INVISIBLE);
		mTV_Msg.setVisibility(INVISIBLE);
//		isProtected(true);
	}

	/**
	 * 初始化属性值
	 */
	private void initAttriBute(AttributeSet attrs) {
		mTitleOn = attrs.getAttributeValue(NAME_SPACE, "titleon");
		mTitleOff = attrs.getAttributeValue(NAME_SPACE, "titleoff");
		mMsgOn = attrs.getAttributeValue(NAME_SPACE, "msgon");
		mMsgOff = attrs.getAttributeValue(NAME_SPACE, "msgoff");
		mImIdOn = attrs.getAttributeIntValue(NAME_SPACE, "srcon", R.drawable.antithief_item_open);
		mImIdOff = attrs.getAttributeIntValue(NAME_SPACE, "srcoff", R.drawable.antithief_item_close);
		mImId = attrs.getAttributeIntValue(NAME_SPACE, "src", R.drawable.antithief_item_open);
	}
	
	/**
	 * 通过是否被保护改变布局文件
	 * @param isProtected
	 */
	public void setProtected(boolean isProtected){
		if (isProtected) {
			//开启状态
			mTV_Title.setText(mTitleOn);
//			mTV_Msg.setText(mMsgOn);
			mIV_Sate.setVisibility(VISIBLE);
			mIV_Sate.setImageResource(R.drawable.antithief_item_open);
			mTV_Msg.setVisibility(INVISIBLE);
		}else {
			//未开启状态
			mTV_Title.setText(mTitleOff);
//			mTV_Msg.setText(mMsgOff);
			mIV_Sate.setVisibility(INVISIBLE);
			mTV_Msg.setVisibility(INVISIBLE);
		}
	}
	
	/**
	 * 
	 * @param isProtected
	 * @param s1
	 * @param s2
	 */
	public void setProtected(boolean isProtected,String s1,final OnProtectedListener protectedListener){
		if (isProtected) {
			//开启状态
			mTV_Title.setText(mTitleOn);
			mIV_Sate.setVisibility(VISIBLE);
			mTV_Msg.setVisibility(VISIBLE);
			if (s1.equals("")) {
				mTV_Msg.setText(mMsgOff);
				mIV_Sate.setImageResource(R.drawable.antithief_item_close);
			}else if (!s1.equals("")) {
				mTV_Msg.setText(s1);
				mIV_Sate.setImageResource(R.drawable.antithief_item_open);
			}
		}else {
			//未开启状态
			mTV_Title.setText(mTitleOff);
			mIV_Sate.setVisibility(VISIBLE);
			mIV_Sate.setImageResource(mImIdOff);
			mTV_Msg.setVisibility(VISIBLE);
			mTV_Msg.setText(mMsgOff);
			mTV_Msg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					protectedListener.onProtected();
				}
			});
		}
		setTextViewState(s1);
	}
	
	public interface OnProtectedListener{
		void onProtected();
	}
	
	/**
	 * 设置下方的TextView的状态
	 * @param s
	 */
	public void setTextViewState(String s){
		if (TextUtils.isEmpty(s)) {			
			mIV_Sate.setImageResource(R.drawable.antithief_item_close);
			mTV_Msg.setText("未设置");
			mTV_Msg.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.red));
		}else {
			mIV_Sate.setImageResource(R.drawable.antithief_item_open);
			mTV_Msg.setText(s);
			mTV_Msg.setTextColor(BaseApplication.getInstance().getResources().getColor(R.color.green));
		}
	}
	
	public void setImageViewState(boolean open){
		if (open) {
			mIV_Sate.setImageResource(R.drawable.antithief_item_open);
		}else {
			mIV_Sate.setImageResource(R.drawable.antithief_item_close);			
		}
	}
}
