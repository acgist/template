package com.acgist.data.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.acgist.data.service.SerializeService;

/**
 * <p>config - 缓存</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
@EnableCaching
@ConditionalOnBean(value = RedisConfig.class)
public class CacheConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		LOGGER.info("配置CacheManager");
		// 缓存配置
		final RedisCacheConfiguration config = RedisCacheConfiguration
			.defaultCacheConfig()
			// 序列化：key
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(SerializeService.buildKeySerializer()))
			// 序列化：value
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(SerializeService.buildValueSerializer()))
//			.disableCachingNullValues() // 禁止缓存空值
			.entryTtl(Duration.ofMinutes(30)); // 设置过期时间
		// 创建缓存
		final RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
			.cacheDefaults(config)
			.build();
		return cacheManager;
	}

}
