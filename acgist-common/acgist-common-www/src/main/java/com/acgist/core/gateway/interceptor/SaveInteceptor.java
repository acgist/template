package com.acgist.core.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.gateway.GatewayType;
import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.core.service.IGatewayService;
import com.acgist.data.service.pojo.entity.GatewayEntity;
import com.acgist.data.service.pojo.entity.GatewayEntity.Status;

/**
 * <p>拦截器 - 保存网关信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class SaveInteceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Reference(version = "${acgist.service.version}")
	private IGatewayService gatewayService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayType gatewayType = gatewaySession.getGatewayType();
		if(gatewayType.isSave()) {
			final GatewayRequest gatewayRequest = gatewaySession.getRequest();
			final GatewayEntity entity = new GatewayEntity();
			entity.setStatus(Status.RECEIVE);
			entity.setQueryId(gatewaySession.getQueryId());
			entity.setRequest(gatewayRequest.toString());
			entity.setUsername(gatewayRequest.getUsername());
			this.gatewayService.save(entity);
		}
		return true;
	}
	
}
