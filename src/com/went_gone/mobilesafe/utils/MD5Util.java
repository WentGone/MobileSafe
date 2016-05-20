package com.went_gone.mobilesafe.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {

	/**给指定字符串按照MD5算法加密
	 * @param psd   需要加密的密码
	 * @return    md5加密后的字符串
	 */
	public static String encoder(String psd) {
		//加盐处理
		psd = psd+"Went_Gone";
		//指定加密算法类型   algorithm(算法)
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//将需要加密的字符串转换成byte类型的数组，进行随机的哈希过程
			byte[] bs = digest.digest(psd.getBytes());
			//循环遍历bs，让其生成32位的字符串，固定写法的规则
			//拼接字符串  StringBuffer
			StringBuffer buffer = new StringBuffer();
			for (byte b : bs) {
				int i = b & 0xff;
				//int i需要转换成16进制的字符
				String hexString = Integer.toHexString(i);
				if (hexString.length()<2) {
					hexString = "0"+hexString;
				}
				buffer.append(hexString);
			}
			return buffer.toString();
//			System.out.println(buffer.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
