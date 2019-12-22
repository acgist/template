package com.acgist.core.pojo.message;

import com.acgist.core.pojo.Pojo;
import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>message - 实体消息</p>
 */
public class EntityMessage<T extends BaseEntity> extends Pojo {

	private static final long serialVersionUID = 1L;

	protected T entity;

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
	
}