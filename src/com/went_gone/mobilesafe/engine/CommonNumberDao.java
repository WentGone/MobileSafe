package com.went_gone.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.utils.ToastUtil;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 常用号码的数据库操作
 * @author Went_Gone
 *
 */
public class CommonNumberDao {
	// 指定数据库的访问路径
	public static String path = "data/data/com.went_gone.mobilesafe/files/commonnum.db";
	
	/**
	 * 获取分组
	 * @return
	 */
	public List<Group> getGroup(){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("classlist",new String[]{"name","idx"}, null, null,null, null, null);
		List<Group> groupList = new ArrayList<CommonNumberDao.Group>();
		while(cursor.moveToNext()){
			Group group = new Group();
			group.setName(cursor.getString(0));//分组名称
			group.setIdx(cursor.getString(1));//分租的索引
			groupList.add(group);
			group.setChildList(getChild(group.getIdx()));
		}
		cursor.close();
		db.close();
		return groupList;
	}
	
	//获取每一个组中孩子节点的数据
	public List<Child> getChild(String idx){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
//		Cursor cursor = db.query("table"+idx,new String[]{"name","idx"}, null, null,null, null, null);
		Cursor cursor = db.rawQuery("select * from table"+idx+";",null);
		List<Child> childList = new ArrayList<CommonNumberDao.Child>();
		while(cursor.moveToNext()){
			Child child = new Child();
			child.set_id(cursor.getString(0));//id
			child.setPhone(cursor.getString(1));//电话号码
			child.setName(cursor.getString(2));//名称
			childList.add(child);
 		}
		cursor.close();
		db.close();
		return childList;
	}
	
	/**
	 * 分组
	 * @author Went_Gone
	 *
	 */
	public class Group{
		private String name;
		private String idx;
		private List<Child> childList;
		
		public List<Child> getChildList() {
			return childList;
		}
		public void setChildList(List<Child> childList) {
			this.childList = childList;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIdx() {
			return idx;
		}
		public void setIdx(String idx) {
			this.idx = idx;
		}
	}
	
	public class Child{
		private String _id;
		private String phone;
		private String name;
		public String get_id() {
			return _id;
		}
		public void set_id(String _id) {
			this._id = _id;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
