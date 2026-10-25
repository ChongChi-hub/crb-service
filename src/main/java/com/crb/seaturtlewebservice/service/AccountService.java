package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.CreateAccountRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.EditAccountRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;

public interface AccountService {
    ResponsePayload getAllAccounts();

    ResponsePayload create(CreateAccountRequestDto createAccountRequestDto);

    ResponsePayload edit(Long accountId, EditAccountRequestDto editAccountRequestDto);

    ResponsePayload remove(Long accountId);

}
