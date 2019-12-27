package com.acgist.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.acgist.data.pojo.queue.EventQueueMessage;

/**
 * <p>service - 事件处理</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public abstract class EventService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

	@Value("${spring.application.name}")
	private String application;
	
	/**
	 * <p>处理事件消息</p>
	 * 
	 * @param message 事件消息
	 */
	public void process(EventQueueMessage message) {
		if(this.application.equalsIgnoreCase(message.getTarget())) {
			doMessage(message);
		} else {
			LOGGER.debug("忽略消息：{}-{}", this.application, message.getTarget());
		}
	}
	
	/**
	 * <p>处理事件消息</p>
	 * 
	 * @param message 事件消息
	 */
	protected abstract void doMessage(EventQueueMessage message);
	
}
