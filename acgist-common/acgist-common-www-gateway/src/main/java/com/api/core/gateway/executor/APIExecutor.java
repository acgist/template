package com.api.core.gateway.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.exception.ErrorCodeException;
import com.acgist.core.gateway.executor.APIExecutor;
import com.acgist.core.gateway.request.APIRequest;
import com.acgist.core.gateway.response.APIResponse;

/**
 * 请求执行器<br>
 * 执行一个完整的接口请求：<br>
 * <pre>
 * 	交易：
 * 		接口：创建订单+付款->完成
 * 		网站：创建订单完成->付款->完成
 * 	服务：
 * 		创建订单服务
 * 		支付服务
 * </pre>
 */
@Component
@Scope("prototype")
public abstract class APIExecutor<T extends APIRequest, K extends APIResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(APIExecutor.class);

	protected T request;
	protected K response;

	/**
	 * 执行器
	 */
	protected abstract void execute();

	/**
	 * 响应
	 */
	public K execute(T request) {
		init(request);
		execute();
		return this.response;
	}

	/**
	 * 初始化
	 */
	private void init(T request) {
		this.request = request;
		this.response = buildResponse();
	}

	/**
	 * 默认生成响应
	 */
	private K buildResponse() {
		K response = this.buildAPIResponse();
		response.valueOfRequest(this.request); // 设置需要原样返回的参数
		return response;
	}

	/**
	 * 生成新的响应
	 * TODO 优化泛型
	 */
	@SuppressWarnings("unchecked")
	private K buildAPIResponse() {
		final ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		final Type[] types = parameterizedType.getActualTypeArguments();
		final Class<K> clazz = (Class<K>) types[1]; // 获取第二个泛型
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			LOGGER.error("生成响应异常", e);
		}
		throw new ErrorCodeException(AcgistCode.CODE_9999);
	}

}
