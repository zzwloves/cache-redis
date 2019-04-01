package io.vicp.wloves.cache.redis.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * redis配置实体类
 * @author zhengwei.zhu
 * @date 2018年9月17日 下午2:29:46
 * @version <b>1.0.0</b>
 */
@Component
public class RedisConfigEntity {

	@Value("${redis.host}")
	private String host;
	private Integer port;
	private Integer dbindex;
	@Value("${redis.password}") 
	private String password;
	private Integer minIdle;
	private Integer maxIdle;
	private Integer maxTotal;
	private Integer maxWaitMillis;
	
	@Value("${redis.port}")
	private String port_;
	@Value("${redis.dbindex}")
	private String dbindex_;
	@Value("${redisPool.minIdle}")
	private String minIdle_;
	@Value("${redisPool.maxIdle}")
	private String maxIdle_;
	@Value("${redisPool.maxTotal}")
	private String maxTotal_;
	@Value("${redisPool.maxWaitMillis}")
	private String maxWaitMillis_;
	
	@PostConstruct
	public void afterPropertiesSet() {
		Assert.isTrue(!"${redis.host}".equals(host), "未配置redis数据库连接地址！");
		Assert.isTrue(!"${redis.password}".equals(password), "未配置redis数据库连接密码！");
		port = "${redis.port}".equals(port_) ? 6379 : new Integer(port_);
		dbindex = "${redis.dbindex}".equals(dbindex_) ? 0 : new Integer(dbindex_);
		minIdle = "${redisPool.minIdle}".equals(minIdle_) ? 10 : new Integer(minIdle_);
		maxIdle = "${redisPool.maxIdle}".equals(maxIdle_) ? 30 : new Integer(maxIdle_);
		maxTotal = "${redisPool.maxTotal}".equals(maxTotal_) ? 1024 : new Integer(maxTotal_);
		maxWaitMillis = "${redisPool.maxWaitMillis}".equals(maxWaitMillis_) ? 3000 : new Integer(maxWaitMillis_);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getDbindex() {
		return dbindex;
	}

	public void setDbindex(Integer dbindex) {
		this.dbindex = dbindex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(Integer maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	@Override
	public String toString() {
		return "ConfigEntity {host=" + host + ", port=" + port + ", dbindex="
				+ dbindex + ", password=" + password + ", minIdle=" + minIdle
				+ ", maxIdle=" + maxIdle + ", maxTotal=" + maxTotal
				+ ", maxWaitMillis=" + maxWaitMillis + "}";
	}

}
