package com.went_gone.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.went_gone.mobilesafe.adapter.ProcessInfoAdapter;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.engine.ProcessInfoProvider;
import com.went_gone.mobilesafe.entity.ProcessInfoEntity;
import com.went_gone.mobilesafe.utils.ToastUtil;

/**
 * 进程管理
 * @author Went_Gone
 *
 */
public class ProcessManagementActivity extends BaseActivity {
	private List<ProcessInfoEntity> mProcessList;
	private List<ProcessInfoEntity> mCustomerList;
	private List<ProcessInfoEntity> mSystemList;
	private ProcessInfoAdapter mAdapter;
	private ListView mLV_Process;
	private TextView mTV_Memory;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (mAdapter == null) {
				mAdapter = new ProcessInfoAdapter(mProcessList, ProcessManagementActivity.this);
				mLV_Process.setAdapter(mAdapter);
			}
		};
	};
	private long mAvailMemory;
	private String mTotilString;

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_process_management);
		mTV_Memory = (TextView) findViewById(R.id.act_process_mg_TV_Memory);
		mLV_Process = (ListView) findViewById(R.id.act_process_mg_LV);
		initClear();
	}

	/**
	 *初始化立即加速 
	 */
	private void initClear() {
		Button btn_Clear = (Button) findViewById(R.id.act_process_mg_Btn_Clear);
		btn_Clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearProcess();
			}
		});
	}

	/**
	 * 清理进程
	 */
	protected void clearProcess() {
		//创建一个记录需要杀死进程的集合
		List<ProcessInfoEntity> processClear = new ArrayList<ProcessInfoEntity>();
		long toatlReleaseSpace = 0;
		//获取选中的进程
		for (ProcessInfoEntity processInfo : mProcessList) {
			if (processInfo.getPackageName().equals(getPackageName())) {
				continue;
			}
			if (processInfo.isCheck()) {
				//被选中  需要清理的进程
				//mProcessList.remove(processInfo);  //不能在集合循环过程中，移除集合中的对象
				//记录需要杀死的进程
				processClear.add(processInfo);
				toatlReleaseSpace += processInfo.getMemory();
			}
			ProcessInfoProvider.killProcess(this,processInfo);
		}
		//移除mProcessList中的需要杀死的进程对象
		for (ProcessInfoEntity processInfoEntity : processClear) {
			mProcessList.remove(processInfoEntity);
		}
		//在集合改变后需要通知适配器刷新数据
		if (mAdapter!=null) {
			mAdapter.notifyDataSetChanged();
		}
		
		mAvailMemory += toatlReleaseSpace;
		mTV_Memory.setText("总共/可用："+mTotilString+"/"+Formatter.formatFileSize(this, mAvailMemory));
		ToastUtil.show(this, "释放内存"+Formatter.formatFileSize(this, toatlReleaseSpace));
	}

	@Override
	protected void initVariables() {
		initMemory();
		initProcessData();
	}

	/**
	 * 初始化进程信息
	 */
	private void initProcessData() {
		new Thread(){
			public void run() {
				mProcessList = ProcessInfoProvider.getProcessInfo(ProcessManagementActivity.this);
				mCustomerList = new ArrayList<ProcessInfoEntity>();
				mSystemList = new ArrayList<ProcessInfoEntity>();
				for (ProcessInfoEntity processInfo : mProcessList) {
					if (processInfo.isSystem()) {
						//系统进程
						mSystemList.add(processInfo);
					}else {
						mCustomerList.add(processInfo);
					}
				}
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}

	/**
	 * 初始化进程所占内存
	 */
	private void initMemory() {
		long totilMemory = ProcessInfoProvider.getTotilMemory(this);
		mAvailMemory = ProcessInfoProvider.getAvailMemory(this);
		mTotilString = Formatter.formatFileSize(this, totilMemory);
		String availString = Formatter.formatFileSize(this, mAvailMemory);
		mTV_Memory.setText("总共/可用："+mTotilString+"/"+availString);
	}

}
