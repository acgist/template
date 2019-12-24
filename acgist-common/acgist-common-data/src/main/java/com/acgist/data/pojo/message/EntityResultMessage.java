package com.acgist.data.pojo.message;

import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>message - 实体消息（状态）</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class EntityResultMessage<T extends BaseEntity> extends ResultMessage {

	private static final long serialVersionUID = 1L;

	protected T entity;

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

}