package com.jobbot.service;

import com.jobbot.dto.UserDto;
import com.jobbot.entity.UserEntity;

public interface UserService {
	UserDto saveUser(UserEntity user); // Controller receives Entity currently

	UserDto saveUser(UserDto userDto);

	UserDto getUser(Long userId);

	UserDto getUserByEmail(String email);

	void deleteUserByNameAndEmail(String name, String email);

	UserDto getUserByNameAndEmail(String name, String email);

}
