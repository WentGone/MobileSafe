package com.went_gone.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.db.BlackNumberOpenHelper;
import com.went_gone.mobilesafe.entity.PhoneEntity;
import com.went_gone.mobilesafe.entity.SmsEntity;

public class PhoneDao {
	private static final String TABLE_NAME = "phoneTable";
	private BlackNumberOpenHelper openHelper;
	private PhoneDao(){
		openHelper = new BlackNumberOpenHelper(BaseApplication.getInstance());
	}
	private static PhoneDao dao;
	public static PhoneDao getInstance(){
		if (dao == null) {
			synchronized (PhoneDao.class) {
				if (dao == null) {
					dao = new PhoneDao();
				}
			}
		}
		return dao;
	}
	/**
	 * 插入一条拦截到的电话
	 * @param phone
	 * @param name
	 */
	public void insert(String phone,String name){
		SQLiteDatabase db = openHelper.getWritableDatabase();
		long datelong = System.currentTimeMillis();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("name", name);
		values.put("dateLong", datelong);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	/**
	 * 查找所有拦截的电话
	 * 
	 * @return 所有的拦截的电话并以倒序排列
	 */
	public List<PhoneEntity> findAll() {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		// "_id desc" 以_id倒序排列
		Cursor cursor = db.query(TABLE_NAME,
				new String[] { "phone", "name","dateLong" }, null, null, null, null,
				"_id desc");
		List<PhoneEntity> phoneList = new ArrayList<PhoneEntity>();
		while (cursor.moveToNext()) {
			PhoneEntity phoneEntity = new PhoneEntity();
			phoneEntity.setPhone(cursor.getString(0));
			phoneEntity.setName(cursor.getString(1));
			phoneEntity.setDateLong(cursor.getLong(2));
			phoneList.add(phoneEntity);
		}
		cursor.close();
		db.close();
		return phoneList;
	}
}
