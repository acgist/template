package com.acgist.core.gateway.gateway;

import org.springframework.http.HttpMethod;

import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.gateway.gateway.response.GatewayResponse;
import com.acgist.core.pojo.Pojo;
import com.acgist.utils.BeanUtils;

/**
 * <p>网关 - 类型</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class GatewayType extends Pojo {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>是否保存记录</p>
	 */
	protected final boolean save;
	/**
	 * <p>是否需要异步回调</p>
	 */
	protected final boolean notify;
	/**
	 * <p>接口名称</p>
	 * <p>命名规范：模块 + 内容 + 操作</p>
	 */
	protected final String name;
	/**
	 * <p>请求地址</p>
	 * <p>命名规范：模块 + 内容 + 操作（省略）</p>
	 * <p>请求方法操作对应：POST-insert/DELETE-delete/PUT-update/GET-select</p>
	 */
	protected final String path;
	/**
	 * <p>请求方法</p>
	 */
	protected final String method;
	/**
	 * <p>请求类型</p>
	 */
	protected final Class<?> requestClazz;
	/**
	 * <p>响应类型</p>
	 */
	protected final Class<?> responseClazz;

	public GatewayType(boolean save, boolean notify, String name, String path, HttpMethod method, Class<?> requestClazz, Class<?> responseClazz) {
		this.save = save;
		this.notify = notify;
		this.name = name;
		this.path = path;
		this.method = method.name();
		this.requestClazz = requestClazz;
		this.responseClazz = responseClazz;
	}

	public boolean isSave() {
		return save;
	}

	public boolean isNotify() {
		return notify;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getMethod() {
		return method;
	}

	public Class<?> getRequestClazz() {
		return requestClazz;
	}

	public Class<?> getResponseClazz() {
		return responseClazz;
	}
	
	/**
	 * <p>创建请求</p>
	 * 
	 * @return 请求
	 */
	public GatewayRequest newRequest() {
		return (GatewayRequest) BeanUtils.newInstance(this.requestClazz);
	}
	
	/**
	 * <p>创建响应</p>
	 * 
	 * @return 响应
	 */
	public GatewayResponse newResponse() {
		return (GatewayResponse) BeanUtils.newInstance(this.responseClazz);
	}

}
