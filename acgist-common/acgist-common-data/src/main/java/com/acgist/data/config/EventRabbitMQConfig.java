package com.acgist.data.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.data.pojo.queue.QueueMessage;

/**
 * <p>config - 事件消息队列</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
public class EventRabbitMQConfig {

	@Value("${spring.application.name}")
	private String application;
	
	/**
	 * <p>队列</p>
	 * 
	 * @return
	 */
	@Bean
	public Queue eventQueue() {
		final String name = QueueMessage.Type.EVENT.buildQueue(this.application);
		// 名称、持久、排他、自动删除
		return new Queue(name, false, false, true);
	}

	/**
	 * <p>交换器</p>
	 * 
	 * @return 交换器
	 */
	@Bean
	public TopicExchange eventExchange() {
		// 名称
		return new Exchange(QueueMessage.Exchange.EVENT.name(), false, true);
	}

	/**
	 * 声明绑定关系
	 * 
	 * @return
	 */
	@Bean
	Binding binding(@Qualifier(RabbitMQConstant.PROGRAMMATICALLY_EXCHANGE) TopicExchange exchange,
		@Qualifier(RabbitMQConstant.PROGRAMMATICALLY_QUEUE) Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstant.PROGRAMMATICALLY_KEY);
	}
	
}
