package com.acgist.core.service;

import com.acgist.data.pojo.dto.SmsDto;

/**
 * <p>服务 - 发送短信</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface ISmsService {

	/**
	 * <p>发送短信</p
	 * 
	 * @param sms 短信信息
	 * 
	 * @return 是否发送成功
	 */
	boolean send(SmsDto sms);
	
}
