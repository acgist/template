package com.acgist.data.pojo.queue;

public class EventQueueMessage {

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
	
}
