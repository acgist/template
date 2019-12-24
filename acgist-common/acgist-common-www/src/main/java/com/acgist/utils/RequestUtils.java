package com.acgist.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>utils - request</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class RequestUtils {

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
	public static final String REQUEST_MESSAGE = "requestMessage";
	
	/**
	 * <p>获取请求信息</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 请求信息
	 */
	public static final String requestMessage(HttpServletRequest request) {
		final Map<String, String> message = new HashMap<>();
		message.put(IP, clientIP(request));
		message.put(URI, request.getRequestURI());
		message.put(METHOD, request.getMethod());
		message.put(QUERY, request.getQueryString());
		final String parameter = request.getParameterMap().entrySet().stream()
			.map(entry -> {
				return entry.getKey() + "=" + String.join(",", entry.getValue());
			})
			.collect(Collectors.joining("&"));
		message.put(REQUEST_MESSAGE, parameter);
		return JSONUtils.toJSON(message);
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
