package com.acgist.core.pojo;

import java.io.Serializable;

import com.acgist.utils.JSONUtils;

/**
 * <p>message - POJO</p>
 * <p>禁止添加业务逻辑</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class Pojo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}
	
}
