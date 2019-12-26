package com.acgist.core.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.acgist.core.service.IMailService;
import com.acgist.data.service.pojo.dto.MailDto;

/**
 * <p>service - 邮件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(version = "${acgist.service.version}")
public class MailServiceImpl implements IMailService {

	@Override
	public boolean send(MailDto mail) {
		// TODO：实现
		return false;
	}

}
