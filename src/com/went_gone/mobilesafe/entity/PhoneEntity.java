package com.went_gone.mobilesafe.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 拦截的电话的实体类
 * @author Went_Gone
 *
 */
public class PhoneEntity {
	private String phone;//拦截的电话
	private String name;//拦截的电话的联系人姓名
	private String dateString;//拦截日期(yyyy-MM-dd HH:mm)
	private long dateLong;//拦截日期(long型)
	
	private SimpleDateFormat format;
	
	public PhoneEntity(){
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}
	
	public PhoneEntity(String phone, String name, long dateLong) {
		super();
		this.phone = phone;
		this.name = name;
		this.dateLong = dateLong;
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
	public String getDateString() {
		return dateString;
	}
	public void setDateLong(long dateLong) {
		this.dateLong = dateLong;
		Date date = new Date(dateLong);
		setDateString(format.format(date));
	}
	private void setDateString(String date){
		this.dateString = date;
	}
}
