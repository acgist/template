package com.acgist.core.service;

import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.gateway.gateway.response.GatewayResponse;
import com.acgist.data.pojo.entity.GatewayEntity.Status;

/**
 * <p>service - 网关</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface IGatewayService {

	/**
	 * <p>保存网关信息</p>
	 * 
	 * @param queryId 请求编号
	 * @param gateway 请求
	 */
	void save(String queryId, GatewayRequest request);
	
	/**
	 * <p>更新网关信息</p>
	 * 
	 * @param queryId 请求编号
	 * @param status 状态
	 * @param response 响应
	 */
	void update(String queryId, Status status, GatewayResponse response);
	
}
