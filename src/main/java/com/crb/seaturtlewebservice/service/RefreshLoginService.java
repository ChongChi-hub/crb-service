package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.payload.ResponsePayload;
import jakarta.servlet.http.HttpServletRequest;

public interface RefreshLoginService {
    ResponsePayload refreshLogin(HttpServletRequest request);
}