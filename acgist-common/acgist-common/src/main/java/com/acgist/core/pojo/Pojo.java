package com.acgist.core.pojo;

import java.io.Serializable;

import com.acgist.utils.JSONUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <p>message - POJO</p>
 * <p>禁止添加业务逻辑</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pojo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}
	
}
