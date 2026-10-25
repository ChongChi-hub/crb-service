package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.BranchRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateBranchRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;

public interface BranchService {
    ResponsePayload getBranches();

    ResponsePayload addBranch(BranchRequestDto branchRequestDto);

    ResponsePayload updateBranch(UpdateBranchRequestDto updateBranchRequestDto);

    ResponsePayload deleteBranchList(DeleteListRequestDto deleteListBranchRequestDto);

    ResponsePayload deleteBranchById(Long id);

}
