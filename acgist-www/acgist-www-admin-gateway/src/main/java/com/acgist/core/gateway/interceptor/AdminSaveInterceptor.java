package com.acgist.core.gateway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.pojo.session.AdminSession;
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
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final AdminSession session = AdminSession.getInstance(this.context);
		final PermissionEntity permission = session.getPermission();
		if(permission.getSave()) {
			// 保存
		}
		return true;
	}
	
}
