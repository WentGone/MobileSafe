package com.went_gone.mobilesafe.db.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.db.BlackNumberOpenHelper;
import com.went_gone.mobilesafe.entity.BlackNumberEntity;
import com.went_gone.mobilesafe.entity.SmsEntity;


public class SmsDao {
	private static final String TABLE_NAME = "smsTable";
	private static SmsDao dao;
	private BlackNumberOpenHelper openHelper;
	private SimpleDateFormat format;
	private SmsDao(){
		openHelper = new BlackNumberOpenHelper(BaseApplication.getInstance());
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}
	public static SmsDao getInstance(){
		if (dao == null) {
			synchronized (SmsDao.class) {
				if (dao == null) {
					dao = new SmsDao();
				}
			}
		}
		return dao;
	}
	
	/**
	 * 插入一条拦截到的短信
	 * @param phone
	 * @param name
	 * @param content
	 */
	public void insert(String phone,String name,String content){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		long datelong = System.currentTimeMillis();
//		Date date = new Date(datelong);
//		String dateString = format.format(date);
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("name", name);
		values.put("content", content);
		values.put("dateLong", datelong);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	/**
	 * 查找所有拦截的短信
	 * 
	 * @return 所有的拦截的短信并以倒序排列
	 */
	public List<SmsEntity> findAll() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		// "_id desc" 以_id倒序排列
		Cursor cursor = db.query(TABLE_NAME,
				new String[] { "phone", "name","content","dateLong" }, null, null, null, null,
				"_id desc");
		List<SmsEntity> smsList = new ArrayList<SmsEntity>();
		while (cursor.moveToNext()) {
			SmsEntity smsEntity = new SmsEntity();
			smsEntity.setPhone(cursor.getString(0));
			smsEntity.setName(cursor.getString(1));
			smsEntity.setContent(cursor.getString(2));
			smsEntity.setDateLong(cursor.getLong(3));
			smsList.add(smsEntity);
		}
		cursor.close();
		db.close();
		return smsList;
	}
}
