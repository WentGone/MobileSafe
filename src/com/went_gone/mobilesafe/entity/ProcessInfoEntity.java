package com.went_gone.mobilesafe.entity;

import android.graphics.drawable.Drawable;

/**
 * 进程信息的实体类
 * @author Went_Gone
 *
 */
public class ProcessInfoEntity {
	private String name;//应用名
	private long memory;
	private String memoryString;//应用进程内存(String)
	private Drawable icon;//应用图标
	private boolean isCheck;
	private boolean isSystem;//是否为系统进程
	private String packageName;//如果服务没有名称，则将其所在应用的包名作为名称
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMemory() {
		return memory;
	}
	public void setMemory(long memory) {
		this.memory = memory;
	}
	public String getMemoryString() {
		return memoryString;
	}
	public void setMemoryString(String memoryString) {
		this.memoryString = memoryString;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	
	
}
