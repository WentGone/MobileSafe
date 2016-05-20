package com.went_gone.mobilesafe.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

/**
 * 短信备份的引擎类
 * @author Went_Gone
 *
 */
public class SmsBackup {
	private static int index = 0;
	/**
	 * 短信备份
	 * @param context
	 * @param path  备份的路径
	 * @param pd
	 */
	public static void backup(Context context, String path, ProgressDialog pd) {
		FileOutputStream fos = null;
		Cursor cursor = null;
		try {
			// 内容解析器上下文 ， 备份文件夹的路径 ，进度条所在对话框对象
			// 1获取备份短信的写入文件
			File file = new File(path);
			// 获取内容解析器
			ContentResolver contentResolver = context.getContentResolver();

			cursor = contentResolver.query(Uri.parse("content://sms/"), new String[] {
					"address", "date", "type", "body" }, null, null, null);
			fos = new FileOutputStream(file);
			//序列化数据库中读取的数据，放置在xml中
			XmlSerializer xmlSerializer = Xml.newSerializer();
			//给此xml做相应的设置  
			xmlSerializer.setOutput(fos, "utf-8");
			//xml规范
			xmlSerializer.startDocument("utf-8", true);
			
			xmlSerializer.startTag(null, "smss");
			
			//备份短信的总数
			pd.setMax(cursor.getCount());
			
			//读取数据库中每一行数据。写入到xml中
			while (cursor.moveToNext()) {
				xmlSerializer.startTag(null, "sms");
				
				xmlSerializer.startTag(null, "address");
				cursor.getString(0);
				xmlSerializer.endTag(null, "address");
				
				xmlSerializer.startTag(null, "date");
				cursor.getLong(1);
				xmlSerializer.endTag(null, "date");
				
				xmlSerializer.startTag(null, "type");
				cursor.getString(2);
				xmlSerializer.endTag(null, "type");
				
				xmlSerializer.startTag(null, "body");
				cursor.getString(3);
				xmlSerializer.endTag(null, "body");
				
				xmlSerializer.endTag(null, "sms");
				
				index++;
				Thread.sleep(500);
				//每循环一次让进度条更新一次
				//progressdialog可以在子线程中更新进度条的改变
				pd.setProgress(index);
			}
			
			xmlSerializer.endTag(null, "smss");
			xmlSerializer.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (fos!=null && cursor!=null) {
				try {
					fos.close();
					cursor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
