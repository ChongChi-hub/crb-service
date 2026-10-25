package com.crb.seaturtlewebservice.controller.admin;

import com.crb.seaturtlewebservice.domain.dto.request.CreateAccountRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.EditAccountRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.PostRequest;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.AccountService;
import com.crb.seaturtlewebservice.service.PostService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class DashboardController {
    private final PostService postService;
    private final AccountService accountService;

    @PostMapping("/post/new")
    public ResponseEntity<ResponsePayload> createPost(@RequestBody PostRequest postRequest) {
        ResponsePayload response = postService.create(postRequest);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/accounts")
    public ResponseEntity<ResponsePayload> accounts() {
        ResponsePayload responsePayload = accountService.getAllAccounts();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping("/accounts")
    public ResponseEntity<ResponsePayload> createAccount(@RequestBody @Valid CreateAccountRequestDto createAccountRequestDto) {
        if (Optional.ofNullable(createAccountRequestDto).isPresent()) {
            new ResponseEntity<>("Request not found", HttpStatus.BAD_REQUEST);
        }
        ResponsePayload responsePayload = accountService.create(createAccountRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PutMapping("/accounts/{accountId}")
    public ResponseEntity<ResponsePayload> editAccount(
            @Valid @RequestBody EditAccountRequestDto editAccountRequestDto,
            @PathVariable Long accountId) {
        if (Optional.ofNullable(editAccountRequestDto).isPresent()) {
            new ResponseEntity<>("Request not found", HttpStatus.BAD_REQUEST);
        }
        ResponsePayload responsePayload = accountService.edit(accountId, editAccountRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }


    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<ResponsePayload> deleteAccount(@PathVariable Long accountId) {
        if (Optional.ofNullable(accountId).isPresent()) {
            new ResponseEntity<>("AccountId not found", HttpStatus.BAD_REQUEST);
        }
        ResponsePayload responsePayload = accountService.remove(accountId);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
}
