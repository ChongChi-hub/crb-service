package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.converter.CategoryConverter;
import com.crb.seaturtlewebservice.domain.converter.TopicConverter;
import com.crb.seaturtlewebservice.domain.dto.ImageDto;
import com.crb.seaturtlewebservice.domain.dto.request.BannerRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.ImageRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.ImageUpdatedRequest;
import com.crb.seaturtlewebservice.domain.dto.response.BannerDto;
import com.crb.seaturtlewebservice.domain.dto.response.PageResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Category;
import com.crb.seaturtlewebservice.domain.entity.Image;
import com.crb.seaturtlewebservice.domain.entity.Topic;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.CategoryRepository;
import com.crb.seaturtlewebservice.repository.ImageRepository;
import com.crb.seaturtlewebservice.repository.TopicRepository;
import com.crb.seaturtlewebservice.service.ImageService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
@Transactional
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);
    private final ImageRepository imageRepository;
    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final TopicConverter topicConverter;

    @Override
    public ResponsePayload addImages(ImageRequestDto imageRequestDto) {
        try {
            Image image = new Image();
            image.setName(imageRequestDto.getName());
            image.setDescription(imageRequestDto.getDesc());
            image.setLink(imageRequestDto.getLink());
            image.setTopicName(imageRequestDto.getTopicName());
            image.setImageCategory(imageRequestDto.getImageCategory());
            image.setType(null);
            image.setYear(Integer.parseInt(imageRequestDto.getYear()));
            image.setIsDelete(false);
            imageRepository.save(image);
            return ResponsePayload.builder()
                    .status(HttpStatus.CREATED)
                    .message("Create new images success")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Failed to save images")
                    .status(HttpStatus.NO_CONTENT)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload uploadImages(String sftpUrl, String sftpHostname, MultipartFile file,
                                        String uploadDir, String imgName) {
        try {
            URI remoteUri = new URI(sftpUrl);

            String username = remoteUri.getUserInfo().split(":")[0];
            String password = remoteUri.getUserInfo().split(":")[1];
            String hostname = remoteUri.getHost();
            int port = remoteUri.getPort() == -1 ? 22 : remoteUri.getPort();
            String remoteDirectory = remoteUri.getPath() + uploadDir;

            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            try {
                sftpChannel.cd(remoteDirectory);
            } catch (SftpException e) {
                // If directory doesn't exist, create it
                sftpChannel.mkdir(remoteDirectory);
                sftpChannel.cd(remoteDirectory);
            }

            byte[] imageBytes = file.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

            sftpChannel.put(inputStream, (imgName + ".jpg"));
            inputStream.close();
            sftpChannel.disconnect();
            session.disconnect();
            String imageUrl = "https://" + sftpHostname + remoteDirectory + "/" + imgName + ".jpg";
            return ResponsePayload.builder()
                    .message("Add image success")
                    .status(HttpStatus.OK)
                    .data(imageUrl)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Add image to server fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .data("Upload fail")
                    .build();
        }
    }

    @Override
    public ResponsePayload getAllImageWithoutPaging(String keyword, Pageable pageable, Integer year) {
        try {
            Page<Image> imagePages = null;
            if ("".equals(keyword)) {
                imagePages = imageRepository.findImagesNotDeleted(pageable, year);
            } else {
                imagePages = imageRepository.findImagesByNameAndNotDeleted(keyword, pageable, year);
            }
            List<Image> imageList = new ArrayList<>();
            for (Image image : imagePages.getContent()) {
                if (image.getType() == null) {
                    imageList.add(image);
                }
            }
            PageResponseDto<Image> pageResponseDto = PageResponseDto.<Image>builder()
                    .totalPages(imagePages.getTotalPages())
                    .pageSize(imagePages.getSize())
                    .totalPages(imagePages.getTotalPages())
                    .totalElements(imagePages.getTotalElements())
                    .currentPageNumber(imagePages.getNumber())
                    .content(imageList)
                    .build();
            return ResponsePayload.builder()
                    .message("List of images retrieved successfully")
                    .status(HttpStatus.OK)
                    .data(pageResponseDto)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Failed to retrieve images")
                    .status(HttpStatus.NO_CONTENT)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload deleteImage(Long id) {
        LOGGER.info("Delete image invoke");
        try {
            Image image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find img id " + id));
            image.setIsDelete(true);
            imageRepository.save(image);
            return ResponsePayload.builder()
                    .message("Delete image success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Failed to delete images")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getImageById(Long id) {
        LOGGER.info("Get image by id invoke");
        try {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Image not found"));
            Topic topic = topicRepository.findByName(image.getTopicName()).orElse(null);
            ImageDto imageDto = null;
            if (topic != null) {
                Category category = categoryRepository.findById(topic.getCategory().getId()).orElseThrow(() -> new EntityNotFoundException("Category not found"));
                imageDto = ImageDto.builder()
                        .id(image.getId())
                        .name(image.getName())
                        .description(image.getDescription())
                        .link(image.getLink())
                        .imageCategory(image.getImageCategory())
                        .topicDto(topicConverter.entityToDto(topic))
                        .categoryDto(categoryConverter.entityToDto(category))
                        .linkToPost(image.getLinkToPost())
                        .build();
            } else {
                imageDto = ImageDto.builder()
                        .id(image.getId())
                        .name(image.getName())
                        .description(image.getDescription())
                        .link(image.getLink())
                        .linkToPost(image.getLinkToPost())
                        .build();
            }

            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .data(imageDto)
                    .message("Post retrieved successfully")
                    .build();

        } catch (RuntimeException e) {
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload updateImage(ImageUpdatedRequest imageUpdatedRequest) {
        LOGGER.info("Update image by id invoke");
        try {
            Image image = imageRepository.findById(imageUpdatedRequest.getId()).orElseThrow(
                    () -> new RuntimeException("cannot find image id" + imageUpdatedRequest.getId()));
            image.setName(imageUpdatedRequest.getName());
            image.setDescription(imageUpdatedRequest.getDesc());
            image.setTopicName(imageUpdatedRequest.getTopicName());
            image.setImageCategory(imageUpdatedRequest.getImageCategory());
            imageRepository.save(image);
            return ResponsePayload.builder()
                    .status(HttpStatus.CREATED)
                    .message("Update new images success")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Failed to save images")
                    .status(HttpStatus.NO_CONTENT)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getBanners(String type) {
        List<Image> images = imageRepository.findAllByIsDeleteFalseAndTypeEquals(type);
        if (images.isEmpty()) {
            return ResponsePayload.builder()
                    .data(images)
                    .message("Get banners success")
                    .status(HttpStatus.OK)
                    .build();
        }
        List<BannerDto> bannerDtoList = images.stream().map(
                        image -> BannerDto.builder()
                                .id(image.getId())
                                .name(image.getName())
                                .description(image.getDescription())
                                .link(image.getLink())
                                .linkToPost(image.getLinkToPost())
                                .build())
                .toList();
        return ResponsePayload.builder()
                .data(bannerDtoList)
                .message("Get banners success")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponsePayload updateBanner(BannerRequestDto bannerRequestDto) {
        LOGGER.info("Update banner by id invoke");
        try {
            Image image = imageRepository.findById(bannerRequestDto.getId()).orElseThrow(
                    () -> new RuntimeException("cannot find image id" + bannerRequestDto.getId()));
            image.setName(bannerRequestDto.getName());
            image.setDescription(bannerRequestDto.getDesc());
            image.setLinkToPost(bannerRequestDto.getLinkToPost());
            imageRepository.save(image);
            return ResponsePayload.builder()
                    .status(HttpStatus.CREATED)
                    .message("Update banner success")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Failed to update banner")
                    .status(HttpStatus.NO_CONTENT)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload addBanner(BannerRequestDto bannerRequestDto) {
        try {
            Image image = new Image();
            image.setName(bannerRequestDto.getName());
            image.setDescription(bannerRequestDto.getDesc());
            image.setLink(bannerRequestDto.getLink());
            image.setLinkToPost(bannerRequestDto.getLinkToPost());
            image.setType(bannerRequestDto.getType());
            image.setIsDelete(false);
            imageRepository.save(image);
            return ResponsePayload.builder()
                    .status(HttpStatus.CREATED)
                    .message("Create new banner success")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Failed to save banner")
                    .status(HttpStatus.NO_CONTENT)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getImageFromSever(String sftpUrl, String sftpHostname, String dir) {
        try {
            String year = "";
            if(dir.length() == 4){
                year = dir;
            }else {
                year = dir.substring(0, 4);
            }
            List<Image> imageList = new ArrayList<>();
            URI remoteUri = new URI(sftpUrl);

            String username = remoteUri.getUserInfo().split(":")[0];
            String password = remoteUri.getUserInfo().split(":")[1];
            String hostname = remoteUri.getHost();
            int port = remoteUri.getPort() == -1 ? 22 : remoteUri.getPort();
            String remoteDirectory = remoteUri.getPath() + dir;

            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(remoteDirectory);
            for (ChannelSftp.LsEntry entry : list) {
                if (!entry.getAttrs().isDir()) {
                    String imageUrl = "https://" + sftpHostname + remoteDirectory + "/" + entry.getFilename();
                    imageList.add( imageRepository.save(Image.builder()
                            .link(imageUrl)
                            .name(entry.getFilename())
                            .type(null)
                            .isDelete(false)
                            .year(Integer.parseInt(year))
                            .build()));
                }
            }
            sftpChannel.disconnect();
            session.disconnect();
            return ResponsePayload.builder()
                    .message("Add image success")
                    .status(HttpStatus.OK)
                    .data(imageList.size())
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Add image to server fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .data("Upload fail")
                    .build();
        }
    }

    @Override
    public ResponsePayload findAllImage(String keyword, Pageable pageable) {
        try {
            Page<Image> imagePages = null;
            if ("".equals(keyword)) {
                imagePages = imageRepository.findAllByIsDeleteFalseAndTypeNull(pageable);
            } else {
                imagePages = imageRepository.findByNameContainingOrDescriptionContainingOrImageCategoryContainingAndIsDeleteFalse(keyword, pageable);
            }
            PageResponseDto<Image> pageResponseDto = PageResponseDto.<Image>builder()
                    .content(imagePages.getContent())
                    .pageSize(imagePages.getSize())
                    .totalPages(imagePages.getTotalPages())
                    .hasNext(imagePages.hasNext())
                    .hasPrevious(imagePages.hasPrevious())
                    .totalElements(imagePages.getTotalElements())
                    .currentPageNumber(imagePages.getNumber())
                    .build();
            return ResponsePayload.builder()
                    .message("List of images retrieved successfully")
                    .status(HttpStatus.OK)
                    .data(pageResponseDto)
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