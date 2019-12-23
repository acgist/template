package com.acgist.core.order.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.order.pojo.message.OrderMessage;
import com.acgist.core.order.service.IOrderService;
import com.acgist.data.order.pojo.entity.OrderEntity;

/**
 * <p>服务 - 订单</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class OrderService implements IOrderService {
	
	@Override
	public OrderMessage build(OrderEntity order) {
		final OrderMessage message = new OrderMessage();
		message.buildMessage(AcgistCode.CODE_0000);
		return message;
	}
	
}
