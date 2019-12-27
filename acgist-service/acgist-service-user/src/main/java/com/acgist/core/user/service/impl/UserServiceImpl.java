package com.acgist.core.user.service.impl;

import javax.transaction.Transactional;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.core.service.IUserService;
import com.acgist.core.user.config.AcgistServiceUserCache;
import com.acgist.data.pojo.entity.RoleEntity;
import com.acgist.data.pojo.entity.UserEntity;
import com.acgist.data.pojo.message.AuthoMessage;
import com.acgist.data.pojo.message.LoginMessage;
import com.acgist.data.pojo.message.UserMessage;
import com.acgist.data.user.repository.UserRepository;

/**
 * <p>service - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	@Cacheable(AcgistServiceUserCache.AUTHO_MESSAGE)
	public AuthoMessage getAuthoMessage(String name) {
		final UserEntity entity = this.userRepository.findByName(name);
		final AuthoMessage authoMessage = new AuthoMessage();
		if(entity == null) {
			authoMessage.buildMessage(AcgistCode.CODE_2000);
		} else {
			authoMessage.buildSuccess();
			authoMessage.setName(entity.getName());
			authoMessage.setPassword(entity.getPassword());
			final String[] roles = entity.getRoles().stream()
				.map(RoleEntity::getId)
				.toArray(String[]::new);
			authoMessage.setRoles(roles);
		}
		return authoMessage;
	}

	@Override
	public UserMessage findByName(String name) {
		final UserMessage message = new UserMessage(this.userRepository.findByName(name));
		message.buildSuccess();
		return message;
	}
	
	@Override
	public ResultMessage update(UserEntity userEntity) {
		this.userRepository.update(userEntity.getNick(), userEntity.getName());
		return ResultMessage.newInstance().buildSuccess();
	}

	@Override
	public LoginMessage login(String name, String password) {
		return null;
	}

}
