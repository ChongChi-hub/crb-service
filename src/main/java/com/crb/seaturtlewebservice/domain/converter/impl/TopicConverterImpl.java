package com.crb.seaturtlewebservice.domain.converter.impl;

import com.crb.seaturtlewebservice.domain.converter.PostConverter;
import com.crb.seaturtlewebservice.domain.converter.TopicConverter;
import com.crb.seaturtlewebservice.domain.dto.PostWithContentDto;
import com.crb.seaturtlewebservice.domain.dto.TopicDto;
import com.crb.seaturtlewebservice.domain.dto.response.PostResponseDto;
import com.crb.seaturtlewebservice.domain.dto.response.TopicResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Post;
import com.crb.seaturtlewebservice.domain.entity.Topic;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TopicConverterImpl implements TopicConverter {
    @Override
    public TopicResponseDto convertToDto(Topic topic) {
        Set<PostResponseDto> postList = new HashSet<>();
        if(topic.getId() == 3 || topic.getId() == 4){
            for(Post post : topic.getPosts()){
                PostResponseDto postResponseDto = PostResponseDto.builder()
                        .id(post.getId())
                        .name(post.getName())
                        .urlName(post.getUrlName())
                        .build();
                postList.add(postResponseDto);
            }
            return TopicResponseDto.builder()
                    .id(topic.getId())
                    .name(topic.getName())
                    .urlName(topic.getUrlName())
                    .posts(postList)
                    .build();
        }
        return TopicResponseDto.builder()
                .id(topic.getId())
                .name(topic.getName())
                .urlName(topic.getUrlName())
                .build();
    }

    @Override
    public TopicDto entityToDto(Topic topic) {
        TopicDto topicDto = new TopicDto();
        BeanUtils.copyProperties(topic, topicDto);
        return topicDto;
    }
}
