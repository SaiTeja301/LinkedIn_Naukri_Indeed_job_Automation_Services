package com.jobbot.service.impl;

import com.jobbot.dto.UserDto;
import com.jobbot.entity.UserEntity;
import com.jobbot.mapper.EntityMapper;
import com.jobbot.repository.UserRepository;
import com.jobbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    // Constructor Injection
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Added saveUser method that accepts UserEntity
    @Override
    public UserDto saveUser(UserEntity user) {
        return EntityMapper.toDto(userRepository.save(user));
    }

    // Added saveUser method that accepts UserDto
    @Override
    public UserDto saveUser(UserDto userDto) {
        Optional<UserEntity> existingUserOpt = userRepository.findByEmail(userDto.getEmail());
        UserEntity userToSave;

        if (existingUserOpt.isPresent()) {
            UserEntity existingUser = existingUserOpt.get();
            existingUser.setName(userDto.getName());
            existingUser.setExperienceYears(userDto.getExperienceYears());
            existingUser.setEncryptedPassword(userDto.getEncryptedPassword());
            existingUser.setPreferredRoles(userDto.getPreferredRoles());
            existingUser.setPreferredCompanies(userDto.getPreferredCompanies());
            existingUser.setRemote(userDto.isRemote());
            existingUser.setHybrid(userDto.isHybrid());
            // Preserve existing sensitive fields like password if necessary
            userToSave = existingUser;
        } else {
            userToSave = EntityMapper.toEntity(userDto);
        }

        return EntityMapper.toDto(userRepository.save(userToSave));
    }

    // Added getUser method by userId
    @Override
    public UserDto getUser(Long userId) {
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepository.findById(userId).orElse(null));
        return userEntity.map(EntityMapper::toDto).orElse(null);
    }

    // Added getUser method by email
    @Override
    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(EntityMapper::toDto)
                .orElse(null);
    }

    // Added getUser by name and email
    @Override
    public UserDto getUserByNameAndEmail(String name, String email) {
        Optional<UserDto> userDto = Optional.ofNullable(userRepository.findByNameAndEmail(name, email))
                .map(user -> EntityMapper.toDto(user));
        return userDto.orElse(null);

    }

    // Delete User by name and email
    @Override
    public void deleteUserByNameAndEmail(String name, String email) {
        Optional<UserEntity> userEntityObj = Optional.ofNullable(userRepository.findByNameAndEmail(name, email));
        userEntityObj.ifPresent(userEntity -> userRepository.delete(userEntity));
    }

}
