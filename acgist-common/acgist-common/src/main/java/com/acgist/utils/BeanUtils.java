package com.acgist.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p>utils - Bean</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class BeanUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

	/**
	 * <p>获取对象实例</p>
	 * 
	 * @param <T> 类型
	 * @param context 上下文
	 * @param clazz 类型
	 * 
	 * @return 实例
	 */
	public static final <T> T newInstance(ApplicationContext context, Class<T> clazz) {
		return context.getBean(clazz);
	}
	
	/**
	 * <p>获取对象实例</p>
	 * 
	 * @param clazz 类型
	 * 
	 * @return 实例
	 */
	public static final Object newInstance(Class<?> clazz) {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			LOGGER.error("获取实例异常：{}", clazz, e);
		}
		return null;
	}
	
}
