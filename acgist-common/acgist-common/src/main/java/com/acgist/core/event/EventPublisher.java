package com.acgist.core.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * <p>utils - 事件推送</p>
 */
public class EventPublisher {

	/**
	 * <p>推送事件</p>
	 * 
	 * @param context 上下文
	 * @param event 事件
	 */
	public static final void publish(ApplicationContext context, ApplicationEvent event) {
		context.publishEvent(event);
	}
	
}
