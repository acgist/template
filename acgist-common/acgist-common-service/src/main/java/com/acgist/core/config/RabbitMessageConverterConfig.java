package com.acgist.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.utils.JSONUtils;

/**
 * <p>config - Rabbit消息转换器</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(EnableRabbit.class)
public class RabbitMessageConverterConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMessageConverterConfig.class);
	
	@Bean
	public MessageConverter rabbitMessageConverter() {
		LOGGER.info("配置Rabbit消息转换器");
		return new Jackson2JsonMessageConverter(JSONUtils.buildSerializeMapper());
	}

}
