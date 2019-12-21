package com.api.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * utils - 事件推送器
 */
public class EventPublisher {

	/**
	 * 事件提交
	 * @param context 上下文
	 * @param event 事件
	 */
	public static final void publish(ApplicationContext context, ApplicationEvent event) {
		context.publishEvent(event);
	}
	
}
