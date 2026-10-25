package com.crb.seaturtlewebservice.domain.dto.request;

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
public class BannerRequestDto {
    private Long id;

    private String name;

    private String desc;

    private String linkToPost;

    private String link;

    private String type;
}
