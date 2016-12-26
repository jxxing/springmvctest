package com.rd.ifaes.common.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Jedis Cache 工具类
 * 
 * @author ThinkGem
 * @version 2014-6-29
 */
public class JedisUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JedisUtils.class);
	
	private static JedisPool jedisPool = SpringContextHolder.getBean(JedisPool.class);

	public static final String KEY_PREFIX = PropertiesUtils.getValue("redis.keyPrefix");
	
	private JedisUtils() {
	}
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
			}
		} catch (Exception e) {
			LOGGER.warn("get {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Object getObject(String key) {
		Object value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = toObject(jedis.get(getBytesKey(key)));
			}
		} catch (Exception e) {
			LOGGER.warn("getObject {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("set {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObject(String key, Object value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.set(getBytesKey(key), toBytes(value));
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setObject {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<String> getList(String key) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.lrange(key, 0, -1);
				LOGGER.debug("getList {} = {}", key, value);					
			}
		} catch (Exception e) {
			LOGGER.warn("getList {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key) {
		List<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list){
					value.add(toObject(bs));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getObjectList {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 设置List缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setList(String key, List<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			for (String v : value) {
				result += jedis.rpush(key,v);
			}
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setList {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 设置List缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			for (Object o : value){
				result += jedis.rpush(getBytesKey(key),toBytes(o));
			}
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setObjectList {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.rpush(key, value);
		} catch (Exception e) {
			LOGGER.warn("listAdd {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listObjectAdd(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			for (Object o : value){
				result += jedis.rpush(getBytesKey(key), toBytes(o));
			}
		} catch (Exception e) {
			LOGGER.warn("listObjectAdd {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<String> getSet(String key) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.smembers(key);
			}
		} catch (Exception e) {
			LOGGER.warn("getSet {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<Object> getObjectSet(String key) {
		Set<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = Sets.newHashSet();
				Set<byte[]> set = jedis.smembers(getBytesKey(key));
				for (byte[] bs : set){
					value.add(toObject(bs));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getObjectSet {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 设置Set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.sadd(key, value.toArray(new String[value.size()]));
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setSet {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			for (Object o : value){
				result += jedis.sadd(getBytesKey(key),toBytes(o));
			}
			
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setObjectSet {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long sadd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.sadd(key, value);
		} catch (Exception e) {
			LOGGER.warn("setSetAdd {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}

	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long saddObj(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			for (Object o : value){
				result += jedis.sadd(getBytesKey(key), toBytes(o));
			}
		} catch (Exception e) {
			LOGGER.warn("setSetObjectAdd {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	/**
	 * Set缓存中删除值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long srem(String key,String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.srem(key, value);
		} catch (Exception e) {
			LOGGER.warn("setSetAdd {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, String> getMap(String key) {
		Map<String, String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.hgetAll(key);
			}
		} catch (Exception e) {
			LOGGER.warn("getMap {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 获取Map缓存的某项值
	 * @param key
	 * @param field
	 * @return	
	 */
	public static String getMapField(String key,String field) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.hget(key, field);
			}
		} catch (Exception e) {
			LOGGER.warn("getMap {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, Object> getObjectMap(String key) {
		Map<String, Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = Maps.newHashMap();
				Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
				for (Map.Entry<byte[], byte[]> e : map.entrySet()){
					value.put(StringUtils.toString(e.getKey()), toObject(e.getValue()));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getObjectMap {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 获取Map缓存的某个对象
	 * @param key
	 * @param field
	 * @return
	 */
	public static Object getObjectMapField(String key, String field) {
		Object value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value =toObject(jedis.hget(getBytesKey(key), getBytesKey(field)));
			}
		} catch (Exception e) {
			LOGGER.warn("getObjectMap {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return value;
	}
	
	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setMap(String key, Map<String, String> value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.hmset(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setMap {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()){
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setObjectMap {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String mapPut(String key, Map<String, String> value) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hmset(key, value);
		} catch (Exception e) {
			LOGGER.warn("mapPut {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String mapObjectPut(String key, Map<String, Object> value) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()){
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
		} catch (Exception e) {
			LOGGER.warn("mapObjectPut {} = {}", key, value, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 移除Map缓存中的值
	 * @param key 键
	 * @param mapKey
	 * @return
	 */
	public static long mapRemove(String key, String... mapKey) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hdel(key, mapKey);
		} catch (Exception e) {
			LOGGER.warn("mapRemove {}  {}", key, mapKey, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 移除Map缓存中的值
	 * @param key 键
	 * @param mapKey
	 * @return
	 */
	public static long mapObjectRemove(String key, String mapKey) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
		} catch (Exception e) {
			LOGGER.warn("mapObjectRemove {}  {}", key, mapKey, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param mapKey
	 * @return
	 */
	public static boolean mapExists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hexists(key, mapKey);
		} catch (Exception e) {
			LOGGER.warn("mapExists {}  {}", key, mapKey, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param mapKey
	 * @return
	 */
	public static boolean mapObjectExists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hexists(getBytesKey(key), getBytesKey(mapKey));
		} catch (Exception e) {
			LOGGER.warn("mapObjectExists {}  {}", key, mapKey, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long del(String... key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.del(key);
		} catch (Exception e) {
			LOGGER.warn("del {}", key, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	
	/**
	 * 批量删除
	 * @param pattern
	 * @return
	 */
	public static long batchDel(String... pattern){
		long result = 0;
		if(pattern == null || pattern.length == 0){
			return result;
		}
		Jedis jedis = null;
		try {
			jedis = getResource();
			for (String kp : pattern) {
				Set<String> sets = jedis.keys(kp+"*");
				if(!CollectionUtils.isEmpty(sets)){
					String [] keys = sets.toArray(new String[sets.size()]);
					result = jedis.del(keys);					
				}
			}
			
		} catch (Exception e) {
			LOGGER.warn("del pattern: {}", pattern, e);
		} finally {
			close(jedis);
		}
		return result;		
	}
	
	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long delObject(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))){
				result = jedis.del(getBytesKey(key));
			}else{
				LOGGER.debug("delObject {} not exists", key);
			}
		} catch (Exception e) {
			LOGGER.warn("delObject {}", key, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(key);
		} catch (Exception e) {
			LOGGER.warn("exists {}", key, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean existsObject(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(getBytesKey(key));
		} catch (Exception e) {
			LOGGER.warn("existsObject {}", key, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 递增数字
	 * @param key 键
	 * @param by 步长
	 * @return
	 */
	public static long incr(String key,long by) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(by>0){
				result = jedis.incrBy(key, by);
			}else{
				result = jedis.incr(key);				
			}
			
		} catch (Exception e) {
			LOGGER.warn("incr key={}, by={}", key, by, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 递减数字
	 * @param key 键
	 * @param by 步长
	 * @return
	 */
	public static long decr(String key,long by) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(by>0){
				result = jedis.decrBy(key, by);
			}else{
				result = jedis.decr(key);				
			}
			
		} catch (Exception e) {
			LOGGER.warn("decr key={}, by={}", key, by, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 递增数字
	 * @param key 键
	 * @param by 步长
	 * @return
	 */
	public static double incr(String key, double by) {
		double result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(by>0){
				result = jedis.incrByFloat(key, by);
			}else{
				result = jedis.incrByFloat(key, 1d);			
			}
			
		} catch (Exception e) {
			LOGGER.warn("incr key={}, by={}", key, by, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	/**
	 * 递减数字
	 * @param key 键
	 * @param by 步长
	 * @return
	 */
	public static double decr(String key, double by) {
		double result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(by>0){
				result = jedis.incrByFloat(key, -by);
			}else{
				result = jedis.incrByFloat(key, -1d);				
			}
			
		} catch (Exception e) {
			LOGGER.warn("decr key={}, by={}, result={}", key, by,result, e);
		} finally {
			close(jedis);
		}
		return result;
	}
	
	

	/**
	 * 获取资源
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (JedisException e) {
			LOGGER.warn("getResource.", e);
			close(jedis);
			throw e;
		}
		return jedis;
	}
	
	/**
	 * 释放资源
	 * @param jedis
	 * @param isBroken
	 */
	public static void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	
	/**
	 * 发布消息到其他节点，更新缓存时使用
	 * @param channel
	 * @param message
	 */
    public static void publish(String channel, Serializable message) {
        if (StringUtils.isBlank(channel) || message == null) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.publish(channel, (String) message);
        } catch (Exception e) {
        	LOGGER.error(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }
        
    /**
     * getAndset
     * @param key
     * @param newVal
     * @return
     */
    public static String getAndset(String key, String newVal){
    	String oldVal = null;
    	Jedis jedis = null;
    	try {
    		jedis = getResource();
    		oldVal = jedis.getSet(key, newVal);
    	} catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
    	} finally {
    		close(jedis);
    	}
    	return oldVal;
    }
    
    /**
     * setnx
     * @param key
     * @param value
     * @return
     */
    public static Long setnx(String key, String value){
    	Long result = 0l;
    	Jedis jedis = null;
    	try {
    		jedis = getResource();
    		result = jedis.setnx(key, value);
    	} catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
    	} finally {
    		close(jedis);
    	}
    	return result;
    }
    
    /**
     * 设置有效期
     * @param key
     * @param seconds
     * @return
     */
    public static Long expire(String key, int seconds){
    	Long result = 0l;
    	Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return result;
    }
		
	/**
	 * 获取byte[]类型Key
	 * @param object
	 * @return
	 */
	public static byte[] getBytesKey(Object object){
		if(object instanceof String){
    		return StringUtils.getBytes((String)object);
    	}else{
    		return ObjectUtils.serialize(object);
    	}
	}
	
	/**
	 * 返回给定key的有效时间，以秒为单位
	 * 	如果是-1则表示永远有效
	 * @author lh
	 * @date 2016年8月11日
	 * @param key
	 * @return
	 */
	public static long ttl(String key){
		Long result = 0l;
    	Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.ttl(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return result;
		
	}
	
	/**
	 * Object转换byte[]类型
	 * @param object
	 * @return
	 */
	public static byte[] toBytes(Object object){
    	return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * @param bytes
	 * @return
	 */
	public static Object toObject(byte[] bytes){
		return ObjectUtils.unserialize(bytes);
	}
	
	/**
	 * 重命名
	 * @param oldkey
	 * @param newkey
	 */
	public static void rename(String oldkey, String newkey) {	
		Jedis jedis = null;
		try {			
			jedis = getResource();			
			jedis.rename(oldkey, newkey);
		}finally{
			close(jedis);
		}
	}
	
	/**
	 * 批量设置map
	 * @param key
	 * @param map
	 * @param cacheSec
	 */
	public static void hmset(String key, Map<String, String> map, int cacheSec) {
		Jedis jedis = null;
		try {			
			jedis = getResource();	
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String field = it.next();
				jedis.hset(key, field, String.valueOf(map.get(field)));
			}
			if(cacheSec > 0){
				jedis.expire(key, cacheSec);
			}
			
		}finally{
			close(jedis);
		}
	}

	/**
	 * 设置map
	 * @param key
	 * @param field
	 * @param value
	 */
	public static void hset(String key, String field, String value) {
		Jedis jedis = null;
		try {			
			jedis = getResource();	
			jedis.hset(key, field, value);
		}finally{
			close(jedis);
		}
	}

}
