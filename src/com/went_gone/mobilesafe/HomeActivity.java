package com.went_gone.mobilesafe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.went_gone.mobilesafe.adapter.CommonAdapter;
import com.went_gone.mobilesafe.adapter.ViewHolder;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.db.dao.BlackNumberDao;
import com.went_gone.mobilesafe.engine.ProcessInfoProvider;
import com.went_gone.mobilesafe.engine.SmsBackup;
import com.went_gone.mobilesafe.ui.LineGridView;
import com.went_gone.mobilesafe.ui.ProgressBarArc;
import com.went_gone.mobilesafe.ui.SlidingMenu;
import com.went_gone.mobilesafe.ui.SlidingMenu;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.MD5Util;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 首页
 * @author Went_Gone
 *
 */
/**
 * @author Went_Gone
 *
 */
public class HomeActivity extends BaseActivity {
	private LineGridView mLineGridView;
	private MainGridViewAdapter mMainGridViewAdapter;
	private List<ToolsImageName> mTINameList;
	private SlidingMenu mSlidingMenuDrawer;
	private TextView mTVMore;
	private GridView mSlidGridView;
	private List<ToolsImageName> mTINameListSlid;
	private GridViewAdapter mGViewAdapter;
	private ViewGroup mMenu;
	private int count;
	private ProgressBarArc arc;
	/**
	 * 总进程
	 */
	private long mTotilMemory;
	/**
	 * 可用进程
	 */
	private long mAvailMemory;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		arc.setMaxProgress(100);
//		mHandler.sendEmptyMessage(0x2);
	}
	@Override
	protected void initVariables() {
		initGridViewData();
		BlackNumberDao dao = BlackNumberDao.getInstance();
		/*dao.insert("15206651832", "王吉帅", "1");
		dao.insert("18866590519", "王震", "3");*/
		initMemory();
	}
	/**
	 * 初始化GridView的数据
	 */
	private void initGridViewData() {
		mTINameListSlid = new ArrayList<ToolsImageName>();
		mTINameListSlid.add(new ToolsImageName("归属地",R.drawable.attribution));
		mTINameListSlid.add(new ToolsImageName("防盗",R.drawable.mobile_anti_theft));
		mTINameListSlid.add(new ToolsImageName("常用号码",R.drawable.common_number));
		mTINameListSlid.add(new ToolsImageName("短信备份",R.drawable.sms_backup));
    	mGViewAdapter = new GridViewAdapter(this, mTINameListSlid, R.layout.item_gridview_slidleft);
    	mSlidGridView.setAdapter(mGViewAdapter);
		
		
		mTINameList = new ArrayList<HomeActivity.ToolsImageName>();
		mTINameList.add(new ToolsImageName("进程管理",R.drawable.progress_management));
		mTINameList.add(new ToolsImageName("应用管理",R.drawable.app_management));
		mTINameList.add(new ToolsImageName("通信卫士",R.drawable.communication_guard));
		mTINameList.add(new ToolsImageName("缓存清理",R.drawable.cache_clean));
		mMainGridViewAdapter = new MainGridViewAdapter(HomeActivity.this,mTINameList,R.layout.item_gridview_main);
		mLineGridView.setAdapter(mMainGridViewAdapter);
		
        checkItemGridViewSlid();
        
        LinearLayout mLayout = (LinearLayout) findViewById(R.id.Main_LinearLayout);
        mLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//手机杀毒
				startActivity(new Intent(HomeActivity.this, AntiVirusActivity.class));
			}
		});
	}
	/**
	 * SlidGridView的点击事件
	 */
	private void checkItemGridViewSlid() {
		mSlidGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//归属地
					enterAttribution();
					break;
				case 1:
					//防盗
					enterMoblieSafe();
					break;
				case 2:
					//常用号码(黄页)
					startActivity(new Intent(HomeActivity.this,CommonNumberActivity.class));
					break;
				case 3:
					//短信备份
					initSmsBackUp();
					break;
				
				default:
					break;
				}
			}
		});
		mLineGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//进程管理
					startActivity(new Intent(HomeActivity.this, ProcessManagementActivity.class));
					break;
				case 1:
					//应用管理
					enterApplicationManager();
					break;
				case 2:
					//通信卫士
					enterCallMsgSafe();
					break;
				case 3:
					startActivity(new Intent(HomeActivity.this, CacheCleanActivity.class));
					break;
				}
			}
		});
	}
	/**
	 * 应用管理
	 */
	protected void enterApplicationManager() {
		startActivity(new Intent(this, AppliactionManagerActivity.class));
	}
	/**
	 * 备份短信
	 */
	protected void initSmsBackUp() {
		//创建一个带进度条的对话框
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("备份短信");
		//指定进度条样式  (水平)
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.show();
		//直接调用备份短信的方法（可能是耗时操作）
		new Thread(){
			public void run() {
				String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"smsbackup.xml";
				SmsBackup.backup(HomeActivity.this,path,progressDialog);
				progressDialog.dismiss();
			};
		}.start();
	}
	/**
	 * 进入通信卫士界面
	 */
	protected void enterCallMsgSafe() {
		Intent intent = new Intent(HomeActivity.this,CallMsgSafe.class);
		startActivity(intent);
	}
	/**
	 * 进入归属地查询界面
	 */
	protected void enterAttribution() {
		Intent intent = new Intent(HomeActivity.this,AttributionActivity.class);
		startActivity(intent);
	}
	/**
	 *进入手机防盗 
	 */
	private void enterMoblieSafe() {
		//判断本地是否有存储密码
		String psd = SpUtil.getString(getApplicationContext(),ConstantValue.MOBLIE_SAFE_PASSWORD,"");
		if (TextUtils.isEmpty(psd)) {
			//初始设置密码则跳转到设置密码界面
			enterSettingPassword();						
		}else {
			//若已设置密码则开启对话框
			showConfirmDialog();						
		}
	}
	/**
	 * 跳转到设置密码界面
	 */
	protected void enterSettingPassword() {
		Intent intent = new Intent(HomeActivity.this, SettingPasswordActivity.class);
		startActivity(intent);
	}
	/**
	 * 确认密码的对话框
	 */
	protected void showConfirmDialog() {
		//因为需要自己自定义一个对话框，所以需要调用dialog.setView(view);
		//view是由自己编写的xml转化成的view对象
		
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this,R.layout.dialog_confirm_psd,null);
		final EditText et_Password = (EditText) view.findViewById(R.id.dia_Confirm_psd_ET_password);
		Button btn_Cancle = (Button) view.findViewById(R.id.dia_Confirm_psd_Btn_cancle);
		Button btn_Sure = (Button) view.findViewById(R.id.dia_Confirm_psd_Btn_sure);
		
		et_Password.setFocusable(true);  
		et_Password.setFocusableInTouchMode(true); 
		et_Password.requestFocus();
//		InputMethodManager inputManager = (InputMethodManager) et_Password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		inputManager.showSoftInput(et_Password,0);
		
		btn_Cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btn_Sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断有没有填写密码
				String password = et_Password.getText().toString();
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "请填写密码",Toast.LENGTH_SHORT).show();
				}else {
					//判断填写的密码与本地密码是否一致
					//将存储在sp中32位的密码获取出来，然后将输入的密码同样进行md5   与其匹配
					String localPsd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBLIE_SAFE_PASSWORD,"");
					password = MD5Util.encoder(password);
					if (password.equals(localPsd)) {
						//相同，则跳转到手机防盗界面
						enterAnitithief();
						dialog.dismiss();
					}else {
						//不相同则提示	
						Toast.makeText(getApplicationContext(), "密码不正确",Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
//		dialog.setView(view);
		//为了兼容低版本  2.3.3   给对话框设置为无内边距
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		
		//0.5s后自动弹出软键盘，便于用户输入
		Timer timer = new Timer();  
	     timer.schedule(new TimerTask()  
	     {  
	           
	         public void run()  
	         {  
	             InputMethodManager inputManager =  
	                 (InputMethodManager)BaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);  
	             inputManager.showSoftInput(et_Password, 0);  
	         }  
	           
	     },  
	         500);  
	}
	/**
	 * 跳转到手机防盗界面
	 */
	protected void enterAnitithief() {
		Intent intent = new Intent(HomeActivity.this,AntithiefActivity.class);
		startActivity(intent);
	}
	@Override
	protected void initViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.act_home);
		mLineGridView = (LineGridView) findViewById(R.id.Main_GridView);
		mTVMore = (TextView) findViewById(R.id.Main_Text_more);
		mSlidingMenuDrawer = (SlidingMenu) findViewById(R.id.Main_SlidingMenu);
		mTVMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				controlSlidingMenu();
			}
		});
		
		mMenu = (ViewGroup) mSlidingMenuDrawer.getChildAt(0);
		mSlidGridView = (GridView) mMenu.findViewById(R.id.slip_left_GridView);
		
		TextView tv_Menu_Setting = (TextView) mMenu.findViewById(R.id.slip_left_TV_setting);
		tv_Menu_Setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enterSetting();
			}
		});
		arc = (ProgressBarArc) findViewById(R.id.progressbararc);
	}
	 /**
	 * 跳转到设置界面
	 */
	protected void enterSetting() {
		Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
		startActivity(intent);
	}
	/**
	 *控制侧拉栏 
	 */
	protected void controlSlidingMenu() {
//		mSlidingMenu.toggle();
		mSlidingMenuDrawer.toggle();
	}
	class ToolsImageName {
	    	private String name;
	    	private int imagesId;
	    	
			public ToolsImageName(String name, int imagesId) {
				super();
				this.name = name;
				this.imagesId = imagesId;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public int getImagesId() {
				return imagesId;
			}
			public void setImagesId(int imagesId) {
				this.imagesId = imagesId;
			}
	    	
	    }
	class MainGridViewAdapter extends CommonAdapter<ToolsImageName>{

		public MainGridViewAdapter(Context context,
				List<ToolsImageName> mDatas, int layoutId) {
			super(context, mDatas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ToolsImageName t) {
			holder.setImageResource(R.id.item_GridView_Main_IV, t.getImagesId())
			.setTextViewText(R.id.item_GridView_Main_TV, t.getName());
		}
	}
	
	
	    class GridViewAdapter extends CommonAdapter<ToolsImageName>{

			public GridViewAdapter(Context context, List<ToolsImageName> mDatas,
					int layoutId) {
				super(context, mDatas, layoutId);
			}

			@Override
			public void convert(ViewHolder holder, ToolsImageName t) {
				holder.setTextViewText(R.id.item_GridView_SlidLeft_TV, t.getName())
				.setImageResource(R.id.item_GridView_SlidLeft_IM,t.getImagesId());
			}
	    }
	    
	    /**
		 * 初始化进程所占内存
		 */
		private void initMemory() {
			mTotilMemory = ProcessInfoProvider.getTotilMemory(this);
			mAvailMemory = ProcessInfoProvider.getAvailMemory(this);
			arc.setMaxProgress(mTotilMemory);
			arc.setCurrentProgress(mTotilMemory-mAvailMemory);
		}
}
