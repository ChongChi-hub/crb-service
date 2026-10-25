package com.crb.seaturtlewebservice.domain.dto;


import com.crb.seaturtlewebservice.domain.entity.Category;
import com.crb.seaturtlewebservice.domain.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private Category category;
    private Topic topic;
    private String name;
    private String icon;
    private String content;
    private String description;
    private String titleImage;
    private Timestamp datePost;
    private String author;
    private String urlName;
    private String tableId;
    private MetaSeoDto metaSeo;
}

