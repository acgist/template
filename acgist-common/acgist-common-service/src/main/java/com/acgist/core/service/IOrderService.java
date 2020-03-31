package com.acgist.core.service;

import com.acgist.data.pojo.entity.OrderEntity;
import com.acgist.data.pojo.message.EntityResultMessage;

/**
 * <p>服务 - 订单</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface IOrderService {

	/**
	 * <p>创建订单</p>
	 * 
	 * @param 订单实体
	 * 
	 * @return 订单消息
	 */
	EntityResultMessage<OrderEntity> build(OrderEntity order);

}
