package com.acgist.data.pojo.dto;

import com.acgist.core.pojo.Pojo;

/**
 * <p>dto - 短信</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class SmsDto extends Pojo {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>手机号码</p>
	 */
	private String mobile;
	/**
	 * <p>短信内容</p>
	 */
	private String content;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
