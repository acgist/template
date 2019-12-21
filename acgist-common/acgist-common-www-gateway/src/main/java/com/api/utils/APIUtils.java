package com.api.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.acgist.core.config.AcgistConst;
import com.acgist.core.gateway.API;
import com.acgist.utils.APIUtils;

/**
 * utils - 对象转MAP
 */
public class APIUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(APIUtils.class);

	private static final String CLASS_KEY = AcgistConst.PROPERTY_CLASS; // 忽略class属性

	/**
	 * 获取对象实例
	 */
	public static final <T> T newInstance(ApplicationContext context, Class<T> clazz) {
		return context.getBean(clazz);
	}
	
	/**
	 * JAVA对象转MAP
	 */
	public static final <T extends API> Map<String, String> beanToMap(T t) {
		if(t == null) {
			return null;
		}
		Map<String, String> data = null;
		try {
			// 对象中有数组时只能获取到第一个数据
			data = BeanUtils.describe(t);
			// 对象中有数组时能获取到完整数据
//			data = PropertyUtils.describe(t);
//			效率差
//			String json = JSONUtils.java2json(t);
//			data = JSONUtils.json2map(json);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("java对象转map异常，对象：{}", t, e);
		}
		if (data != null) { // 过滤key和null值
			return data.entrySet().stream()
				.filter(entry -> !entry.getKey().equals(CLASS_KEY))
				.filter(entry -> entry.getKey() != null && entry.getValue() != null)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		}
		return null;
	}

	/**
	 * MAP填充JAVA对象
	 */
	public static final void mapToBean(API api, Map<String, String> data) {
		if(api == null || data == null) {
			return;
		}
		try {
			BeanUtils.populate(api, data);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("map数据填充JAVA对象异常，map数据：{}", data, e);
		}
	}
	
}
