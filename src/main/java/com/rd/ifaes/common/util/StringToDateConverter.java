package com.rd.ifaes.common.util;

import java.util.Date;
import org.springframework.core.convert.converter.Converter;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 日期转换器
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年11月1日
 */
public class StringToDateConverter implements Converter<String, Date> {
	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm",
			"yyyy.MM" };

	@Override
	public Date convert(String value) {
		return doConvertToDate(value);
	}

	private Date doConvertToDate(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		Date result = null;
		try {
			result = DateUtils.parseDate((String) value, parsePatterns);
			if (result == null && StringUtils.isNumeric(value)) {
				result = new Date(Long.valueOf((String) value).longValue());
			}
		} catch (Exception e) {
		}
		return result;
	}
}
