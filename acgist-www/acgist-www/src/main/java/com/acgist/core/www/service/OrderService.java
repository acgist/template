package com.acgist.core.www.service;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acgist.core.service.IOrderService;

/**
 * <p>service - 订单</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
public class OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
	
	@Reference(version = "${acgist.version}")
	private IOrderService orderService;
	
	public void build() {
		final var message = this.orderService.build(null);
		LOGGER.info("{}", message.getCode());
	}
	
}
