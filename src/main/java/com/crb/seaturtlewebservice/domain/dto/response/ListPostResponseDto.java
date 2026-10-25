package com.crb.seaturtlewebservice.domain.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ListPostResponseDto {
    private Long id;
    private String topicName;
    private String name;
    private Timestamp datePost;
    private Boolean isHidden;
    private String urlName;
}
