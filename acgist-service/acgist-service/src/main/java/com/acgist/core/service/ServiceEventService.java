package com.acgist.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acgist.data.pojo.queue.EventQueueMessage;

/**
 * <p>service - 事件处理</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
public class ServiceEventService extends EventService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEventService.class);

	@Override
	protected void doCache(EventQueueMessage message) {
		LOGGER.info("缓存事件");
	}

	@Override
	protected void doConfig(EventQueueMessage message) {
		LOGGER.info("配置事件");
	}
	
}
