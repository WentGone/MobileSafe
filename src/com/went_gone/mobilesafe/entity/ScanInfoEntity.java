package com.went_gone.mobilesafe.entity;

/**
 * 病毒的实体类
 * @author Went_Gone
 *
 */
public class ScanInfoEntity {
	private boolean isVirus;//是否是病毒
	private String packagename;//应用所在的包名
	private String appName;//应用名称
	public boolean isVirus() {
		return isVirus;
	}
	public void setVirus(boolean isVirus) {
		this.isVirus = isVirus;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
}
