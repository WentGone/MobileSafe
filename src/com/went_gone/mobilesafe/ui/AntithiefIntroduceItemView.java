package com.went_gone.mobilesafe.ui;

import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.utils.ConstantValue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AntithiefIntroduceItemView extends LinearLayout{
	private int imageId;
	private String title;
	private String des;
	private TextView tv_Des;

	public AntithiefIntroduceItemView(Context context) {
		this(context,null);
	}

	public AntithiefIntroduceItemView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	@SuppressLint("NewApi")
	public AntithiefIntroduceItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		View view = View.inflate(context, R.layout.antithifeintroduce_item_layout,this);
		ImageView iv_Right = (ImageView) findViewById(R.id.AFI_Item_IamgeView);
		TextView tv_Title = (TextView) findViewById(R.id.AFI_Item_TV_Title);
		tv_Des = (TextView) findViewById(R.id.AFI_Item_TV_Des);
		
		initAttrs(attrs);
		
		iv_Right.setImageResource(imageId);
		tv_Title.setText(title);
		tv_Des.setText(des);
	}

	private void initAttrs(AttributeSet attrs) {
		imageId = attrs.getAttributeResourceValue(ConstantValue.NAME_SPACE,"srcimage", R.drawable.splash);
		title = attrs.getAttributeValue(ConstantValue.NAME_SPACE,"title_text");
		des = attrs.getAttributeValue(ConstantValue.NAME_SPACE,"des_text");
	}
	
	public void setSpandForDes(String source){
		Spanned spanned = Html.fromHtml(source);
		tv_Des.setText(spanned);
	}
}
