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
 * 
 * @author acgist
 * @since 1.0.0
 */
@Aspect
@Component
public final class TimeAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeAspect.class);

	@Pointcut("@annotation(com.acgist.core.aop.time.TimePoint)")
	private void point() {
	}

	@Around("point()")
	public Object arround(ProceedingJoinPoint pjp) throws Throwable {
		final TimePoint point = getAnnotation(pjp); // 注解
		final long beginTime = System.currentTimeMillis();
		try {
			return pjp.proceed();
		} catch (Exception e) {
			throw e;
		} finally {
			final long endTime = System.currentTimeMillis();
			final long executeTime = endTime - beginTime;
			if (executeTime > point.time()) {
				LOGGER.info("{}执行时间{}", point.name(), executeTime);
			}
		}
	}

	private TimePoint getAnnotation(ProceedingJoinPoint pjp) throws Exception, SecurityException {
		TimePoint point = null;
		if (pjp.getSignature() instanceof MethodSignature) {
			final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
			point = methodSignature.getMethod().getAnnotation(TimePoint.class);
		}
		return point;
	}

}
