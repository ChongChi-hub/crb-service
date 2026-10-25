package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.BannerRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.ImageRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.ImageUpdatedRequest;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    ResponsePayload findAllImage(String keyword, Pageable pageable);
    ResponsePayload addImages(ImageRequestDto imageRequestDto);
    ResponsePayload uploadImages(String sftpUrl, String sftpHostname,
                                 MultipartFile file, String uploadDir,
                                 String imgName) throws Exception;

    ResponsePayload getAllImageWithoutPaging(String keyword, Pageable pageable, Integer year);

    ResponsePayload deleteImage(Long id);

    ResponsePayload getImageById(Long id);

    ResponsePayload updateImage(ImageUpdatedRequest imageUpdatedRequest);

    ResponsePayload getBanners(String type);

    ResponsePayload updateBanner(BannerRequestDto bannerRequestDto);

    ResponsePayload addBanner(BannerRequestDto bannerRequestDto);

    ResponsePayload getImageFromSever(String sftpUrl, String sftpHostname, String dir);
}