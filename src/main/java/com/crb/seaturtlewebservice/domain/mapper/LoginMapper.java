package com.crb.seaturtlewebservice.domain.mapper;

import com.crb.seaturtlewebservice.domain.dto.response.LoginResponseDto;
import com.crb.seaturtlewebservice.domain.entity.User;

public interface LoginMapper {
    LoginResponseDto entityToDto(User currentUser, String token);
}
