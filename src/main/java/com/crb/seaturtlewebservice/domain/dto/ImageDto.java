package com.crb.seaturtlewebservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {
    private Long id;

    private String name;

    private String description;

    private String link;

    private TopicDto topicDto;

    private CategoryDto categoryDto;

    private String imageCategory;

    private String linkToPost;

}
