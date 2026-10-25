package com.crb.seaturtlewebservice.domain.dto.request;

import com.crb.seaturtlewebservice.domain.dto.MetaSeoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostRequest {
    private Long id;
    private long categoryId;
    private long topicId;
    private String icon;
    private String title;
    private String urlName;
    private String titleImage;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Timestamp datePost;
    private String description;
    private String tableId;
    private Boolean isHidden;
    private MetaSeoDto metaSeo;

}

