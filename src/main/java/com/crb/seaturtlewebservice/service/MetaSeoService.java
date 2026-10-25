package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.MetaSeoDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;

public interface MetaSeoService {
    ResponsePayload updateMetaSeo(String entityType, Long entityId, MetaSeoDto metaSeoDto);
    ResponsePayload getMetaSeo(String entityType, Long entityId);
}