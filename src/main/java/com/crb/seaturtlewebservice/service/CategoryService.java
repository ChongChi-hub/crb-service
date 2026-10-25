package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.payload.ResponsePayload;

public interface CategoryService {
    ResponsePayload findCategory();

    ResponsePayload getDataHomePage();
}
