package com.crb.seaturtlewebservice.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageUpdatedRequest {
    private Long id;
    private String topicName;
    private String name;
    private String desc;
    private String imageCategory;
}
