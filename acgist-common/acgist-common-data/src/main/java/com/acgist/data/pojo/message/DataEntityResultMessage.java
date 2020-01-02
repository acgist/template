package com.acgist.data.pojo.message;

import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>message - 实体结果消息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class DataEntityResultMessage<T extends BaseEntity> extends ResultMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>数据</p>
	 */
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
