package com.went_gone.mobilesafe.net;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

public abstract class HttpCallBack {
	
	public abstract void onSuccess(ResponseInfo<String> response);
	public  void onFailure(HttpException e,String s){
		
	}
}
