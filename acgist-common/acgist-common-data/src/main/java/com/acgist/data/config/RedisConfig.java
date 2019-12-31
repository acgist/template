package com.acgist.data.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.acgist.data.service.SerializeService;

/**
 * <p>config - Redis</p>
 * <p>注意：不能使用数组对象</p>
 */
@Configuration
@ConditionalOnClass(RedisConfiguration.class)
public class RedisConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		LOGGER.info("配置RedisTemplate");
		final RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setKeySerializer(SerializeService.buildKeySerializer());
		template.setValueSerializer(SerializeService.buildValueSerializer());
		template.setConnectionFactory(factory);
		template.afterPropertiesSet();
		return template;
	}

}