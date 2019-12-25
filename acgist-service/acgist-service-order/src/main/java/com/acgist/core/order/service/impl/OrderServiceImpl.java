package com.acgist.core.order.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.service.IOrderService;
import com.acgist.data.service.pojo.entity.OrderEntity;
import com.acgist.data.service.pojo.message.OrderMessage;

/**
 * <p>服务 - 订单</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(retries = 0, version = "${acgist.version}")
public class OrderServiceImpl implements IOrderService {
	
	@Override
	public OrderMessage build(OrderEntity order) {
		final OrderMessage message = new OrderMessage();
		message.buildMessage(AcgistCode.CODE_0000);
		return message;
	}
	
}
