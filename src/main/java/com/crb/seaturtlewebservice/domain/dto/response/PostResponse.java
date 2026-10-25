package com.crb.seaturtlewebservice.domain.dto.response;


import com.crb.seaturtlewebservice.domain.dto.MetaSeoDto;
import com.crb.seaturtlewebservice.domain.dto.PostWithContentDto;
import com.crb.seaturtlewebservice.domain.entity.TablePrice;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse {
    private PostWithContentDto postDto;
    private List<TablePrice> tablePriceList;
    private MetaSeoDto metaSeo;
}