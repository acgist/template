package com.acgist.core.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.acgist.data.pojo.queue.EventQueueMessage;
import com.acgist.main.ApplicationLauncher;

import ch.qos.logback.classic.LoggerContext;

/**
 * <p>service - 事件处理</p>
 * 
 * TODO：实现应用消息
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
		final String target = message.getTarget();
		if(
			StringUtils.isEmpty(target) ||
			this.application.equalsIgnoreCase(target)
		) {
			switch (message.getEventType()) {
			case CACHE:
				doCache(message);
				break;
			case CONFIG:
				doConfig(message);
				break;
			case SHUTDOWN:
				doShutdown(message);
				break;
			default:
				LOGGER.warn("未适配事件：{}", message.getEventType());
				break;
			}
		} else {
			LOGGER.debug("忽略消息：{}-{}", this.application, message.getTarget());
		}
	}
	
	/**
	 * <p>处理事件消息 - 缓存</p>
	 * 
	 * @param message 事件消息
	 */
	protected abstract void doCache(EventQueueMessage message);
	
	/**
	 * <p>处理事件消息 - 配置</p>
	 * 
	 * @param message 事件消息
	 */
	protected abstract void doConfig(EventQueueMessage message);
	
	/**
	 * <p>处理事件消息 - 关机</p>
	 * 
	 * @param message 事件消息
	 */
	protected void doShutdown(EventQueueMessage message) {
		LOGGER.info("关闭系统");
		final var factory = LoggerFactory.getILoggerFactory();
		if(factory instanceof LoggerContext) {
			LOGGER.debug("关闭日志系统");
			final LoggerContext context = (LoggerContext) factory;
			context.stop();
		}
		ApplicationLauncher.getInstance().shutdown(0);
	}
	
}
