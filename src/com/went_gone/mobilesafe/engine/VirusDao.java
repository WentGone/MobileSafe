package com.went_gone.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 病毒数据库的操作类
 * @author Went_Gone
 *
 */
public class VirusDao {
	// 指定数据库的访问路径
	public static String path = "data/data/com.went_gone.mobilesafe/files/antivirus.db";

	/**
	 *获取数据库MD5码
	 * @return
	 */
	public static List<String> getVirusList(){
		List<String> md5String = new ArrayList<String>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("datable", new String[]{"md5"}, null, null, null, null, null);
		while (cursor.moveToNext()) {
			md5String.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return md5String;
	}
}
