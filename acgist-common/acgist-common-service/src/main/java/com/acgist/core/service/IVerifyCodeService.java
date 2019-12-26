package com.acgist.core.service;

import com.acgist.core.pojo.message.ResultMessage;

/**
 * <p>service - 验证码</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface IVerifyCodeService {

	/**
	 * <p>生成验证码</p>
	 * 
	 * @param key 验证码Key
	 * 
	 * @return 验证码
	 */
	String build(String key);
	
	/**
	 * <p>发送邮件验证码</p>
	 * 
	 * @param mail 邮箱
	 * 
	 * @return 是否成功
	 */
	boolean buildMail(String mail);
	
	/**
	 * <p>发送手机验证码</p>
	 * 
	 * @param mobile 手机号码
	 * 
	 * @return 是否成功
	 */
	boolean buildMobile(String mobile);
	
	/**
	 * <p>验证验证码</p>
	 * 
	 * @param key 验证码Key
	 * @param code 验证码
	 * 
	 * @return 验证结果
	 */
	ResultMessage verify(String key, String code);
	
}
