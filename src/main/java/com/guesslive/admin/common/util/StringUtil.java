package com.guesslive.admin.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

/**
 * @author: jerry
 * @Email: jierui@guesslive.com
 * @Company: haigou©2016
 * @Action: 自定义字符串处理类
 * @DATE: 2016-9-19
 */

public class StringUtil {
	public static String objToString(Object obj){
		return null==obj?"":obj.toString();
	}
	
	public static int objToInt(Object obj){
		return null==obj||"".equals(obj)?null:Integer.parseInt(obj.toString());
	}
	
	public static String objTojson(Object obj){
		return new Gson().toJson(obj);
	}
	
	/**
	 * Exception to String
	 * @param Exception e
	 * @return String e
	 */
	public static String getStackMsg(Exception e) {    
         StringBuffer sb = new StringBuffer();    
         StackTraceElement[] stackArray = e.getStackTrace();    
         for (int i = 0; i < stackArray.length; i++) {    
             StackTraceElement element = stackArray[i];    
             sb.append(element.toString() + "\n");    
         }    
         return sb.toString();    
     }

	/**
	 * 创建指定数量的随机字符串
	 * 
	 * @param numberFlag
	 *            是否是数字
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}
	
	public static boolean checkNull(Object obj){
		if(obj==null || "".equals(obj)){
			return true;
		}
		return false;
	}
	
	public static Date String2Date(String dateStr) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateStr);
	}
	
	public static String date2String (Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String dateFormat(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:HH:ss");
		String result = "";
		if(!"".equals(date) && null != date){
			try {
				result = sdf.format(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
