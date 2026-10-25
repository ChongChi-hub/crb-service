package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.dto.TopicDto;
import com.crb.seaturtlewebservice.domain.dto.response.TopicsResponseDto;
import com.crb.seaturtlewebservice.domain.dto.request.TopicRequestDto;
import com.crb.seaturtlewebservice.domain.dto.response.CategoryResponseDto;
import com.crb.seaturtlewebservice.domain.dto.response.TopicResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Category;
import com.crb.seaturtlewebservice.domain.entity.Topic;
import com.crb.seaturtlewebservice.domain.mapper.TopicMapper;
import com.crb.seaturtlewebservice.exception.CategoryNotFoundException;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.CategoryRepository;
import com.crb.seaturtlewebservice.repository.TopicRepository;
import com.crb.seaturtlewebservice.service.TopicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicService.class);
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ResponsePayload getTopicsByCategoryId(Long id) {
        LOGGER.info("TopicServiceImpl -> get all sub-category invoked");
        try {
            Iterable<Topic> topics = topicRepository.getAllByCategoryId(id);
            return ResponsePayload.builder()
                    .data(topics)
                    .message("Find all data of sub-category")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not found all data of sub-category")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getTopicsByCategoryUrlName(String urlName) {
        LOGGER.info("TopicServiceImpl -> get topics by category url name");
        try {
            Category category = categoryRepository.getCategoryByUrlName(urlName)
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            Long categoryId = category.getId();
            List<Topic> topics = topicRepository.findAllByCategoryId(categoryId);
            List<Object> topicDtos = topics.stream()
                    .map(topic -> {
                        TopicsResponseDto topicsResponseDto = new TopicsResponseDto();
                        BeanUtils.copyProperties(topic, topicsResponseDto);
                        return topicsResponseDto;
                    }).collect(Collectors.toList());
            return ResponsePayload.builder()
                    .data(topicDtos)
                    .status(HttpStatus.OK)
                    .message("Successfully")
                    .build();
        } catch (Exception e) {
            LOGGER.error("TopicServiceImpl -> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Failed")
                    .error(e.getMessage())
                    .build();
        }

    }

    @Override
    public ResponsePayload deleteTopicById(Long id) {
        LOGGER.info("TopicServiceImpl -> delete sub-category invoked");
        try {
            topicRepository.deleteById(id);
            List<TopicResponseDto> topicResponseDtos = getTopicResponseDto();
            return ResponsePayload.builder()
                    .data(topicResponseDtos)
                    .message("Delete sub-category success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Delete sub-category fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload addTopic(TopicRequestDto topicRequestDto) {
        LOGGER.info("TopicServiceImpl -> Add new sub-category invoked");
        try {
            Topic topic = topicMapper.dtoToEntity(topicRequestDto);
            Optional<Category> category = categoryRepository.findById(topicRequestDto.getCategoryId());
            topic.setCategory(category.get());
            topicRepository.save(topic);
            List<TopicResponseDto> topicResponseDtos = getTopicResponseDto();
            return ResponsePayload.builder()
                    .data(topicResponseDtos)
                    .message("Add new sub-category success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Add new  sub-category fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload updateTopic(TopicRequestDto topicRequestDto) {
        LOGGER.info("TopicServiceImpl -> update sub-category invoked");
        try {
            Topic topic = topicRepository.findById(topicRequestDto.getId()).orElse(null);
            if (topic != null) {
                if (!topicRequestDto.getName().isEmpty()) {
                    topic.setName(topicRequestDto.getName());
                }
                if (!topicRequestDto.getUrlName().isEmpty()) {
                    topic.setUrlName(topicRequestDto.getUrlName());
                }
                if (!topicRequestDto.getCategoryId().describeConstable().isEmpty()) {
                    Category category = categoryRepository.findById(topicRequestDto.getCategoryId()).orElse(null);
                    topic.setCategory(category);
                }
                topicRepository.save(topic);
            }
            List<TopicResponseDto> topicResponseDtos = getTopicResponseDto();
            return ResponsePayload.builder()
                    .data(topicResponseDtos)
                    .message("Update sub-category success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Update  sub-category fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage()).build();
        }
    }

    @Override
    public ResponsePayload findAllTopicByCategoryId(Long id) {
        try {
            List<Topic> topicList = topicRepository.findAllByCategoryId(id);
            List<TopicDto> topicDtoList = new ArrayList<>();
            for(Topic topic : topicList){
                topicDtoList.add(
                        TopicDto.builder()
                        .id(topic.getId())
                        .name(topic.getName())
                        .urlName(topic.getUrlName())
                        .icon(topic.getIcon())
                        .build()
                        );
            }
            return ResponsePayload.builder()
                    .data(topicDtoList)
                    .status(HttpStatus.OK)
                    .message("Find all data topic by category id " + id)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not find data topic by category id " + id)
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload getTopics() {
        LOGGER.info("TopicServiceImpl -> find all sub-category invoked");
        try {
            List<TopicResponseDto> topicResponseDtos = getTopicResponseDto();
            return ResponsePayload.builder()
                    .data(topicResponseDtos)
                    .status(HttpStatus.OK)
                    .message("Find all data topic")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not find data topic")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public List<TopicResponseDto> getTopicResponseDto() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map(item -> new TopicResponseDto(item.getId(), new CategoryResponseDto(item.getCategory().getId(),
                        item.getCategory().getUrlName(), item.getCategory().getName(),null), item.getName(),
                        item.getUrlName(),null)).collect(Collectors.toList());
    }
}
