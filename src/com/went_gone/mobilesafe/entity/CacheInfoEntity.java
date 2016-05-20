package com.went_gone.mobilesafe.entity;

import android.graphics.drawable.Drawable;

/**
 * 缓存信息的实体类
 * @author Went_Gone
 *
 */
public class CacheInfoEntity {
	private String appName;//应用名
	private String packageame;//包名
	private long cacheSize;//缓存大小long型
	private Drawable icon;//图标
	
	public long getCacheSize() {
		return cacheSize;
	}
	public void setCacheSize(long cacheSize) {
		this.cacheSize = cacheSize;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageame() {
		return packageame;
	}
	public void setPackageame(String packageame) {
		this.packageame = packageame;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
}
