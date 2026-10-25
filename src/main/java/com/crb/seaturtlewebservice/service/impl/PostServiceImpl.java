package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.constant.ConstantId;
import com.crb.seaturtlewebservice.domain.converter.PageResponseConverter;
import com.crb.seaturtlewebservice.domain.converter.PostConverter;
import com.crb.seaturtlewebservice.domain.dto.MetaSeoDto;
import com.crb.seaturtlewebservice.domain.dto.PostDto;
import com.crb.seaturtlewebservice.domain.dto.PostNoContentDto;
import com.crb.seaturtlewebservice.domain.dto.PostWithContentDto;
import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.PostRequest;
import com.crb.seaturtlewebservice.domain.dto.response.*;
import com.crb.seaturtlewebservice.domain.entity.Category;
import com.crb.seaturtlewebservice.domain.entity.MetaSeo;
import com.crb.seaturtlewebservice.domain.entity.Post;
import com.crb.seaturtlewebservice.domain.entity.TablePrice;
import com.crb.seaturtlewebservice.domain.entity.Topic;
import com.crb.seaturtlewebservice.exception.TopicNotFoundException;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.CategoryRepository;
import com.crb.seaturtlewebservice.repository.MetaSeoRepository;
import com.crb.seaturtlewebservice.repository.PostRepository;
import com.crb.seaturtlewebservice.repository.TableRepository;
import com.crb.seaturtlewebservice.repository.TopicRepository;
import com.crb.seaturtlewebservice.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crb.seaturtlewebservice.constant.ConstantId.CONTACT_CATEGORY_ID;
import static com.crb.seaturtlewebservice.constant.ConstantId.INTRODUCE_TOPIC_ID;
import static com.crb.seaturtlewebservice.constant.ConstantId.NEWS_TOPIC_ID;
import static com.crb.seaturtlewebservice.constant.PostConstant.SIZE_POST;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final PostConverter postConverter;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final TableRepository tableRepository;
    private final PageResponseConverter<ListPostResponseDto> pageResponseConverter;
    private final PageResponseConverter<NewsPostsResponseDto> pageNewsPostResponseConverter;
    private final MetaSeoRepository metaSeoRepository;

    
    @Override
    public ResponsePayload create(PostRequest postRequest) {
        try {
            Topic topic = topicRepository.findById(postRequest.getTopicId()).orElseThrow(
                    () -> new Exception("Cannot find Topic id " + postRequest.getTopicId()));
            Category category = categoryRepository.findById(postRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("cannot find category"));

            Post post = Post.builder()
                    .id(postRequest.getId()) 
                    .topic(topic)
                    .icon(postRequest.getIcon())
                    .name(postRequest.getTitle())
                    .urlName(postRequest.getUrlName())
                    .titleImage(postRequest.getTitleImage())
                    .description(postRequest.getDescription())
                    .content(postRequest.getContent())
                    .datePost(postRequest.getDatePost())
                    .tableId(postRequest.getTableId())
                    .category(category)
                    .isHidden(postRequest.getIsHidden() != null ? postRequest.getIsHidden() : false)
                    .build();

            Post savedPost = postRepository.save(post);
            Long savedPostId = savedPost.getId();

            MetaSeoDto metaSeoDto = postRequest.getMetaSeo();
            if (metaSeoDto != null) {
                Optional<MetaSeo> optionalMetaSeo = metaSeoRepository.findByEntityIdAndEntityType(savedPostId, "POST");

                MetaSeo metaSeoToSave;

                if (optionalMetaSeo.isPresent()) {
                    metaSeoToSave = optionalMetaSeo.get();
                    Long id = metaSeoToSave.getId();
                    modelMapper.map(metaSeoDto, metaSeoToSave);
                    metaSeoToSave.setId(id); 
                    metaSeoToSave.setEntityId(savedPostId);
                    metaSeoToSave.setEntityType("POST");
                } else {
                    metaSeoToSave = modelMapper.map(metaSeoDto, MetaSeo.class);
                    metaSeoToSave.setEntityId(savedPostId);
                    metaSeoToSave.setEntityType("POST");
                }

                metaSeoRepository.save(metaSeoToSave); 
            }

            String message = (postRequest.getId() == null) ? "Create new post success" : "Update post success";

            return ResponsePayload.builder()
                    .data(null)
                    .status(HttpStatus.OK) 
                    .message(message)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Create/Update post fail: {}", e.getMessage(), e); 
            return ResponsePayload.builder()
                    .message("Create/Update post fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload getListPost(Pageable pageableRequest, String keyword) {
        LOGGER.info("PostServiceImpl -> getListPost invoked");
        try {
            List<Long> excludedTopicIds = Arrays.asList(ConstantId.INTRODUCE_TOPIC_ID, ConstantId.CONTACT_TOPIC_ID);
            Page<Post> postPage;

            if (keyword != null && !keyword.isEmpty()) {
                postPage = postRepository.findAllByNameContainsAndTopicIdNotIn(keyword, excludedTopicIds,
                        pageableRequest);
            } else {
                postPage = postRepository.findAllByTopicIdNotIn(excludedTopicIds, pageableRequest);
            }

            List<ListPostResponseDto> listPostResponseDtos = postPage.stream()
                    .map(post -> {
                        ListPostResponseDto listPostResponseDto = new ListPostResponseDto();
                        BeanUtils.copyProperties(post, listPostResponseDto);
                        Topic topic = post.getTopic();
                        listPostResponseDto.setTopicName(topic.getName());
                        return listPostResponseDto;
                    })
                    .collect(Collectors.toList());
            PageResponseDto<ListPostResponseDto> pageResponseDto = pageResponseConverter.buildPageResponseDto(postPage,
                    listPostResponseDtos);
            return ResponsePayload.builder()
                    .message("Get list post successful")
                    .status(HttpStatus.OK)
                    .data(pageResponseDto)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Override
    public ResponsePayload getListPostByTopicId(Pageable pageable, String keyword) {
        LOGGER.info("PostServiceImpl -> get list post by topic id invoked");
        try {
            Page<Post> newsPosts;
            if (keyword != null && !keyword.isEmpty()) {
                newsPosts = postRepository.findAllByNameContains(keyword, pageable);
            } else {
                newsPosts = postRepository.findAllByTopicIdAndIsHiddenFalse(pageable, NEWS_TOPIC_ID);
            }

            List<NewsPostsResponseDto> newsPostResponseDtos = newsPosts.stream()
                    .map(post -> {
                        NewsPostsResponseDto newsPostsResponseDto = new NewsPostsResponseDto();
                        BeanUtils.copyProperties(post, newsPostsResponseDto);
                        Topic topic = post.getTopic();
                        newsPostsResponseDto.setTopicNewsName(topic.getName());
                        return newsPostsResponseDto;
                    }).collect(Collectors.toList());
            PageResponseDto<NewsPostsResponseDto> pageResponseDto = pageNewsPostResponseConverter
                    .buildPageResponseDto(newsPosts, newsPostResponseDtos);
            return ResponsePayload.builder()
                    .message("Get list post successfully")
                    .data(pageResponseDto)
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("PostServiceImpl -> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .message("Get list post failed")
                    .status(HttpStatus.NOT_FOUND)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload deletePostById(Long id) {
        LOGGER.info("PostServiceImpl -> deletePostById invoked");
        try {
            metaSeoRepository.findByEntityIdAndEntityType(id, "POST")
                    .ifPresent(metaSeoRepository::delete);

            postRepository.deleteById(id);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Delete post id: " + id + "successful")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload deletePostByListId(DeleteListRequestDto deleteListPostRequestDto) {
        LOGGER.info("PostServiceImpl -> DeleteListPostRequestDto invoked");
        try {
            List<Long> ids = deleteListPostRequestDto.getId();

            for (Long id : ids) {
                metaSeoRepository.findByEntityIdAndEntityType(id, "POST")
                        .ifPresent(metaSeoRepository::delete);
            }

            postRepository.deleteAllByIdInBatch(ids);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Delete list post successful")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload updatePostStatus(Long id, Boolean isHidden) {
        LOGGER.info("PostServiceImpl -> updatePostStatus invoked");
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setIsHidden(isHidden);
            postRepository.save(post);
            String message = isHidden ? "Hide post successful" : "Open post successful";
            HttpStatus status = HttpStatus.OK;
            return ResponsePayload.builder()
                    .message(message)
                    .status(status)
                    .build();
        } else {
            return ResponsePayload.builder()
                    .message("Post not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Override
    public ResponsePayload getPostsByTopicUrlName(String urlName, int pageNumber, int pageSize) {
        LOGGER.info("PostServiceImpl -> get posts by topic url name invoked");
        try {
            Topic topic = topicRepository.getTopicByUrlName(urlName)
                    .orElseThrow(() -> new TopicNotFoundException("Topic not found"));
            Long topicId = topic.getId();
            Pageable pageable = PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.by("datePost").descending());
            Page<Post> posts = postRepository.findAllByTopicIdAndIsHiddenFalse(pageable, topicId);
            List<Object> postDtos = posts.stream()
                    .map(post -> {
                        PostsResponseDto postsResponseDto = new PostsResponseDto();
                        BeanUtils.copyProperties(post, postsResponseDto);
                        return postsResponseDto;
                    })
                    .collect(Collectors.toList());
            PageResponseDto<Object> pageResponseDto = PageResponseDto.builder()
                    .content(postDtos)
                    .pageSize(pageSize)
                    .currentPageNumber(pageNumber)
                    .totalPages(posts.getTotalPages())
                    .hasNext(posts.hasNext())
                    .hasPrevious(posts.hasPrevious())
                    .totalElements(posts.getTotalElements())
                    .build();
            return ResponsePayload.builder()
                    .data(pageResponseDto)
                    .status(HttpStatus.OK)
                    .message("Successfully")
                    .build();
        } catch (Exception e) {
            LOGGER.error("PostServiceImpl -> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Failed")
                    .error(e.getMessage())
                    .build();
        }
    }

    public ResponsePayload getPostDtoById(Long id) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));

            PostDto postDto = postConverter.entityToDto(post);
            if (postDto.getTopic() != null) {
                postDto.getTopic().setPosts(null); 
            }
            metaSeoRepository.findByEntityIdAndEntityType(post.getId(), "POST")
                    .ifPresent(metaSeo -> {
                        MetaSeoDto metaDto = modelMapper.map(metaSeo, MetaSeoDto.class);
                        postDto.setMetaSeo(metaDto);
                    });

            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .data(postDto)
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
    public ResponsePayload getPostDtoUrlName(String urlName) {
        try {
            String url = "/" + urlName;
            Post post = postRepository.findByUrlNameAndIsHiddenFalse(url)
                    .orElseThrow(() -> new RuntimeException("Post with URL name '" + url + "' not found"));

            PostResponse postResponse = new PostResponse();
            if (post.getTableId() != null) {
                List<Long> listId = Arrays.stream(post.getTableId().split(","))
                        .map(String::trim)
                        .map(Long::valueOf)
                        .toList();
                List<TablePrice> tablePrices = new ArrayList<>();
                for (Long id : listId) {
                    TablePrice tablePrice = tableRepository.findById(id).orElse(null);
                    if (tablePrice != null) {
                        tablePrices.add(tablePrice);
                    }
                }
                postResponse.setTablePriceList(tablePrices);
            }

            PostWithContentDto postsDto = postConverter.entityToWithContentDto(post);
            postResponse.setPostDto(postsDto);
            metaSeoRepository.findByEntityIdAndEntityType(post.getId(), "POST")
                    .ifPresent(metaSeo -> {
                        MetaSeoDto metaDto = modelMapper.map(metaSeo, MetaSeoDto.class);
                        postResponse.setMetaSeo(metaDto); 
                    });

            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Post retrieved successfully")
                    .data(postResponse)
                    .build();
        } catch (RuntimeException e) {
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Failed to retrieve post")
                    .build();
        }
    }

    @Override
    public ResponsePayload getFiveNewestPosts(int pageNumber, int pageSize, Long id) {
        LOGGER.info("PostServiceImpl -> get all post new invoked");
        try {
            Pageable sortedByDatePostDesc = PageRequest.of(pageNumber, pageSize, Sort.by("datePost").descending());
            Page<Post> posts = postRepository.findAllByTopicIdAndIsHiddenFalse(sortedByDatePostDesc, id);
            List<PostNoContentDto> postDto = posts.getContent().stream()
                    .map(postConverter::entityToNoContentDto)
                    .collect(Collectors.toList());
            return ResponsePayload.builder()
                    .data(postDto)
                    .message("Find all data of post")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("PostServiceImpl -> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .message("Not found all data of post")
                    .status(HttpStatus.BAD_REQUEST)

                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload addPostContact(PostRequest postRequest) {
        LOGGER.info("PostServiceImpl -> add post contact invoked");
        try {
            if (postRequest.getCategoryId() == CONTACT_CATEGORY_ID) {
                List<Post> posts = postRepository.getAllByCategoryIdAndIsHiddenFalse(postRequest.getCategoryId());
                Post postNew = null;
                if (posts.size() == SIZE_POST) {
                    for (Post post : posts) {
                        post.setContent(postRequest.getContent());
                        postNew = postRepository.save(post);
                    }
                } else if (posts.isEmpty()) {
                    Post post = new Post();
                    Optional<Category> category = categoryRepository.findById(postRequest.getCategoryId());
                    if (category.isPresent()) {
                        post.setCategory(category.get());
                        post.setContent(postRequest.getContent());
                        postNew = postRepository.save(post);
                    }
                }
                return ResponsePayload.builder()
                        .data(postNew)
                        .message("Add data of post contact success")
                        .status(HttpStatus.OK)
                        .build();
            }
            return ResponsePayload.builder()
                    .message("Not found index category in data of post contact")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not add data of post contact fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getPostContact() {
        LOGGER.info("PostServiceImpl -> find post contact invoked");
        try {
            List<Post> posts = postRepository.getAllByCategoryIdAndIsHiddenFalse(CONTACT_CATEGORY_ID);
            ContactResponseDto contactResponseDto = null;
            Optional<Post> firstPost = posts.stream().findFirst();
            if (firstPost.isPresent()) {
                Post post = firstPost.get();
                contactResponseDto = new ContactResponseDto(
                        post.getId(),
                        post.getCategory().getId(),
                        post.getContent());
            }
            return ResponsePayload.builder()
                    .data(contactResponseDto)
                    .message("Find all data of post")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not found add data of post contact fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getPostIntroduce() {
        LOGGER.info("PostServiceImpl -> find post introduce invoked");
        try {
            List<Post> posts = postRepository.findAllByTopicIdAndIsHiddenFalse(INTRODUCE_TOPIC_ID);
            IntroduceResponseDto introduceResponseDto;
            if (posts.size() == SIZE_POST) {
                Stream<PostDto> postDtoStream = posts.stream().map(postConverter::entityToDtos);
                PostDto postDto = postDtoStream.reduce(null, (acc, postDtoItem) -> postDtoItem);
                introduceResponseDto = modelMapper.map(postDto, IntroduceResponseDto.class);
            } else {
                return ResponsePayload.builder()
                        .message("Not found add data of post introduce fail")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return ResponsePayload.builder()
                    .data(introduceResponseDto)
                    .message("Find all data of post")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not found add data of post introduce fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload addPostIntroduce(PostRequest postRequest) {
        LOGGER.info("PostServiceImpl -> add post introduce invoked");
        try {
            if (postRequest.getTopicId() == INTRODUCE_TOPIC_ID) {
                List<Post> posts = postRepository.findAllByTopicIdAndIsHiddenFalse(postRequest.getTopicId());
                Post postNew = null;
                if (posts.size() == SIZE_POST) {
                    for (Post post : posts) {
                        post.setContent(postRequest.getContent());
                        postNew = postRepository.save(post);
                    }
                }
                IntroduceResponseDto introduceResponseDto = modelMapper.map(postNew, IntroduceResponseDto.class);
                return ResponsePayload.builder()
                        .data(introduceResponseDto)
                        .message("Add data of post introduce success")
                        .status(HttpStatus.OK)
                        .build();
            }
            return ResponsePayload.builder()
                    .message("Not found index topic in data of post introduce")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not add data of post introduce fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload isUrlExist(String urlName, String id) {
        LOGGER.info("PostServiceImpl -> check url exist invoked");
        try {
            if (id.isEmpty()) {
                Optional<Post> postOptional = postRepository.findByUrlNameAndIsHiddenFalse(urlName);
                if (postOptional.isPresent()) {
                    return ResponsePayload.builder()
                            .message("Url is exist")
                            .data(true)
                            .status(HttpStatus.OK)
                            .build();
                } else {
                    throw new RuntimeException("Post with URL name '" + urlName + "' not found");
                }
            }
            Post postFindById = postRepository.findById(Long.parseLong(id))
                    .orElseThrow(() -> new RuntimeException("Post with id '" + id + "' not found"));
            Optional<Post> postOptional = postRepository.findByUrlNameAndIsHiddenFalse(urlName);
            Post post = postOptional
                    .orElseThrow(() -> new RuntimeException("Post with URL name '" + urlName + "' not found"));
            if (postFindById.getUrlName().equals(post.getUrlName())) {
                throw new RuntimeException("Post with id = " + id + " match with URL  =" + urlName);
            }
            return ResponsePayload.builder()
                    .message("Url is exist")
                    .data(true)
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message(e.getMessage())
                    .data(false)
                    .status(HttpStatus.OK)
                    .build();
        }
    }

    @Override
    public ResponsePayload getPostsByKeyword(String keyword, int limit) {
        LOGGER.info("PostServiceImpl -> get posts by keyword invoked");
        try {
            RelatePostsResponseDto relatePostsResponseDto = new RelatePostsResponseDto();

            String regexPattern = "";
            if (keyword != null && !keyword.trim().isEmpty()) {
                if (keyword.contains(",")) {
                    regexPattern = Arrays.stream(keyword.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.joining("|"));
                } else {
                    regexPattern = Arrays.stream(keyword.split(" "))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty() && s.length() > 2)
                            .collect(Collectors.joining("|"));
                }
            } else {
                return ResponsePayload.builder()
                        .message("No keyword provided")
                        .status(HttpStatus.OK)
                        .build();
            }
            Pageable pageable = PageRequest.of(0, limit + 2);
            Page<Post> relatedPosts = postRepository.findRelatedPostsByRegex(regexPattern, pageable);

            List<Object> postDtos = relatedPosts.stream()
                    .map(postConverter::toSuggestion)
                    .collect(Collectors.toList());

            relatePostsResponseDto.setPostDtos(postDtos);
            
            Long PROJECT_TOPIC_ID = 14L;

            Pageable projectPageable = PageRequest.of(0, 5, Sort.by("datePost").descending());

            Page<Post> projectPosts = postRepository.findAllByTopicIdAndIsHiddenFalse(projectPageable,
                    PROJECT_TOPIC_ID);

            List<Object> shippedProjectDtos = projectPosts.getContent().stream()
                    .map(postConverter::toSuggestion)
                    .collect(Collectors.toList());

            relatePostsResponseDto.setShippedProjectPostDtos(shippedProjectDtos);

            return ResponsePayload.builder()
                    .message("Get related posts successfully")
                    .data(relatePostsResponseDto)
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception e) {
            LOGGER.error("PostServiceImpl -> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .message("Failed to get related posts")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getPostByUrlNameToSeverSide(String urlName) {
        try {
            String url = "/" + urlName;
            Post post = postRepository.findByUrlNameAndIsHiddenFalse(url)
                    .orElseThrow(() -> new RuntimeException("Post with URL name '" + url + "' not found"));

            Optional<MetaSeo> metaSeoOpt = metaSeoRepository.findByEntityIdAndEntityType(post.getId(), "POST");
            String titleSeo = metaSeoOpt.map(MetaSeo::getMetaTitle).orElse(post.getName());

            PostSeverSideResponse postResponse = PostSeverSideResponse.builder()
                    .title(post.getName())
                    .titleSeo(titleSeo) 
                    .description(post.getDescription())
                    .build();
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Post retrieved successfully")
                    .data(postResponse)
                    .build();
        } catch (RuntimeException e) {
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Failed to retrieve post")
                    .build();
        }
    }

    @Override
    public ResponsePayload getPostsToSeverSide() {
        LOGGER.info("PostServiceImpl -> getPostsToSeverSide invoked");
        try {
            List<Post> posts = postRepository.findAllByIsHiddenFalse();
            List<PostSeverSideResponse> postDtos = posts.stream()
                    .map(post -> PostSeverSideResponse.builder()
                            .title(post.getName())
                            .description(post.getDescription())
                            .urlName(post.getUrlName())
                            .datePost(post.getDatePost())
                            .build())
                    .collect(Collectors.toList());
            return ResponsePayload.builder()
                    .data(postDtos)
                    .status(HttpStatus.OK)
                    .message("Successfully")
                    .build();
        } catch (Exception e) {
            LOGGER.error("PostServiceImpl -> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Failed")
                    .error(e.getMessage())
                    .build();
        }
    }

    @Override
    public ResponsePayload getLatestShippedGoods(int limit) {
    try {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("datePost").descending());
        Page<Post> posts = postRepository.findAllByCategoryIdAndIsHiddenFalse(3L, pageable);
        List<PostDto> result = posts.getContent().stream()
            .map(post -> {
                PostDto dto = new PostDto();
                dto.setId(post.getId());
                dto.setName(post.getName());     
                dto.setTitleImage(post.getTitleImage()); 
                dto.setUrlName(post.getUrlName());   
                return dto;
            })
            .collect(Collectors.toList());
            
        return ResponsePayload.builder()
                .status(HttpStatus.OK)
                .message("Get shipped goods success")
                .data(result)
                .build();
    } catch (Exception e) {
        return ResponsePayload.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Error: " + e.getMessage())
                .build();
    }
}
}