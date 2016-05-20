package com.went_gone.mobilesafe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.went_gone.mobilesafe.R.id;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.MD5Util;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;


public class SettingPasswordActivity extends BaseActivity {

	private EditText mET_Password;
	private EditText mET_Again;
	private CheckBox mCheckBox;
	private Button mBtnNext;

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_setting_password);
		mET_Password = (EditText) findViewById(R.id.SettingPassword_EditText_Password);
		mET_Again = (EditText) findViewById(R.id.SettingPassword_EditText_Again);
		mCheckBox = (CheckBox) findViewById(R.id.SettingPassword_CheckBox);
		mBtnNext = (Button) findViewById(R.id.SettingPassword_Button_Next);
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				showPassword();
			}
		});
		mET_Again.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				setButtonBackground(s);
			}

			
		});
		mBtnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
	}

	/**
	 * 提示用户设置密码成功的提示框
	 */
	protected void showDialog() {
		//验证是否符合正则表达式
		final String password = mET_Password.getText().toString();
		Pattern pattern = Pattern.compile("^[0-9]{6}$");
		Matcher matcherPassword = pattern.matcher(mET_Password.getText().toString());
		boolean passwordBoolean = matcherPassword.matches();
		if (!passwordBoolean) {
			Toast.makeText(getApplicationContext(), "密码格式错误,请修正!", Toast.LENGTH_SHORT).show();
			return;
		}else if (!mET_Again.getText().toString().equals(mET_Password.getText().toString())) {
			Toast.makeText(getApplicationContext(), "密码不一致,请修正!", Toast.LENGTH_SHORT).show();			
			return;
		}
		
		//创建并显示一个提示信息
		Builder builder = new AlertDialog.Builder(SettingPasswordActivity.this);
		builder.setTitle("恭喜您，密码设置成功");
		builder.setMessage("密码设置成功!\n私密空间、手机防盗的密码通用，请牢记!");
		builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SpUtil.putString(BaseApplication.getInstance(), ConstantValue.MOBLIE_SAFE_PASSWORD,MD5Util.encoder(password));
				enterMobileSafe();
			}
		});
		builder.show();
	}

	/**
	 * 进入手机防盗页面
	 */
	protected void enterMobileSafe() {
		Intent intent = new Intent(SettingPasswordActivity.this,AntithiefActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 设置"下一步"按钮的背景
	 * @param s
	 */
	@SuppressLint("ResourceAsColor")
	private void setButtonBackground(Editable s) {
		if (!s.toString().equals("")&&!mET_Password.getText().toString().equals("")) {
			mBtnNext.setBackgroundResource(R.drawable.setting_button_pressed);
			mBtnNext.setTextColor(Color.BLACK);
			mBtnNext.setFocusable(true);
			mBtnNext.setFocusableInTouchMode(true);
			mBtnNext.setClickable(true);
			if (s.toString().length()==6) {
				InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);   
				imm.hideSoftInputFromWindow(SettingPasswordActivity.this.getCurrentFocus().getWindowToken(),    
		                InputMethodManager.HIDE_NOT_ALWAYS);   
			}
		}else {
			mBtnNext.setTextColor(android.R.color.darker_gray);
			mBtnNext.setBackgroundResource(R.drawable.setting_button_normal);
			mBtnNext.setFocusable(false);
			mBtnNext.setFocusableInTouchMode(false);
			mBtnNext.setClickable(false);
		}
	}
	/**
	 * 用于显示或隐藏密码的事件
	 */
	protected void showPassword() {
		boolean isChecked = mCheckBox.isChecked();
		/*设置内容可见
		et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		设置内容隐藏
		et.setTransformationMethod(PasswordTransformationMethod.getInstance());*/
		if (isChecked) {			
			mET_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			mET_Again.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			mET_Password.setSelection(mET_Password.getText().length());
			mET_Again.setSelection(mET_Again.getText().length());
		}else {			
			mET_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
			mET_Again.setTransformationMethod(PasswordTransformationMethod.getInstance());
			mET_Password.setSelection(mET_Password.getText().length());
			mET_Again.setSelection(mET_Again.getText().length());
		}
	}

	@Override
	protected void initVariables() {
		
	}

}
