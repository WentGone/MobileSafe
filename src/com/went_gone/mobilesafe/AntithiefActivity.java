package com.went_gone.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.entity.ContactsEntity;
import com.went_gone.mobilesafe.ui.AntithiefItemView;
import com.went_gone.mobilesafe.ui.AntithiefItemView.OnProtectedListener;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.MD5Util;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

public class AntithiefActivity extends BaseActivity {
	private static final String TAG = "Antithief";
	private TextView mTV_ComeBack;
	private AntithiefItemView mAIV_Lock, mAIV_Alert, mAIV_Location,
			mAIV_DestoryDate, mAIV_ChangeCard, mAIV_BackupsDate;
	private RelativeLayout mRL_Close, mRL_Open,mRL_Introduce;
	private Button mBtn_Antifief;
	private ImageView mIV_State, mIV_Setting;
	List<AntithiefItemView> mAIV_ViewList = new ArrayList<AntithiefItemView>();
	private String simSerialNumber = "";
	private EditText mDia_Et_Password;

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_antithief);
		mTV_ComeBack = (TextView) findViewById(R.id.ComeBack);
		ImageView mIM_Setting = (ImageView) findViewById(R.id.MobileSafe_Setting);
		mAIV_Lock = (AntithiefItemView) findViewById(R.id.MobileSafe_AIV_LockMobile);
		mAIV_Alert = (AntithiefItemView) findViewById(R.id.MobileSafe_AIV_Alert);
		mAIV_Location = (AntithiefItemView) findViewById(R.id.MobileSafe_AIV_LocationTracking);
		mAIV_DestoryDate = (AntithiefItemView) findViewById(R.id.MobileSafe_AIV_DestoryDate);
		mAIV_ChangeCard = (AntithiefItemView) findViewById(R.id.MobileSafe_AIV_ChangeCard);
		mAIV_BackupsDate = (AntithiefItemView) findViewById(R.id.MobileSafe_AIV_BackupsDate);
		mAIV_ViewList.add(mAIV_Lock);
		mAIV_ViewList.add(mAIV_Alert);
		mAIV_ViewList.add(mAIV_Location);
		mAIV_ViewList.add(mAIV_DestoryDate);
		mAIV_ViewList.add(mAIV_ChangeCard);
		mAIV_ViewList.add(mAIV_BackupsDate);

		mRL_Close = (RelativeLayout) findViewById(R.id.MobileSafe_Relayout_Close);
		mRL_Open = (RelativeLayout) findViewById(R.id.MobileSafe_Relayout_Open);
		mRL_Introduce = (RelativeLayout) findViewById(R.id.Antithief_Introduce);
		mRL_Introduce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enterAntithiefIntroduce();
			}
		});
		mBtn_Antifief = (Button) findViewById(R.id.MobileSafe_OpenAnitithief);
		mIV_State = (ImageView) findViewById(R.id.MoblieSafe_IM_state);
		mIV_Setting = (ImageView) findViewById(R.id.MobileSafe_Setting);

		mIV_Setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enterSettingActivity();
			}
		});
		mBtn_Antifief.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SpUtil.putBoolean(AntithiefActivity.this,
						ConstantValue.SETUP_OVER, true);
				getSimNumber();
				if (TextUtils.isEmpty(getSimNumber())) {
					// 如果序列卡号为空
					Toast.makeText(BaseApplication.getInstance(), "未检测到sim卡",
							Toast.LENGTH_SHORT).show();
				} else {
					String sim_number = SpUtil.getString(
							AntithiefActivity.this, ConstantValue.SIM_NUMBER,
							"");
					SpUtil.putString(AntithiefActivity.this,
							ConstantValue.SIM_NUMBER, getSimNumber());
					decideProtected();
				}
			}
		});
		mTV_ComeBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 进入防盗介绍界面
	 */
	protected void enterAntithiefIntroduce() {
		Intent intent = new Intent(AntithiefActivity.this,AntithiefIntroduceActivity.class);
		startActivity(intent);
	}

	/**
	 * 由本机获取sim卡卡号 TelephoneManager
	 */
	protected String getSimNumber() {
		TelephonyManager tpManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 获取sim卡的序列卡号
		simSerialNumber = tpManager.getSimSerialNumber();
		return simSerialNumber;
	}

	/**
	 * 进入手机防盗设置界面
	 */
	protected void enterSettingActivity() {
		Intent intent = new Intent(AntithiefActivity.this,
				AntithiefSettingActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		decideProtected();
	}

	/**
	 * 判断是不是已开启保护状态
	 */
	private void decideProtected() {
		boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER,
				false);
		if (setup_over) {
			mRL_Close.setVisibility(View.INVISIBLE);
			mRL_Open.setVisibility(View.VISIBLE);
			mIV_State.setImageResource(R.drawable.antithief_state_open);
		} else {
			mRL_Close.setVisibility(View.VISIBLE);
			mRL_Open.setVisibility(View.INVISIBLE);
			mIV_State.setImageResource(R.drawable.antithief_state_close);
		}
		initAIV_Views(setup_over);
	}

	@Override
	protected void initVariables() {

	}

	/**
	 * 初始化AntithiefItemViews
	 * 
	 * @param isProtected
	 *            保护状态
	 */
	private void initAIV_Views(boolean isProtected) {
		if (isProtected) {
			// 保护状态开启时
			for (int i = 0; i < mAIV_ViewList.size(); i++) {
				if (i == 4) {
					String contactPhone = SpUtil.getString(BaseApplication.getInstance(),ConstantValue.CONTACT_PHONE,"");
					boolean isProtecteds = false;
					if (TextUtils.isEmpty(contactPhone)) {
						isProtecteds = false;
					}else {
						isProtecteds = true;
					}
					// 换卡通知
					mAIV_ViewList.get(i).setProtected(isProtecteds, contactPhone,
							new OnProtectedListener() {
								@Override
								public void onProtected() {
									showChangeCardDailog();
								}
							});
					boolean open = SpUtil.getBoolean(AntithiefActivity.this,ConstantValue.CONTACT_PHONE_SERVICE,false);
					mAIV_ViewList.get(i).setImageViewState(open);
				} else if (i == 5) {
					// 数据备份
					mAIV_ViewList.get(i).setProtected(!isProtected, "",
							new OnProtectedListener() {
								@Override
								public void onProtected() {
									ToastUtil.show(
											BaseApplication.getInstance(), "e额");
								}
							});
				} else {
					// 其他
					mAIV_ViewList.get(i).setProtected(isProtected);
				}
			}
		} else {
			// 未保护状态
			for (int i = 0; i < mAIV_ViewList.size(); i++) {
				mAIV_ViewList.get(i).setProtected(isProtected);
			}
		}
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
				//判断输入框是否为空
				if (TextUtils.isEmpty(mDia_Et_Password.getText().toString())) {
					Toast.makeText(BaseApplication.getInstance(),"联系人不能为空", Toast.LENGTH_SHORT).show();
				}else {
					SpUtil.putString(BaseApplication.getInstance(),ConstantValue.CONTACT_PHONE,mDia_Et_Password.getText().toString());
					SpUtil.putBoolean(BaseApplication.getInstance(),ConstantValue.CONTACT_PHONE_SERVICE,true);
					mAIV_ChangeCard.setTextViewState(mDia_Et_Password.getText().toString());
					dialog.dismiss();
				}
			}
		});
		// dialog.setView(view);
		// 为了兼容低版本 2.3.3 给对话框设置为无内边距
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 选择联系人
	 */
	protected void enterChooseContacts() {
		Intent intent = new Intent(AntithiefActivity.this,ContactsListActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//返回到当前界面的时候  接受结果的方法
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			if (requestCode==100) {
				if (resultCode==RESULT_OK) {
					//获取返回过来的电话号码，并设置到输入框中
					String phoneNumber = data.getStringExtra("phoneNumber");
					mDia_Et_Password.setText(phoneNumber);
					//存储联系人至sp中
					SpUtil.putString(BaseApplication.getInstance(),ConstantValue.CONTACT_PHONE, phoneNumber);
					mAIV_ChangeCard.setTextViewState(phoneNumber);
				}
			}
		}
	}
	
	
}
