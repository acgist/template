package com.acgist.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.data.repository.UserRepository;
import com.acgist.data.service.EntityService;
import com.acgist.data.service.pojo.entity.UserEntity;

@Service
public class UserService extends EntityService<UserEntity> {
	
	@Autowired
	public UserService(UserRepository repository) {
		super(repository);
	}
	
}
