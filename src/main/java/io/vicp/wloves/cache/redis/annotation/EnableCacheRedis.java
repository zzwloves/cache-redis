package io.vicp.wloves.cache.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.vicp.wloves.cache.redis.config.RedisConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 注解，使用后会将{@link RedisConfiguration}导入到spring中
 *
 * @author zhengwei.zhu
 * @version <b>1.0.0</b>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisConfiguration.class)
public @interface EnableCacheRedis {

}
