package com.acgist.core.order.service;

import com.acgist.core.order.pojo.message.OrderMessage;
import com.acgist.data.order.pojo.entity.OrderEntity;

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
	OrderMessage build(OrderEntity order);

}
