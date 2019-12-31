package com.acgist.core.interceptor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.core.pojo.request.GatewaySession;
import com.acgist.utils.DateUtils;
import com.acgist.utils.RedirectUtils;

/**
 * <p>拦截器 - 验证时间</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class GatewayTimeInteceptor implements HandlerInterceptor {

	@Value("${acgist.gateway.duration:10}")
	private int duration;
	@Autowired
	private ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final GatewaySession gatewaySession = GatewaySession.getInstance(this.context);
		final GatewayRequest gatewayRequest = gatewaySession.getRequest();
		final String requestTime = gatewayRequest.getRequestTime();
		if(StringUtils.isEmpty(requestTime)) {
			RedirectUtils.error(AcgistCode.CODE_3000, "请求时间不能为空", request, response);
			return false;
		}
		final Date requestDate = DateUtils.parse(requestTime);
		if(requestDate == null) {
			RedirectUtils.error(AcgistCode.CODE_3000, "请求时间格式错误", request, response);
			return false;
		}
		final Duration duration = Duration.between(LocalDateTime.now(), requestDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		if(Math.abs(duration.toMinutes()) > this.duration) {
			RedirectUtils.error(AcgistCode.CODE_3002, "请求超时", request, response);
			return false;
		}
		return true;
	}
	
}