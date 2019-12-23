package com.acgist.core.gateway.gateway.executor;

import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.gateway.gateway.response.GatewayResponse;
import com.acgist.core.pojo.Pojo;

/**
 * <p>网关类型</p>
 */
public final class GatewayType<T extends GatewayRequest, K extends GatewayResponse> extends Pojo {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>是否保存记录</p>
	 */
	protected final boolean save;
	/**
	 * <p>接口名称</p>
	 */
	protected final String name;
	/**
	 * <p>请求地址</p>
	 */
	protected final String path;
	/**
	 * <p>执行器</p>
	 */
	protected final Class<?> executor;

	public GatewayType(boolean save, String name, String path, Class<?> executor) {
		this.save = save;
		this.name = name;
		this.path = path;
		this.executor = executor;
	}

	public boolean isSave() {
		return save;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	@SuppressWarnings("unchecked")
	public Class<GatewayExecutor<T, K>> getExecutor() {
		return (Class<GatewayExecutor<T, K>>) executor;
	}

}
