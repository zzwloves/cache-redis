package io.vicp.wloves.cache.redis.service;

import java.util.List;
import java.util.Map;

/**
 * 缓存service接口
 * @author zhengwei.zhu
 * @date 2018年4月23日 上午10:20:13
 * @version <b>1.0.0</b>
 */
public interface RedisService {

	/**
	 * 按默认时间(30分钟)缓存字符串类型
	 * @author zhengwei.zhu
	 * @date 2018年5月10日 下午1:22:35
	 * @param key
	 * @param value
	 * @return
	 */
	boolean setStringCacheWithDefaultExpireTime(String key, String value);

	/**
	 *  按设定时间缓存字符串类型
	 * @author zhengwei.zhu
	 * @date 2018年5月10日 下午1:23:27
	 * @param key
	 * @param value
	 * @param exp 过期时间，单位分钟
	 * @return
	 */
	boolean setStringCacheWithExpireTime(String key, String value, int exp);

	/**
	 * 获取字符串类型的value值
	 * @author zhengwei.zhu
	 * @date 2018年5月10日 下午1:23:56
	 * @param key
	 * @return
	 */
	String getStringValue(String key);

	/**
	 * 删除缓存
	 * @author zhengwei.zhu
	 * @date 2018年5月10日 下午1:24:33
	 * @param key
	 * @return
	 */
	boolean deleteCache(String key);
	
	/**
	 * 设置list类型的缓存
	 * @author zhengwei.zhu
	 * @date 2018年5月10日 下午1:24:50
	 * @param key
	 * @param value 单个实体对象，或者单个实体对象的List集合
	 * @return
	 */
	boolean setListCache(String key, Object value);

	/**
	 * 获取list类型的缓存
	 * @author zhengwei.zhu
	 * @date 2018年5月10日 下午1:25:13
	 * @param key
	 * @param t 返回的集合中元素类型
	 * @return
	 */
	<T> List<T> getListCache(String key, Class<T> t);

	/**
	 * 设置hash类型缓存
	 * @author zhengwei.zhu
	 * @date 2018年6月7日 下午4:40:08
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 * @return
	 */
	boolean setHashCache(String key, String hashKey, Object hashValue);
	
	/**
	 * 设置hash类型缓存
	 * @author zhengwei.zhu
	 * @date 2018年6月7日 下午2:33:43
	 * @param key
	 * @param value
	 * @return
	 */
	boolean setHashCache(String key, Map<String, Object> value);
	
	/**
	 * 获取hash类型的缓存，但是缓存还存在
	 * @author zhengwei.zhu
	 * @date 2018年6月7日 下午4:09:57
	 * @param key
	 * @return
	 */
	Map<String, Object> getHashCache(String key);
	
	/**
	 * 获取hash类型的缓存，并将缓存删除
	 * @author zhengwei.zhu
	 * @date 2018年6月7日 下午4:21:10
	 * @param key
	 * @return
	 */
	Map<String, Object> getAndDeleteHashCache(String key);

	/**
	 * 设置不会失效的String类型的缓存
	 * @author zhengwei.zhu
	 * @date 2018年6月28日 上午11:13:41
	 * @param key
	 * @param value
	 * @return
	 */
	boolean setStringCache(String key, String value);
}
