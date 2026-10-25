package com.crb.seaturtlewebservice.domain.mapper;

import com.crb.seaturtlewebservice.domain.dto.response.ListPostResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Post;

import java.util.List;

public interface PostMapper {
    ListPostResponseDto entityToDto(Post post);
    List<ListPostResponseDto> postListEntityToDto(List<Post> postList);
}
