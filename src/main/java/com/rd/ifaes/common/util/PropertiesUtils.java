package com.rd.ifaes.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class PropertiesUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

	/** 资源属性 */
	private static Properties properties;

	/**
	 * 私有构造方法
	 */
	private PropertiesUtils() {
	}

	static {
		properties = new Properties();
	  	// 读取配置文件
		loadPropertiesFile(new String[]{"config.properties","message.properties","uploadUrl.properties"});
	}
   /**
    * 读取配置文件
    * @author  FangJun
    * @date 2016年8月16日
    * @param fileName
    */
	private static  void   loadPropertiesFile(String[] fileNames){
		for( String fileName : fileNames){
		 final 	InputStream  jedisIs=PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
			if(jedisIs!=null){
		          try {
					properties.load(jedisIs);
				} catch (IOException e) {
				  LOGGER.error("读取配置文件文件{}读取错误!",fileName);
				}
			}else{
				LOGGER.warn("读取配置文件文件{}路径错误!",fileName);
			}
		}
	}
	
	/**
	 * 获取配置信息
	 * 
	 * @param key 键
	 * @return 配置信息
	 */
	public static String getValue(String key) {
		String value = properties.getProperty(key);
		if (StringUtils.isBlank(value)) {
			LOGGER.warn("没有获取指定key的值，请确认资源文件中是否存在【{}】" , key);
		}
		return value;
	}

	/**
	 * 获取配置信息
	 * 
	 * @param key 键
	 * @param param 参数
	 * @return 配置信息
	 */
	public static String getValue(String key, Object[] param) {
		String value = getValue(key);
		return MessageFormat.format(value, param);
	}

}
