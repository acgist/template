package com.acgist.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.data.pojo.entity.PermissionEntity;

/**
 * <p>utils - request</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class RequestUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtils.class);

	/**
	 * <p>客户端IP</p>
	 */
	public static final String IP = "ip";
	/**
	 * <p>请求地址</p>
	 */
	public static final String URI = "uri";
	/**
	 * <p>请求方法</p>
	 */
	public static final String METHOD = "method";
	/**
	 * <p>请求参数</p>
	 */
	public static final String QUERY = "query";
	/**
	 * <p>请求信息</p>
	 */
	public static final String REQUEST = "request";
	
	/**
	 * <p>获取请求表单信息</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 请求信息
	 */
	public static final String requestMessage(HttpServletRequest request) {
		final Map<String, Object> message = new HashMap<>();
		message.put(IP, clientIP(request));
		message.put(METHOD, request.getMethod());
		message.put(URI, request.getRequestURI());
		message.put(QUERY, request.getQueryString());
		message.put(REQUEST, requestData(request));
		return JSONUtils.toJSON(message);
	}
	
	
	/**
	 * <p>获取请求网关信息</p>
	 * 
	 * @param permission 权限
	 * @param request 请求
	 * 
	 * @return 网关信息
	 */
	public static final GatewayRequest requestGateway(PermissionEntity permission, HttpServletRequest request) {
		final Class<?> requestClazz = BeanUtils.forName(permission.getRequestClazz());
		final GatewayRequest gatewayRequest = (GatewayRequest) BeanUtils.newInstance(requestClazz);
		GatewayUtils.pack(gatewayRequest, requestData(request));
		return gatewayRequest;
	}
	
	/**
	 * <p>获取请求数据</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 请求数据
	 */
	public static final Map<String, String> requestData(HttpServletRequest request) {
		final Map<String, String> data;
		// 请求数据
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
		final String json = builder.toString();
		if(json.isEmpty()) {
			data = new HashMap<String, String>();
		} else {
			data = JSONUtils.toMapSimple(json);
		}
		// 表单数据
		request.getParameterMap().entrySet().stream()
			.filter(entry -> entry.getKey() != null && entry.getValue() != null && entry.getValue().length > 0)
			.forEach(entry -> data.put(entry.getKey(), entry.getValue()[0]));
		return data;
	}
	
	/**
	 * <p>获取客户端IP</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 客户端IP
	 */
	public static final String clientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
}
