package com.acgist.core.config;

/**
 * <p>config - 队列</p>
 * <table border="1">
 * 	<tr>
 * 		<th>类型</th>
 * 		<th>说明</th>
 * 		<th>原理</th>
 * 	</tr>
 * 	<tr>
 * 		<td>所有应用处理一次消息</td>
 * 		<td>交换机相同、队列相同</td>
 * 		<td>示例：ACGIST.EVENT</td>
 * 	</tr>
 * 	<tr>
 * 		<td>同类应用处理一次消息</td>
 * 		<td>交换机相同、同类应用队列相同</td>
 * 		<td>示例：ACGIST.EVENT-${spring.application.name}</td>
 * 	</tr>
 * 	<tr>
 * 		<td>每个应用实例处理一次消息</td>
 * 		<td>交换机相同、队列不同</td>
 * 		<td>示例：ACGIST.EVENT-${spring.application.name}-${acgist.id}</td>
 * 	</tr>
 * </table>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface RabbitConfig {

	/**
	 * <p>交换机</p>
	 */
	public enum Exchange {
		
		/** 事件交换机 */
		EVENT,
		/** 网关交换机 */
		GATEWAY;
		
	}
	
	/**
	 * <p>队列类型</p>
	 */
	public enum Type {
		
		/** 事件：刷新配置、刷新缓存、关机 */
		EVENT(Exchange.EVENT),
		/** 网关保存 */
		GATEWAY_SAVE(Exchange.GATEWAY),
		/** 网关通知 */
		GATEWAY_NOTIFY(Exchange.GATEWAY);
		
		/**
		 * <p>交换机</p>
		 */
		private final Exchange exchange;
		
		private Type(Exchange exchange) {
			this.exchange = exchange;
		}
		
		public Exchange exchange() {
			return this.exchange;
		}
		
	}
	
	/**
	 * <p>事件交换机</p>
	 */
	public static final String EXCHANGE_EVENT = "event";
	/**
	 * <p>网关交换机</p>
	 */
	public static final String EXCHANGE_GATEWAY = "gateway";
	/**
	 * <p>事件队列</p>
	 */
	public static final String QUEUE_EVENT = "event-${spring.application.name}-${acgist.id}";
	/**
	 * <p>网关保存队列</p>
	 */
	public static final String QUEUE_GATEWAY_SAVE = "gateway-save";
	/**
	 * <p>网关通知队列</p>
	 */
	public static final String QUEUE_GATEWAY_NOTIFY = "gateway-notify";
	
}
