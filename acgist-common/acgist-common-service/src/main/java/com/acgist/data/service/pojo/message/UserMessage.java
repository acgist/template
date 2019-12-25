package com.acgist.data.service.pojo.message;

import com.acgist.data.pojo.message.EntityResultMessage;
import com.acgist.data.service.pojo.entity.UserEntity;

/**
 * <p>message - 用户信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class UserMessage extends EntityResultMessage<UserEntity> {

	private static final long serialVersionUID = 1L;

	public UserMessage() {
	}
	
	public UserMessage(UserEntity entity) {
		super(entity);
	}
	
}
