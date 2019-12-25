package com.acgist.core.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.acgist.core.gateway.GatewayType;
import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.core.service.IGatewayService;
import com.acgist.data.service.pojo.entity.GatewayEntity;
import com.acgist.data.service.pojo.entity.GatewayEntity.Status;

/**
 * <p>拦截器 - 更新网关信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class UpdateInteceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Reference(version = "${acgist.service.version}")
	private IGatewayService gatewayService;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayType gatewayType = gatewaySession.getGatewayType();
		if(gatewaySession.done() && gatewayType.isSave()) {
			final GatewayResponse gatewayResponse = gatewaySession.getResponse();
			final Status status = gatewayType.isNotify() ? Status.ANSWER : Status.ANSWER;
			final GatewayEntity entity = new GatewayEntity();
			entity.setCode(gatewayResponse.getCode());
			entity.setStatus(status);
			entity.setQueryId(gatewaySession.getQueryId());
			entity.setMessage(gatewayResponse.getMessage());
			entity.setResponse(gatewayResponse.toString());
			this.gatewayService.update(entity);
		}
	}
	
}