package com.acgist.core.config;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import com.acgist.core.service.EventService;
import com.acgist.data.pojo.queue.EventQueueMessage;

/**
 * <p>config - 事件队列</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
@ConditionalOnBean(EventService.class)
@ConditionalOnClass(EnableRabbit.class)
public class RabbitEventConfig {

	@Autowired
	private EventService eventService;
	
	/**
	 * <p>监听 - 事件消息</p>
	 * 
	 * @param message 事件消息
	 */
	@RabbitListener(
		bindings = @QueueBinding(
			exchange = @Exchange(
				name = RabbitConfig.EXCHANGE_EVENT,
				type = ExchangeTypes.FANOUT,
				durable = Exchange.FALSE,
				autoDelete = Exchange.TRUE
			),
			value = @Queue(
				name = RabbitConfig.QUEUE_EVENT,
				durable = Exchange.FALSE,
				exclusive = Exchange.FALSE,
				autoDelete = Exchange.TRUE
			)
		)
	)
	public void process(EventQueueMessage message) {
		this.eventService.process(message);
	}

}
