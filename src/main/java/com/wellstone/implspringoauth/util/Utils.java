package com.wellstone.implspringoauth.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

	/**
	 * String Null Check 
	 */
	public static boolean checkNullStr(String str) {
		
		if(str != null && !"".equals(str) && !"null".equals(str)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Exception -> String convert
	 */
	public static String getAsString(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
  }

	
}
