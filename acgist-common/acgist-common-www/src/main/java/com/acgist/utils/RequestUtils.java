package com.acgist.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.acgist.utils.JSONUtils;

/**
 * utils - request
 */
public class RequestUtils {

	/**
	 * 请求IP
	 */
	public static final String IP = "ip";
	/**
	 * 请求方法
	 */
	public static final String METHOD = "method";
	/**
	 * 请求地址
	 */
	public static final String URI = "uri";
	/**
	 * 请求参数
	 */
	public static final String PARAMETER = "parameter";
	
	/**
	 * 获取请求信息
	 */
	public static final String requestMessage(HttpServletRequest request) {
		final Map<String, String> message = new HashMap<>();
		message.put(IP, clientIP(request));
		message.put(METHOD, request.getMethod());
		message.put(URI, request.getRequestURI());
		final String parameter = request.getParameterMap().entrySet().stream()
		.map(entry -> {
			return entry.getKey() + "=" + String.join(",", entry.getValue());
		}).collect(Collectors.joining("&"));
		message.put(PARAMETER, parameter);
		return JSONUtils.toJSON(message);
	}
	
	/**
	 * 获取客户端IP地址
	 * @param request 请求
	 * @return ip地址
	 */
	public static final String clientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
}
