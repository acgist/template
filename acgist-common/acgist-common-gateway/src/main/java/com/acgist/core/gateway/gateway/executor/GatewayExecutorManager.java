package com.acgist.core.gateway.gateway.executor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.gateway.gateway.response.GatewayResponse;
import com.acgist.utils.BeanUtils;

/**
 * <p>请求执行器管理器</p>
 */
public final class GatewayExecutorManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayExecutorManager.class);

	/**
	 * <p>请求地址=请求执行器类型</p>
	 */
	private static final Map<String, GatewayType<?, ?>> GATEWAY_TYPE = new HashMap<>();

	/**
	 * <p>注册请求执行器</p>
	 * 
	 * @param path 请求地址
	 * @param gatewayType 请求执行器类型
	 */
	public static final void register(GatewayType<?, ?> gatewayType) {
		LOGGER.info("请求执行器注册：{}-{}-{}", gatewayType.getName(), gatewayType.getPath(), gatewayType.getExecutor());
		GATEWAY_TYPE.put(gatewayType.getPath(), gatewayType);
	}
	
	/**
	 * <p>获取请求执行器</p>
	 * 
	 * @param context 上下文
	 * @param path 请求地址
	 * 
	 * @return 请求执行器
	 */
	@SuppressWarnings("unchecked")
	public static final GatewayExecutor<GatewayRequest, GatewayResponse> getExecutor(ApplicationContext context, String path) {
		final var gatewayType = GATEWAY_TYPE.get(path);
		if(gatewayType == null) {
			return null;
		}
		return (GatewayExecutor<GatewayRequest, GatewayResponse>) BeanUtils.newInstance(context, gatewayType.getExecutor());
	}
	
}
