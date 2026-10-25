package com.crb.seaturtlewebservice.domain.dto.response;

import com.crb.seaturtlewebservice.domain.dto.CategoryDto;
import com.crb.seaturtlewebservice.domain.dto.PostNoContentDto;

import com.crb.seaturtlewebservice.domain.dto.TopicDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomepageResponse {
    private List<CategoryDto> category;
    private List<PostNoContentDto> mainServiceTopic;
    private List<PostNoContentDto> extraServiceTopic;
    private List<PostNoContentDto> historyPost;
    private List<TopicDto> priceList;
//    private List<Topic> tablePrice;
}
