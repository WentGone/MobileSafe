package com.went_gone.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.db.BlackNumberOpenHelper;
import com.went_gone.mobilesafe.entity.BlackNumberEntity;

public class BlackNumberDao {
	private String mDateBaseName = "blacknumber";
	private BlackNumberOpenHelper mBlackNumberOpenHelper;

	// 单例模式 使其在应用中之创建一个对象
	private BlackNumberDao() {
		mBlackNumberOpenHelper = new BlackNumberOpenHelper(
				BaseApplication.getInstance());
	}

	private static BlackNumberDao blackNumberDao;

	public static BlackNumberDao getInstance() {
		if (blackNumberDao == null) {
			synchronized (BlackNumberDao.class) {
				if (blackNumberDao == null) {
					blackNumberDao = new BlackNumberDao();
				}
			}
		}
		return blackNumberDao;
	}

	/**
	 * 插入一条数据
	 * @param phone	 电话号码
	 * @param name	备注  可不填
	 * @param mode	1:短信 2：电话 3：拦截所有(短信+电话)
	 */
	public void insert(String phone,String name, String mode) {
		if (phone.startsWith("+")) {
			phone = phone.substring(3);//去掉前三个字符
		}
		
		// 开启数据库，准备写入的操作
		SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
		// 表名 没有赋值时的默认值 要插入的集合
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		values.put("name", name);
		db.insert(mDateBaseName, null, values);
		db.close();
	}

	/**
	 * 删除一条电话号码
	 * 
	 * @param phone
	 *            要删除的电话号码
	 */
	public void delete(String phone) {
		SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
		db.delete(mDateBaseName, "phone = ?", new String[] { phone });
		db.close();
	}

	/**
	 * 根据电话号码去更新
	 * 
	 * @param phone
	 *            要更新的电话号码
	 * @param mode
	 *            1:短信 2：电话 3：拦截所有(短信+电话)
	 */
	public void update(String phone,String name, String mode ) {
		SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		values.put("name", name);
		db.update(mDateBaseName, values, "phone = ?", new String[] { phone });
		db.close();
	}

	/**
	 * 查找所有黑名单
	 * 
	 * @return 所有的黑名单 且以倒序排列
	 */
	public List<BlackNumberEntity> findAll() {
		SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
		// "_id desc" 以_id倒序排列
		Cursor cursor = db.query(mDateBaseName,
				new String[] { "phone", "name","mode" }, null, null, null, null,
				"_id desc");
		List<BlackNumberEntity> blackNumberList = new ArrayList<BlackNumberEntity>();
		while (cursor.moveToNext()) {
			BlackNumberEntity blackNumberEntity = new BlackNumberEntity();
			blackNumberEntity.setPhone(cursor.getString(0));
			blackNumberEntity.setName(cursor.getString(1));
			blackNumberEntity.setMode(cursor.getString(2));
			blackNumberList.add(blackNumberEntity);
		}
		cursor.close();
		db.close();
		return blackNumberList;
	}
	
	/**
	 * 分页查找数据库  (20条)
	 * @param index  查询的索引值
	 * @return
	 */
	public List<BlackNumberEntity> findForPart(int index){
		SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
		// "_id desc" 以_id倒序排列
		Cursor cursor = db.rawQuery("select phone,name,mode from blacknumber order by _id desc limit ?,20;", new String[]{""+index});
		List<BlackNumberEntity> blackNumberList = new ArrayList<BlackNumberEntity>();
		while (cursor.moveToNext()) {
			BlackNumberEntity blackNumberEntity = new BlackNumberEntity();
			blackNumberEntity.setPhone(cursor.getString(0));
			blackNumberEntity.setName(cursor.getString(1));
			blackNumberEntity.setMode(cursor.getString(2));
			blackNumberList.add(blackNumberEntity);
		}
		cursor.close();
		db.close();
		return blackNumberList;
	}
	
	/**
	 * @return 数据库中数据的总条目数，返回0表示没有数据或者异常
	 */
	public int getCount(){
		SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
		// "_id desc" 以_id倒序排列
		Cursor cursor = db.rawQuery("select count(*) from blacknumber ;", null);
		int count = 0;
		if (cursor.moveToNext()) {
			try {
				count = Integer.parseInt(cursor.getString(0));
			} catch (Exception e) {
				count = 0;
			}
		}
		cursor.close();
		db.close();
		return count;
	}
	
	/**
	 * 获取对应的电话号码的拦截模式
	 * @param phone  电话号码
	 * @return 拦截模式  0代表黑名单中没有此号码
	 */
	public int getMode(String phone){
		if (phone.startsWith("+")) {
			phone = phone.substring(3);//去掉前三个字符
		}
		SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
		// "_id desc" 以_id倒序排列
		Cursor cursor = db.rawQuery("select mode from blacknumber where phone=?;", new String[]{phone});
		int mode = 0;
		if (cursor.moveToNext()) {
			mode = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return mode;
	}
	/**
	 * 获取对应的电话号码的姓名
	 * @param phone
	 * @return
	 */
	public String getName(String phone){
		if (phone.startsWith("+")) {
			phone = phone.substring(3);//去掉前三个字符
		}
		SQLiteDatabase db = mBlackNumberOpenHelper.getWritableDatabase();
		// "_id desc" 以_id倒序排列
		Cursor cursor = db.rawQuery("select name from blacknumber where phone=?;", new String[]{phone});
		String name = "";
		if (cursor.moveToNext()) {
			name = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return name;
	}
	
	
}
