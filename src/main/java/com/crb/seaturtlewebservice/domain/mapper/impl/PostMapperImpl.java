package com.crb.seaturtlewebservice.domain.mapper.impl;

import com.crb.seaturtlewebservice.domain.dto.response.ListPostResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Post;
import com.crb.seaturtlewebservice.domain.mapper.PostMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PostMapperImpl implements PostMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostMapperImpl.class);
    private final ModelMapper modelMapper;
    @Override
    public ListPostResponseDto entityToDto(Post post) {
        LOGGER.debug("PostMapperImpl -> entityToDto invoked");
        ListPostResponseDto listPostResponseDto = modelMapper.map(post, ListPostResponseDto.class);
        return listPostResponseDto;
    }

    @Override
    public List<ListPostResponseDto> postListEntityToDto(List<Post> postList) {
        LOGGER.debug("PostMapperImpl -> postListEntityToDto invoked");
        List<ListPostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post element : postList) {
            postResponseDtos.add(this.entityToDto(element));
        }
        return postResponseDtos;
    }
}
