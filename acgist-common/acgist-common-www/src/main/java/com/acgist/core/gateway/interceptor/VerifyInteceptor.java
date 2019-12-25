package com.acgist.core.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.utils.RedirectUtils;
import com.acgist.utils.ValidatorUtils;

/**
 * <p>拦截器 - 数据校验</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class VerifyInteceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayRequest gatewayRequest = gatewaySession.getRequest();
		final String message = ValidatorUtils.verify(gatewayRequest);
		if(StringUtils.isNotEmpty(message)) {
			RedirectUtils.error(AcgistCode.CODE_3000, message, request, response);
			return false;
		}
		return true;
	}
	
}
