package com.crb.seaturtlewebservice.domain.dto.request;

import lombok.Data;

@Data
public class UpdateContactRequestDto {
    private Long id;
    private String phoneNumber;
    private String description;
}
