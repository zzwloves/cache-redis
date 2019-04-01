package io.vicp.wloves.cache.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置类
 * @author zhengwei.zhu
 * @date 2018年9月17日 下午1:45:01
 * @version <b>1.0.0</b>
 */
@Configuration
@ComponentScan("io.vicp.wloves.cache.redis")
public class RedisConfiguration {
	
	@Autowired
	RedisConfigEntity redisConfigEntity;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMinIdle(redisConfigEntity.getMinIdle());
		poolConfig.setMaxIdle(redisConfigEntity.getMaxIdle());
		poolConfig.setMaxTotal(redisConfigEntity.getMaxTotal());
		poolConfig.setMaxWaitMillis(redisConfigEntity.getMaxWaitMillis());

		// jedis factory
		JedisConnectionFactory jsf = new JedisConnectionFactory(poolConfig);
		jsf.setHostName(redisConfigEntity.getHost());
		jsf.setPort(redisConfigEntity.getPort());
		jsf.setPassword(redisConfigEntity.getPassword());
		jsf.setDatabase(redisConfigEntity.getDbindex());
		return jsf;
	}
	
	@Bean(name = "businessRedisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}
	
}
