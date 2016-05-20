package com.went_gone.mobilesafe.base;

import android.app.Application;

public class BaseApplication extends Application{
	public static BaseApplication application;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		application = this;
	}
	public static BaseApplication getInstance(){
		return application;
	}
}
