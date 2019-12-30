package com.acgist.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.gateway.GatewaySession;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.utils.RedirectUtils;
import com.acgist.utils.ValidatorUtils;

/**
 * <p>拦截器 - 验证格式</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class GatewayVerifyInteceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayRequest gatewayRequest = gatewaySession.getRequest();
		final var message = ValidatorUtils.verify(gatewayRequest);
		if(message.fail()) {
			RedirectUtils.error(message.getCode(), message.getMessage(), request, response);
			return false;
		}
		return true;
	}
	
}
