package com.acgist.data.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

/**
 * <p>config - redis</p>
 */
@Configuration
@ConditionalOnClass(RedisConfiguration.class)
public class RedisConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		LOGGER.info("配置RedisTemplate");
		final RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setKeySerializer(buildKeySerializer());
		template.setValueSerializer(buildValueSerializer());
		template.setConnectionFactory(factory);
		template.afterPropertiesSet();
		return template;
	}

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
		final ObjectMapper mapper = new ObjectMapper();
		final PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
			.allowIfBaseType(Object.class)
			.build();
		final Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL);
		mapper.setSerializationInclusion(Include.NON_NULL);
		serializer.setObjectMapper(mapper);
		return serializer;
	}

}