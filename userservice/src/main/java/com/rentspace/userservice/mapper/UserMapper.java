package com.rentspace.userservice.mapper;

import com.rentspace.userservice.dto.UserDto;
import com.rentspace.userservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper implements BaseMapper<User, UserDto> {

    @Override
    public User toEntity(UserDto userDto) {
        return Optional.ofNullable(userDto)
                .map(d -> {
                    User user = new User();
                    user.setName(d.getName());
                    user.setEmail(d.getEmail());
                    user.setPassword(d.getPassword());
                    user.setRole(d.getRole());
                    return user;
                }).orElse(null);
    }

    @Override
    public UserDto toDto(User user) {
        return Optional.ofNullable(user)
                .map(u -> {
                    UserDto userDto = new UserDto();
                    userDto.setName(u.getName());
                    userDto.setEmail(u.getEmail());
                    userDto.setPassword(u.getPassword());
                    userDto.setRole(u.getRole());
                    return userDto;
                }).orElse(null);
    }
}
