package com.acgist.core.order.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.service.IOrderService;
import com.acgist.data.pojo.entity.OrderEntity;
import com.acgist.data.pojo.message.EntityResultMessage;

/**
 * <p>service - 订单</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class OrderServiceImpl implements IOrderService {
	
	@Override
	public EntityResultMessage<OrderEntity> build(OrderEntity order) {
		final EntityResultMessage<OrderEntity> message = new EntityResultMessage<>();
		message.buildMessage(AcgistCode.CODE_0000);
		return message;
	}
	
}
