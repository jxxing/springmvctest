package com.rd.ifaes.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.NumberUtils;
/**
 * 表单数字转换
 * @author  FangJun
 * @date 2016年6月27日
 */
public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

	@Override
	public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToNumber<T>(targetType);
	}


	private static final class StringToNumber<T extends Number> implements Converter<String, T> {

		private final Class<T> targetType;

		public StringToNumber(Class<T> targetType) {
			this.targetType = targetType;
		}

		@Override
		public T convert(String source) {
			//页面传递数值类型数据，默认设置为0
			if (source.length() == 0) {
				if (Byte.class == targetType) {
					return (T)  Byte.valueOf((byte)0);
				}
				else if (Short.class == targetType) {
					return (T)  Short.valueOf((short)0);
				}
				else if (Integer.class == targetType) {
					return (T)   Integer.valueOf(0);
				}
				else if (Long.class == targetType) {
					return (T)  Long.valueOf(0);
				}
				else if (BigInteger.class == targetType) {
					return (T) BigInteger.valueOf(0L);
				}
				else if (Float.class == targetType) {
					return (T) Float.valueOf(0);
				}
				else if (Double.class == targetType) {
					return (T) Double.valueOf(0);
				}
				else if (BigDecimal.class == targetType || Number.class == targetType) {
					return (T) new BigDecimal(0);
				}
				 
				return null;
			}
			return NumberUtils.parseNumber(source, this.targetType);
		}
	}

}