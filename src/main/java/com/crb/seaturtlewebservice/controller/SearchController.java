package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.crb.seaturtlewebservice.constant.PageConstant.PAGE_NUMBER;
import static com.crb.seaturtlewebservice.constant.PageConstant.PAGE_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<ResponsePayload> search(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(defaultValue = PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = PAGE_SIZE) int pageSize) {
        if (Optional.ofNullable(keyword).isPresent()) {
            new ResponseEntity<>("Keyword not found", HttpStatus.BAD_REQUEST);
        }
        ResponsePayload responsePayload = searchService.searchAll(keyword, pageNumber, pageSize);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

}
