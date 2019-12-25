package com.acgist.core.service;

import com.acgist.data.service.pojo.entity.GatewayEntity;

/**
 * <p>服务 - 网关</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface IGatewayService {

	/**
	 * <p>保存网关信息</p>
	 * 
	 * @param entity 网关信息
	 */
	void save(GatewayEntity entity);
	
	/**
	 * <p>更新网关信息</p>
	 * 
	 * @param entity 网关信息
	 */
	void update(GatewayEntity entity);
	
}
