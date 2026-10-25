package com.crb.seaturtlewebservice.domain.converter.impl;

import com.crb.seaturtlewebservice.domain.converter.PostConverter;
import com.crb.seaturtlewebservice.domain.dto.PostDto;
import com.crb.seaturtlewebservice.domain.dto.PostNoContentDto;
import com.crb.seaturtlewebservice.domain.dto.PostSuggestionDto;
import com.crb.seaturtlewebservice.domain.dto.PostWithContentDto;
import com.crb.seaturtlewebservice.domain.dto.response.PostResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Category;
import com.crb.seaturtlewebservice.domain.entity.Post;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PostConverterImpl implements PostConverter {
    @Override
    public PostDto entityToDto(Post post) {
        PostDto postDto = new PostDto();
        Category category = Category.builder()
                .id(post.getCategory().getId())
                .name(post.getCategory().getName())
                .urlName(post.getCategory().getUrlName())
                .build();
        BeanUtils.copyProperties(post, postDto);
        postDto.setCategory(category);
        return postDto;
    }

    @Override
    public PostNoContentDto entityToNoContentDto(Post post) {
        PostNoContentDto postDto = new PostNoContentDto();
        BeanUtils.copyProperties(post, postDto);
        postDto.setCategoryId(post.getCategory().getId());
        postDto.setTopicId(post.getTopic().getId());
        return postDto;
    }

    @Override
    public PostDto entityToDtos(Post post) {
        PostDto dto = PostDto.builder()
                .category(post.getTopic().getCategory())
                .id(post.getId())
                .topic(post.getTopic())
                .name(post.getName())
                .content(post.getContent())
                .description(post.getDescription())
                .titleImage(post.getTitleImage())
                .datePost(post.getDatePost())
                .author(post.getAuthor())
                .urlName(post.getUrlName())
                .build();
        return dto;
    }

    @Override
    public PostSuggestionDto toSuggestion(Post post){
        PostSuggestionDto dto = PostSuggestionDto.builder()
                .id(post.getId())
                .name(post.getName())
                .urlName(post.getUrlName())
                .build();
        return dto;
    }


    @Override
    public PostWithContentDto entityToWithContentDto(Post post) {
        PostWithContentDto postDto = new PostWithContentDto();
        BeanUtils.copyProperties(post, postDto);
        postDto.setCategoryId(post.getCategory().getId());
        postDto.setTopicId(post.getTopic().getId());
        return postDto;
    }
    public PostResponseDto convertToDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .name(post.getName())
                .urlName(post.getUrlName())
                .build();
    }
}