package com.acgist.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.service.PermissionService;
import com.acgist.data.pojo.entity.PermissionEntity;

/**
 * <p>拦截器 - 重要操作保存</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class AdminSaveInterceptor implements HandlerInterceptor {

	@Autowired
	private PermissionService permissionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final PermissionEntity permission = this.permissionService.getPermission(request.getRequestURI());
		if(permission.getSave()) {
			// 保存
		}
		return true;
	}
	
}
