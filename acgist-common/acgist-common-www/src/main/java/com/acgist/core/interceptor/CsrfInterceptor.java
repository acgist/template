package com.acgist.core.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.config.AcgistConstSession;
import com.acgist.core.controller.AcgistErrorController;
import com.acgist.utils.RedirectUtils;

/**
 * <p>拦截器 - CSRF</p>
 * <p>防止CSRF攻击：POST请求验证是否含有Token</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class CsrfInterceptor implements HandlerInterceptor {
	
	/**
	 * <p>表单Token：{@value}</p>
	 */
	private static final String FORM_TOKEN = "token";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String method = request.getMethod();
		final String uri = request.getRequestURI();
		final HttpSession session = request.getSession();
		final String trueToken = (String) session.getAttribute(AcgistConstSession.SESSION_CSRF_TOKEN);
		if(
			!AcgistErrorController.ERROR_PATH.equals(uri) && // 忽略错误页面
			HttpMethod.POST.name().equalsIgnoreCase(method) // 拦截POST请求
		) {
			final String token = (String) request.getParameter(FORM_TOKEN);
			if(StringUtils.equals(token, trueToken)) {
				buildCsrfToken(session);
				return true;
			}
			RedirectUtils.error(AcgistCode.CODE_4403, request, response);
			return false;
		}
		// 没有Token创建
		if(trueToken == null) {
			buildCsrfToken(session);
		}
		return true;
	}
	
	/**
	 * <p>生成Token</p>
	 * 
	 * @param session session
	 */
	private static final void buildCsrfToken(HttpSession session) {
		final String token = UUID.randomUUID().toString();
		session.setAttribute(AcgistConstSession.SESSION_CSRF_TOKEN, token);
	}
	
}
