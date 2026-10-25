package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.domain.dto.request.PostRequest;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.crb.seaturtlewebservice.constant.PageConstant.PAGE_NUMBER;
import static com.crb.seaturtlewebservice.constant.PageConstant.PAGE_SIZE;
import static com.crb.seaturtlewebservice.constant.PageConstant.PAGE_SIZE_FIVE_NEWEST_POST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getPostById(@PathVariable Long id) {
        ResponsePayload responsePayload = postService.getPostDtoById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/url")
    public ResponseEntity<ResponsePayload> getPostByUrlName(@RequestParam(name = "value") String urlName) {
        ResponsePayload responsePayload = postService.getPostDtoUrlName(urlName);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/newest-post-detail-page/{id}")
    private ResponseEntity<ResponsePayload> getFiveNewestPost(
            @RequestParam(defaultValue = PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = PAGE_SIZE_FIVE_NEWEST_POST) int pageSize,
            @PathVariable(name = "id") long id) {
        ResponsePayload responsePayload = postService.getFiveNewestPosts(pageNumber, pageSize, id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/topicUrl")
    private ResponseEntity<ResponsePayload> getTopicByUrlName(
            @RequestParam(name = "urlName") String urlName,
            @RequestParam(defaultValue = PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = PAGE_SIZE) int pageSize) {
        if (Optional.ofNullable(urlName).isPresent()) {
            new ResponseEntity<>("Url name not found", HttpStatus.BAD_REQUEST);
        }
        ResponsePayload responsePayload = postService.getPostsByTopicUrlName(urlName, pageNumber, pageSize);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping("/contact")
    public ResponseEntity<ResponsePayload> addPostContact(@RequestBody PostRequest postRequest) {
        ResponsePayload responsePayload = postService.addPostContact(postRequest);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/contact")
    public ResponseEntity<ResponsePayload> getPostContact() {
        ResponsePayload responsePayload = postService.getPostContact();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PutMapping("/introduce")
    public ResponseEntity<ResponsePayload> addPostIntroduce(@RequestBody PostRequest postRequest) {
        ResponsePayload responsePayload = postService.addPostIntroduce(postRequest);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/introduce")
    public ResponseEntity<ResponsePayload> getPostIntroduce() {
        ResponsePayload responsePayload = postService.getPostIntroduce();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/url-exist")
    public ResponseEntity<ResponsePayload> isUrlExist(@RequestParam(name = "value") String urlName, @RequestParam(name = "id") String id) {
        ResponsePayload responsePayload = postService.isUrlExist(urlName, id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/relate-post")
    public ResponseEntity<ResponsePayload> getRelatePost(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(defaultValue = PAGE_SIZE_FIVE_NEWEST_POST) int limit) {
        ResponsePayload responsePayload = postService.getPostsByKeyword(keyword, limit);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/sever/url")
    public ResponseEntity<ResponsePayload> getPostByUrlNameToSeverSide(
            @RequestParam(name = "value") String urlName) {
        ResponsePayload responsePayload = postService.getPostByUrlNameToSeverSide(urlName);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responsePayload);
    }

    @GetMapping("/sever")
    public ResponseEntity<ResponsePayload> getPostsToSeverSide() {
        ResponsePayload responsePayload = postService.getPostsToSeverSide();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responsePayload);
    }

    @GetMapping("/shipped")
    public ResponseEntity<ResponsePayload> getShippedGoodsSlider(
            @RequestParam(defaultValue = "8") int limit) {
        ResponsePayload responsePayload = postService.getLatestShippedGoods(limit);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
}
