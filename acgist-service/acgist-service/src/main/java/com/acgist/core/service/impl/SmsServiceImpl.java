package com.acgist.core.service.impl;

import org.apache.dubbo.config.annotation.Service;

import com.acgist.core.service.ISmsService;
import com.acgist.data.service.pojo.dto.SmsDto;

/**
 * <p>service - 短信</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(version = "${acgist.service.version}")
public class SmsServiceImpl implements ISmsService {

	@Override
	public boolean send(SmsDto sms) {
		// TODO：实现
		return false;
	}

}
