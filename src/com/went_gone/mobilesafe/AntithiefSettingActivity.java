package com.went_gone.mobilesafe;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.ui.SettingItemView;
import com.went_gone.mobilesafe.ui.TextCheckBoxView;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.SpUtil;

public class AntithiefSettingActivity extends BaseActivity {
	private TextCheckBoxView mTB_AntithiefService;
	private TextCheckBoxView mTB_ChangeCardMsg;
	private TextCheckBoxView mTB_ChangeCardContent;
	private SettingItemView mSIV_ContactNumber;
	private String simSerialNumber;
	private EditText mDia_Et_Password;

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_antithief_setting);
		TextView tv_ComeBack = (TextView) findViewById(R.id.ComeBack);
		tv_ComeBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AntithiefSettingActivity.this.finish();
			}
		});
		initAntithiefService();
		initChangeCardMsg();
		initContactNumber();
		initChangeCardContent();
	}

	/**
	 * 初始化编辑换卡通知内容
	 */
	private void initChangeCardContent() {
		mTB_ChangeCardContent = (TextCheckBoxView) findViewById(R.id.Antithief_Setting_ChangeCardContent);
		mTB_ChangeCardContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showChangeCardContentDialog();
			}
		});
	}

	/**
	 * 弹出提示用户设置换卡通知的Dialog
	 */
	protected void showChangeCardContentDialog() {
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog__change_card_content, null);
		final EditText et_Content = (EditText) view.findViewById(R.id.dia_ChangeCardContent_ET);
		Button btn_Cancle = (Button) view.findViewById(R.id.dia_ChangeCard_Content_Btn_cancle);
		Button btn_Sure = (Button) view.findViewById(R.id.dia_ChangeCard_Content_Btn_sure);
		btn_Cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btn_Sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SpUtil.putString(AntithiefSettingActivity.this,ConstantValue.CONTACT_CONTENT, et_Content.getText().toString());
				dialog.dismiss();
			}
		});
		//先查看sp中是否存在contact_content短信通知内容，如果存在设置到edtText中
		String contact_content = SpUtil.getString(AntithiefSettingActivity.this,ConstantValue.CONTACT_CONTENT,"");
		et_Content.setText(contact_content);
		// 为了兼容低版本 2.3.3 给对话框设置为无内边距
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 初始化紧急联系人号码
	 */
	private void initContactNumber() {
		mSIV_ContactNumber = (SettingItemView) findViewById(R.id.Antithief_Setting_ContactNumber);
		String contactNumber = SpUtil.getString(this,
				ConstantValue.CONTACT_PHONE, "");
		mSIV_ContactNumber.setArrowRightText(contactNumber);
		mSIV_ContactNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showChangeCardDailog();
			}
		});
	}

	/**
	 * 提示换卡通知时的通知的Dailog
	 */
	protected void showChangeCardDailog() {
		// 因为需要自己自定义一个对话框，所以需要调用dialog.setView(view);
		// view是由自己编写的xml转化成的view对象
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_change_card, null);
		mDia_Et_Password = (EditText) view
				.findViewById(R.id.dia_ChangeCard_EditText);
		Button btn_Contacts = (Button) view
				.findViewById(R.id.dia_ChangeCard_Btn_Contacts);
		Button btn_Cancle = (Button) view
				.findViewById(R.id.dia_ChangeCard_Btn_cancle);
		Button btn_Sure = (Button) view
				.findViewById(R.id.dia_ChangeCard_Btn_sure);
		btn_Contacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterChooseContacts();
			}
		});
		btn_Cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btn_Sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SpUtil.putString(BaseApplication.getInstance(),
						ConstantValue.CONTACT_PHONE, mDia_Et_Password.getText()
								.toString());
				if (TextUtils.isEmpty(mDia_Et_Password.getText().toString())) {
					mSIV_ContactNumber.setArrowRightText("尚未设置安全号码");
					SpUtil.putBoolean(AntithiefSettingActivity.this,
							ConstantValue.CONTACT_PHONE_SERVICE, false);
					mTB_ChangeCardMsg.setChecked(false);
				} else {
					mSIV_ContactNumber.setArrowRightText(mDia_Et_Password
							.getText().toString());
				}
				dialog.dismiss();
			}
		});
		// dialog.setView(view);
		// 为了兼容低版本 2.3.3 给对话框设置为无内边距
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 初始化换卡通知信息
	 */
	private void initChangeCardMsg() {
		mTB_ChangeCardMsg = (TextCheckBoxView) findViewById(R.id.Antithief_Setting_ChangeCardMsg);
		mTB_ChangeCardMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean checked = mTB_ChangeCardMsg.isChecked();// 当前条目是否选中状态
				mTB_ChangeCardMsg.setChecked(!checked);
				SpUtil.putBoolean(AntithiefSettingActivity.this,
						ConstantValue.CONTACT_PHONE_SERVICE, !checked);
			}
		});
		String contactPhone = SpUtil.getString(AntithiefSettingActivity.this,
				ConstantValue.CONTACT_PHONE, "");
		boolean open = SpUtil.getBoolean(AntithiefSettingActivity.this,
				ConstantValue.CONTACT_PHONE_SERVICE, false);
		if (!open) {
			// 如果没有绑定换卡通知,checkBox为未选中状态
			mTB_ChangeCardMsg.setChecked(false);
		} else {
			mTB_ChangeCardMsg.setChecked(true);
		}
		/*
		 * if (TextUtils.isEmpty(contactPhone)) {
		 * 
		 * }else {
		 * 
		 * }
		 */
	}

	/**
	 * 初始化手机防盗服务
	 */
	private void initAntithiefService() {
		mTB_AntithiefService = (TextCheckBoxView) findViewById(R.id.Antithief_Setting_AntithiefService);
		mTB_AntithiefService.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(getSimNumber())) {
					// 无sim卡
					Toast.makeText(BaseApplication.getInstance(), "未检测到sim卡",
							Toast.LENGTH_SHORT).show();
				} else {
					boolean checked = mTB_AntithiefService.isChecked();
					mTB_AntithiefService.setChecked(!checked);
					SpUtil.putBoolean(AntithiefSettingActivity.this,
							ConstantValue.SETUP_OVER, !checked);
					if (!checked) {
						// 存储sim卡序列号
						SpUtil.putString(AntithiefSettingActivity.this,
								ConstantValue.SIM_NUMBER, getSimNumber());
					} else {
						// 抹除掉sim卡序列号（将节点抹掉）
						SpUtil.remove(AntithiefSettingActivity.this,
								ConstantValue.SIM_NUMBER);
					}
				}
			}
		});
		boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER,
				false);
		mTB_AntithiefService.setChecked(setup_over);
	}

	@Override
	protected void initVariables() {

	}

	/**
	 * 由本机获取sim卡卡号 TelephoneManager
	 */
	protected String getSimNumber() {
		TelephonyManager tpManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		simSerialNumber = tpManager.getSimSerialNumber();
		return simSerialNumber;
	}

	/**
	 * 选择联系人
	 */
	protected void enterChooseContacts() {
		Intent intent = new Intent(AntithiefSettingActivity.this,
				ContactsListActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 返回到当前界面的时候 接受结果的方法
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == 100) {
				if (resultCode == RESULT_OK) {
					// 获取返回过来的电话号码，并设置到输入框中
					String phoneNumber = data.getStringExtra("phoneNumber");
					mDia_Et_Password.setText(phoneNumber);
					// 存储联系人至sp中
					SpUtil.putString(BaseApplication.getInstance(),
							ConstantValue.CONTACT_PHONE, phoneNumber);

				}
			}
		}
	}
}
