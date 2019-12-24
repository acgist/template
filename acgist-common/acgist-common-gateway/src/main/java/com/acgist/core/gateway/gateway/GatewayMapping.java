package com.acgist.core.gateway.gateway;

import java.util.ArrayList;
import java.util.List;

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
	
	private List<GatewayType> MAPPING = new ArrayList<GatewayType>();
	
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
		LOGGER.info("注册网关映射：{}-{}-{}", gatewayType.getName(), gatewayType.getPath(), gatewayType.getMethod());
		this.MAPPING.add(gatewayType);
	}
	
	/**
	 * <p>获取映射</p>
	 * 
	 * @param path 请求地址
	 * @param path 请求方法
	 * 
	 * @return 映射
	 */
	public GatewayType getGatewayType(String path, String method) {
		final var optional = this.MAPPING.stream().filter(mapping -> {
			return mapping.getPath().equals(path) && mapping.getMethod().equals(method);
		}).findAny();
		if(optional.isEmpty()) {
			return null;
		}
		return optional.get();
	}
	
}
