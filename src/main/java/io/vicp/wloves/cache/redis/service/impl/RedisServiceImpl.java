package io.vicp.wloves.cache.redis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import io.vicp.wloves.cache.redis.util.JsonUtil;
import io.vicp.wloves.cache.redis.util.StringUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vicp.wloves.cache.redis.service.RedisService;

/**
 * 缓存service接口实现类
 * @author zhengwei.zhu
 * @date 2018年4月23日 上午10:20:34
 * @version <b>1.0.0</b>
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {

    @Resource(name = "businessRedisTemplate")
    private volatile RedisTemplate<String, Object> businessRedisTemplate;

    /**
	 * 按默认时间30分钟存缓存
	 * @author zhengwei.zhu
	 * @date 2018年4月23日 上午10:27:24
	 * @param key
	 * @param value
	 * @return
	 */
    @Override
    public boolean setStringCacheWithDefaultExpireTime(final String key,final String value) {
    	if (StringUtil.isNotEmptyAll(key, value)) {
    		return setStringCacheWithExpireTime(key, value, 30);
		}
        return false;
    }
    
    /**
	 * 按设置时间存缓存
	 * @author zhengwei.zhu
	 * @date 2018年4月23日 上午10:27:32
	 * @param key 
	 * @param value
	 * @param exp 缓存保存时间，单位是分钟
	 * @return
	 */
    @Override
    public boolean setStringCacheWithExpireTime(final String key, final String value, final int exp) {
    	if (StringUtil.isNotEmptyAll(key, value) && exp > 0) {
			businessRedisTemplate.opsForValue().set(key, value, exp, TimeUnit.MINUTES);
			return true;
		}
    	return false;
    }

    /**
	 * 通过key获取缓存中的value值
	 * @author zhengwei.zhu
	 * @date 2018年4月23日 上午10:27:38
	 * @param key
	 * @return
	 */
    @Override
    public String getStringValue(String key) {
    	if (StringUtil.isNotEmpty(key)) {
    		Object value = businessRedisTemplate.opsForValue().get(key);
    		
    		if (value != null){
    			setStringCacheWithDefaultExpireTime(key, value.toString());
    			return value.toString();
    		}
    	}
        return null;
    }

    /**
	 * 删除缓存
	 * @author zhengwei.zhu
	 * @date 2018年4月23日 上午10:27:57
	 * @param key
	 * @return
	 */
    @Override
    public boolean deleteCache(String key) {
    	if (StringUtil.isEmpty(key)) {
			return false;
		}
        businessRedisTemplate.delete(key);			
        return true;
    }
    
    /**
     * 设置list类型缓存
     * @author zhengwei.zhu
     * @date 2018年5月9日 下午1:19:46
     * @param key
     * @param value 单个实体对象，或者单个实体对象的List集合
     * @return
     */
    @Override
    public boolean setListCache(String key, Object value) {
    	if (StringUtil.isEmpty(key) || Objects.isNull(value)) {
			return false;
		}
		businessRedisTemplate.opsForList().rightPush(key, JsonUtil.toJson(value));
		return true;
    }
    
    /**
     * 获取list类型的缓存
     * @author zhengwei.zhu
     * @param <T>
     * @date 2018年4月23日 上午11:46:45
     * @param key
     * @param t list中返回结果的元素类型
     * @return
     */
    @Override
	public <T> List<T> getListCache(String key, Class<T> t) {
		List<T> list = new ArrayList<T>();
		boolean isListType = true;
		for (;;) {
			Object leftPop = businessRedisTemplate.opsForList().leftPop(key);
			if (leftPop == null) {
				break;
			}
			
			if (isListType) {
				try {
					List<T> value = JsonUtil.fromJson_(leftPop.toString(), new TypeReference<List<T>>() {});
					list.addAll(value);
				} catch (Exception e) {
					isListType = false;
				}
			} 
			if (!isListType) {
				T value = JsonUtil.fromJson(leftPop.toString(), t);
				list.add(value);
			}
		}

		return list;
	}

	/**
	 * 设置hash类型缓存
	 * @author zhengwei.zhu
	 * @date 2018年6月7日 下午2:33:43
	 * @see RedisService#setHashCache(java.lang.String, java.util.Map)
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 * @return
	 */
	@Override
	public boolean setHashCache(String key, String hashKey, Object hashValue) {
		if (!StringUtil.isNotEmptyAll(key, hashKey) || Objects.isNull(hashValue)) {
			return false;
		}
		businessRedisTemplate.opsForHash().put(key, hashKey, hashValue);
		return true;
	}

	/**
	 * 设置hash类型缓存
	 * @author zhengwei.zhu
	 * @date 2018年6月7日 下午2:33:43
	 * @see RedisService#setHashCache(java.lang.String, java.util.Collection)
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean setHashCache(String key, Map<String, Object> value) {
		if (StringUtil.isEmpty(key) || Objects.isNull(value)) {
			return false;
		}
		for (Entry<String, Object> entry : value.entrySet()) {
			if (StringUtil.isEmpty(entry.getKey()) || Objects.isNull(entry.getValue())) {
				return false;
			}
		}
		businessRedisTemplate.opsForHash().putAll(key, value);
		return true;
	}

	/**
	 * 获取hash类型的缓存，但是缓存还存在
	 * @author zhengwei.zhu
	 * @date 2018年6月7日 下午4:09:57
	 * @see RedisService#getHashCache(java.lang.String)
	 * @param key
	 * @return
	 */
	@Override
	public Map<String, Object> getHashCache(String key) {
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		HashOperations<String, String, Object> opsForHash = businessRedisTemplate.opsForHash();
		return opsForHash.entries(key);
	}

	/**
	 * 获取hash类型的缓存，并将缓存删除
	 * @author zhengwei.zhu
	 * @date 2018年6月7日 下午4:21:10
	 * @see RedisService#getAndDeleteHashCache(java.lang.String)
	 * @param key
	 * @return
	 */
	@Override
	public Map<String, Object> getAndDeleteHashCache(String key) {
		Map<String, Object> result = getHashCache(key);
		deleteCache(key);
		return result == null || result.isEmpty() ? null : result;
	}

	/**
	 * 设置不会失效的String类型的缓存
	 * @author zhengwei.zhu
	 * @date 2018年6月28日 上午11:14:38
	 * @see RedisService#setStringCache(java.lang.String, java.lang.String)
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean setStringCache(String key, String value) {
		businessRedisTemplate.opsForValue().set(key, value);
		return true;
	}
}
