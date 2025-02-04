package com.rentspace.userservice.service.impl;

import com.rentspace.userservice.dto.UserCreateDto;
import com.rentspace.userservice.dto.UserCreateRequest;
import com.rentspace.userservice.dto.UserDto;
import com.rentspace.userservice.entity.User;
import com.rentspace.userservice.enums.Role;
import com.rentspace.userservice.exception.UserAlreadyExistsException;
import com.rentspace.userservice.exception.UserNotFoundException;
import com.rentspace.userservice.mapper.UserMapper;
import com.rentspace.userservice.repository.UserRepository;
import com.rentspace.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest request) {
        log.info("Creating new user {}", request);

        if (userRepository.existsByPhone(request.getPhone()) || userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User", "email/phone", request.getEmail());
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("User", "username", request.getEmail());
        }

        User user = userMapper.toEntity(request);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toResponseDto(user);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User", "email", email)
        );
        UserDto userDto = userMapper.toResponseDto(user);
        log.info("User fetched successfully with Email: {}", userDto.getEmail());
        return userDto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        log.info("Fetching user with ID: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User", "userId", userId)
        );
        UserDto userDto = userMapper.toResponseDto(user);
        log.info("User fetched successfully with ID: {}", userDto.getId());
        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserCreateDto userDto) {
        log.info("Updating user with ID: {}", userId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User", "userId", userId)
        );

        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("User", "email", userDto.getEmail());
        }
        if (!user.getPhone().equals(userDto.getMobileNumber()) && userRepository.existsByPhone(userDto.getMobileNumber())) {
            throw new UserAlreadyExistsException("User", "mobileNumber", userDto.getMobileNumber());
        }

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setRole(userDto.getRole());
        user.setPhone(userDto.getMobileNumber());

        User updatedUser = userRepository.save(user);

        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User", "userId", userId);
        }
        userRepository.deleteById(userId);

        log.info("User deleted successfully with ID: {}", userId);
    }
}
