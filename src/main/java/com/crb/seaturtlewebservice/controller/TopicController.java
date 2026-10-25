package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.domain.dto.request.TopicRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/topics")
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/{id}")
    private ResponseEntity<ResponsePayload> getTopicsByCategoryId(@PathVariable("id") Long id) {
        ResponsePayload responsePayload = topicService.getTopicsByCategoryId(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/categoryUrl")
    private ResponseEntity<ResponsePayload> getTopicsByCategoryUrlName(@RequestParam(name = "urlName") String urlName) {
        if (Optional.ofNullable(urlName).isPresent()){
           new ResponseEntity<>("Url name not found",HttpStatus.BAD_REQUEST);
        }
        ResponsePayload responsePayload = topicService.getTopicsByCategoryUrlName(urlName);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<ResponsePayload> removeTopic(@PathVariable("id") Long id) {
        ResponsePayload responsePayload = topicService.deleteTopicById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping
    private ResponseEntity<ResponsePayload> addTopic(@RequestBody TopicRequestDto topicRequestDto) {
        ResponsePayload responsePayload = topicService.addTopic(topicRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PutMapping
    private ResponseEntity<ResponsePayload> editTopic(@RequestBody TopicRequestDto topicRequestDto) {
        ResponsePayload responsePayload = topicService.updateTopic(topicRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping()
    private ResponseEntity<ResponsePayload> getTopics() {
        ResponsePayload responsePayload = topicService.getTopics();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
}
