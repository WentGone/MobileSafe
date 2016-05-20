package com.went_gone.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.went_gone.mobilesafe.adapter.BlackNumberAdapter;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.db.dao.BlackNumberDao;
import com.went_gone.mobilesafe.entity.BlackNumberEntity;
import com.went_gone.mobilesafe.ui.SettingItemView;

public class BlackListActivity extends BaseActivity {
	private List<BlackNumberEntity> mBlackNumberLists;
	private ListView mLV_BlackNumber;
	private BlackNumberDao dao;
	private BlackNumberAdapter mBlackNumberAdapter;
	private boolean isLoad;
	private int mCount;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mBlackNumberAdapter == null) {
				mBlackNumberAdapter = new BlackNumberAdapter(mBlackNumberLists,
						BlackListActivity.this,mCount);
				mLV_BlackNumber.setAdapter(mBlackNumberAdapter);
			}else {
				mBlackNumberAdapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	protected void initViews(Bundle savedInstanceState) {
		// 获取操作黑名单数据库的对象
		dao = BlackNumberDao.getInstance();
		setContentView(R.layout.act_black_list);
		TextView tv_ComeBack = (TextView) findViewById(R.id.ComeBack);
		tv_ComeBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BlackListActivity.this.finish();
			}
		});
		Button btn_Add = (Button) findViewById(R.id.blacklist_Add);
		mLV_BlackNumber = (ListView) findViewById(R.id.blacklist_ListView);
		btn_Add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogChooseAdd();
			}
		});
		// 监听滚动状态
		mLV_BlackNumber.setOnScrollListener(new OnScrollListener() {
			// 滚动过程中，状态发生改变调用此方法()
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				// OnScrollListener.SCROLL_STATE_FLING//飞速滚动
				// OnScrollListener.SCROLL_STATE_IDLE;//空闲状态
				// OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;//拿手触摸着滚动的状态

				// 容错出来
				if (mBlackNumberLists != null) {

					// 滚动空闲状态 最后一个条目可见(最后一条索引值>=当前数据适配器中总条目-1)
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
							&& mLV_BlackNumber.getLastVisiblePosition() >= mBlackNumberLists
									.size() - 1 && !isLoad) {
						// 加载下一页数据
						// isLoad防止重复加载的变量 如果正在加载 isLoad = true； 加载完毕后 isLoad =
						// false
						
						//如果条目的总数大于集合的大小时  才继续加载更多
						if (mCount>mBlackNumberLists.size()) {
							
							new Thread() {
								public void run() {
//								loadData(0);
									List<BlackNumberEntity> moreData = new ArrayList<BlackNumberEntity>();
									// mBlackNumberLists = dao.findAll();
									moreData = dao.findForPart(mBlackNumberLists.size());
									//添加后续数据的过程
									mBlackNumberLists.addAll(moreData);
									//通知Handler
									mHandler.sendEmptyMessage(0);
								};
							}.start();
						}
						
					}
				}

			}

			// 滚动过程中调用此方法
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 展示“添加黑名单”的Dialog
	 */
	protected void showDialogChooseAdd() {
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view_dialog_Add = View.inflate(this,
				R.layout.dialog_add_blacknumber_by, null);
		TextView tv_add_yourself = (TextView) view_dialog_Add
				.findViewById(R.id.dialog_add_blacknumber_addyourself);
		tv_add_yourself.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				addYourself();
			}
		});
		dialog.setView(view_dialog_Add, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 手动添加
	 */
	protected void addYourself() {
		Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view_dialog = View.inflate(this, R.layout.dialog_add_blacknumber,
				null);
		EditText et_Phone = (EditText) view_dialog
				.findViewById(R.id.dia_AddBlackNumber_ET_phone);
		EditText et_Name = (EditText) view_dialog
				.findViewById(R.id.dia_AddBlackNumber_ET_name);
		Button btn_Cancle = (Button) view_dialog
				.findViewById(R.id.dia_AddBlackNumber_Btn_Cancle);
		Button btn_Sure = (Button) view_dialog
				.findViewById(R.id.dia_AddBlackNumber_Btn_Sure);
		btn_Cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 让dialog消失
				dialog.dismiss();
			}
		});
		btn_Sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 添加黑名单
			}
		});
		dialog.setView(view_dialog, 0, 0, 0, 0);
		dialog.show();
	}

	@Override
	protected void initVariables() {
		new Thread() {

			public void run() {
//				loadData(0);
				mBlackNumberLists = new ArrayList<BlackNumberEntity>();
				// mBlackNumberLists = dao.findAll();
				mBlackNumberLists = dao.findForPart(0);
				mCount = dao.getCount();
				mHandler.sendEmptyMessage(0);
			};
		}.start();
	}
}
