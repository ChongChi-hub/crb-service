package com.crb.seaturtlewebservice.controller.admin;

import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.PostRequest;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@AllArgsConstructor
public class ArticleController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<ResponsePayload> getAllPost(Pageable pageable, @RequestParam(name = "search") String keyword) {
        Pageable pageableRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "id");
        ResponsePayload responsePayload = postService.getListPost(pageableRequest, keyword);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/news")
    public ResponseEntity<ResponsePayload> getAllPostByTopicId(
            @RequestParam(name = "search") String keyword,
            Pageable pageable) {
        Pageable pageableRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "id");
        ResponsePayload responsePayload = postService.getListPostByTopicId(pageableRequest, keyword);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PutMapping("/update-article-status/{id}")
    public ResponseEntity<ResponsePayload> updatePostStatus(@PathVariable Long id, @RequestParam Boolean isHidden) {
        ResponsePayload responsePayload = postService.updatePostStatus(id, isHidden);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deletePostId(@PathVariable Long id) {
        ResponsePayload responsePayload = postService.deletePostById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping()
    public ResponseEntity<ResponsePayload> deletePostList(@RequestBody DeleteListRequestDto deleteListPostRequestDto) {
        ResponsePayload responsePayload = postService.deletePostByListId(deleteListPostRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping()
    public ResponseEntity<ResponsePayload> createPost(@RequestBody PostRequest postRequest) {
        postRequest.setId(null); 
        ResponsePayload response = postService.create(postRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getPostByIdForEdit(@PathVariable Long id) {
        ResponsePayload responsePayload = postService.getPostDtoById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    
    @PutMapping("/{id}") 
    public ResponseEntity<ResponsePayload> updatePost(
        @PathVariable Long id,
        @RequestBody PostRequest postRequest) {

        postRequest.setId(id);
        ResponsePayload response = postService.create(postRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
