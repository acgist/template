package com.acgist.data.pojo.queue;

import com.acgist.core.config.RabbitConfig;
import com.acgist.core.pojo.Pojo;

/**
 * <p>message - MQ消息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class QueueMessage extends Pojo {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>消息类型</p>
	 */
	private RabbitConfig.Type type;
	/**
	 * <p>消息目标：{@code application.name}</p>
	 */
	private String target;

	public RabbitConfig.Type getType() {
		return type;
	}

	public void setType(RabbitConfig.Type type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
}
