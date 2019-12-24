package com.acgist.data.service.pojo.dto;

import com.acgist.core.pojo.Pojo;

/**
 * <p>dto - 邮件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class MailDto extends Pojo {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>收件邮箱</p>
	 */
	private String receiver;
	/**
	 * <p>主题</p>
	 */
	private String subject;
	/**
	 * <p>主题</p>
	 */
	private String content;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
