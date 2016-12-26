package com.rd.ifaes.common.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
	
	public static final String PATTERN_YYMMDDHHMMSS="yyMMddHHmmss";
	
	/** 格式 ：yyyy-MM-dd HH:mm:ss */
	public static final String DATEFORMAT_STR_001 = "yyyy-MM-dd HH:mm:ss";
	/** 格式 ：yyyy-MM-dd */
	public static final String DATEFORMAT_STR_002 = "yyyy-MM-dd";
	/** 格式 ：MM-dd */
	public static final String DATEFORMAT_STR_003 = "MM-dd";
	/** 格式 ：HH:mm:ss */
	public static final String DATEFORMAT_STR_004 = "HH:mm:ss";
	
	/** 格式 ：yyyyMMddHHmmss */
	public static final String DATEFORMAT_STR_011 = "yyyyMMddHHmmss";
	/** 格式 ：yyyyMMdd */
	public static final String DATEFORMAT_STR_012 = "yyyyMMdd";
	
	/** 格式 ：yyyy年MM月dd日 HH时mm分ss秒 */
	public static final String DATEFORMAT_STR_021 = "yyyy年MM月dd日 HH时mm分ss秒";
	/** 格式 ：yyyy年MM月dd日 */
	public static final String DATEFORMAT_STR_022 = "yyyy年MM月dd日";
	/** 格式 ：MM月dd日 hh:mm */
	public static final String DATEFORMAT_STR_023 = "MM月dd日 hh:mm";
	
	/** 日期开始 */
	public static final String DATE_START = " 00:00:00";
	/** 日期开始 */
	public static final String DATE_END = " 23:59:59";
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 获取过去的秒数
	 * @param date
	 * @return
	 */
	public static long pastSeconds(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = timeMillis/(60*60*1000)-day*24;
		long min = timeMillis/(60*1000)-day*24*60-hour*60;
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
    /** 
     * 计算两个日期之间相差的天数 
     * @param date1 
     * @param date2 
     * @return 
     */  
    public static int daysBetween(Date begin, Date end){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long betweenDays = 0L;
		try {
			begin = sdf.parse(sdf.format(begin));
			end = sdf.parse(sdf.format(end));
			Calendar cal = Calendar.getInstance();
			cal.setTime(begin);
			long time1 = cal.getTimeInMillis();
			cal.setTime(end);
			long time2 = cal.getTimeInMillis();
			betweenDays = (time2 - time1) / (1000 * 3600 * 24);
		} catch (ParseException e) {
		}
		return Integer.parseInt(String.valueOf(betweenDays));
	}
	/**
	 * 获取两个日期之间的小时数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoHours(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60);
	}
	
	/**
	 * 得到当前日期和时间字符串
	 */
	public static String getDateTime(String pattern) {
		if(StringUtils.isBlank(pattern)){
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return formatDate(new Date(), pattern);
	}
	
	/**
	 * 获得当前时间
	 * @return
	 */
	public static Date getNow() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();
		return currDate;
	}
	/**
	 * 获得当前日期
	 * @return
	 */
	public static Date getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date currDate = cal.getTime();
		return currDate;
	}
	/**
	 * 截掉时分秒，到日
	 * @return
	 */
	public static Date trunc(Date date) {
		if(date==null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date res = cal.getTime();
		return res;
	}
	/**
	 * 截掉时分秒，到日
	 * @return
	 */
	public static String trunc(String date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.format(format.parse(date).getTime());
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 日期转换为字符串 格式自定义
	 * 
	 * @param date
	 * @param f
	 * @return
	 */
	public static String dateStr(Date date, String f) {
		SimpleDateFormat format = new SimpleDateFormat(f);
		String str = format.format(date);
		return str;
	}

	/**
	 * 日期转换为字符串 MM月dd日 hh:mm
	 * 
	 * @param date
	 * @return
	 */
	public static String dateStr(Date date) {
		return dateStr(date, DATEFORMAT_STR_023);
	}

	/**
	 * 日期转换为字符串 yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateStr2(Date date) {
		return dateStr(date, DATEFORMAT_STR_002);
	}

	/**
	 * yyyy年MM月dd日HH时mm分ss秒
	 * 
	 * @param date
	 * @return
	 */
	public static String dateStr5(Date date) {
		return dateStr(date, DATEFORMAT_STR_021);
	}

	/**
	 * yyyyMMddHHmmss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateStr3(Date date) {
		return dateStr(date, DATEFORMAT_STR_011);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateStr4(Date date) {
		return dateStr(date, DATEFORMAT_STR_001);

	}

	/**
	 * yyyy年MM月dd日
	 * 
	 * @param date
	 * @return
	 */
	public static String dateStr6(Date date) {
		return dateStr(date, DATEFORMAT_STR_022);
	}

	/**
	 * yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateStr7(Date date) {
		return dateStr(date, DATEFORMAT_STR_012);
	}

	/**
	 * MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateStr8(Date date) {
		return dateStr(date, DATEFORMAT_STR_003);
	}
	
	/**
	 * 默认的valueOf 方法，格式化 yyyy-mm-dd HH:mm:ss
	 * @param str 
	 * @return
	 */
	public static Date valueOf(String str){
		return valueOf(str, DATEFORMAT_STR_001);
	}
	
	/**
	 * 
	 * 自定义format格式化字符串为date
	 * @param str 要格式化的字符串
	 * @param dateFormatStr 
	 * @return
	 */
	public static Date valueOf(String str, String dateFormatStr){
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormatStr);
		ParsePosition pos = new ParsePosition(0);
		Date strtoDate = formatter.parse(str, pos);
		return strtoDate;
	}
	
	/**
	 * 前/后?分钟
	 * 
	 * @param d
	 * @param minute
	 * @return
	 */
	public static Date rollMinute(Date d, int minute) {
		return new Date(d.getTime() + minute * 60 * 1000);
	}
	
	/**
	 * 前/后?秒
	 * 
	 * @param d
	 * @param second
	 * @return
	 */
	public static Date rollSecond(Date d, int second) {
		return new Date(d.getTime() + second * 60 * 60 * 1000);
	}

	/**
	 * 前/后?天
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date rollDay(Date d, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
	
	/**
	 * 固定日期
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date fixDay(Date d, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int month = cal.get(Calendar.MONTH);
		cal.set(Calendar.DAY_OF_MONTH, day);
		int fixMonth = cal.get(Calendar.MONTH);
		if(month != fixMonth){
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return cal.getTime();
	}

	/**
	 * 前/后?月
	 * 
	 * @param d
	 * @param mon
	 * @return
	 */
	public static Date rollMon(Date d, int mon) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, mon);
		return cal.getTime();
	}

	/**
	 * 前/后?年
	 * 
	 * @param d
	 * @param year
	 * @return
	 */
	public static Date rollYear(Date d, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}

	public static Date rollDate(Date d, int year, int mon, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, mon);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
	
	/**
	 * 获取当前时间-时间戳
	 * 
	 * @return
	 */
	public static int getNowTime() {
		return Integer.parseInt(StringUtils.isNull(System.currentTimeMillis() / 1000));
	}
	
	/**
	 * 判断日期对应的时间是否在给定的时间之前
	 * @param date	日期
	 * @param time	给定的时间
	 * @return
	 */
	public static boolean before(Date date, String time){
		if(StringUtils.isBlank(time)){
//			throw new BussinessException("time is null");
		}
		String defTime = "%s:%s:%s";
		String [] timeArray = time.split(":");
		String ftime = null;
		if(timeArray.length == 3){
			ftime = String.format(defTime, timeArray[0], timeArray[1], timeArray[2]);
		}else if(timeArray.length == 2){
			ftime = String.format(defTime, timeArray[0], timeArray[1], "00");
		}else if(timeArray.length == 1){
			ftime = String.format(defTime, timeArray[0], "00", "00");
		}
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); 
		try {
			long t1 = timeFormat.parse(timeFormat.format(date)).getTime();
			long t2 = timeFormat.parse(ftime).getTime();
			return t1 < t2;
		} catch (ParseException e) {
//			throw new BussinessException("time formart error,except  HH:mm:ss ");
		}
		return false;
	}
	
	/**
	 * 取得传入时间之后X天后的固定时间
	 * @author fxl
	 * @date 2016年8月15日
	 * @param date 传入时间
	 * @param time 固定时间
	 * @param day 几天后
	 * @return
	 */
	public static Date after(Date date, String time,int day){
		if(StringUtils.isBlank(time)){
//			throw new BussinessException("time is null");
		}
		String defTime = "%s:%s:%s";
		String [] timeArray = time.split(":");
		String ftime = null;
		if(timeArray.length == 3){
			ftime = String.format(defTime, timeArray[0], timeArray[1], timeArray[2]);
		}else if(timeArray.length == 2){
			ftime = String.format(defTime, timeArray[0], timeArray[1], "00");
		}else if(timeArray.length == 1){
			ftime = String.format(defTime, timeArray[0], "00", "00");
		}
		String newDate = formatDate(rollDay(date, day), "yyyy-MM-dd");
		String newTime = newDate +" "+ftime;
		return valueOf(newTime);
	}
	/**
	 * 
	 * 获取Date的开始时间
	 * @author wyw
	 * @date 2016-8-6
	 * @param date
	 * @return
	 */
	public static  String getDateStart(Date date){
		return DateUtils.formatDate(date, DateUtils.DATEFORMAT_STR_002)+DATE_START;
	}
	/**
	 * 
	 * 获取Date的结束时间
	 * @author wyw
	 * @date 2016-8-6
	 * @param date
	 * @return
	 */
	public static  String getDateEnd(Date date){
		return DateUtils.formatDate(date, DateUtils.DATEFORMAT_STR_002)+DATE_END;
	}
	/**
	 * 
	 * 功能：得到年份月份月初 格式为： (eg: 2016-01-01 00:00:00)<br> 
	 * @author wyw
	 * @date 2016-8-19
	 * @param year
	 * @param month 从一月份开始
	 * @return
	 */
    public static String monthStartTime(String year,int month) {
        String strDay = null;
        strDay = month >= 10 ? String.valueOf(month) : "0" + month;
        return year + "-" + strDay + "-01"+DATE_START;
    }
    /**
     * 功能：得到年份月份月底 格式为： (eg: 2016-02-29 23:59:59)<br> 
     * @return String
     * @author pure
     */
    public static String monthEndTime(String year,int month) {
        String strY = null;
        String strZ = null;
        boolean leap = false;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 ||month == 12) {
            strZ = "31";
        }else if  (month == 4 ||month == 6 ||month == 9 || month == 11) {
            strZ = "30";
        }else if (month == 2) {
            leap = leapYear(Integer.valueOf(year));
            if (leap) {
                strZ = "29";
            }
            else {
                strZ = "28";
            }
        }
        strY = month >= 10 ? String.valueOf(month) : "0" + month;
        return year + "-" + strY + "-" + strZ+DATE_END;
    }
    /**
     * 功能：判断输入年份是否为闰年<br>
     * 
     * @param year
     * @return 是：true  否：false
     * @author pure
     */
    public static boolean leapYear(int year) {
        boolean leap;
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) leap = true;
                else leap = false;
            }
            else leap = true;
        }
        else leap = false;
        return leap;
    }

    /**
     * 校验是否同一个月
     * @author fxl
     * @date 2016年10月11日
     * @param date1
     * @param date2
     * @return
     */
    public static boolean checkSameMonth(Date date1,Date date2){
    	if((formatDate(date1, "MM")).equals(formatDate(date2, "MM"))){
    		return true;
    	}else{
    		return false;
    	}
	}
    
    /**
     * 获取今年的生日月第一天
     * @author fxl
     * @date 2016年10月11日
     * @param date
     * @return
     */
    public static Date getBirthMonth(Date date){
    	String month = formatDate(date, "MM");
    	String year = getYear();
    	return valueOf(year+"-"+month+"-"+"01",DATEFORMAT_STR_002);
	}
    
    /**
     * 动态取得一年的时间区间（以当前月往前推5个月，往后推6个月）
     * @return
     */
    public static String[] getYearDynamicSection(){
    	String[] section = new String[2];
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MONTH, -5);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT_STR_002);
    	section[0] = format.format(cal.getTime()) +" 00:00:00";
    	
    	cal.add(Calendar.MONTH, 12);
    	cal.add(Calendar.DAY_OF_MONTH, -1);
    	section[1] = format.format(cal.getTime()) + " 23:59:59";
    	return section;    	
    }
    
    /**
	 * @param Begin 开始日期 ，格式：yyyy-MM-dd
	 * @param end   结束时间， 格式：yyyy-MM-dd
	 * @return 返回Map 获取相隔多少年 get("YEAR")及为俩个时间年只差，月 天，类推 Key ： YEAR MONTH DAY 如果开始时间 晚于 结束时间 return null；
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Integer> getApartTime(String Begin, String end) {
		String[] temp = Begin.split("-");
		String[] temp2 = end.split("-");
		if (temp.length > 1 && temp2.length > 1) {
			Calendar ends = Calendar.getInstance();
			Calendar begin = Calendar.getInstance();

			begin.set(StringUtils.toInt(temp[0]), StringUtils.toInt(temp[1]), StringUtils.toInt(temp[2]));
			ends.set(StringUtils.toInt(temp2[0]), StringUtils.toInt(temp2[1]), StringUtils.toInt(temp2[2]));
			if (begin.compareTo(ends) < 0) {
				Map map = new HashMap<>();
				ends.add(Calendar.YEAR, -StringUtils.toInt(temp[0]));
				ends.add(Calendar.MONTH, -StringUtils.toInt(temp[1]));
				ends.add(Calendar.DATE, -StringUtils.toInt(temp[2]));
				map.put("YEAR", ends.get(Calendar.YEAR));
				map.put("MONTH", ends.get(Calendar.MONTH) + 1);
				map.put("DAY", ends.get(Calendar.DATE));
				return map;
			}
		}
		return null;
	}
    
}
