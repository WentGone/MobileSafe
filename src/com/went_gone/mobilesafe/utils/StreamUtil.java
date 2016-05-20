package com.went_gone.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

public class StreamUtil {

	/**
	 * 流转换成字符串
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	public static String stream2String(InputStream is) throws IOException {
		//字符流    读取流
		BufferedReader br = new BufferedReader(new BufferedReader(new InputStreamReader(is)));
		//写入流
		StringWriter sw = new StringWriter();
		//数据缓冲区
		String str = null;
		while((str = br.readLine())!=null){
			sw.write(str);
		}
		sw.close();
		br.close();
		return sw.toString();
	}

}
