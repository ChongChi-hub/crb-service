package com.crb.seaturtlewebservice.domain.mapper;

import com.crb.seaturtlewebservice.domain.dto.request.TopicRequestDto;
import com.crb.seaturtlewebservice.domain.entity.Topic;

public interface TopicMapper {
    Topic dtoToEntity(TopicRequestDto topicRequestDto);
}
