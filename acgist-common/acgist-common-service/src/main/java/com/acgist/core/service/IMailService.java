package com.acgist.core.service;

import com.acgist.data.service.pojo.dto.MailDto;

/**
 * <p>服务 - 邮件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface IMailService {

	/**
	 * <p>发送邮件</p>
	 * 
	 * @param mail 邮件信息
	 * 
	 * @return 是否发送成功
	 */
	boolean send(MailDto mail);
	
}
