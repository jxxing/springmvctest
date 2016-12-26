package com.rd.ifaes.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML操作工具类
 * @author lh
 * @version 3.0
 * @since 2016-9-21
 *
 */
public class HtmlUtils extends org.springframework.web.util.HtmlUtils {
	
	
	private static final String REG_SCRIPT="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
	private static final String REG_STYLE="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
	private static final String REG_HTML="<[^>]+>"; //定义HTML标签的正则表达式 
    
	public static String filterHtmlTags(final String input) {
		String result = input;
		// 过滤script标签
		Pattern pscript = Pattern.compile(REG_SCRIPT, Pattern.CASE_INSENSITIVE);
		Matcher mscript = pscript.matcher(result);
		result = mscript.replaceAll(""); 

		// 过滤style标签
		Pattern pstyle = Pattern.compile(REG_STYLE, Pattern.CASE_INSENSITIVE);
		Matcher mstyle = pstyle.matcher(result);
		result = mstyle.replaceAll(""); 
		
		// 过滤html标签
		Pattern phtml = Pattern.compile(REG_HTML, Pattern.CASE_INSENSITIVE);
		Matcher mhtml = phtml.matcher(result);
		result = mhtml.replaceAll("");
		return result;
	}

}
