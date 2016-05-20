package com.went_gone.mobilesafe.entity;


/**
 * 黑名单的实体类
 * @author Went_Gone
 *
 */
public class BlackNumberEntity {
	private String phone;
	private String name;
	private String mode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "BlackNumberEntity [phone=" + phone + ", name=" + name
				+ ", mode=" + mode + "]";
	}
	
}
