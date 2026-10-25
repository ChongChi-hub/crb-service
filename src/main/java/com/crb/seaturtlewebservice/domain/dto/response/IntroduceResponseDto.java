package com.crb.seaturtlewebservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntroduceResponseDto {
    private Long id;
    private Long topicId;
    private String content;
}
