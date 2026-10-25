package com.crb.seaturtlewebservice.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageRequestDto {
    private String topicName;
    private String name;
    private String desc;
    private String link;
    private String imageCategory;
    private String year;
}
