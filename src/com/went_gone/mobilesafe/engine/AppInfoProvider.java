package com.went_gone.mobilesafe.engine;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;

import com.went_gone.mobilesafe.base.BaseApplication;
import com.went_gone.mobilesafe.entity.AppInfoEntity;
import com.went_gone.mobilesafe.utils.ToastUtil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.util.Log;

/**
 * 手机应用信息
 * @author Went_Gone
 *
 */
public class AppInfoProvider {
	
	/**
	 * 返回当前手机上所有的应用的相关信息(名称，包名，（内存，SD卡），（系统，用户）)
	 * @param context
	 * @return   包含手机安装的应用的信息的集合
	 */
	public static List<AppInfoEntity> getAppListInfo(Context context) {
		//获取包的管理者
		PackageManager pm = context.getPackageManager();
		//获取手机安装在手机上应用相关信息的集合
		List<PackageInfo> packgeInfoList = pm.getInstalledPackages(0);
		List<AppInfoEntity> appInfoList = new ArrayList<AppInfoEntity>();
		for (PackageInfo packageInfo : packgeInfoList) {
			AppInfoEntity appInfoEntity = new AppInfoEntity();
			appInfoEntity.setPackageName(packageInfo.packageName);//包名
			//应用名称
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			
			
			
			/*if (packageInfo.permissions!=null) {
				for (PermissionInfo permissionInfo : packageInfo.permissions) {
					Log.d("TAG", "权限："+permissionInfo.name);
					permissions.add(permissionInfo.name);
				}
				appInfoEntity.setPermission(permissions);
			}*/
			
			appInfoEntity.setName(applicationInfo.loadLabel(pm).toString());
			//图标
			appInfoEntity.setIcon(applicationInfo.loadIcon(pm));
			//判断是否为系统应用(每一个手机上对应的flags都不一致)
			if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM) {
				//系统应用
				appInfoEntity.setSystem(true);
			}else {
				//用户应用
				appInfoEntity.setSystem(false);
				PackageInfo pi;
				List<String> permissionList = new ArrayList<String>();
				try {
				    pi = pm.getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS);
				    String[] permissions = pi.requestedPermissions;
				    if(permissions != null){
				        for(String str : permissions){
				        	permissionList.add("权限："+str);
				        }
				    }
				} catch (NameNotFoundException e) {
				    e.printStackTrace();
				}finally{
					appInfoEntity.setPermission(permissionList);
				}
				
			}
			if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
				//SD卡应用
				appInfoEntity.setSDCard(true);
			}else {
				appInfoEntity.setSDCard(false);
			}
			appInfoList.add(appInfoEntity);
		}
		return appInfoList;
	}
}
