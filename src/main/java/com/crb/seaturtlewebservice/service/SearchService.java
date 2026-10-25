package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.payload.ResponsePayload;

public interface SearchService {
    ResponsePayload searchAll(String keyword, int pageNumber, int pageSize);
}
