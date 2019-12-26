package com.acgist.core.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.www.service.PermissionService;
import com.acgist.data.service.pojo.entity.PermissionEntity;
import com.acgist.data.service.pojo.message.AuthoMessage;
import com.acgist.utils.RedirectUtils;

/**
 * <p>拦截器 - 权限鉴定</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class PermissionInteceptor implements HandlerInterceptor {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private PermissionService permissionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final PermissionEntity permission = gatewaySession.getPermission();
		final AuthoMessage authoMessage = gatewaySession.getAuthoMessage();
		final boolean verify = permissionService.hasPermission(authoMessage.getRoles(), permission);
		if(verify) {
			return true;
		}
		RedirectUtils.error(AcgistCode.CODE_2001, request, response);
		return false;
	}
	
}
