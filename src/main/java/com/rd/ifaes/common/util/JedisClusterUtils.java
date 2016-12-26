package com.rd.ifaes.common.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


/**
 * JedisCluster工具类
 * @author lihua
 * @version 2016-4-20
 */
public class JedisClusterUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterUtils.class);
	
	public static JedisCluster jedisCluster = SpringContextHolder.getBean(JedisCluster.class);

	public static final String KEY_PREFIX = PropertiesUtils.getValue("redis.keyPrefix");
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key) {
		String value = null;
		try {
			if (jedisCluster.exists(key)) {
				value = jedisCluster.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
			}
		} catch (Exception e) {
			LOGGER.warn("get {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(getBytesKey(key))) {
				value = toObject(jedisCluster.get(getBytesKey(key)));
			}
		} catch (Exception e) {
			LOGGER.warn("getObject {} = {}", key, value, e);
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
		try {
			
			result = jedisCluster.set(key, value);
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("set {} = {}", key, value, e);
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
		try {
			result = jedisCluster.set(getBytesKey(key), toBytes(value));
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setObject {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(key)) {
				value = jedisCluster.lrange(key, 0, -1);
			}
		} catch (Exception e) {
			LOGGER.warn("getList {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(getBytesKey(key))) {
				List<byte[]> list = jedisCluster.lrange(getBytesKey(key), 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list){
					value.add(toObject(bs));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getObjectList {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(key)) {
				jedisCluster.del(key);
			}
			for (String v : value) {
				result += jedisCluster.rpush(key, v);
			}
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setList {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(getBytesKey(key))) {
				jedisCluster.del(key);
			}
			for (Object o : value){
				result += jedisCluster.rpush(getBytesKey(key),toBytes(o));
			}
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setObjectList {} = {}", key, value, e);
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
		try {
			result = jedisCluster.rpush(key, value);
		} catch (Exception e) {
			LOGGER.warn("listAdd {} = {}", key, value, e);
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
		try {
			for (Object o : value){
				result += jedisCluster.rpush(getBytesKey(key),toBytes(o));
			}
		} catch (Exception e) {
			LOGGER.warn("listObjectAdd {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(key)) {
				value = jedisCluster.smembers(key);
			}
		} catch (Exception e) {
			LOGGER.warn("getSet {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(getBytesKey(key))) {
				value = Sets.newHashSet();
				Set<byte[]> set = jedisCluster.smembers(getBytesKey(key));
				for (byte[] bs : set){
					value.add(toObject(bs));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getObjectSet {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(key)) {
				jedisCluster.del(key);
			}
			for (String v : value) {
				result += jedisCluster.sadd(key, v);				
			}
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setSet {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(getBytesKey(key))) {
				jedisCluster.del(key);
			}
			for (Object o : value){
				result += jedisCluster.sadd(getBytesKey(key),toBytes(o));
			}
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setObjectSet {} = {}", key, value, e);
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
		try {
			result = jedisCluster.sadd(key, value);
		} catch (Exception e) {
			LOGGER.warn("setSetAdd {} = {}", key, value, e);
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
		try {
			for (Object o : value){
				result += jedisCluster.sadd(getBytesKey(key), toBytes(o));
			}
		} catch (Exception e) {
			LOGGER.warn("setSetObjectAdd {} = {}", key, value, e);
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
		try {
			result = jedisCluster.srem(key, value);
		} catch (Exception e) {
			LOGGER.warn("setSetAdd {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(key)) {
				value = jedisCluster.hgetAll(key);
			}
		} catch (Exception e) {
			LOGGER.warn("getMap {} = {}", key, value, e);
		}
		return value;
	}
	
	/**
	 * 获取Map缓存某项
	 * @param key
	 * @param field
	 * @return
	 */
	public static String getMapField(String key, String field) {
		String value = null;
		try {
			if (jedisCluster.exists(key)) {
				value = jedisCluster.hget(key, field);
			}
		} catch (Exception e) {
			LOGGER.warn("getMap {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(getBytesKey(key))) {
				value = Maps.newHashMap();
				Map<byte[], byte[]> map = jedisCluster.hgetAll(getBytesKey(key));
				for (Map.Entry<byte[], byte[]> e : map.entrySet()){
					value.put(StringUtils.toString(e.getKey()), toObject(e.getValue()));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("getObjectMap {} = {}", key, value, e);
		} 
		return value;
	}
	
	/**
	 * 获取Map缓存
	 * @param key 键
	 * @param field
	 * @return 值
	 */
	public static Object getObjectMapField(String key, String field) {
		Object value = null;
		try {
			if (jedisCluster.exists(getBytesKey(key))) {
				value = toObject(jedisCluster.hget(getBytesKey(key), getBytesKey(field)));
			}
		} catch (Exception e) {
			LOGGER.warn("getObjectMap {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(key)) {
				jedisCluster.del(key);
			}
			result = jedisCluster.hmset(key, value);
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setMap {} = {}", key, value, e);
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
		try {
			if (jedisCluster.exists(getBytesKey(key))) {
				jedisCluster.del(key);
			}
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()){
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedisCluster.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
			if (cacheSeconds != 0) {
				jedisCluster.expire(key, cacheSeconds);
			}
		} catch (Exception e) {
			LOGGER.warn("setObjectMap {} = {}", key, value, e);
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
		try {
			result = jedisCluster.hmset(key, value);
		} catch (Exception e) {
			LOGGER.warn("mapPut {} = {}", key, value, e);
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
		try {
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()){
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedisCluster.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
		} catch (Exception e) {
			LOGGER.warn("mapObjectPut {} = {}", key, value, e);
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
		try {
			result = jedisCluster.hdel(key, mapKey);
		} catch (Exception e) {
			LOGGER.warn("mapRemove {}  {}", key, mapKey, e);
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
		try {
			result = jedisCluster.hdel(getBytesKey(key), getBytesKey(mapKey));
		} catch (Exception e) {
			LOGGER.warn("mapObjectRemove {}  {}", key, mapKey, e);
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
		try {
			result = jedisCluster.hexists(key, mapKey);
		} catch (Exception e) {
			LOGGER.warn("mapExists {}  {}", key, mapKey, e);
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
		try {
			result = jedisCluster.hexists(getBytesKey(key), getBytesKey(mapKey));
		} catch (Exception e) {
			LOGGER.warn("mapObjectExists {}  {}", key, mapKey, e);
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
		try {
			result = jedisCluster.del(key);
		} catch (Exception e) {
			LOGGER.warn("del {}", key, e);
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
		try {
			if (jedisCluster.exists(getBytesKey(key))){
				result = jedisCluster.del(getBytesKey(key));
			}else{
				LOGGER.debug("delObject {} not exists", key);
			}
		} catch (Exception e) {
			LOGGER.warn("delObject {}", key, e);
		}
		return result;
	}
	
	public static long batchDel(String... pattern) {
		long result = 0;
//		if (StringUtils.isBlank(pattern)) {
//			return result;
//		}
//		try {
//			for (String kp : pattern) {
//				Set<String> sets = jedisCluster.keys(kp+"*");
//				if(!CollectionUtils.isEmpty(sets)){
//					String[] keys = sets.toArray(new String[sets.size()]);
//					result = jedisCluster.del(keys);
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.warn("del pattern: {}", pattern, e);
//		}
		return result;		
	}
	
	/**
	 * 清空缓存
	 * @param pattern
	 * @return
	 */
	@Deprecated
	public static long clear(String pattern){
		long result = 0;
//		try {
//			result = jedisCluster.clear(pattern);	
//		} catch (Exception e) {
//			LOGGER.warn("del pattern: {}", pattern, e);
//		} 
		return result;		
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		try {
			result = jedisCluster.exists(key);
		} catch (Exception e) {
			LOGGER.warn("exists {}", key, e);
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
		try {
			result = jedisCluster.exists(getBytesKey(key));
		} catch (Exception e) {
			LOGGER.warn("existsObject {}", key, e);
		} 
		return result;
	}
	
	/**
	 * 递增数字
	 * @param key 键
	 * @param by 递增步长
	 * @return
	 */
	public static long incr(String key,long by) {
		long result = 0;
		try {
			if(by > 0){
				result = jedisCluster.incrBy(key, by);
			}else{
				result = jedisCluster.incr(key);				
			}
			
		} catch (Exception e) {
			LOGGER.warn("incr key={}, by={}", key, by, e);
		}
		return result;
	}
	
	/**
	 * 递减数字
	 * @param key 键
	 * @param by 递减步长
	 * @return
	 */
	public static long decr(String key,long by) {
		long result = 0;
		try {
			if(by > 0){
				result = jedisCluster.decrBy(key, by);
			}else{
				result = jedisCluster.decr(key);				
			}
			
		} catch (Exception e) {
			LOGGER.warn("decr key={}, by={}", key, by, e);
		}
		return result;
	}
	
	/**
	 * 递增数字
	 * @param key 键
	 * @param by 递增步长
	 * @return
	 */
	public static double incr(String key,double by) {
		double result = 0;
		try {
			if(by > 0){
				result = jedisCluster.incrByFloat(getBytesKey(key), by);
			}else{
				result = jedisCluster.incrByFloat(getBytesKey(key), 1d);				
			}
			
		} catch (Exception e) {
			LOGGER.warn("incr key={}, by={}", key, by, e);
		}
		return result;
	}
	
	/**
	 * 递减数字
	 * @param key 键
	 * @param by 递减步长
	 * @return
	 */
	public static double decr(String key,double by) {
		double result = 0;
		try {
			if(by > 0){
				result = jedisCluster.incrByFloat(getBytesKey(key), -by);
			}else{
				result = jedisCluster.incrByFloat(getBytesKey(key), -1d);			
			}
			
		} catch (Exception e) {
			LOGGER.warn("decr key={}, by={}", key, by, e);
		}
		return result;
	}
	
	/**
	 * 更新缓存信息时通知其他节点
	 * @param channel
	 * @param message
	 */
   public static void publish(String channel, Serializable message) {
        if (StringUtils.isBlank(channel) || message == null) {
            return;
        }
        Map<String, JedisPool> nodes = jedisCluster.getClusterNodes();
        for (String node : nodes.keySet()) {
            Jedis jedis = null;
            try {
                jedis = nodes.get(node).getResource();
                jedis.publish(channel, (String) message);
            } catch (Exception e) {
                // 任意节点损坏都会在此抛出异常
                LOGGER.warn("发送消息到{} {} : {}失败", node, channel, message, e);
            } finally {
                close(jedis);
            }
        }
    }

   /**
    * 销毁连接
    * @param jedis
    */
    public static void close(Jedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            LOGGER.warn("销毁连接时出现异常", e);
        }
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
	 *  指定缓存失效时间
	 * @author  FangJun
	 * @date 2016年8月14日
	 * @param key 缓存KEY
	 * @param seconds 失效时间（秒）
	 * @return
	 */
	public static Long expire(String key, int seconds) {
		Long result = 0l;
		try {
			result = jedisCluster.expire(key, seconds);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 重命名
	 * @param oldkey
	 * @param newkey
	 */
	public static void rename(String oldkey, String newkey) {
		try {
			jedisCluster.rename(oldkey, newkey);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * map赋值
	 * @param key
	 * @param map
	 * @param cacheSec
	 */
	public static void hmset(String key, Map<String, String> map, int cacheSec) {
		try {
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String field = it.next();
				jedisCluster.hset(key, field, String.valueOf(map.get(field)));
			}
			if(cacheSec > 0){
				JedisClusterUtils.expire(key, cacheSec);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	/**
	 * map赋值
	 * @param key
	 * @param map
	 * @param cacheSec
	 */
	public static void hset(String key, String field, String value) {
		try {
			jedisCluster.hset(key, field, value);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	
}
