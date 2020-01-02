package com.acgist.core.pojo.message;

/**
 * <p>message - 数据</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class DataResultMessage extends ResultMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>数据</p>
	 */
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
