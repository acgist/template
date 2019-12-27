package com.acgist.data.pojo.queue;

/**
 * <p>message - MQ事件消息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class EventQueueMessage extends QueueMessage {

	private static final long serialVersionUID = 1L;

	public enum EventType {
		
		/** 刷新缓存 */
		CACHE,
		/** 刷新配置 */
		CONFIG,
		/** 关机 */
		SHUTDOWN;
		
	}
	
	private EventType eventType;
	private String value;

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
