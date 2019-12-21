package com.acgist.core.aop.time;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>AOP - 方法时间统计</p>
 */
@Aspect
@Component
public class TimeAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeAspect.class);

	@Pointcut("@annotation(com.acgist.core.aop.time.TimePoint)")
	private void point() {
	}

	@Around("point()")
	public Object arround(ProceedingJoinPoint pjp) throws Throwable {
		final TimePoint sign = getAnnotation(pjp); // 注解
		final long begin = System.currentTimeMillis();
		try {
			return pjp.proceed();
		} catch (Exception e) {
			throw e;
		} finally {
			final long end = System.currentTimeMillis();
			final long executeTime = end - begin;
			if (executeTime > sign.time()) {
				LOGGER.debug(sign.name() + "执行时间" + executeTime);
			}
		}
	}

	private TimePoint getAnnotation(ProceedingJoinPoint pjp) throws Exception, SecurityException {
		TimePoint sign = null;
		if (pjp.getSignature() instanceof MethodSignature) {
			MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
			sign = methodSignature.getMethod().getAnnotation(TimePoint.class);
		}
		return sign;
	}

}
