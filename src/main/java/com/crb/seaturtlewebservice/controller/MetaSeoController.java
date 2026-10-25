package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.domain.dto.MetaSeoDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.MetaSeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meta-seo")
@RequiredArgsConstructor
@CrossOrigin("*") 
public class MetaSeoController {

    private final MetaSeoService metaSeoService;

    @GetMapping("/{entityType}/{entityId}")
    public ResponseEntity<ResponsePayload> getMetaSeo(
            @PathVariable String entityType,
            @PathVariable Long entityId) {

        String type = entityType.toUpperCase(); 
        ResponsePayload response = metaSeoService.getMetaSeo(type, entityId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/{entityType}/{entityId}")
    public ResponseEntity<ResponsePayload> updateMetaSeo(
            @PathVariable String entityType,
            @PathVariable Long entityId,
            @RequestBody MetaSeoDto metaSeoDto) {

        String type = entityType.toUpperCase();
        ResponsePayload response = metaSeoService.updateMetaSeo(type, entityId, metaSeoDto);
        return new ResponseEntity<>(response, response.getStatus());
    }
}