package com.acgist.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.core.gateway.service.IGatewayService;
import com.acgist.data.repository.GatewayRepository;
import com.acgist.data.service.pojo.entity.GatewayEntity;
import com.acgist.data.service.pojo.entity.GatewayEntity.Status;

/**
 * <p>service - 网关</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
public class GatewayService implements IGatewayService {

	@Autowired
	private GatewayRepository gatewayRepository;
	
	@Override
	public void save(String queryId, GatewayRequest request) {
		final GatewayEntity gateway = new GatewayEntity();
		gateway.setStatus(Status.RECEIVE);
		gateway.setQueryId(queryId);
		gateway.setRequest(request.toString());
		gateway.setUsername(request.getUsername());
		this.gatewayRepository.save(gateway);
	}

	@Override
	public void update(String queryId, Status status, GatewayResponse response) {
		final GatewayEntity gateway = new GatewayEntity();
		gateway.setQueryId(queryId);
		gateway.setCode(response.getCode());
		gateway.setStatus(status);
		gateway.setMessage(response.getMessage());
		gateway.setResponse(response.toString());
		this.gatewayRepository.updateResponse(gateway);
	}

}
