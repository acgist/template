package com.acgist.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.GatewaySession;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.core.service.IUserService;
import com.acgist.data.pojo.message.AuthoMessage;
import com.acgist.utils.GatewayUtils;
import com.acgist.utils.RedirectUtils;

/**
 * <p>拦截器 - 验证签名</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class GatewaySignatureInteceptor implements HandlerInterceptor {

	@Reference(version = "${acgist.service.version}")
	private IUserService userService;
	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayRequest gatewayRequest = gatewaySession.getRequest();
		final AuthoMessage authoMessage = gatewaySession.getAuthoMessage();
		final boolean verify = GatewayUtils.verify(authoMessage.getPassword(), gatewayRequest);
		if(verify) {
			return true;
		}
		RedirectUtils.error(AcgistCode.CODE_3001, request, response);
		return false;
	}
	
}
