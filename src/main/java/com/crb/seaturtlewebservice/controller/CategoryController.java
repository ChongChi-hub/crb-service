package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.CategoryService;
import com.crb.seaturtlewebservice.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final TopicService topicService;

    @GetMapping
    private ResponseEntity<ResponsePayload> getCategories(){
        ResponsePayload responsePayload = categoryService.findCategory();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
    @GetMapping("/home")
    private ResponseEntity<ResponsePayload> getDataHomePage(){
        ResponsePayload responsePayload = categoryService.getDataHomePage();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @GetMapping("/category/{id}")
    private ResponseEntity<ResponsePayload> findAllTopicByCategoryId(@PathVariable(name = "id") Long id){
        ResponsePayload response = topicService.findAllTopicByCategoryId(id);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
