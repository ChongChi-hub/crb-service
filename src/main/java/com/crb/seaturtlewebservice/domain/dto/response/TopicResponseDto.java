package com.crb.seaturtlewebservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicResponseDto {
    private Long id;
    private CategoryResponseDto categoryResponseDto;
    private String name;
    private String urlName;
    private Set<PostResponseDto> posts;
}
