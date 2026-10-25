package com.crb.seaturtlewebservice.domain.dto.request;

import lombok.Data;

@Data
public class ContactRequestDto {
    private String phoneNumber;
    private String description;
}
