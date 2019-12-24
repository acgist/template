package com.acgist.core.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.user.service.IUserService;

/**
 * <p>拦截器 - 签名校验</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class SignInteceptor implements HandlerInterceptor {

	@Reference(version = "${acgist.service.version}")
	private IUserService userService;
	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayRequest gatewayRequest = gatewaySession.getRequest();
		return true;
	}
	
}
