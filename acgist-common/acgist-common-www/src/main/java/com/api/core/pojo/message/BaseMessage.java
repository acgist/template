package com.api.core.pojo.message;

import java.io.Serializable;

import com.acgist.utils.JSONUtils;

/**
 * message - 服务间通信
 */
public class BaseMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 重新toString方法
	 */
	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}
	
}
