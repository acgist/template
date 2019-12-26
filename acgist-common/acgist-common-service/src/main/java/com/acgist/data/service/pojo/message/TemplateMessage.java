package com.acgist.data.service.pojo.message;

import com.acgist.core.pojo.Pojo;

/**
 * <p>message - 模板信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class TemplateMessage extends Pojo {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>模板名称</p>
	 */
	private String name;
	/**
	 * <p>模板内容</p>
	 */
	private String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
