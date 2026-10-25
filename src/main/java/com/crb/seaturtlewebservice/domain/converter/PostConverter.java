package com.crb.seaturtlewebservice.domain.converter;

import com.crb.seaturtlewebservice.domain.dto.PostDto;
import com.crb.seaturtlewebservice.domain.dto.PostNoContentDto;
import com.crb.seaturtlewebservice.domain.dto.PostWithContentDto;
import com.crb.seaturtlewebservice.domain.dto.PostSuggestionDto;
import com.crb.seaturtlewebservice.domain.dto.response.PostResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Post;

public interface PostConverter {
    PostDto entityToDto(Post post);

    PostNoContentDto entityToNoContentDto(Post post);
    PostDto entityToDtos(Post post);

    PostSuggestionDto toSuggestion(Post post);

    PostWithContentDto entityToWithContentDto(Post post);
    PostResponseDto convertToDto(Post post);
}