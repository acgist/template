package com.acgist.core.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.config.RabbitConfig;
import com.acgist.data.pojo.entity.GatewayEntity;
import com.acgist.data.repository.GatewayRepository;

@Service
public class GatewayService {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private GatewayRepository gatewayRepository;
	
	/**
	 * <p>保存网关信息</p>
	 * 
	 * @param entity 网关信息
	 */
	public void save(GatewayEntity entity) {
		// 保存
		this.gatewayRepository.save(entity);
		// 通知：成功、已经响应
		if(
			AcgistCode.success(entity.getCode()) &&
			GatewayEntity.Status.ANSWER != entity.getStatus()
		) {
			// 发送通知队列
			this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_GATEWAY, RabbitConfig.QUEUE_GATEWAY_NOTIFY, entity);
		}
	}

	public void notify(GatewayEntity entity) {
		// TODO：发送
	}
	
}
