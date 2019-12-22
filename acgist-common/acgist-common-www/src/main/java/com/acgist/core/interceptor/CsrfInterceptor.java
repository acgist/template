package com.api.core.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api.core.config.APIConstSession;
import com.api.core.controller.APIErrorController;
import com.api.core.gateway.APICode;
import com.api.utils.RedirectUtils;

/**
 * 拦截器 - CSRF<br>
 * POST请求验证是否含有TOKEN，防止CSRF攻击<br>
 * CSRF如果不存在或者严重成功后生成TOKEN（放入session），如果验证失败禁止当前访问
 */
@Component
public class CsrfInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String method = request.getMethod();
		final HttpSession session = request.getSession();
		final String uri = request.getRequestURI();
		final String trueToken = (String) session.getAttribute(APIConstSession.SESSION_CSRF_TOKEN);
		if(
			HttpMethod.POST.name().equalsIgnoreCase(method) && // 拦截POST
			!APIErrorController.ERROR_PATH.equals(uri) // 不拦截ERROR
		) {
			final String token = (String) request.getParameter(APIConstSession.SESSION_CSRF_TOKEN);
			if(StringUtils.equals(token, trueToken)) {
				buildCsrfToken(session);
				return true;
			}
			RedirectUtils.error(APICode.CODE_4403, request, response);
			return false;
		}
		if(trueToken == null) {
			buildCsrfToken(session);
		}
		return true;
	}
	
	/**
	 * 生成TOKEN
	 * @param session session
	 */
	private static final void buildCsrfToken(HttpSession session) {
		final String token = UUID.randomUUID().toString();
		session.setAttribute(APIConstSession.SESSION_CSRF_TOKEN, token);
	}
	
}
