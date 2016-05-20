package com.went_gone.mobilesafe.entity;

import java.io.Serializable;
import java.util.List;

import com.went_gone.mobilesafe.utils.BitmapUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 手机应用的相关信息(名称，包名，（内存，SD卡），（系统，用户）)
 * @author Went_Gone
 *
 */
public class AppInfoEntity implements Serializable{
	private String name;
	private String packageName;
	private Drawable icon;
	private boolean isSDCard;
	private boolean isSystem;
	private List<String> permission;//权限
	
	
	private Bitmap bitmap;
	
	
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public List<String> getPermission() {
		return permission;
	}
	public void setPermission(List<String> permission) {
		this.permission = permission;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
		bitmap = BitmapUtil.drawableToBitamp(icon);
	}
	public boolean isSDCard() {
		return isSDCard;
	}
	public void setSDCard(boolean isSDCard) {
		this.isSDCard = isSDCard;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	
}
