package com.went_gone.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.db.AppLockOpenHelper;
import com.went_gone.mobilesafe.db.BlackNumberOpenHelper;
import com.went_gone.mobilesafe.entity.BlackNumberEntity;

public class AppLockDao{
	private String mDateBaseName = "applock";
	private AppLockOpenHelper mAppLockOpenHelper;
	private Context context;

	// 单例模式 使其在应用中之创建一个对象
	private AppLockDao() {
		context=BaseApplication.getInstance();
		mAppLockOpenHelper = new AppLockOpenHelper(BaseApplication.getInstance());
	};

	private static AppLockDao appLockDao;

	public static AppLockDao getInstance() {
		if (appLockDao == null) {
			synchronized (AppLockDao.class) {
				if (appLockDao == null) {
					appLockDao = new AppLockDao();
				}
			}
		}
		return appLockDao;
	}
	
	/**
	 * 插入一条加锁应用
	 * @param packageName
	 */
	public void insert(String packageName){
		SQLiteDatabase db = mAppLockOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packagename", packageName);
		db.insert(mDateBaseName, null, values);
		db.close();
		
		context.getContentResolver().notifyChange(Uri.parse("content://applock/change"),null);
		
	}
	
	/**
	 * 删除一条加锁应用
	 * @param packageName
	 */
	public void delete(String packageName){
		SQLiteDatabase db = mAppLockOpenHelper.getWritableDatabase();
		db.delete(mDateBaseName, "packagename = ?", new String[]{packageName});
		db.close();
		context.getContentResolver().notifyChange(Uri.parse("content://applock/change"),null);
	}
	
	/**
	 * 查询所有
	 * @return
	 */
	public List<String> selectAll(){
		SQLiteDatabase db = mAppLockOpenHelper.getWritableDatabase();
		Cursor cursor = db.query(mDateBaseName,  new String[]{"packagename"}, null, null, null, null, null);
		List<String> lockPackageName=new ArrayList<String>();
		while (cursor.moveToNext()) {
			lockPackageName.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return lockPackageName;
	}
}
