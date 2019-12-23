package com.acgist.core.order.service;

import com.acgist.core.order.pojo.message.OrderMessage;
import com.acgist.data.order.pojo.entity.OrderEntity;

/**
 * <p>服务 - 订单</p>
 */
public interface IOrderService {

	/**
	 * <p>生成订单编号</p>
	 * <p>格式："O" + SN + yyyyMMddHHmmss + INDEX</p>
	 * 
	 * @return 订单编号
	 */
	String buildCode();
	
	/**
	 * <p>创建订单</p>
	 * 
	 * @param 订单实体
	 * 
	 * @return 订单消息
	 */
	OrderMessage build(OrderEntity order);

}
