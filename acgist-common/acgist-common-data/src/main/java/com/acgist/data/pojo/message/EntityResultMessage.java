package com.acgist.data.pojo.message;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>message - 实体结果消息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class EntityResultMessage<T extends BaseEntity> extends ResultMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>实体</p>
	 */
	protected T entity;

	public EntityResultMessage() {
	}
	
	public EntityResultMessage(T entity) {
		this.entity = entity;
		if(this.entity == null) {
			this.code = AcgistCode.CODE_3003.getCode();
			this.message = AcgistCode.CODE_3003.getMessage();
		}
	}
	
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

}
