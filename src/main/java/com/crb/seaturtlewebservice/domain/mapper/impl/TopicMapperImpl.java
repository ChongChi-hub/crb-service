package com.crb.seaturtlewebservice.domain.mapper.impl;

import com.crb.seaturtlewebservice.domain.dto.request.TopicRequestDto;
import com.crb.seaturtlewebservice.domain.entity.Topic;
import com.crb.seaturtlewebservice.domain.mapper.TopicMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class TopicMapperImpl implements TopicMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicMapperImpl.class);
    private final ModelMapper modelMapper;
    @Override
    public Topic dtoToEntity(TopicRequestDto topicRequestDto) {
        LOGGER.debug("TopicMapperImpl -> DtoToEntity invoked");
        return modelMapper.map(topicRequestDto, Topic.class);
    }
}
