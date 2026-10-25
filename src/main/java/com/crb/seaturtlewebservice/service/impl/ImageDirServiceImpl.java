package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.entity.ImageDir;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.ImageDirRepository;
import com.crb.seaturtlewebservice.service.ImageDirService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ImageDirServiceImpl implements ImageDirService {
    private final ImageDirRepository imageDirRepository;

    @Override
    public ResponsePayload getImageDirs() {
        try {
            List<ImageDir> imageDirs = imageDirRepository.findAll();
            return ResponsePayload.builder()
                    .message("List of image dirs retrieved successfully")
                    .status(HttpStatus.OK)
                    .data(imageDirs)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Failed to retrieve images")
                    .status(HttpStatus.NO_CONTENT)
                    .error(e.getMessage())
                    .build();
        }
    }
}