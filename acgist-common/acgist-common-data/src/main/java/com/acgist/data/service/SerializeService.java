package com.acgist.data.service;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.acgist.utils.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>service - 序列化</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class SerializeService {


	/**
	 * <p>创建key序列化方法</p>
	 * 
	 * @return key序列化方法
	 */
	public static final RedisSerializer<String> buildKeySerializer() {
		return StringRedisSerializer.UTF_8;
	}
	
	/**
	 * <p>创建value序列化方法</p>
	 * 
	 * @return value序列化方法
	 */
	public static final RedisSerializer<?> buildValueSerializer() {
		final ObjectMapper mapper = JSONUtils.buildMapper();
		final Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		serializer.setObjectMapper(mapper);
		return serializer;
	}
	
}
