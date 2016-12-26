package com.rd.ifaes.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
/**
 * 数值类型操作工具类
 * @version 3.0
 *
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
	
	private NumberUtils() {
	}
	
	/**
	 * 判断是否为整数
	 * @param  
	 * @return boolean
	 * @author xxb
	 * @date 2016年10月8日
	 */
	public static boolean isInteger(String str){
		if(StringUtils.isBlank(str)){
			return false;
		}else{
			return str.matches("^[0-9]*$");
		}
	}

	public static double format(double d,String format){
		DecimalFormat df = new DecimalFormat(format); 
		String ds=df.format(d);
		return Double.parseDouble(ds);
	}

	public static int getInt(String str){
		 return toInt(str,INTEGER_ZERO);
	}
	
	public static int compare(double x,double y){
		BigDecimal val1=BigDecimal.valueOf(x);
		BigDecimal val2=BigDecimal.valueOf(y);
		return val1.compareTo(val2);
	}
	
	/**
	 * @param d
	 * @param len
	 * @return
	 */
	public static double ceil(double d,int len){
		String str=Double.toString(d);
		int a=str.indexOf('.');
		if(a+3>str.length()){
			a=str.length();
		}else{
			a=a+3;
		}
		str=str.substring(0, a);
		return Double.parseDouble(str);
	}
	
	public static double ceil(double d){
		return ceil(d,2);
	}
	
	/**
	 * 去除数字的科学计数法
	 * @param d
	 * @return
	 */
	public static String format(double d) {
		if (d < 10000000) {
			return Double.toString(d);
		}
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		return nf.format(d);
	}
	
	/**
	 * 是否默认值（含 0 ）
	 * @param obj
	 * @return
	 */
	public static boolean isDefault(Object obj){
		if(obj == null){
			return true;
		}
		if(obj instanceof Byte){
			return ((Byte)obj).equals((byte)0);
		}else if(obj instanceof Short){
			return ((Short)obj).intValue() == 0;
		}else if(obj instanceof Integer){
			return ((Integer)obj).equals(0);
		}else if(obj instanceof Long){
			return ((Long)obj).equals((Long)0l);
		}else if(obj instanceof Double){
			return ((Double)obj).equals((Double)0d);
		}else if(obj instanceof Float){
			return ((Float)obj).equals((Float)0f);
		}else if(obj instanceof BigDecimal){
			return ((BigDecimal)obj).compareTo(BigDecimal.ZERO)==0;
		}
		return false;
	}
		
	/**
	 * 比较大小
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static boolean isBigger(Object n1, Object n2){		
		if(n1 == null || n2 == null){
//			throw new BussinessException("两比较对象不能为空");
			return false;
		}
		if(!n1.getClass().equals(n2.getClass())){
//			throw new BussinessException("两比较对象类型不同");
			return false;
		}
		
		if(n1 instanceof Byte){
			return ((Byte)n1).compareTo((Byte)n2) > 0;
		}else if(n1 instanceof Short){
			return ((Short)n1).compareTo((Short)n2) > 0;
		}else if(n1 instanceof Integer){
			return ((Integer)n1).compareTo((Integer)n2) > 0;
		}else if(n1 instanceof Long){
			return ((Long)n1).compareTo((Long)n2) > 0;
		}else if(n1 instanceof Double){
			return ((Double)n1).compareTo((Double)n2) > 0;
		}else if(n1 instanceof Float){
			return ((Float)n1).compareTo((Float)n2) > 0;
		}else if(n1 instanceof BigDecimal){
			return ((BigDecimal)n1).compareTo((BigDecimal)n2) > 0;
		}
		return false;
	}
	
	
	public static BigDecimal getBigDecimal( Object value ) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
            	if(!StringUtils.isBlank((String) value )){
            		ret = new BigDecimal( (String) value );
            	}
            } else if( value instanceof BigInteger ) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = BigDecimal.valueOf(((Number)value).doubleValue() );
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }
	
	public static String format2Str(BigDecimal b){
		DecimalFormat df = new DecimalFormat("#####0.00");
		return df.format(BigDecimalUtils.round(b, 2));
	}
	
	public static String format2Str(Object b){
		BigDecimal temp = BigDecimalUtils.round(StringUtils.isNull(b));
		return format2Str(temp);
	}
	
	
	public static double formatDouble(double d, int scale){
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(d));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_DOWN).doubleValue();
	}
	public static double getDouble(Object str){
		if(!(str instanceof String || str instanceof Number)){
//			throw new BussinessException("类型不匹配");
			return 0.0;
		}
		
		if(StringUtils.isBlank(str))
			return 0.0;
		double ret=0.0;
		try {
			ret=Double.parseDouble(str.toString());
		} catch (NumberFormatException e) {
			ret=0.0;
		}
		return formatDouble(ret, 6);
	}
	
	public static long getLong(String str){
		if(StringUtils.isBlank(str)){
			return 0L;			
		}
		long ret=0;
		try {
			ret=Long.parseLong(str);
		} catch (NumberFormatException e) {
			ret=0;
		}
		return ret;
	}
}
