package com.went_gone.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

	public BlackNumberOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	public BlackNumberOpenHelper(Context context) {
		this(context, "blacknumber.db", null,1);
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		//_id  自增长的id，phone  电话号码，备注,mode模式   即拦截的是什么(1:短信  2：电话  3：拦截所有(短信+电话))
		db.execSQL("create Table  if not exists blacknumber " +
				"(_id integer primary key autoincrement , phone varchar(20) , " +
				"name varchar(20),mode varchar(10));");
		db.execSQL("create Table if not exists smsTable" +
				"(_id integer primary key autoincrement , phone varchar(15)," +
				"name varchar(20),content varchar(200),dateLong long);");
		db.execSQL("create Table if not exists phoneTable" +
				"(_id integer primary key autoincrement , phone varchar(15)," +
				"name varchar(20),dateLong Long);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
