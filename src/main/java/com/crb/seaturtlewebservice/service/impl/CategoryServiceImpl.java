package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.constant.ConstantId;
import com.crb.seaturtlewebservice.domain.converter.CategoryConverter;
import com.crb.seaturtlewebservice.domain.converter.PostConverter;
import com.crb.seaturtlewebservice.domain.converter.TopicConverter;
import com.crb.seaturtlewebservice.domain.dto.CategoryDto;
import com.crb.seaturtlewebservice.domain.dto.PostNoContentDto;
import com.crb.seaturtlewebservice.domain.dto.TopicDto;
import com.crb.seaturtlewebservice.domain.dto.response.CategoryResponseDto;
import com.crb.seaturtlewebservice.domain.dto.response.HomepageResponse;
import com.crb.seaturtlewebservice.domain.entity.Category;
import com.crb.seaturtlewebservice.domain.entity.Post;
import com.crb.seaturtlewebservice.domain.entity.Topic;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.CategoryRepository;
import com.crb.seaturtlewebservice.repository.PostPageableRepository;
import com.crb.seaturtlewebservice.repository.PostRepository;
import com.crb.seaturtlewebservice.repository.TopicRepository;
import com.crb.seaturtlewebservice.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crb.seaturtlewebservice.constant.PageConstant.PAGE_SIZE_HOME;


@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final PostPageableRepository postPageableRepository;
    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final CategoryConverter categoryConverter;
    private final TopicRepository topicRepository;
    private final TopicConverter topicConverter;

    @Override
    public ResponsePayload findCategory() {
        LOGGER.info("CategoryServiceImpl -> get all category invoked");
        try {
            List<Category> categories = categoryRepository.findAll();
            Stream<CategoryResponseDto> categoryResponseDtoStream = categories.stream().map(categoryConverter::convertToDto);
            return ResponsePayload.builder()
                    .data(categoryResponseDtoStream)
                    .status(HttpStatus.OK)
                    .message("Find all data of category")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not find data of category")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }


    @Override
    public ResponsePayload getDataHomePage() {
        LOGGER.info("CategoryServiceImpl -> get all category invoked");
        Pageable pageable = PageRequest.ofSize(PAGE_SIZE_HOME);
        try {
            List<Category> category = categoryRepository.findAll();
            List<CategoryDto> categoryDto = category.stream()
                    .map(categoryConverter::entityToDto)
                    .collect(Collectors.toList());
            List<Post> mainServiceTopic = postRepository.findAllByTopicIdAndIsHidden(ConstantId.MAIN_SERVICE_ID, false);
            List<PostNoContentDto> mainServiceTopicDto = mainServiceTopic.stream()
                    .map(postConverter::entityToNoContentDto)
                    .collect(Collectors.toList());
            List<Post> extraServiceTopic = postRepository.findAllByTopicIdAndIsHidden(ConstantId.EXTRA_SERVICE_ID, false);
            List<PostNoContentDto> extraServiceTopicDto = extraServiceTopic.stream()
                    .map(postConverter::entityToNoContentDto)
                    .collect(Collectors.toList());
            Page<Post> historyPost = postPageableRepository.findAllByCategoryId(pageable, ConstantId.HISTORY_TOPIC_ID);
            List<PostNoContentDto> historyPostDto = historyPost.getContent().stream()
                    .map(postConverter::entityToNoContentDto)
                    .collect(Collectors.toList());
            List<Topic> priceList = topicRepository.findAllByCategoryId(ConstantId.PRICE_CATEG0RY_ID);
            List<TopicDto> priceListDto = priceList.stream()
                    .map(topicConverter::entityToDto)
                    .collect(Collectors.toList());
            HomepageResponse response = HomepageResponse.builder()
                    .category(categoryDto)
                    .mainServiceTopic(mainServiceTopicDto)
                    .extraServiceTopic(extraServiceTopicDto)
                    .historyPost(historyPostDto)
                    .priceList(priceListDto)
                    .build();
            return ResponsePayload.builder()
                    .data(response)
                    .status(HttpStatus.OK)
                    .message("Find all data of category")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not find data of category")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
