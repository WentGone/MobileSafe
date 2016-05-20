package com.went_gone.mobilesafe;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.went_gone.mobilesafe.base.BaseActivity;
import com.went_gone.mobilesafe.utils.ConstantValue;
import com.went_gone.mobilesafe.utils.SpUtil;
import com.went_gone.mobilesafe.utils.ToastUtil;

public class ToastLocationActivity extends BaseActivity {
	private TextView mTV_Center, mTV_Top, mTV_Bottom;
	private int mScreenWidth;
	private int mScreenHeight;
	private long startTime;
	private long[] mHits = new long[2];

	@Override
	protected void initViews(Bundle savedInstanceState) {
		setContentView(R.layout.act_toast_location);
		mTV_Center = (TextView) findViewById(R.id.toast_location_TV_Center);
		mTV_Top = (TextView) findViewById(R.id.toast_location_TV_Top);
		mTV_Bottom = (TextView) findViewById(R.id.toast_location_TV_Bottom);

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mScreenWidth = wm.getDefaultDisplay().getWidth();
		mScreenHeight = wm.getDefaultDisplay().getHeight();

		// 获取开始显示的位置
		int locationX = SpUtil.getInt(ToastLocationActivity.this,
				ConstantValue.LEFT, 0);
		int locationY = SpUtil.getInt(ToastLocationActivity.this,
				ConstantValue.TOP, 0);
		if (locationY >= mScreenHeight / 2) {
			mTV_Top.setVisibility(View.VISIBLE);
			mTV_Bottom.setVisibility(View.INVISIBLE);
		} else {
			mTV_Bottom.setVisibility(View.VISIBLE);
			mTV_Top.setVisibility(View.INVISIBLE);
		}

		// TextView在相对布局中，所以其所在位置的规则需要由相对布局提供
		// 指定宽高都为自适应
		LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// 将左上角的坐标作用在TextViewCenter对呀的规则参数上
		layoutParams.leftMargin = locationX;
		layoutParams.topMargin = locationY;
		// 将以上规则作用在TextViewCenter上
		mTV_Center.setLayoutParams(layoutParams);

		// 监听某一控件的拖拽过程
		mTV_Center.setOnTouchListener(new OnTouchListener() {
			private int start_RawX;
			private int start_RawY;
			private int left;
			private int top;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					start_RawX = (int) event.getRawX();
					start_RawY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_MOVE:
					int move_RawX = (int) event.getRawX();
					int move_RawY = (int) event.getRawY();

					int disRawX = move_RawX - start_RawX;
					int disRawY = move_RawY - start_RawY;

					// 得到当前控件左上角的位置
					/*
					 * int left = mTV_Center.getLeft();//控件距离左边缘的距离(即为x) int top
					 * = mTV_Center.getTop();//控件距离上边缘的距离(即为y)
					 */

					left = mTV_Center.getLeft() + disRawX;
					top = mTV_Center.getTop() + disRawY;
					int right = mTV_Center.getRight() + disRawX;// 距离左边屏幕边距距离
					int bottom = mTV_Center.getBottom() + disRawY;

					// 容错处理mTV_Center不能被拖出屏幕外
					// 左边缘不能超出屏幕
					if (left < 0) {
						return true;
					}
					// 右边缘不能超出屏幕
					if (right > mScreenWidth) {
						return true;
					}
					// 上边缘不能超出屏幕
					if (top < 0) {
						return true;
					}
					// 下边缘不能超出屏幕(屏幕高度-通知栏的高度)
					if (bottom > mScreenHeight - 30) {
						return true;
					}
					// 可以将其组合成一个
					/*
					 * if (left < 0 || right > mScreenWidth || top < 0 || bottom
					 * > mScreenHeight - 30) { return true; }
					 */

					// 按照计算出来的坐标做展示
					mTV_Center.layout(left, top, right, bottom);

					// 重置起始坐标
					start_RawX = (int) event.getRawX();
					start_RawY = (int) event.getRawY();

					if (top >= mScreenHeight / 2) {
						mTV_Top.setVisibility(View.VISIBLE);
						mTV_Bottom.setVisibility(View.INVISIBLE);
					} else {
						mTV_Bottom.setVisibility(View.VISIBLE);
						mTV_Top.setVisibility(View.INVISIBLE);
					}

					break;

				case MotionEvent.ACTION_UP:
					// 记录最终位置
					SpUtil.putInt(ToastLocationActivity.this,
							ConstantValue.LEFT, mTV_Center.getLeft());
					SpUtil.putInt(ToastLocationActivity.this,
							ConstantValue.TOP, mTV_Center.getTop());
					break;
				}
				// return false;
				// 既要响应点击事件，又要响应拖拽过程，则此返回结果需要修改为false
				return false;
			}
		});

		// 双击
		mTV_Center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 双击事件 arraycopy拷贝数组
				// 源数组，从哪个位置开始拷贝，拷贝到哪个数组，拷贝到的数组的位置，拷贝的次数
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				
				int left = mScreenWidth / 2 - mTV_Center.getWidth() / 2;
				int top = mScreenHeight / 2 - mTV_Center.getHeight() / 2;
				int right = mScreenWidth / 2 + mTV_Center.getWidth() / 2;
				int bottom = mScreenHeight / 2 + mTV_Center.getHeight() / 2;
				if (mHits[mHits.length - 1] - mHits[0] < 300) {
					mTV_Center.layout(left, top, right, bottom);
				}
				
				// 记录最终位置
				SpUtil.putInt(ToastLocationActivity.this,
						ConstantValue.LEFT, mTV_Center.getLeft());
				SpUtil.putInt(ToastLocationActivity.this,
						ConstantValue.TOP, mTV_Center.getTop());
			}
		});
	}

	@Override
	protected void initVariables() {
	}

}
