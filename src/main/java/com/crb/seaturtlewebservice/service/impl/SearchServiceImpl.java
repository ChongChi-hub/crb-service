package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.dto.response.PageResponseDto;
import com.crb.seaturtlewebservice.domain.dto.response.PostsResponseDto;
import com.crb.seaturtlewebservice.domain.entity.Category;
import com.crb.seaturtlewebservice.domain.entity.Post;
import com.crb.seaturtlewebservice.domain.entity.Topic;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.CategoryRepository;
import com.crb.seaturtlewebservice.repository.PostRepository;
import com.crb.seaturtlewebservice.repository.TopicRepository;
import com.crb.seaturtlewebservice.service.SearchService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final PostRepository postRepository;

    @Override
    public ResponsePayload searchAll(String keyword, int pageNumber, int pageSize) {
        LOGGER.info("SearchServiceImpl -> search by name contains invoked");
        try {
            Pageable pageable = PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.by("datePost").descending());
            Optional<Category> category = categoryRepository.getCategoryByName(keyword);
            Optional<Topic> topic = topicRepository.getTopicByName(keyword);
            if (category.isPresent()) {
                Long categoryId = category.get().getId();
                Page<Post> posts = postRepository.findAllByCategoryIdAndIsHiddenFalse(categoryId, pageable);
                List<Object> postDtos = posts.stream()
                        .map(post -> {
                            PostsResponseDto postsResponseDto = new PostsResponseDto();
                            BeanUtils.copyProperties(post, postsResponseDto);
                            return postsResponseDto;
                        }).collect(Collectors.toList());
                PageResponseDto<Object> pageResponseDto = PageResponseDto.builder()
                        .content(postDtos)
                        .pageSize(pageSize)
                        .currentPageNumber(pageNumber)
                        .totalPages(posts.getTotalPages())
                        .hasNext(posts.hasNext())
                        .hasPrevious(posts.hasPrevious())
                        .totalElements(posts.getTotalElements()).build();
                return ResponsePayload.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(pageResponseDto).build();
            } else if (topic.isPresent()) {
                Long topicId = topic.get().getId();
                Page<Post> posts = postRepository.findAllByTopicIdAndIsHiddenFalse(pageable, topicId);
                List<Object> postDtos = posts.stream()
                        .map(post -> {
                            PostsResponseDto postsResponseDto = new PostsResponseDto();
                            BeanUtils.copyProperties(post, postsResponseDto);
                            return postsResponseDto;
                        }).collect(Collectors.toList());
                PageResponseDto<Object> pageResponseDto = PageResponseDto.builder()
                        .content(postDtos)
                        .pageSize(pageSize)
                        .currentPageNumber(pageNumber)
                        .totalPages(posts.getTotalPages())
                        .hasNext(posts.hasNext())
                        .hasPrevious(posts.hasPrevious())
                        .totalElements(posts.getTotalElements()).build();
                return ResponsePayload.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(pageResponseDto).build();
            } else {
                Page<Post> posts = postRepository.findAllByNameContains(keyword, pageable);
                List<Object> postDtos = posts.stream()
                        .map(post -> {
                            PostsResponseDto postsResponseDto = new PostsResponseDto();
                            BeanUtils.copyProperties(post, postsResponseDto);
                            return postsResponseDto;
                        }).collect(Collectors.toList());
                PageResponseDto<Object> pageResponseDto = PageResponseDto.builder()
                        .content(postDtos)
                        .pageSize(pageSize)
                        .currentPageNumber(pageNumber)
                        .totalPages(posts.getTotalPages())
                        .hasNext(posts.hasNext())
                        .hasPrevious(posts.hasPrevious())
                        .totalElements(posts.getTotalElements()).build();
                return ResponsePayload.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(pageResponseDto).build();
            }
        } catch (Exception e) {
            LOGGER.error("SearchServiceImpl -> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Failed")
                    .error(e.getMessage())
                    .build();
        }
    }
}
