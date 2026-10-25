package com.crb.seaturtlewebservice.domain.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RelatePostsResponseDto {
    List<Object> postDtos;
    List<Object> shippedProjectPostDtos;
}
