package com.acgist.data.pojo.message;

import com.acgist.data.pojo.entity.OrderEntity;

/**
 * <p>message - 订单信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class OrderMessage extends EntityResultMessage<OrderEntity> {

	private static final long serialVersionUID = 1L;

	public OrderMessage() {
	}
	
	public OrderMessage(OrderEntity entity) {
		super(entity);
	}
	
}
