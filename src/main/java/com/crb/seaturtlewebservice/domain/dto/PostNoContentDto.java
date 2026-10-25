package com.crb.seaturtlewebservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostNoContentDto {
    private Long id;
    private Long categoryId;
    private Long topicId;
    private String name;
    private String icon;
    private String description;
    private String titleImage;
    private Timestamp datePost;
    private String author;
    private String urlName;
    private String tableId;
}
