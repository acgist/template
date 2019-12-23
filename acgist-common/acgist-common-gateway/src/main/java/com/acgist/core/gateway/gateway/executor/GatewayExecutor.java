package com.acgist.core.gateway.gateway.executor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.exception.ErrorCodeException;
import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.gateway.gateway.response.GatewayResponse;

/**
 * <p>请求执行器</p>
 * <p>执行一个完整的网关请求</p>
 */
@Component
@Scope("prototype")
public abstract class GatewayExecutor<T extends GatewayRequest, K extends GatewayResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayExecutor.class);

	/**
	 * <p>请求</p>
	 */
	protected T request;
	/**
	 * <p>响应</p>
	 */
	protected K response;

	/**
	 * <p>执行请求</p>
	 */
	protected abstract void execute();

	/**
	 * <p>执行请求</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 响应
	 */
	public K execute(T request) {
		this.request = request;
		this.response = buildResponse();
		this.execute();
		return this.response;
	}

	/**
	 * <p>生成响应</p>
	 * 
	 * @return 响应
	 */
	private K buildResponse() {
		final K response = this.buildAcgistResponse();
		response.valueOfRequest(this.request); // 设置需要原样返回的参数
		return response;
	}

	/**
	 * <p>生成响应</p>
	 * 
	 * @return 响应
	 */
	@SuppressWarnings("unchecked")
	private K buildAcgistResponse() {
		final ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		final Type[] types = parameterizedType.getActualTypeArguments();
		final Class<K> clazz = (Class<K>) types[1]; // 获取响应泛型
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			LOGGER.error("生成响应异常", e);
		}
		throw new ErrorCodeException(AcgistCode.CODE_9999);
	}

}
