package com.acgist.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.gateway.GatewaySession;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.core.service.IUserService;
import com.acgist.data.pojo.message.AuthoMessage;
import com.acgist.utils.RedirectUtils;

/**
 * <p>拦截器 - 用户授权</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class GatewayAuthoInterceptor implements HandlerInterceptor {

	@Reference(version = "${acgist.service.version}")
	private IUserService userService;
	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayRequest gatewayRequest = gatewaySession.getRequest();
		final AuthoMessage authoMessage = this.userService.getAuthoMessage(gatewayRequest.getUsername());
		if(authoMessage.fail()) {
			RedirectUtils.error(authoMessage.getCode(), request, response);
			return false;
		}
		gatewaySession.setAuthoMessage(authoMessage);
		return true;
	}
	
}
