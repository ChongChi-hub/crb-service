package com.crb.seaturtlewebservice.domain.dto.request;

import lombok.Data;

@Data
public class UpdateTableRequestDto {
    private Long id;
    private String titleTable;
    private String contentTable;
}
