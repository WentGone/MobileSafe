package com.went_gone.mobilesafe.base;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.went_gone.mobilesafe.engine.ProcessInfoProvider;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseActivity extends Activity {
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initViews(savedInstanceState);
		initVariables();
		loadData();
		//2.创建手势管理的对象，用作管理在onTouchEvent(event)传递过来的手势动作
		gestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						// 监听手势的移动
						// 按下 移动到 x轴的速度 y轴的移动速度
						if (e1.getX() - e2.getX() > 0) {
							// 由右向左移动到下一页
							ToastUtil.show(BaseActivity.this,"由右向左移动到下一页");
						}
						if (e1.getX() - e2.getX() < 0) {
							// 由左向右移动到上一页
							ToastUtil.show(BaseActivity.this,"由左向右移动到上一页");
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}

	/**
	 * 初始化layout的布局文件
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void initViews(Bundle savedInstanceState);

	/**
	 * 初始化变量
	 */
	protected abstract void initVariables();

	/**
	 * 初始化UI的显示
	 */
	// protected abstract void initUI();
	/**
	 * 获取数据
	 */
	protected void loadData() {

	}

	// 1.监听屏幕上响应的事件类型(按下(1次)，移动(多次)，抬起(1次))
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 2.通过手势的处理类，接受多种类型的事件用于处理
//		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	public void comeBack(View view){
		this.finish();
	}
}
