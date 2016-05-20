package com.went_gone.mobilesafe.engine;

import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	// 指定数据库的访问路径
	public static String path = "data/data/com.went_gone.mobilesafe/files/address.db";
	private static String mAddress = "未知号码";

	/**
	 * 传递一个电话号码，开启数据库连接，进行访问，返回一个归属地
	 * 
	 * @param phoneNumber
	 *            要查询的电话号码
	 * @return 返回归属地
	 */
	public static String getAddress(String phoneNumber) {
		phoneNumber.replace("—", "");
		phoneNumber.replace("-", "");
		phoneNumber.replace("——", "");
		// 通过正则表达式 手机号码的正则表达式
		String regularExpression = "^1[3-8]\\d{9}$";
		// 开启数据库连接(以只读形式打开)
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		boolean matches = phoneNumber.matches(regularExpression);
		if (matches) {
			phoneNumber = phoneNumber.substring(0, 7);
			// 数据库查询
			Cursor cursor = db.query("data1", new String[] { "outkey" },
					"id = ?", new String[] { phoneNumber }, null, null, null);
			// 查到即可
			if (cursor.moveToNext()) {
				String outkey = cursor.getString(0);
				// 将data1查询出来的结果作为外键查询data2
				Cursor indexCursor = db.query("data2",
						new String[] { "location" }, "id = ?",
						new String[] { outkey }, null, null, null);
				if (indexCursor.moveToNext()) {
					// 获取查询到的电话归属地
					mAddress = indexCursor.getString(0);
				}
			} else {
				mAddress = "未知号码";
			}
		} else {
			int length = phoneNumber.length();
			switch (length) {
			case 3:// 119 110 120 114
				if (phoneNumber.equals("119")) {
					mAddress = "火警电话";
				} else if (phoneNumber.equals("110")) {
					mAddress = "匪警电话";
				} else if (phoneNumber.equals("120")) {
					mAddress = "救护电话";
				} else if (phoneNumber.equals("114")) {
					mAddress = "查询电话";
				} else {
					mAddress = "未知号码";
				}
				break;
			case 5:
				if (phoneNumber.equals("10086")) {
					mAddress = "中国移动";
				}else if (phoneNumber.equals("10010")) {
					mAddress = "中国联通";
				}
				break;
			case 11:
				//(3+8)   /*(4+7)*/   区号+座机号(外地)  查询data2
				String area = phoneNumber.substring(1,3);
				Cursor cursor = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area}, null, null, null);
				if (cursor.moveToNext()) {
					mAddress = cursor.getString(0);
				}else {
					mAddress = "未知号码";
				}
				break;
			case 12:
				String area1 = phoneNumber.substring(1,4);
				Cursor cursor1 = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area1}, null, null, null);
				if (cursor1.moveToNext()) {
					mAddress = cursor1.getString(0);
				}else {
					mAddress = "未知号码";
				}
				//(4+8)   区号+座机号(外地)  查询data2
				break;
			default:
				mAddress = "未知号码";
				break;
			}
		}
		return mAddress;
	}
}
