package com.crb.seaturtlewebservice.payload;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponsePayload {
    private String message;
    private HttpStatus status;
    private Object data;
    private String error;
}
