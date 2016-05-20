package com.went_gone.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
	private static SharedPreferences sp;

	/**
	 * 写入boolean变量至sp中
	 * @param context  上下文
	 * @param key   节点名称
	 * @param value  节点值boolean
	 */
	public static void putBoolean(Context context,String key,boolean value) {
		if (sp == null) {			
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	/**
	 * 读取boolean标示
	 * @param context  上下文
	 * @param key   节点名称
	 * @param defaultValue    默认值boolean
	 * @return   此节点读取到的值
	 */
	public static boolean getBoolean(Context context,String key,boolean defaultValue) {
		if (sp == null) {			
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defaultValue);
	}
	/**
	 * 写入String变量至sp中
	 * @param context  上下文
	 * @param key   节点名称
	 * @param value  节点值String
	 */
	public static void putString(Context context,String key,String value) {
		if (sp == null) {			
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	/**
	 * 读取String标示
	 * @param context  上下文
	 * @param key   节点名称
	 * @param defaultValue    默认值String
	 * @return   此节点读取到的值
	 */
	public static String getString(Context context,String key,String defaultValue) {
		if (sp == null) {			
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getString(key, defaultValue);
	}
	/**
	 * 写入Int变量至sp中
	 * @param context  上下文
	 * @param key   节点名称
	 * @param value  节点值int
	 */
	public static void putInt(Context context,String key,int value) {
		if (sp == null) {			
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}
	/**
	 * 读取Int标示
	 * @param context  上下文
	 * @param key   节点名称
	 * @param defaultValue    默认值int
	 * @return   此节点读取到的值
	 */
	public static int getInt(Context context,String key,int defaultValue) {
		if (sp == null) {			
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getInt(key, defaultValue);
	}
	/**
	 * 移除指定的节点内容及本身
	 * @param context 上下文
	 * @param key  需要移除的节点名称
	 */
	public static void remove(Context context,String key) {
		if (sp == null) {			
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().remove(key).commit();
	}
}
