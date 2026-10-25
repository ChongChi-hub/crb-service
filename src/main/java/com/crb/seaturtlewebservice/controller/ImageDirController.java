package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.ImageDirService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image-dirs")
public class ImageDirController {
    private final ImageDirService imageDirService;

    @GetMapping
    private ResponseEntity<ResponsePayload> getImageDirs() {
        ResponsePayload responsePayload = imageDirService.getImageDirs();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
}

