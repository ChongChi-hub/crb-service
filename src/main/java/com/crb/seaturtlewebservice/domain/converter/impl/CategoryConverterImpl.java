package com.crb.seaturtlewebservice.domain.converter.impl;

import com.crb.seaturtlewebservice.domain.converter.CategoryConverter;
import com.crb.seaturtlewebservice.domain.converter.TopicConverter;
import com.crb.seaturtlewebservice.domain.dto.CategoryDto;
import com.crb.seaturtlewebservice.domain.dto.response.CategoryResponseDto;
import com.crb.seaturtlewebservice.domain.dto.response.TopicResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CategoryConverterImpl implements CategoryConverter {
    private final TopicConverter topicConverter;
    @Override
    public CategoryResponseDto convertToDto(Category category) {
        Set<TopicResponseDto> topicResponseDtoStream = category.getTopics().stream().map(topicConverter::convertToDto).collect(Collectors.toSet());
        return  CategoryResponseDto.builder()
                .id(category.getId())
                .urlName(category.getUrlName())
                .name(category.getName())
                .topics(topicResponseDtoStream)
                .build();
    }
    @Override
    public CategoryDto entityToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .urlName(category.getUrlName())
                .build();
    }
}
