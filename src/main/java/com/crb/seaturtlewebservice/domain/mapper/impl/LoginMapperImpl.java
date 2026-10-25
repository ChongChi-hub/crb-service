package com.crb.seaturtlewebservice.domain.mapper.impl;

import com.crb.seaturtlewebservice.domain.dto.response.LoginResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Role;
import com.crb.seaturtlewebservice.domain.entity.User;
import com.crb.seaturtlewebservice.domain.mapper.LoginMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LoginMapperImpl implements LoginMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginMapperImpl.class);
    private final ModelMapper modelMapper;

    @Override
    public LoginResponseDto entityToDto(User user, String token) {
        LOGGER.debug("LoginMapperImpl -> entityToDto invoked");
        LoginResponseDto loginResponseDto = modelMapper.map(user, LoginResponseDto.class);
        Role role = user.getRole();
        loginResponseDto.setRole(role.getName());
        return loginResponseDto;
    }
}