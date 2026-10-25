package com.crb.seaturtlewebservice.domain.dto.request;

import lombok.Data;

@Data
public class UpdateBranchRequestDto {
    private Long id;
    private String name ;
    private String address;
}
