package com.acgist.core.service.impl;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.core.service.IGatewayService;
import com.acgist.data.repository.GatewayRepository;
import com.acgist.data.service.pojo.entity.GatewayEntity;

/**
 * <p>service - 网关</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(version = "${acgist.service.version}")
public class GatewayServiceImpl implements IGatewayService {

	@Autowired
	private GatewayRepository gatewayRepository;
	
	@Override
	public void save(GatewayEntity entity) {
		this.gatewayRepository.save(entity);
	}

	@Override
	public void update(GatewayEntity entity) {
		this.gatewayRepository.updateResponse(entity);
	}

}
