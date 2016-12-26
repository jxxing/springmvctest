package com.rd.ifaes.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * 注解到对象复制，只复制能匹配上的方法。
	 * @param annotation
	 * @param object
	 */
	public static void annotationToObject(final Object annotation, Object object){
		if (annotation != null){
			Class<?> annotationClass = annotation.getClass();
			Class<?> objectClass = object.getClass();
			for (Method m : objectClass.getMethods()){
				if (StringUtils.startsWith(m.getName(), "set")){
					try {
						String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
						Object obj = annotationClass.getMethod(s).invoke(annotation);
						if (obj != null && !"".equals(obj.toString())){
							m.invoke(object, obj);
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	/**
	 * 序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serialize(final Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			if (object != null){
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new byte[0];
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			if (bytes != null && bytes.length > 0){
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 拆分集合
	 * 
	 * @param <T>
	 * @param resList
	 *            要拆分的集合
	 * @param count
	 *            每个集合的元素个数
	 * @return 返回拆分后的各个集合
	 */
	public static <T> List<List<T>> splitList(List<T> resList, int count) {
		if (resList == null ){
			return null;
		}
		if( count < 1){
			throw new IllegalArgumentException("The arg (count) for the splitList method must great than 1!");
		}
		
		List<List<T>> ret = new ArrayList<List<T>>();
		int size = resList.size();
		if (size <= count) { // 数据量不足count指定的大小
			ret.add(resList);
		} else {
			int numPage = size%count;	//是否整页数
			int pageCount = numPage > 0 ?size/count + 1:size/count;//页码总数	
			for (int i = 0; i < pageCount; i++) {
				if(pageCount==i+1){
					ret.add(resList.subList(i*count, size));
				}else{
					ret.add(resList.subList(i*count, (i+1)*count));
				}
			}
		}
		return ret;
	}
	
}
