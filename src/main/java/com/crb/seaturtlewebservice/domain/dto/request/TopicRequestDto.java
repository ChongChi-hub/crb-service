package com.crb.seaturtlewebservice.domain.dto.request;

import lombok.Data;

@Data
public class TopicRequestDto {
    private Long id;
    private Long categoryId;
    private String name;
    private String urlName;
}
