package com.acgist.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.acgist.core.pojo.session.GatewaySession;

/**
 * <p>service - 网关</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public abstract class GatewayService {

	@Autowired
	private ApplicationContext context;
	
	/**
	 * <p>获取网关信息</p>
	 * 
	 * @return 网关信息
	 */
	protected GatewaySession gatewaySession() {
		return GatewaySession.getInstance(this.context);
	}
	
}
