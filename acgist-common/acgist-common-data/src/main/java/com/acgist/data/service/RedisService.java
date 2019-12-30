package com.acgist.data.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.acgist.data.config.RedisConfig;

/**
 * <p>service - Redis</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
@ConditionalOnBean(value = RedisConfig.class)
public class RedisService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * <p>获取对象</p>
	 * 
	 * @param <T> 类型
	 * @param key key
	 * 
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) this.redisTemplate.opsForValue().get(key);
	}
	
	/**
	 * <p>缓存对象</p>
	 * 
	 * @param key key
	 * @param value 对象
	 */
	public void store(String key, Object value) {
		this.redisTemplate.opsForValue().set(key, value);
	}
	
	/**
	 * <p>缓存时间期限对象</p>
	 * 
	 * @param key key
	 * @param value 对象
	 * @param timeout 过期时间
	 * @param unit 时间单位
	 */
	public void store(String key, Object value, long timeout, TimeUnit unit) {
		this.redisTemplate.opsForValue().set(key, value, timeout, unit);
	}
	
	/**
	 * <p>删除对象</p>
	 * 
	 * @param key key
	 */
	public void delete(String key) {
		this.redisTemplate.delete(key);
	}

}
