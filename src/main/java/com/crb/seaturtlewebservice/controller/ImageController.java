package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.domain.dto.request.BannerRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.ImageRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.ImageUpdatedRequest;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;
    private static final int DEFAULT_PAGE_SIZE = 30;
    @Value("${sftp.url}")
    private String sftpUrl;

    @Value("${sftp.hostname}")
    private String sftpHostname;

    @GetMapping
    private ResponseEntity<ResponsePayload> getAllImage(
            @RequestParam(defaultValue = "") String keyword,
            @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "id", direction = Sort.Direction.DESC ) Pageable pageable) {
        ResponsePayload responsePayload = imageService.findAllImage(keyword, pageable);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping
    public ResponseEntity<ResponsePayload> addImages(@RequestBody ImageRequestDto imageRequestDto) {
        ResponsePayload responsePayload = imageService.addImages(imageRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponsePayload> uploadImages(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "uploadDir") String uploadDir,
            @RequestParam(name = "imgName") String imgName) throws Exception {
        ResponsePayload response = imageService.uploadImages(sftpUrl, sftpHostname, file, uploadDir, imgName);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/all")
    private ResponseEntity<ResponsePayload> getAllImageWithoutPaging
            (
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name  = "year", defaultValue = "2024") Integer year
            ) {
        Pageable pageable = PageRequest.of(page, 60, Sort.by("id").descending());
        ResponsePayload responsePayload = imageService.getAllImageWithoutPaging(keyword, pageable, year);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<ResponsePayload> deleteImage(@PathVariable(name = "id") Long id) {
        ResponsePayload responsePayload = imageService.deleteImage(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/{id}")
    private ResponseEntity<ResponsePayload> getImageById(@PathVariable(name = "id") Long id) {
        ResponsePayload responsePayload = imageService.getImageById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PutMapping
    private ResponseEntity<ResponsePayload> updateImage(@RequestBody ImageUpdatedRequest imageUpdatedRequest) {
        ResponsePayload responsePayload = imageService.updateImage(imageUpdatedRequest);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/banners")
    private ResponseEntity<ResponsePayload> getBanners(@RequestParam(name = "type", defaultValue = "desktop") String type){
        ResponsePayload responsePayload = imageService.getBanners(type);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PutMapping("/banner")
    private ResponseEntity<ResponsePayload> updateBanner(@RequestBody BannerRequestDto bannerRequestDto) {
        ResponsePayload responsePayload = imageService.updateBanner(bannerRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping("/banner")
    public ResponseEntity<ResponsePayload> addBanner(@RequestBody BannerRequestDto bannerRequestDto) {
        ResponsePayload responsePayload = imageService.addBanner(bannerRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/imagesever")
    private ResponseEntity<ResponsePayload> getImageFromSever(@RequestParam(name = "dir", defaultValue = "") String dir){
        ResponsePayload responsePayload = imageService.getImageFromSever(sftpUrl, sftpHostname, dir);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
}

