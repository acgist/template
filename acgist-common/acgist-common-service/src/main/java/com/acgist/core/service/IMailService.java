package com.acgist.core.service;

import com.acgist.core.pojo.dto.MailDto;

/**
 * <p>service - 邮件</p>
 */
public interface IMailService {

	/**
	 * <p>发送邮件</p>
	 * 
	 * @param mail 邮件信息
	 */
	void mail(MailDto mail);
	
}
