package com.acgist.core.aop.time;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>AOP - 方法时间统计</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE })
@Inherited
public @interface TimePoint {

	/**
	 * <p>任务名称</p>
	 * <p>默认：未知任务</p>
	 */
	String name() default "未知任务";

	/**
	 * <p>时间阀值（秒）</p>
	 * <p>默认：{@code 4}</p>
	 */
	long time() default 4L * 1000;

}
