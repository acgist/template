package com.acgist.core.gateway.gateway;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>网关 - 映射</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class GatewayMapping {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayMapping.class);
	
	private static final GatewayMapping INSTANCE = new GatewayMapping();
	
	private Map<String, GatewayType> MAPPING = new HashMap<>();
	
	private GatewayMapping() {
	}
	
	public static final GatewayMapping getInstance() {
		return INSTANCE;
	}
	
	/**
	 * <p>注册映射</p>
	 * 
	 * @param gatewayType 映射信息
	 */
	public void register(GatewayType gatewayType) {
		LOGGER.info("注册网关映射：{}-{}", gatewayType.getName(), gatewayType.getPath());
		this.MAPPING.put(gatewayType.getPath(), gatewayType);
	}
	
	/**
	 * <p>获取映射</p>
	 * 
	 * @param path 请求地址
	 * 
	 * @return 映射
	 */
	public GatewayType getGatewayType(String path) {
		return this.MAPPING.get(path);
	}
	
}
