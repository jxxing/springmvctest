package jxx.util;

public class StringUtils {
	public static String isNull(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}
	
	public static String out(){
		return "hello";
	}
}
