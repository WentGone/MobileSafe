package com.went_gone.mobilesafe.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 拦截的短信的实体类
 * @author Went_Gone
 *
 */
public class SmsEntity{
	private String phone;
	private String name;
	private String content;
	private String dateString;
	private long dateLong;
	private SimpleDateFormat format;
	
	public SmsEntity(){
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}
	
	public SmsEntity(String phone, String name, String content, long dateLong) {
		super();
		this.phone = phone;
		this.name = name;
		this.content = content;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
