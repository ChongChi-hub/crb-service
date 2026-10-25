package com.crb.seaturtlewebservice.domain.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NewsPostsResponseDto {
    private Long id;
    private String topicNewsName;
    private String name;
    private Timestamp datePost;
    private Boolean isHidden;
    private String urlName;
}
