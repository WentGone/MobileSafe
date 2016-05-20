package com.went_gone.mobilesafe.entity;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 联系人的实体类
 * @author Went_Gone
 *
 */
public class ContactsEntity implements Serializable{
	private HashMap<String,String> name; //姓名
	private HashMap<String,String> phoneNumber; //电话
	public ContactsEntity(){}
	public ContactsEntity(HashMap<String, String> name,
			HashMap<String, String> phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	public HashMap<String, String> getName() {
		return name;
	}
	public void setName(HashMap<String, String> name) {
		this.name = name;
	}
	public HashMap<String, String> getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(HashMap<String, String> phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
