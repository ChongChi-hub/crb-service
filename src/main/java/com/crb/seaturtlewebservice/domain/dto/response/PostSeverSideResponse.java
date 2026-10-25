package com.crb.seaturtlewebservice.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class PostSeverSideResponse {
    String title;
    String titleSeo;
    String description;
    String urlName;
    Timestamp datePost;
}
