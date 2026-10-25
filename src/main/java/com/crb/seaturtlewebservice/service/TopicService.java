package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.TopicRequestDto;
import com.crb.seaturtlewebservice.domain.dto.response.TopicResponseDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;

import java.util.List;

public interface TopicService {
    ResponsePayload getTopicsByCategoryId(Long id);
    ResponsePayload getTopicsByCategoryUrlName(String urlName);

    ResponsePayload deleteTopicById(Long id);

    ResponsePayload addTopic(TopicRequestDto topicRequestDto);

    ResponsePayload updateTopic(TopicRequestDto topicRequestDto);

    ResponsePayload findAllTopicByCategoryId(Long id);

    ResponsePayload getTopics();

    List<TopicResponseDto> getTopicResponseDto();
}
