package com.acgist.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.data.service.pojo.entity.PermissionEntity;

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
	public static final String FORM = "form";
	
	/**
	 * <p>获取请求表单信息</p>
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
		message.put(QUERY, request.getQueryString()); // TODO：JSON数据
		final String form = request.getParameterMap().entrySet().stream()
			.map(entry -> {
				return entry.getKey() + "=" + String.join(",", entry.getValue());
			})
			.collect(Collectors.joining("&"));
		message.put(FORM, form);
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
		final Class<?> requestClazz = BeanUtils.forName(permission.getRequestClazz());
		if(builder.length() != 0) {
			gatewayRequest = (GatewayRequest) JSONUtils.toJava(builder.toString(), requestClazz);
		}
		// 读取请求表单
		final var from = request.getParameterMap().entrySet().stream()
			.filter(entry -> entry.getKey() != null && entry.getValue() != null && entry.getValue().length > 0)
			.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()[0]));
		if(MapUtils.isEmpty(from)) {
			if(gatewayRequest == null) {
				gatewayRequest = (GatewayRequest) BeanUtils.newInstance(requestClazz);
			}
			GatewayUtils.pack(gatewayRequest, from);
		}
		return gatewayRequest;
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
