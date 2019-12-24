package com.acgist.core.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.gateway.gateway.GatewayType;
import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.gateway.gateway.response.GatewayResponse;
import com.acgist.core.gateway.service.IGatewayService;

/**
 * <p>拦截器 - 保存</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class SaveInteceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private IGatewayService gatewayService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayRequest gatewayRequest = gatewaySession.getRequest();
		final GatewayType gatewayType = gatewaySession.getGatewayType();
		if(gatewayType.isSave()) {
			this.gatewayService.save(gatewaySession.getQueryId(), gatewayRequest);
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayResponse gatewayResponse = gatewaySession.getResponse();
		final GatewayType gatewayType = gatewaySession.getGatewayType();
		if(gatewayType.isSave()) {
			final Status status = gatewayType.isNotify() ? Status.ANSWER : Status.ANSWER;
			this.gatewayService.update(gatewaySession.getQueryId(), status, gatewayResponse);
		}
	}
	
}
