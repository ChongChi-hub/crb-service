package com.crb.seaturtlewebservice.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DeleteListRequestDto {
    List<Long> id;
}
