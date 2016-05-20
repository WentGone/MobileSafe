package com.went_gone.mobilesafe.net;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HttpConnection {
	HttpUtils http = new HttpUtils();
	/*public abstract class HttpCallBack{
		public abstract void onSuccess(ResponseInfo<String> response);
		public  void onFailure(HttpException e,String s){
			
		}
//		void onFailure(HttpException e,String s);
	}*/
//	private HttpCallBack callBack;
	
/*	public void setCallBack(HttpCallBack callBack) {
		this.callBack = callBack;
	}*/
	
	private static HttpConnection conn;
	public static HttpConnection getInstance(){
		synchronized (HttpConnection.class) {
			if (conn == null) {
				conn = new HttpConnection();
			}
		}
		return conn;
	}
	private HttpCallBack callBack;
	
	public void setCallBack(HttpCallBack callBack) {
		this.callBack = callBack;
	}

	public void httpGet(String url,final HttpCallBack callBack){
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				callBack.onFailure(arg0, arg1);
//				Log.e("Splash","Failure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				callBack.onSuccess(arg0);
			}
			@Override
			public void onStart() {
				super.onStart();
			}
		});
	}
}
