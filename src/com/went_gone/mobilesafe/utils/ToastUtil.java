package com.went_gone.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	/**
	 * 打印吐司
	 * @param context 上下文
	 * @param content 内容
	 */
	public static void show(Context context,String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
}
