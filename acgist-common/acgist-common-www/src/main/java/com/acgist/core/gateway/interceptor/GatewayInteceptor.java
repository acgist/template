package com.acgist.core.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.GatewayMapping;
import com.acgist.core.gateway.GatewayType;
import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.utils.RedirectUtils;
import com.acgist.utils.RequestUtils;
import com.acgist.utils.UuidUtils;

/**
 * <p>拦截器 - 包装网关信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class GatewayInteceptor implements HandlerInterceptor {
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		gatewaySession.setQueryId(UuidUtils.uuid());
		final GatewayType gatewayType = GatewayMapping.getInstance().getGatewayType(request.getRequestURI());
		if(gatewayType == null) {
			RedirectUtils.error(AcgistCode.CODE_1000, request, response);
			return false;
		}
		gatewaySession.setGatewayType(gatewayType);
		final GatewayRequest gatewayRequest = RequestUtils.gateway(gatewayType, request);
		if(gatewayRequest == null) {
			RedirectUtils.error(AcgistCode.CODE_4400, "请求数据不能为空", request, response);
			return false;
		}
		gatewaySession.buildResponse(gatewayRequest);
		return true;
	}
	
}
