package com.acgist.core.config;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.acgist.core.service.GatewayService;
import com.acgist.data.pojo.entity.GatewayEntity;

/**
 * <p>config - 网关保存队列</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
public class RabbitGatewaySaveConfig {
	
	@Autowired
	private GatewayService gatewayService;
	
	/**
	 * <p>监听 - 网关保存</p>
	 * 
	 * @param message 网关消息
	 */
	@RabbitListener(
		bindings = @QueueBinding(
			exchange = @Exchange(
				name = RabbitConfig.EXCHANGE_GATEWAY,
				type = ExchangeTypes.DIRECT,
				durable = Exchange.TRUE,
				autoDelete = Exchange.FALSE
			),
			value = @Queue(
				name = RabbitConfig.QUEUE_GATEWAY_SAVE,
				durable = Exchange.TRUE,
				exclusive = Exchange.FALSE,
				autoDelete = Exchange.FALSE
			),
			key = RabbitConfig.QUEUE_GATEWAY_SAVE
		),
		concurrency = "4-10" // 多条线程
	)
	public void process(GatewayEntity gateway) {
		this.gatewayService.save(gateway);
	}
	
}
