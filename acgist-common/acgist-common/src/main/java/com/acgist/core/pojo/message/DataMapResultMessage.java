package com.acgist.core.pojo.message;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>message - 数据</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class DataMapResultMessage extends ResultMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>数据</p>
	 */
	private Map<String, Object> data = new HashMap<String, Object>();

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	/**
	 * <p>设置数据</p>
	 * 
	 * @param key key
	 * @param value value
	 */
	public void put(String key, Object value) {
		this.data.put(key, value);
	}
	
}
