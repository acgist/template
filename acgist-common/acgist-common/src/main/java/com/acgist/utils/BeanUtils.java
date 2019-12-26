package com.acgist.utils;

import org.springframework.context.ApplicationContext;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.exception.ErrorCodeException;

/**
 * <p>utils - Bean</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class BeanUtils {
	
	/**
	 * <p>获取Class</p>
	 * 
	 * @param clazz 名称
	 * 
	 * @return Class
	 */
	public static final Class<?> forName(String clazz) {
		try {
			return Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			throw new ErrorCodeException(AcgistCode.CODE_9999, e);
		}
	}
	
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
			throw new ErrorCodeException(AcgistCode.CODE_9999, e);
		}
	}
	
	/**
	 * <p>获取对象实例</p>
	 * 
	 * @param clazz 类型
	 * 
	 * @return 实例
	 */
	public static final Object newInstance(String clazz) {
		return newInstance(forName(clazz));
	}
	
}
