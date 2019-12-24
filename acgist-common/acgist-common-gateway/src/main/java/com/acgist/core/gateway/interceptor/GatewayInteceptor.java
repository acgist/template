package com.acgist.core.gateway.interceptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.gateway.GatewayMapping;
import com.acgist.core.gateway.gateway.GatewaySession;
import com.acgist.core.gateway.gateway.GatewayType;
import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.utils.GatewayUtils;
import com.acgist.utils.JSONUtils;
import com.acgist.utils.RedirectUtils;
import com.acgist.utils.UuidUtils;

/**
 * <p>拦截器 - 获取请求信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
public class GatewayInteceptor implements HandlerInterceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayInteceptor.class);

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
		final GatewayRequest gatewayRequest = request(gatewayType, request);
		if(gatewayRequest == null) {
			RedirectUtils.error(AcgistCode.CODE_4400, "请求数据不能为空", request, response);
			return false;
		}
		gatewaySession.buildResponse(gatewayRequest);
		return true;
	}
	
	/**
	 * <p>获取请求数据</p>
	 * 
	 * @param gatewayType 请求类型
	 * @param request 请求
	 * 
	 * @return 网关请求
	 */
	private GatewayRequest request(GatewayType gatewayType, HttpServletRequest request) {
		GatewayRequest gatewayRequest = null;
		// 读取请求数据
		final StringBuffer builder = new StringBuffer();
		try {
			String tmp = null;
			final var input = request.getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			while((tmp = reader.readLine()) != null) {
				builder.append(tmp);
			}
		} catch (Exception e) {
			LOGGER.error("获取请求数据异常", e);
		}
		if(builder.length() != 0) {
			gatewayRequest = (GatewayRequest) JSONUtils.toJava(builder.toString(), gatewayType.getRequestClazz());
		}
		// 读取请求参数
		final var data = request.getParameterMap().entrySet().stream()
			.filter(entry -> entry.getKey() != null && entry.getValue() != null && entry.getValue().length > 0)
			.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()[0]));
		if(MapUtils.isEmpty(data)) {
			if(gatewayRequest == null) {
				gatewayRequest = gatewayType.newRequest();
			}
			// 设置数据
			GatewayUtils.pack(gatewayRequest, data);
		}
		return gatewayRequest;
	}
	
}
