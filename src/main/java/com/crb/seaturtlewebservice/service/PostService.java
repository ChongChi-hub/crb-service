package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.PostRequest;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import org.springframework.data.domain.Pageable;

public interface PostService {
    ResponsePayload create(PostRequest postRequest);

    ResponsePayload getListPost(Pageable pageableRequest, String keyword);

    ResponsePayload getListPostByTopicId(Pageable pageable, String keyword);

    ResponsePayload deletePostById(Long id);

    ResponsePayload deletePostByListId(DeleteListRequestDto deleteListPostRequestDto);

    ResponsePayload updatePostStatus(Long id, Boolean isHidden);

    ResponsePayload getPostsByTopicUrlName(String urlName, int pageNumber, int pageSize);

    ResponsePayload getPostDtoById(Long id);

    ResponsePayload getPostDtoUrlName(String urlName);

    ResponsePayload getFiveNewestPosts(int pageNumber, int pageSize, Long id);

    ResponsePayload addPostContact(PostRequest postRequest);

    ResponsePayload getPostContact();

    ResponsePayload getPostIntroduce();

    ResponsePayload addPostIntroduce(PostRequest postRequest);

    ResponsePayload isUrlExist(String urlName, String id);
    ResponsePayload getPostsByKeyword(String keyword, int limit);

    ResponsePayload getPostByUrlNameToSeverSide(String urlName);

    ResponsePayload getPostsToSeverSide();
    ResponsePayload getLatestShippedGoods(int limit);
}
