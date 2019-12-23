package com.acgist.utils;

import org.springframework.context.ApplicationContext;

/**
 * <p>utils - Bean</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class BeanUtils {

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
	
}
