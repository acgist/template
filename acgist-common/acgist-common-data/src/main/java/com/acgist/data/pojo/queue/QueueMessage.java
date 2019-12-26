package com.acgist.data.pojo.queue;

import com.acgist.core.pojo.Pojo;
import com.acgist.main.ApplicationLauncher;

/**
 * <p>message - MQ消息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class QueueMessage extends Pojo {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>交换机</p>
	 */
	public enum Exchange {
		
		/** 事件信息 */
		EVENT,
		/** 网关信息 */
		GATEWAY;
		
	}
	
	/**
	 * <p>消费类型</p>
	 */
	public enum Cost {
		
		/**
		 * <p>所有应用处理一次消息</p>
		 * <p>交换机相同、队列相同</p>
		 * <p>示例：ACGIST.CACHE</p>
		 */
		ONE_ALL,
		/**
		 * <p>同类应用处理一次消息</p>
		 * <p>交换机相同、同类应用队列相同</p>
		 * <p>示例：ACGIST.CACHE-ACGIST-WWW、ACGIST.CACHE-ACGIST-GATEWAY</p>
		 */
		ONE_APP,
		/**
		 * <p>每个应用实例处理一次消息</p>
		 * <p>交换机相同、队列不同</p>
		 * <p>示例：ACGIST.CACHE-ACGIST-WWW-{@linkplain ApplicationLauncher#id() UUID}</p>
		 */
		ONE_IST;
		
	}
	
	/**
	 * <p>消息类型</p>
	 */
	public enum Type {
		
		/** 事件：刷新配置、刷新缓存、关机 */
		EVENT(Exchange.EVENT, false, Cost.ONE_IST),
		/** 网关保存 */
		GATEWAY_SAVE(Exchange.GATEWAY, true, Cost.ONE_ALL),
		/** 网关通知 */
		GATEWAY_NOTIFY(Exchange.GATEWAY, true, Cost.ONE_ALL);
		
		/**
		 * <p>交换机</p>
		 */
		private final Exchange exchange;
		/**
		 * <p>是否持久保持</p>
		 */
		private final boolean persist;
		/**
		 * <p>消费类型</p>
		 */
		private final Cost cost;
		/**
		 * <p>队列名称</p>
		 */
		private String queue;
		
		private Type(Exchange exchange, boolean persist, Cost cost) {
			this.exchange = exchange;
			this.persist = persist;
			this.cost = cost;
		}
		
		public Exchange exchange() {
			return this.exchange;
		}
		
		public boolean persist() {
			return this.persist;
		}
		
		public Cost cost() {
			return this.cost;
		}
		
		public String buildQueue(String application) {
			switch (this.cost) {
			case ONE_ALL:
				this.queue = this.name();
				break;
			case ONE_APP:
				this.queue = this.name() + "-" + application.toUpperCase();
				break;
			case ONE_IST:
			default:
				this.queue = this.name() + "-" + application.toUpperCase() + "-" + ApplicationLauncher.getInstance().id();
				break;
			}
			return this.queue;
		}
		
		public String queue() {
			return this.queue;
		}
		
	}
	
	/**
	 * <p>消息类型</p>
	 */
	private Type type;
	/**
	 * <p>消息目标：{@code application.name}</p>
	 */
	private String target;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
}
