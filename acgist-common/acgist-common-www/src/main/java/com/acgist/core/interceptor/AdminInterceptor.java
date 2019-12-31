package com.acgist.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.request.AdminSession;
import com.acgist.data.pojo.message.AuthoMessage;
import com.acgist.data.service.RedisService;
import com.acgist.utils.RedirectUtils;

/**
 * <p>拦截器 - 管理员</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

	/**
	 * <p>权限Token</p>
	 */
	private static final String X_TOKEN = "X-Token";
	
	@Autowired
	private RedisService redisService;
	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String token = request.getHeader(X_TOKEN);
		if(StringUtils.isEmpty(token)) {
			RedirectUtils.error(AcgistCode.CODE_2001, request, response);
			return false;
		}
		final AdminSession session = AdminSession.getInstance(this.context);
		final AuthoMessage authoMessage = this.redisService.get(token);
		if(authoMessage == null) {
			RedirectUtils.error(AcgistCode.CODE_2001, request, response);
			return false;
		}
		if(authoMessage.fail()) {
			RedirectUtils.error(authoMessage.getCode(), request, response);
			return false;
		}
		session.setAuthoMessage(authoMessage);
		return true;
	}
	
}
