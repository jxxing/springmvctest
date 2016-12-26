package com.rd.ifaes.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关于异常的工具类.
 * @author calvin
 * @version 2013-01-15
 */
public class ExceptionUtils {
	/**
	 * 异常的工具类的日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtils.class);
	
	private ExceptionUtils() {
	}
	
	/**
	 * 将CheckedException转换为UncheckedException.
	 * @param e
	 * @return
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 * @param e
	 * @return
	 */
	public static String getStackTraceAsString(Throwable e) {
		if (e == null){
			return "";
		}
		LOGGER.error("error：{}", e.getMessage(), e);	
		String msg = e.getMessage()==null?"未知异常":e.getMessage();
//		if(e instanceof IllegalArgumentException || e instanceof BindException){
//			msg = getMsg(msg);
//		}else if(e instanceof NullPointerException){
//			msg = (msg == null|| "".equals(msg))?"非空参数中出现空值":msg;
//		}else if(e instanceof BussinessException){
//			
//		}else{
//			msg = "很抱歉，您访问的页面找不到了！";
//		}
		
		return msg;
	}
	
//	@SuppressWarnings("unused")
//	private static List<String> getMatcher(String regex, String source) {  
//        List<String> list = new ArrayList<>();
//        Pattern pattern = Pattern.compile(regex);  
//        Matcher matcher = pattern.matcher(source);  
//        while (matcher.find()) {  
//            list.add(matcher.group());
//        }  
//        return list;  
//    }
//	
//	@SuppressWarnings("unused")
//	private static String getMsg(String source){
//		String regex = "(?<=\\')(.+?)(?=\\')";
//        List<String> properties = getMatcher(regex, source);
//        StringBuilder propBuilder = new StringBuilder();
//		for (String prop : properties) {
//			propBuilder.append(prop).append(",");
//		}		
//		String msg = "输入参数";
//		if(propBuilder.length() > 0){
//			msg += "["+propBuilder.deleteCharAt(propBuilder.length() -1)+"]";
//		}
//		msg += "非法";
//		return msg;
//    }

	/**
	 *  判断异常是否由某些底层的异常引起.
	 * @param ex
	 * @param causeExceptionClasses
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	/**
	 * 在request中获取异常类
	 * @param request
	 * @return 
	 */
	public static Throwable getThrowable(HttpServletRequest request){
		Throwable ex = null;
		if (request.getAttribute("exception") != null) {
			ex = (Throwable) request.getAttribute("exception");
		} else if (request.getAttribute("javax.servlet.error.exception") != null) {
			ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
		}
		return ex;
	}
	
}
