package com.acgist.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.core.config.AcgistConst;
import com.acgist.core.gateway.gateway.AcgistGateway;

/**
 * <p>utils - 网关信息</p>
 */
public class GatewayUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayUtils.class);

	/**
	 * <p>忽略属性：{@value}</p>
	 */
	private static final String CLASS_KEY = AcgistConst.PROPERTY_CLASS;

	/**
	 * <p>网关实例转为网关信息Map</p>
	 * 
	 * @param <T> 网关类型
	 * @param t 网关实例
	 * 
	 * @return 网关信息Map
	 */
	public static final <T extends AcgistGateway> Map<String, String> unpack(T t) {
		if(t == null) {
			return null;
		}
		Map<String, String> data = null;
		try {
			data = BeanUtils.describe(t);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("网关信息转为Map异常：{}", t, e);
		}
		if (data != null) {
			// 过滤
			return data.entrySet().stream()
				.filter(entry -> !CLASS_KEY.equals(entry.getKey())) // class
				.filter(entry -> entry.getKey() != null && entry.getValue() != null) // 空值
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		}
		return null;
	}

	/**
	 * <p>网关属性填充</p>
	 * 
	 * @param gateway 网关实例
	 * @param data 网关信息
	 */
	public static final void pack(AcgistGateway gateway, Map<String, String> data) {
		if(gateway == null || data == null) {
			return;
		}
		try {
			BeanUtils.populate(gateway, data);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("网关属性填充异常：{}", data, e);
		}
	}
	
}
