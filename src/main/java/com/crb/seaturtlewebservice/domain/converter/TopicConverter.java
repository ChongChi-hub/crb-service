package com.crb.seaturtlewebservice.domain.converter;

import com.crb.seaturtlewebservice.domain.dto.TopicDto;
import com.crb.seaturtlewebservice.domain.dto.response.TopicResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Topic;

public interface TopicConverter {
    TopicResponseDto convertToDto(Topic topic);

    TopicDto entityToDto(Topic topic);
}
