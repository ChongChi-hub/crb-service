package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.domain.dto.request.BranchRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateBranchRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController{
    @Autowired
    private BranchService branchService;
    @GetMapping
    private ResponseEntity<ResponsePayload> findBranches(){
        ResponsePayload responsePayload = branchService.getBranches();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping
    private ResponseEntity<ResponsePayload> addBranch(@RequestBody BranchRequestDto branchRequestDto) {
        ResponsePayload responsePayload = branchService.addBranch(branchRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PutMapping
    private ResponseEntity<ResponsePayload> updateBranch(@RequestBody UpdateBranchRequestDto updateBranchRequestDto) {
        ResponsePayload responsePayload = branchService.updateBranch(updateBranchRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping
    private ResponseEntity<ResponsePayload> deleteBranchList(@RequestBody DeleteListRequestDto deleteListBranchRequestDto) {
        ResponsePayload responsePayload = branchService.deleteBranchList(deleteListBranchRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping("{id}")
    private ResponseEntity<ResponsePayload> deleteBranchById(@PathVariable Long id) {
        ResponsePayload responsePayload = branchService.deleteBranchById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
}
