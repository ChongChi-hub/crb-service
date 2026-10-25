package com.crb.seaturtlewebservice.domain.dto.response;

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
public class BannerDto {
    private Long id;

    private String name;

    private String description;

    private String link;

    private String linkToPost;
}
