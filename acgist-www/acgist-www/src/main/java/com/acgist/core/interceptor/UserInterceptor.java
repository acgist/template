package com.acgist.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.controller.UserContoller;
import com.acgist.core.pojo.session.UserSession;
import com.acgist.utils.RedirectUtils;
import com.acgist.utils.URLUtils;

/**
 * <p>拦截器 - 用户中心</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final var session = UserSession.get(request);
		final var uri = request.getRequestURI();
		if(session == null) {
			RedirectUtils.redirectToGet(response, UserContoller.LOGIN + "?uri=" + URLUtils.encode(uri));
			return false;
		}
		return true;
	}
	
}
