package com.acgist.core.user.service.impl;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
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
import com.acgist.utils.PasswordUtils;

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
	public ResultMessage save(UserEntity user) {
		this.userRepository.save(user);
		return ResultMessage.newInstance().buildSuccess();
	}
	
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
				.map(RoleEntity::getToken)
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
		return this.login(name, password, null);
	}
	
	@Override
	public LoginMessage login(String name, String password, UserEntity.Type type) {
		final LoginMessage message = new LoginMessage();
		final var user = this.userRepository.findByName(name);
		if(user == null) {
			message.buildMessage(AcgistCode.CODE_2000, "用户没有注册");
			return message;
		}
		if(type != null && user.getType() != type) {
			message.buildMessage(AcgistCode.CODE_2001, "用户没有权限");
			return message;
		}
		if(!StringUtils.equals(PasswordUtils.encrypt(password), user.getPassword())) {
			message.buildMessage(AcgistCode.CODE_2000, "密码错误");
			return message;
		}
		message.setId(user.getId());
		message.setName(user.getName());
		message.setNick(user.getNick());
		message.buildSuccess();
		return message;
	}
	
	@Override
	public boolean checkUserName(String name) {
		return this.userRepository.findIdByName(name) == null;
	}
	
	@Override
	public boolean checkUserMail(String mail) {
		return this.userRepository.findIdByMail(mail) == null;
	}

}
